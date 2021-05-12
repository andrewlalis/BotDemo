package nl.andrewlalis.bot_demo.command;

import net.dv8tion.jda.api.entities.Message;

public interface Command {
	void execute(String[] args, Message message);
}
