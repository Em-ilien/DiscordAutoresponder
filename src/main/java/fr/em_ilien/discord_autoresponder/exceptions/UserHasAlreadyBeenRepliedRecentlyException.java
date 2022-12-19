package fr.em_ilien.discord_autoresponder.exceptions;

public class UserHasAlreadyBeenRepliedRecentlyException extends Exception {
	private static final long serialVersionUID = -8969492936135853336L;

	public UserHasAlreadyBeenRepliedRecentlyException() {
		super("This user has already been replied recently by the autoresponder. Use method Autoresponder#isUserHasAlreadyBeenRepliedRecently(String) or Autoresponder#withMinimumDelayBetweenTwoAutomaticResponses(int).");
	}
}
