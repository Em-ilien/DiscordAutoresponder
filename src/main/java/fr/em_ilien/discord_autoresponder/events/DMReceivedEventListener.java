package fr.em_ilien.discord_autoresponder.events;

import fr.em_ilien.discord_autoresponder.Main;
import fr.em_ilien.discord_autoresponder.exceptions.AutoresponderIsDisabledException;
import fr.em_ilien.discord_autoresponder.exceptions.AutoresponderMessageWasNotDefinedException;
import fr.em_ilien.discord_autoresponder.exceptions.NotifierIsDisbaledException;
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

		Notifier notifier = Notifier.getInstance();
		Autoresponder autoresponder = Autoresponder.getInstance();

		try {
			if (notifier.isEnabled())
				notifier.sendWith( //
						new Placeholder("{%DM_AUTHOR_USERNAME%}", event.getAuthor().getAsTag()), //
						new Placeholder("{%DM_CONTENT%}", event.getMessage().getContentRaw().replace("\n", "<br>")), //
						new Placeholder("{%DM_AUTHOR_ID%}", event.getChannel().getId()) //
				);

			if (autoresponder.isEnabled())
				autoresponder.replyIfUserHasntAlreadyReceivedAResponseRecently(event.getAuthor(), event.getChannel());
		} catch (AutoresponderMessageWasNotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotifierParameterWasNotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotifierIsDisbaledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AutoresponderIsDisabledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
