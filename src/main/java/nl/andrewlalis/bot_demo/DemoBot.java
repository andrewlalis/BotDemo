package nl.andrewlalis.bot_demo;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import nl.andrewlalis.bot_demo.command.Command;
import nl.andrewlalis.bot_demo.command.HelpCommand;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DemoBot {
	private final Map<String, Command> commandMap;

	public DemoBot(String token) throws Exception {
		commandMap = new ConcurrentHashMap<>();
		commandMap.put("help", new HelpCommand());
		JDA jda = JDABuilder.createLight(token)
				.addEventListeners(new MessageListener(commandMap))
				.build();
		jda.awaitReady();
	}
}
