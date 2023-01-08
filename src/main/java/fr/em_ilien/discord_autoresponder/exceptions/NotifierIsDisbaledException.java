package fr.em_ilien.discord_autoresponder.exceptions;

public class NotifierIsDisbaledException extends Exception {
	private static final long serialVersionUID = 1L;

	public NotifierIsDisbaledException() {
		super("You must enable the notifier to send emails. Use method Notifier#setEnabled(boolean).");
	}
}
