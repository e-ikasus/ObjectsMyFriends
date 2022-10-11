package fr.eikasus.objectsmyfriends.misc;

public enum ControllerError
{
	// General error codes.
	UNKNOWN_ERROR_CODE(0x10400),

	PASSWORD_DOESNT_MATCH(0x10410);

	private final int value;

	ControllerError(int value) {this.value = value;}

	public int getValue() {return value;}
}
