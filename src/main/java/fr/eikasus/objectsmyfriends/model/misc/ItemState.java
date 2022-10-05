package fr.eikasus.objectsmyfriends.model.misc;

public enum ItemState
{
	CA("CANCELED"), WT("WAITING"), AC("ACTIVE"), SD("SOLD");

	private final String value;

	ItemState(String value) {this.value = value;}

	public String getValue() {return value;}
}
