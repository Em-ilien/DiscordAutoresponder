package fr.em_ilien.discord_autoresponder.exceptions;

public class AutoresponderMessageWasNotDefinedException extends Exception {
	private static final long serialVersionUID = -3990239371333366444L;

	public AutoresponderMessageWasNotDefinedException() {
		super("You must define a message for the autoresponder to respond. Use method Autoresponder#withMessage(String).");
	}
}
