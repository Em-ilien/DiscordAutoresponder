package fr.em_ilien.discord_autoresponder.exceptions;

public class PlaceholderKeyContainsForbiddenCharactersException extends Exception {

	private static final long serialVersionUID = -9171234387918123349L;

	public PlaceholderKeyContainsForbiddenCharactersException() {
		super("Placeholder keys must contains only uppercase letters, '{', '}', '%' and '_' characters.");
	}
}
