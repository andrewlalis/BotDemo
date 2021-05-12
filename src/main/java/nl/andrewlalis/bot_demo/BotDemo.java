package nl.andrewlalis.bot_demo;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import nl.andrewlalis.bot_demo.listeners.MessageListener;

public class BotDemo {
	public BotDemo(String token) throws Exception {
		JDA jda = JDABuilder.createLight(token)
				.addEventListeners(new MessageListener())
				.build();
		jda.awaitReady();
	}
}
