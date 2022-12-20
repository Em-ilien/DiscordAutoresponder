package fr.em_ilien.discord_autoresponder.model;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

import fr.em_ilien.discord_autoresponder.exceptions.NotifierParameterWasNotDefinedException;

/**
 * Represents the emails notifier which send you email when users send you DMs.
 * 
 * To instanciate: <code>Notifier notifier = Notifier.getInstance();</code>
 * 
 * You have to specify the title and the body of the notifier by using methods
 * {@link Notifier#setTitle(String)} and {@link Notifier#setBody(String)}.
 * 
 * You have also to specify SMTP configurations using
 * {@link Notifier#configureSMTP(String, int, String, String)}.
 * 
 * This class follows the Singleton design pattern, so the attributes are the
 * sames all the time.
 * 
 * @author Emilien Cosson
 *
 */
public class Notifier {
	private static final String DISCORD_DM_NOTIFICATIONS = "Discord DM notifications";

	private static Notifier instance;

	private Mailer mailer;

	private String notifierEmailAddress;
	private String notifiedEmailAddress;

	private String title;
	private String body;

	private Notifier() {
		mailer = null;

		notifierEmailAddress = null;
		notifiedEmailAddress = null;

		title = null;
		body = null;
	}

	/**
	 * Create and get or just get the already existing unique Notifier instance
	 * 
	 * @return the created or already existing instance
	 */
	public static Notifier getInstance() {
		if (instance == null)
			instance = new Notifier();

		return instance;
	}

	/**
	 * Send the email.
	 * 
	 * @param placeholders List of placeholders which replace placeholder keys by
	 *                     strings specified as values.
	 * @throws NotifierParameterWasNotDefinedException if
	 *                                                 {@link Notifier#configureSMTP(String, int, String, String)},
	 *                                                 {@link Notifier#setTitle(String)}
	 *                                                 or
	 *                                                 {@link Notifier#setBody(String)}
	 *                                                 was not used.
	 */
	public void sendWith(Placeholder... placeholders) throws NotifierParameterWasNotDefinedException {
		if (oneOfTheseIsNull(title, body, notifierEmailAddress, notifiedEmailAddress, mailer))
			throw new NotifierParameterWasNotDefinedException();

		String customTitle = this.title;
		String customBody = this.body;

		for (int i = 0; i < placeholders.length; i++) {
			Placeholder placeholder = placeholders[i];

			customTitle = customTitle.replace(placeholder.getGenericReplacedValue(),
					placeholder.getCustomReplacerValue());
			customBody = customBody.replace(placeholder.getGenericReplacedValue(), placeholder.getCustomReplacerValue());
		}

		Email email = EmailBuilder.startingBlank() //
				.to(null, notifiedEmailAddress) //
				.from(DISCORD_DM_NOTIFICATIONS, notifierEmailAddress) //
				.withSubject(customTitle) //
				.withHTMLText(customBody) //
				.buildEmail(); //

		mailer.sendMail(email);
	}

	private boolean oneOfTheseIsNull(Object... objects) {
		for (Object object : objects)
			if (object == null)
				return true;

		return false;
	}

	/**
	 * Configures the mailer (SMTP, port, sender credentials)
	 * 
	 * @param smtp_domain                  (e.g. 'smtp.gmail.com',
	 *                                     'smtp.your-domain.com'...)
	 * @param port                         (e.g. 587, 465...)
	 * @param senderEmailAddress           (e.g.
	 *                                     'notifier.discord@your-domain.com'...)
	 * @param senderPassword               Password of the sender email account
	 * @param debugLoggingMailerConnection Determines if the connection with the
	 *                                     Mailer will be shown in the console
	 * 
	 * @return the unique Notifier instance for chaining
	 */
	public Notifier configureSMTP(String smtp_domain, int port, String senderEmailAddress, String senderPassword,
			boolean debugLoggingMailerConnection) {
		this.notifierEmailAddress = senderEmailAddress;

		mailer = MailerBuilder //
				.withSMTPServer(smtp_domain, port, senderEmailAddress, senderPassword) //
				.withTransportStrategy(TransportStrategy.SMTP) //
				.withDebugLogging(debugLoggingMailerConnection).buildMailer(); //

		return instance;
	}

	/**
	 * Set your email address who will received notifications.
	 * 
	 * @param recipientEmailAddress your address email who received notifications
	 * @return the unique Notifier instance for chaining
	 */
	public Notifier setNotifiedEmail(String recipientEmailAddress) {
		this.notifiedEmailAddress = recipientEmailAddress;

		return instance;
	}

	/**
	 * Set the generic pattern title (which will be edited each time by
	 * {@link Notifier#sendWith(Placeholder...)} thanks to {@link Placeholder}. You
	 * can include placeholder keys which will be replaces by placeholder values.
	 * 
	 * @param title Generic pattern title of email
	 * @return the unique Notifier instance for chaining
	 */
	public Notifier setTitle(String title) {
		this.title = title;

		return instance;
	}

	/**
	 * Set the generic pattern body (which will be edited each time by
	 * {@link Notifier#sendWith(Placeholder...)} thanks to {@link Placeholder}. You
	 * can include placeholder keys which will be replaces by placeholder values.
	 * 
	 * @param body Generic pattern body of email
	 * @return the unique Notifier instance for chaining
	 */
	public Notifier setBody(String body) {
		this.body = body;

		return instance;
	}
}