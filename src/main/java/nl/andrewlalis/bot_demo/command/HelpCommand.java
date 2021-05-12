package nl.andrewlalis.bot_demo.command;

import net.dv8tion.jda.api.entities.Message;

public class HelpCommand implements Command {
	@Override
	public void execute(String[] args, Message message) {
		message.reply("This is a help message").queue();
	}
}
