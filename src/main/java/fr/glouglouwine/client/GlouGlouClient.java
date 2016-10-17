package fr.glouglouwine.client;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GlouGlouClient {

	private static Logger logger = Logger.getLogger("GlouGlouClient"); 
	
	private CommandHandler commandHandler = new CommandHandler();

	public static void main(String[] args) {
		GlouGlouClient client = new GlouGlouClient();
		client.commandHandler.help();
		client.acceptOrders();

	}

	private void acceptOrders() {
		Scanner sc = new Scanner(System.in);
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			handleCommand(tokenize(line));
		}
		sc.close();
	}

	private void handleCommand(ArrayList<String> tokens) {
		if (tokens.size() > 0) {
			Method m = findMethod(tokens.get(0));
			if (m == null) {
				System.out.println("Unknown command");
			} else {
				try {
					if (m.getParameterCount() == tokens.size() - 1) {
						if (tokens.size() == 1) {
							m.invoke(commandHandler);
						} else {
							m.invoke(commandHandler, tokens.subList(1, tokens.size()).toArray());
						}
					} else {
						System.out.println("[" + m.getName() + "] requires [" + m.getParameterCount() + "] parameters");
					}
				} catch (Exception e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}
	}

	private Method findMethod(String methodeName) {
		for (Method method : CommandHandler.class.getDeclaredMethods()) {
			if (method.getName().equals(methodeName)) {
				return method;
			}
		}
		return null;
	}

	private ArrayList<String> tokenize(String line) {
		String grammar = "(\\w+|'(\\w|\\s)+')\\s?";
		Pattern pattern = Pattern.compile(grammar);
		Matcher matcher = pattern.matcher(line);
		ArrayList<String> tokens = new ArrayList<>();
		while (matcher.find()) {
			String token = matcher.group(1);
			if (token.matches("'.*'")) {
				token = token.substring(1, token.length() - 1);
			}
			tokens.add(token);
		}
		return tokens;
	}

}
