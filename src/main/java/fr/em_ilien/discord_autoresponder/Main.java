package fr.em_ilien.discord_autoresponder;

import javax.security.auth.login.LoginException;

import fr.em_ilien.discord_autoresponder.events.MessageEventListener;
import fr.em_ilien.discord_autoresponder.model.Autoresponder;
import fr.em_ilien.discord_autoresponder.model.Notifier;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;

public class Main {
//	---

	/////////////////////// DISCORD PRIVATE TOKEN ///////////////////////
	//
	private static final String TOKEN = "";
	//
	/////////////////////// DISCORD PRIVATE TOKEN ///////////////////////

//	---

	/////////////////////// AUTORESPONSER PARAMETERS ///////////////////////
	//
	private static final String TIMEZONE_STRING = "Europe/Paris";
	private static final boolean ENABLE_PRINTING = true;
	private static final int DELAY_IN_SECONDS_BEFORE_SENDING_AUTO_RESPONSE = 3;
	private static final int DELAY_IN_SECONDS_BETWEEN_TWO_AUTO_RESPONSES = 60 * 60 * 10;
	private static final String MESSAGE_AUTORESPONDER = "Mon compte Discord hiberne jusqu'à la fin des vacances, si vous voulez me contacter :\n" //
			+ "\n" //
			+ "--> SMS (je suis susceptible de répondre vite)\n" //
			+ "--> emiliencoss@gmail.com (je réponds en journée en 3h)\n" //
			+ "--> MP Discord (je réponds en 48h)\n" //
			+ "\n" //
			+ "En cas d'urgence, essayez tous les canaux."; //
	//
	/////////////////////// AUTORESPONSER PARAMETERS ///////////////////////

//	---

	/////////////////////// NOTIFIER PARAMETERS ///////////////////////
	//
	private static final String RECIPIENT_EMAIL_ADDRESS = "emiliencoss@gmail.com";
	private static final String SENDER_PASSWORD = "";
	private static final String SENDER_EMAIL_ADDRESS = "";
	private static final int PORT = 587;
	private static final String SMTP_DOMAIN = "";
	private static final String EMAIL_TITLE = "Discord DM from {%DM_AUTHOR_USERNAME%}";
	private static final String EMAIL_BODY = "<h1 style='color: #000;'>Discord DM from {%DM_AUTHOR_USERNAME%}</h1>" //
			+ "<p style='color: #000;'>You received a Discord DM from <b>{%DM_AUTHOR_USERNAME%}</b>.</p>" //
			+ "<div style='width:50%;border:1px solid #bbb;border-radius:1em;padding:0.25em 1em;font-size:1.2em;margin:2em 0px'>" //
			+ "<p style='color: #777'>{%DM_CONTENT%}<p>" //
			+ "</div>" //
			+ "<a style='color:#fff;font-size:1.5em;text-decoration:none;display:block;background:#5779de;border-radius:0.5em;width: fit-content;padding: 0.75em 1em;margin-left: 2em;' href='https://discord.com/channels/@me/{%DM_AUTHOR_ID%}'>Y aller</a>" //
			+ ""; //
	//
	///////////////////////////////////////////////////////////////////////////////////////////

//	---

	public static JDA jda;

	public static void main(String[] args) {
		JDABuilder builder = new JDABuilder(AccountType.CLIENT).setToken(TOKEN);

		builder.setStatus(OnlineStatus.INVISIBLE);

		builder.addEventListeners(new MessageEventListener());

		Autoresponder.getInstance() //
				.setMessage(MESSAGE_AUTORESPONDER) //
				.enableDebugInConsole(ENABLE_PRINTING) //
				.setDelay(DELAY_IN_SECONDS_BEFORE_SENDING_AUTO_RESPONSE) //
				.setMinimumDelayBetweenTwoAutoResponses(DELAY_IN_SECONDS_BETWEEN_TWO_AUTO_RESPONSES) //
				.setTimezone(TIMEZONE_STRING) //
		;

		Notifier.getInstance() //
				.configureSMTP(SMTP_DOMAIN, PORT, SENDER_EMAIL_ADDRESS, SENDER_PASSWORD) //
				.setRecipient(RECIPIENT_EMAIL_ADDRESS) //
				.setTitle(EMAIL_TITLE) //
				.setBody(EMAIL_BODY) //
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
