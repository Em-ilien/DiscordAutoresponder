package fr.em_ilien.discord_autoresponder;

import javax.security.auth.login.LoginException;

import fr.em_ilien.discord_autoresponder.events.DMReceivedEventListener;
import fr.em_ilien.discord_autoresponder.model.Autoresponder;
import fr.em_ilien.discord_autoresponder.model.Notifier;
import fr.em_ilien.discord_autoresponder.model.Placeholder;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;

public class Main {
//	---

	/////////////////////// DISCORD PRIVATE TOKEN ///////////////////////
	/**
	 * To get your token:
	 * - Login with your account on the web app client (discord.com/app) and then CTRL+SHIFT+I and paste this code on console:
	 * <code>
	 * window.webpackChunkdiscord_app.push([[Math.random()], {}, (req) => {for (const m of Object.keys(req.c).map((x) => req.c[x].exports).filter((x) => x)) {if (m.default && m.default.getToken !== undefined) {return copy(m.default.getToken())}if (m.getToken !== undefined) {return copy(m.getToken())}}}]); console.log("%cWorked!", "font-size: 50px"); console.log(`%cYou now have your token in the clipboard!`, "font-size: 16px")
	 * </code>
	 * - The code will copy your token on your clipboard.
	 * - Finally, paste the token below:
	 */
	private static final String TOKEN = ""; //TODO: Please set here your discord account token (read the Javadoc above)

//	---

	/////////////////////// AUTORESPONSER PARAMETERS ///////////////////////
	private static final String AUTORESPONDER_TIMEZONE_STRING = "";
	private static final boolean AUTORESPONDER_ENABLE_PRINTING = true;
	private static final int DELAY_IN_SECONDS_BEFORE_SENDING_AUTO_RESPONSE = 2;
	private static final int DELAY_IN_SECONDS_BETWEEN_TWO_AUTO_RESPONSES = 60 * 60 * 10;
	private static final String AUTORESPONDER_MESSAGE = "" //
			+ "Pendant les vacances, je laisse mon compte Discord hiberner. Néanmoins, je reçois et lis tous les messages.\n" //
			+ "\n" //
			+ "Si vous m'avez contacté en attendant de ma part une réponse, laissez-moi votre adresse e-mail __sous__ ce message et je vous y répondrai.\n" //
			+ "\n" //
			+ "---\n" //
			+ "Je suis susceptible de répondre plus rapidement par SMS et par courriel.\n" //
			+ "emilien@em-ilien.fr\n" //
			+ "-";

//	---

	/////////////////////// NOTIFIER PARAMETERS ///////////////////////
	private static final String NOTIFIED_EMAIL_ADDRESS = ""; //TODO: please set here your email address which will receive notifications
	private static final String NOTIFIER_EMAIL_ADDRESS = ""; //TODO: please set here the email address which will send notifications
	private static final String NOTIFIER_PASSWORD = ""; //TODO: please set here the password of notifier email account
	private static final int NOTIFIER_PORT = 587;
	private static final String NOTIFIER_SMTP_SERVER_DOMAIN = "ssl0.ovh.net";
	private static final boolean NOTIFIER_DEBUG_LOGGING_MAILER_CONNECTION = false;

	/**
	 * The generic title will be cutomize each time Notifier send a new mail.
	 * 
	 * You can custime by setting {@link Placeholder} on
	 * {@link Notifier#sendWith(Placeholder...)} method.
	 * 
	 * @see Placeholder
	 * @see Notifier#sendWith(Placeholder...)
	 */
	private static final String NOTIFIER_GENERIC_TITLE = "Discord DM from {%DM_AUTHOR_USERNAME%}";

	/**
	 * The generic body will be cutomize each time Notifier send a new mail.
	 * 
	 * You can custime by setting {@link Placeholder} on
	 * {@link Notifier#sendWith(Placeholder...)} method.
	 * 
	 * @see Placeholder
	 * @see Notifier#sendWith(Placeholder...)
	 */
	private static final String NOTIFIER_GENERIC_BODY = "" //
			+ "<div style='max-width:550px;margin:2em;border: 1px solid #ddd;padding: 2em 4em;border-radius: 0.25em;'>" //
			+ "<h1 style='color: #000;'>Discord DM from {%DM_AUTHOR_USERNAME%}</h1>" //
			+ "<p style='color: #000;'>You received a Discord DM from <b>{%DM_AUTHOR_USERNAME%}</b>.</p>" //
			+ "<div style='border:1px solid #ccc;border-radius:1em;padding:0.25em 1em;font-size:1.2em;margin:2em 0px;'>" //
			+ "<p style='color: #777'>{%DM_CONTENT%}<p>" //
			+ "</div>" //
			+ "<a style='color:#fff;font-size:1.25em;text-decoration:none;display:block;background:#5779de;border-radius:0.4em;width:fit-content;padding:0.6em 3em;margin: auto;' href='https://discord.com/channels/@me/{%DM_AUTHOR_ID%}'>Y aller</a>" //
			+ "</div>" //
			+ ""; //

//	---

	public static JDA jda;

	public static void main(String[] args) {
		JDABuilder builder = new JDABuilder(AccountType.CLIENT).setToken(TOKEN);

		builder.setStatus(OnlineStatus.INVISIBLE);

		builder.addEventListeners(new DMReceivedEventListener());

		Autoresponder.getInstance() //
				.setMessage(AUTORESPONDER_MESSAGE) //
				.enableDebugInConsole(AUTORESPONDER_ENABLE_PRINTING) //
				.setDelay(DELAY_IN_SECONDS_BEFORE_SENDING_AUTO_RESPONSE) //
				.setMinimumDelayBetweenTwoAutoResponses(DELAY_IN_SECONDS_BETWEEN_TWO_AUTO_RESPONSES) //
				.setTimezone(AUTORESPONDER_TIMEZONE_STRING) //
		;

		Notifier.getInstance() //
				.configureSMTP(NOTIFIER_SMTP_SERVER_DOMAIN, NOTIFIER_PORT, NOTIFIER_EMAIL_ADDRESS, NOTIFIER_PASSWORD,
						NOTIFIER_DEBUG_LOGGING_MAILER_CONNECTION) //
				.setNotifiedEmail(NOTIFIED_EMAIL_ADDRESS) //
				.setTitle(NOTIFIER_GENERIC_TITLE) //
				.setBody(NOTIFIER_GENERIC_BODY) //
		;

		jda = null;
		try {
			jda = builder.build();
		} catch (LoginException e) {
			e.printStackTrace();
		}

		try {
			jda.awaitReady();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
