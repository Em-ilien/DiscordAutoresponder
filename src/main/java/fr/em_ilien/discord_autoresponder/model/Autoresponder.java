package fr.em_ilien.discord_autoresponder.model;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import fr.em_ilien.discord_autoresponder.exceptions.AutoresponderMessageWasNotDefinedException;
import fr.em_ilien.discord_autoresponder.exceptions.UserHasAlreadyBeenRepliedRecentlyException;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

/**
 * Represents the autorespondeur which send auto responses to users who send you
 * DM
 * 
 * To instanciate:
 * <code>Autoresponder autoresponder = Autoresponder.getInstance();</code>
 * 
 * You have to specify the message the autoresponder will send by using method
 * {@link Autoresponder#setMessage(String)}.
 * 
 * This class follows the Singleton design pattern, so the attributes are the
 * sames all the time.
 * 
 * @author Emilien Cosson
 *
 */
public class Autoresponder {
	private static final int DEFAULT_DELAY_BETWEEN_TWO_AUTO_RESPONSES = 24 * 60 * 60;
	private static final int DEFAULT_DELAY = 0;
	private static final boolean DEFAULT_DEBUG_MOD_IS_ACTIVE = false;

	private static Autoresponder instance;

	private Map<String, Date> usersWhoReceivedAResponse;

	private String message;
	private int delay;
	private int minimumDelayBetweenTwoAutoResponses;
	private String timezone;
	private boolean printInConsoleWhenUsersReceiveAResponse;

	private Autoresponder() {
		usersWhoReceivedAResponse = new HashMap<>();

		message = null;
		delay = DEFAULT_DELAY;
		minimumDelayBetweenTwoAutoResponses = DEFAULT_DELAY_BETWEEN_TWO_AUTO_RESPONSES;
		timezone = null;
		printInConsoleWhenUsersReceiveAResponse = DEFAULT_DEBUG_MOD_IS_ACTIVE;
	}

	/**
	 * Create and get or just get the already existing unique Autoresponder instance
	 * 
	 * @return the created or already existing instance
	 */
	public static Autoresponder getInstance() {
		if (instance == null)
			instance = new Autoresponder();

		return instance;
	}

	private Calendar getCalendar() {
		if (timezone == null)
			return Calendar.getInstance();

		return Calendar.getInstance(TimeZone.getTimeZone(timezone));
	}

	/**
	 * Determines if the user has received the {@link Autoresponder#message} from
	 * the Autoresponder recently.
	 * 
	 * "Recently" means that the current datetime is before the last time user
	 * received the message plus the
	 * {@link Autoresponder#minimumDelayBetweenTwoAutoResponses} in seconds.
	 * 
	 * @param userId The Discord user ID
	 * @return true if user has already received a response or false if not
	 * 
	 * @see UserHasAlreadyBeenRepliedRecentlyException
	 * @see {@link Autoresponder#delay}
	 */
	public boolean isUserHasAlreadyReceiveAResponseRecently(String userId) {
		if (usersWhoReceivedAResponse.containsKey(userId))
			if (getCalendar().getTime().before(usersWhoReceivedAResponse.get(userId)))
				return true;

		return false;
	}

	/**
	 * Send the {@link Autoresponder#message} to the user with a delay in seconds of
	 * {@link Autoresponder#delay}.
	 * 
	 * Before using this method, please check that
	 * {@link Autoresponder#isUserHasAlreadyReceiveAResponseRecently(String)} is
	 * false and that {@link Autoresponder#setMessage(String)} was used.
	 * 
	 * @param user    The user who send you a DM
	 * @param channel The TextChannel representing the DM channel between you and
	 *                the user
	 * @throws AutoresponderMessageWasNotDefinedException if message attribute
	 *                                                    wasn't
	 * @throws UserHasAlreadyBeenRepliedRecentlyException if user has already
	 *                                                    received the message from
	 *                                                    autoresponder recently
	 * 
	 * @see Autoresponder#setMessage(String)
	 * @see Autoresponder#isUserHasAlreadyReceiveAResponseRecently(String)
	 * @see Autoresponder#setMinimumDelayBetweenTwoAutoResponses(int)
	 */
	public void reply(User user, MessageChannel channel)
			throws AutoresponderMessageWasNotDefinedException, UserHasAlreadyBeenRepliedRecentlyException {

		if (message == null)
			throw new AutoresponderMessageWasNotDefinedException();

		if (isUserHasAlreadyReceiveAResponseRecently(user.getId()))
			throw new UserHasAlreadyBeenRepliedRecentlyException();

		final Calendar calendar = getCalendar();

		if (printInConsoleWhenUsersReceiveAResponse)
			System.out.println("[" + calendar.getTime().toString() + "] User " + user.getAsTag()
					+ " sent you a DM, autoresponder will reply him in " + delay + " seconds.");

		calendar.add(Calendar.SECOND, minimumDelayBetweenTwoAutoResponses);
		usersWhoReceivedAResponse.put(user.getId(), calendar.getTime());

		channel.sendMessage(message).queueAfter(delay, TimeUnit.SECONDS);
	}

	/**
	 * Set the message that the autoresponder will send to users who contact you in
	 * DM
	 * 
	 * @param message The message formatted by Markdown Discord format
	 * @return the unique Autoresponder instance for chaining
	 * 
	 * @see AutoresponderMessageWasNotDefinedException
	 */
	public Autoresponder setMessage(String message) {
		this.message = message;

		return instance;
	}

	/**
	 * Set the delay between the moment where the user send you a DM and the moment
	 * where autoresponder send him the message.
	 * 
	 * @param delayInSeconds The delay in seconds
	 * @return the unique Autoresponder instance for chaining
	 */
	public Autoresponder setDelay(int delayInSeconds) {
		this.delay = delayInSeconds;

		return instance;
	}

	/**
	 * Set the minimum delay between two automatic responses.
	 * 
	 * Allow to autoresponder to don't send message to users who have already
	 * received it less than x seconds ago.
	 * 
	 * @param delayInSeconds The delay in seconds
	 * @return the unique Autoresponder instance for chaining
	 * 
	 * @see UserHasAlreadyBeenRepliedRecentlyException
	 */
	public Autoresponder setMinimumDelayBetweenTwoAutoResponses(int delayInSeconds) {
		this.minimumDelayBetweenTwoAutoResponses = delayInSeconds;

		return instance;
	}

	/**
	 * Set the local timezone
	 * 
	 * @param timezone (e.g. 'Europe/Paris', 'America/New_York'...)
	 * @return the unique Autoresponder instance for chaining
	 */
	public Autoresponder setTimezone(String timezone) {
		this.timezone = timezone;

		return instance;
	}

	/**
	 * Enable or disable logging debug in console when autoresponder send the
	 * message to users.
	 * 
	 * @param enablePrinting (boolean) Does the program has to print debugs in
	 *                       console?
	 * @return the unique Autoresponder instance for chaining
	 */
	public Autoresponder enableDebugInConsole(boolean enablePrinting) {
		printInConsoleWhenUsersReceiveAResponse = enablePrinting;

		return instance;
	}

}
