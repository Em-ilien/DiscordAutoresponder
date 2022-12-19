package fr.em_ilien.discord_autoresponder.exceptions;

public class NotifierParameterWasNotDefinedException extends Exception {

	private static final long serialVersionUID = 2098306589428126555L;

	public NotifierParameterWasNotDefinedException() {
		super("You must define a title, a body, configure SMTP for the notifier.");
	}
}
