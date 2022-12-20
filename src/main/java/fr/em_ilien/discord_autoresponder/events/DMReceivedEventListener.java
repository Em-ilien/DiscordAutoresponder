package fr.em_ilien.discord_autoresponder.events;

import fr.em_ilien.discord_autoresponder.Main;
import fr.em_ilien.discord_autoresponder.exceptions.AutoresponderMessageWasNotDefinedException;
import fr.em_ilien.discord_autoresponder.exceptions.NotifierParameterWasNotDefinedException;
import fr.em_ilien.discord_autoresponder.model.Autoresponder;
import fr.em_ilien.discord_autoresponder.model.Notifier;
import fr.em_ilien.discord_autoresponder.model.Placeholder;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DMReceivedEventListener extends ListenerAdapter {

	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		if (event.getAuthor().equals(Main.jda.getSelfUser()))
			return;
		
		try {
			Notifier notifier = Notifier.getInstance();
			Autoresponder autoresponder = Autoresponder.getInstance();

			notifier.sendWith( //
					new Placeholder("{%DM_AUTHOR_USERNAME%}", event.getAuthor().getAsTag()), //
					new Placeholder("{%DM_CONTENT%}", event.getMessage().getContentRaw().replace("\n", "<br>")), //
					new Placeholder("{%DM_AUTHOR_ID%}", event.getChannel().getId()) //
			);

			autoresponder.replyIfUserHasntAlreadyReceivedAResponseRecently(event.getAuthor(), event.getChannel());
		} catch (NotifierParameterWasNotDefinedException e1) {
			e1.printStackTrace();
		} catch (AutoresponderMessageWasNotDefinedException e) {
			e.printStackTrace();
		}

	}

}
