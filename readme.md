# About

This little application is the second version of a project i firstly made during my formation to become web developer. I
decided to rewrite it from scratch to practice what i learned during my formation and at the same time, what i learned
by my own during the time passed to found a job. This application is based on JavaEE and the Hibernate ORM.

This document describes what the application is intended to be. It describes the final specifications of the
application which could not reflect its actual state, as the application is still under development.

# Introduction

The purpose of this application is to allow people to sell objects they don't care about and want to sell via a system
of auctions, like ebay in a certain point of view, but without money. They use tokens to buy objects and those
tokens can be bought via a dedicated interface.

# Application operation

## The main page

The main page of the application, also known as the welcome page, is the HTML interface people use to search for
objects and display them. This page looks like images below. At the left of the HTML page is a form that allows people
to filter the objects that are displayed in the central area. Depending on the fact that a user is logged-in or not,
different controls are available to him. As a not logged-in user, he can only choose the type of category the objects
he wants to display, and filter them by their name with the use of a pattern. As a logged-in user, more controls are
available to him, because he can then be a seller or a buyer.

In the top right corner inside the application header, a menu is available to the user from which he can do some actions
like log in, display and modify his profile, sell an object and log out. The content of this menu changes as the user is
logged-in or not. Be aware that when there is not enough room to display the menu in the form of a row of texts, this
menu is changed to a popup menu with an icon to open it. Under this menu, information is present to allow the user to
know is state. In the top left corner, the application name allows the user to be redirected to this welcome page each
time he clicks on it.

In the central part of the page, all objects that correspond to the user criteria are displayed as series of cards.
Each card contains the necessary object information needed to have a global view of it. A card contains the current
price of the object, the end of the auctions and his seller. If the object seller supplies images, they are also
displayed in the card. If more than one images are supplied, they are put on top of each other and cycle alternatively
with a fade animation. If a user is logged-in, a click on a seller name will display his profile. When the user clicks
on the object card, a more details object form is displayed and if this user is logged-in, he can then make an offer.

| Desktop                              | Mobile                                     |
|--------------------------------------|--------------------------------------------|
| ![Welcome](/docs/images/welcome.png) | ![Welcome](/docs/images/welcome_small.png) | 

## User connexion

If a user wants to buy or sell an object, it must be logged-in. By choosing "log in" option in the application menu, the
user has access to a form where he can enter his identifier and his password. The identifier can be an email or a
username. The "remember me" button prevents the user from entering his identifier each time he wants to connect to the
application. "The remembre me" function doesn't store the user password for security purpose. Of course, the user must
have an already created profile. Once connected, the user is redirected to the welcome page. If he enters a wrong
password or identifier, it returns to the connexion page with error messages displayed under each wrong input fields.

| Desktop and Mobile                    |
|---------------------------------------|
| ![Connexion](/docs/images/log_in.png) |

## User profile

### Displaying user profile

Once connected, the user has access to his profile from the top right menu of the application. By choosing the "display
profile" option, he is redirected to the display profile page where he can see all his information. He can decide to
update his profile by clicking on the corresponding button at the bottom of the form: he is then redirected to the
create and modify profile. When a user is connected, he can display the profile of a particular seller of an item by
clicking on his name in the item card from the welcome page.

| Desktop and Mobile                          |
|---------------------------------------------|
| ![Connexion](/docs/images/show_profile.png) |

### Modifying user profile

From the show profile page, a user has access to the modify profile form where he can update his personal information.
His identifier and email address can't be modified, but all others can. If password fields are empty, that means the
user doesn't want to change his current password, otherwise, both fields need to be the same in order to register his
changes. Note that this is the only place where the user can see the number of his tokens. Be aware of the delete
button which lead to disabling the user, but doesn't remove it from the database.

| Desktop                                     | Mobile                                            |
|---------------------------------------------|---------------------------------------------------|
| ![Welcome](/docs/images/modify_profile.png) | ![Welcome](/docs/images/modify_profile_small.png) |

### Creating user profile

A user can create a profile by choosing the "create profile" option in the application menu. Of course this option is
only available for unconnected users. The user is then redirected to the "create profile" page where a form prompt him
to enter his personal information. This form is almost the same as for updating a profile, only buttons at the bottom of
the form change. All fields are required. After clicking on the "create" button, the information entered by the user are
sent to the application server side and pass a validation process to ensure they conform to the expected data. In case
of problem, data user are send back to him via the same form in which error messages are displayed under incorrect data
which fields are cleared. Correct data remain unchanged, so the user doesn't need to enter them again.

| Desktop                                | Mobile                                       |
|----------------------------------------|----------------------------------------------|
| ![Welcome](/docs/images/subscribe.png) | ![Welcome](/docs/images/subscribe_small.png) |

# The objects

## Selling objects

When a user is logged in, he can sell an object by clicking on the "sell object" option of the application menu. He is
then redirected to the object sell page at the center of which a form prompt him to enter all necessary information
about his object. On the left side of the form, the user can add multiple images showing his object in different
positions by clicking on the yellow box. The accepted image file formats are the jpeg and the png. Each time an image is
added to the object, this image is automatically sent to the server side of the application and stored asynchronously.
This allows the images to be retrieved if the form is returned to the user because of incorrect entered data. Each
successfully uploaded image is then add to the images area of the form. If there are more images that dedicated area can
display, the user can use the mouse wheel to display the hidden images. The auction start date and auction end date
must be valid, that is the end date must not be before the start date. The start date can't be in the past, but can be
in the future: This allows the user to schedule the sell. By default, the pickup place is the address of the seller, but
this can be modified. When the seller is satisfied about the information he entered, a click on the "save" button send
them to the server side of the application. If all data are valid, the user is redirected to the welcome page,
otherwise, the form is displayed again with error messages under each invalid input fields: Correct data remains
unchanged, but wrong data are cleared.

| Desktop                                | Mobile                                       |
|----------------------------------------|----------------------------------------------|
| ![Welcome](/docs/images/item_sell.png) | ![Welcome](/docs/images/item_sell_small.png) |

## Displaying object information

When a user is not logged-in, he can access to the detailed object information by clicking on the object card he wants
information on. It is then redirected to the object bid page which will show him a form containing all information the
seller gave, including images, pickup place, the current price of the object and the best bidder. As for the sell form,
object images that are hidden because of the images area size to small can be displayed be using the mouse wheel.

| Desktop                                | Mobile                                       |
|----------------------------------------|----------------------------------------------|
| ![Welcome](/docs/images/show_item.png) | ![Welcome](/docs/images/show_item_small.png) |

## Making offer on an object

Clicking on an object card in the welcome page bring a connected user to the object auction page where he can make an
offer on that selected object. This is the same form as the one used to display object information except it contains
more interface elements to allow him to make his offer. By default, the offer input field is filled with the current
object price plus one token. The user can't make an offer on him, nor can he make a lower offer that the current object
price. Clicking on the "make offer" button send his offer to the server side application. If something goes wrong due to
the object auction closed or because another user made an offer between the time the form was displayed and the
submitting of his offer, the form is displayed again otherwise his is redirected to the welcome page. After his offer
was accepted, his credit is updated and the previous best bidder account is refund.

| Desktop                               | Mobile                                      |
|---------------------------------------|---------------------------------------------|
| ![Welcome](/docs/images/item_bid.png) | ![Welcome](/docs/images/item_bid_small.png) |
