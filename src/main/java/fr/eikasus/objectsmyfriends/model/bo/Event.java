package fr.eikasus.objectsmyfriends.model.bo;

import fr.eikasus.objectsmyfriends.model.misc.ItemState;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity(name="Event") @Table(name="Events")
public class Event
{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long identifier;

	@Column(name = "date", nullable = false) @Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinColumn(name ="item_identifier")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Item itemIdentifier;

	@Column(name = "old_item_state", nullable = false, length = 2) @Enumerated(EnumType.STRING)
	private ItemState oldItemState;

	@Column(name = "new_item_state", nullable = false, length = 2) @Enumerated(EnumType.STRING)
	private ItemState newItemState;
}
