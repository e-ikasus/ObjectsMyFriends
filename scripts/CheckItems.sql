/* Vérifie l'état des articles ou d'un article en particulier par rapport à la date courante
   et par rapport à l'état dans lequel se trouve chaque article. Cette vérification peut
   conduire ou pas à la modification des états et à l'actualisation d'un historique des
   opérations via la table 'log_debug'. Cette procédure devrait être exécutée avant chaque
   'select' sur la liste des articles.

   Liste des paramètres:

   asked_item   : Identifiant de l'article à analyser ou 0 pour tous les analyser.
   trace        : Indique si les modifications doivent être sauvegardées dans la table 'events'.
   simulate     : Active ou pas la simulation de la modification d'un état.
   append       : Indique si la table 'log_debug' doit être vidée avant le traitement.
   result       : Indique si un article à changé d'état ou pas.
 */

DROP PROCEDURE IF EXISTS check_items;

DELIMITER //

CREATE PROCEDURE check_items(IN asked_item INTEGER, IN trace BOOLEAN, IN simulate BOOLEAN, IN append BOOLEAN,
                             OUT result BOOLEAN)
BEGIN
    -- *****************************
    -- * Déclaration des variables *
    -- *****************************

    -- Constantes (à défaut de mieux) ! Proviennent de l'entité itemState.
    DECLARE CANCELED_STATE CHAR(2) DEFAULT 'CA';
    DECLARE WAITING_STATE CHAR(2) DEFAULT 'WT';
    DECLARE ACTIVE_STATE CHAR(2) DEFAULT 'AC';
    DECLARE SOLD_STATE CHAR(2) DEFAULT 'SD';

    -- Variable pour détecter la position de fin du curseur.
    DECLARE items_list_end_reached INTEGER DEFAULT 0;

    -- Variables recevant les valeurs des champs des items.
    DECLARE var_name, var_description VARCHAR(512);
    DECLARE var_state CHAR(2);
    DECLARE var_bidding_end DATETIME;
    DECLARE var_identifier, var_category INTEGER;

    -- Variable contenant l'id de l'ancien état.
    DECLARE old_state CHAR(2);

    -- Date actuelle.
    DECLARE cur_date DATETIME DEFAULT NOW();

    -- Curseur utilisé pour parcourir le résultat de la requête d'un article en particulier.
    DECLARE cursor_one_item CURSOR FOR
        SELECT i.identifier,
               i.name,
               i.description,
               i.category,
               i.state,
               i.bidding_end
        FROM items i
        WHERE i.identifier = asked_item;

    -- Curseur utilisé pour parcourir le résultat de la requête de toutes les articles.
    DECLARE cursor_all_items CURSOR FOR
        SELECT i.identifier,
               i.name,
               i.description,
               i.category,
               i.state,
               i.bidding_end
        FROM items i;

    -- Déclare le gestionnaire NOT FOUND.
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET items_list_end_reached = 1;

    -- *************************
    -- * Corps de la procédure *
    -- *************************

    -- Par défaut, aucun article ne change.
    SET result = 0;

    -- Vide la table de débogage si demandé.
    IF ((NOT append) AND (trace)) THEN DELETE FROM events; END IF;

    -- Ouvre le curseur.
    IF (NOT asked_item) THEN OPEN cursor_all_items; ELSE OPEN cursor_one_item; END IF;

    -- Pour chaque ligne de la table 'items'.
    scan_items_list:
    LOOP
        -- Récupère une ligne.
        IF (NOT asked_item) THEN
            FETCH cursor_all_items INTO var_identifier, var_name, var_description, var_category, var_state, var_bidding_end;
        ELSE
            FETCH cursor_one_item INTO var_identifier, var_name, var_description, var_category, var_state, var_bidding_end;
        END IF;

        -- S'il n'y en a plus, alors c'est fini.
        IF items_list_end_reached = 1 THEN
            LEAVE scan_items_list;
        END IF;

        -- L'état actuel deviendra donc l'ancien état après le traitement.
        SET old_state = var_state;

        -- Si l'article est en vente.
        IF (var_state = ACTIVE_STATE) THEN
            IF (cur_date > var_bidding_end) THEN
                SET var_state = SOLD_STATE;
            END IF;
        END IF;

        -- Si l'état doit être modifié.
        IF (old_state != var_state) THEN

            -- Un article a au moins changé d'état.
            SET result = TRUE;

            -- Si le mode trace est actif, sauvegarde dans la BD ce qui doit être fait.
            IF (trace) THEN
                INSERT INTO events (date, new_item_state, old_item_state, item_identifier)
                VALUES (cur_date, var_state, old_state, var_identifier);
            END IF;

            IF (NOT simulate) THEN UPDATE items SET state = var_state WHERE identifier = var_identifier; END IF;
        END IF;

    END LOOP scan_items_list;

    -- ferme le curseur.
    IF (NOT asked_item) THEN CLOSE cursor_all_items; ELSE CLOSE cursor_one_item; END IF;
END //
