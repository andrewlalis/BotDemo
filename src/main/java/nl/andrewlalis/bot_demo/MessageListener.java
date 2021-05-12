package nl.andrewlalis.bot_demo;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import nl.andrewlalis.bot_demo.command.Command;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class MessageListener extends ListenerAdapter {

	private final Map<String, Command> commandMap;

	public MessageListener(Map<String, Command> commandMap) {
		this.commandMap = commandMap;
	}

	@Override
	public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
		String content = event.getMessage().getContentRaw();

		if (content.startsWith("!")) {
			String[] words = content.split("\\s+");
			String commandString = words[0].substring(1);
			if (this.commandMap.containsKey(commandString)) {
				Command cmd = this.commandMap.get(commandString);
				cmd.execute(Arrays.copyOfRange(words, 1, words.length), event.getMessage());
			} else {
				event.getMessage().reply("Invalid command.").queue();
			}
			return;
		}

		Pattern pattern = Pattern.compile("```java\\n(?<source>(.|\\s)*)\\n```");
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			String match = matcher.group("source");
			Map<String, String> params = new HashMap<>();
			params.put("api_dev_key", System.getenv("PASTEBIN_DEV_KEY"));
			params.put("api_paste_expire_date", "1D");
			params.put("api_option", "paste");
			params.put("api_paste_format", "java");
			params.put("api_paste_code", match);
			String formEncoded = params.keySet().stream()
					.map(key -> key + "=" + URLEncoder.encode(params.get(key), StandardCharsets.UTF_8))
					.collect(Collectors.joining("&"));
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("https://pastebin.com/api/api_post.php"))
					.header("Content-Type", "application/x-www-form-urlencoded")
					.POST(HttpRequest.BodyPublishers.ofString(formEncoded))
					.timeout(Duration.ofSeconds(5))
					.build();
			try {
				var response = client.send(request, HttpResponse.BodyHandlers.ofString());
				log.info("Got response: {}", response.body());
				event.getMessage().reply("Your code on pastebin: " + response.body()).queue();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
				event.getMessage().reply("Could not upload code.").queue();
			}
		}
	}
}
