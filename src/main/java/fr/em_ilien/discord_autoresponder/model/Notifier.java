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
	private static Notifier instance;

	private Mailer mailer;

	private String senderEmailAddress;
	private String recipientEmailAddress;

	private String title;
	private String body;

	private Notifier() {
		mailer = null;

		senderEmailAddress = null;
		recipientEmailAddress = null;

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
		if (title == null || body == null || mailer == null || senderEmailAddress == null
				|| recipientEmailAddress == null)
			throw new NotifierParameterWasNotDefinedException();

		String localTitle = this.title;
		String localBody = this.body;

		for (int i = 0; i < placeholders.length; i++) {
			Placeholder placeholder = placeholders[i];

			localTitle = localTitle.replace(placeholder.getKey(), placeholder.getValue());
			localBody = localBody.replace(placeholder.getKey(), placeholder.getValue());
		}

		Email email = EmailBuilder.startingBlank() //
				.to(null, recipientEmailAddress) //
				.from(senderEmailAddress) //
				.withSubject(localTitle) //
				.withHTMLText(localBody) //
				.buildEmail(); //

		mailer.sendMail(email);
	}

	/**
	 * Configures the mailer (SMTP, port, sender credentials)
	 * 
	 * @param smtp_domain        (e.g. 'smtp.gmail.com'...)
	 * @param port               (e.g. 587, 465...)
	 * @param senderEmailAddress (e.g. 'notifier.discord@your-domain.fr'...)
	 * @param senderPassword     Password of the sender email account
	 * @return the unique Notifier instance for chaining
	 */
	public Notifier configureSMTP(String smtp_domain, int port, String senderEmailAddress, String senderPassword) {
		this.senderEmailAddress = senderEmailAddress;

		mailer = MailerBuilder //
				.withSMTPServer(smtp_domain, port, senderEmailAddress, senderPassword) //
				.withTransportStrategy(TransportStrategy.SMTP) //
				.buildMailer(); //

		return instance;
	}

	/**
	 * Set your email address who will received notifications.
	 * 
	 * @param recipientEmailAddress your address email who received notifications
	 * @return the unique Notifier instance for chaining
	 */
	public Notifier setRecipient(String recipientEmailAddress) {
		this.recipientEmailAddress = recipientEmailAddress;

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