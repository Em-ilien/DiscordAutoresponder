package fr.em_ilien.discord_autoresponder.events;

import fr.em_ilien.discord_autoresponder.Main;
import fr.em_ilien.discord_autoresponder.exceptions.AutoresponderMessageWasNotDefinedException;
import fr.em_ilien.discord_autoresponder.exceptions.NotifierParameterWasNotDefinedException;
import fr.em_ilien.discord_autoresponder.exceptions.UserHasAlreadyBeenRepliedRecentlyException;
import fr.em_ilien.discord_autoresponder.model.Autoresponder;
import fr.em_ilien.discord_autoresponder.model.Notifier;
import fr.em_ilien.discord_autoresponder.model.Placeholder;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageEventListener extends ListenerAdapter {

	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		if (event.getAuthor().equals(Main.jda.getSelfUser()))
			return;

		Notifier notifier = Notifier.getInstance();
		try {
			notifier.sendWith(new Placeholder("{%DM_AUTHOR_USERNAME%}", event.getAuthor().getAsTag()),
					new Placeholder("{%DM_CONTENT%}", event.getMessage().getContentDisplay()),
					new Placeholder("{%DM_AUTHOR_ID%}", event.getChannel().getId()));
		} catch (NotifierParameterWasNotDefinedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Autoresponder autoresponder = Autoresponder.getInstance();

		if (autoresponder.isUserHasAlreadyReceiveAResponseRecently(event.getAuthor().getId()))
			return;

		try {
			autoresponder.reply(event.getAuthor(), event.getChannel());
		} catch (AutoresponderMessageWasNotDefinedException e) {
			e.printStackTrace();
		} catch (UserHasAlreadyBeenRepliedRecentlyException e) {
			e.printStackTrace();
		}

	}

}
