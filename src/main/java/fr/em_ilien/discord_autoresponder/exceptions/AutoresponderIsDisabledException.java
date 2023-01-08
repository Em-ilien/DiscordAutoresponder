package fr.em_ilien.discord_autoresponder.exceptions;

public class AutoresponderIsDisabledException extends Exception {
	private static final long serialVersionUID = 1L;

	public AutoresponderIsDisabledException() {
		super("You must enable the autoresponder to respond. Use method Autoresponder#setEnabled(boolean).");
	}
}
