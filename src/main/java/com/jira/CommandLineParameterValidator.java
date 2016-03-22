package com.jira;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineParameterValidator {
	
	public static Map<String, String> verifyCommandLineArguments(String[] args) {	
		CLOptions sprintissues = CLOptions.sprintIssues;
		CLOptions id = CLOptions.id;
		CLOptions username = CLOptions.username;
		CLOptions password = CLOptions.password;
		CLOptions jiraDomain = CLOptions.jiraDomain;
		
		final Options options = new Options();
		options.addOption(sprintissues.getOptionAsString(), true, sprintissues.getText());
		options.addOption(id.getOptionAsString(), true, id.getText());
		options.addOption(username.getOptionAsString(), true, username.getText());
		options.addOption(password.getOptionAsString(), true, password.getText());
		options.addOption(jiraDomain.getOptionAsString(), true, jiraDomain.getText());
		
		final CommandLineParser parser = new DefaultParser();
		CommandLine commandLine = null;
		try {
			commandLine = parser.parse(options, args, true);
			return collectArguments(sprintissues, id, username, password, jiraDomain, commandLine);					
		} catch ( IllegalArgumentException | ParseException e) {
			System.out.println(e.getMessage());
			printHelp(options);
			System.exit(0);
		}
		//TODO: Hating to return null need to think how to make things better..
		return null;
	}

	private static void printHelp(final Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "help", options );
	}

	private static Map<String, String> collectArguments(CLOptions sprintissues,final CLOptions id,
			final CLOptions username,final CLOptions password, final CLOptions jiraDomain, final CommandLine commandLine) {
		Map<String, String> arguments = new HashMap<String, String>();
		
		arguments.put(sprintissues.getOptionAsString(), 
					  getOption(sprintissues, commandLine));
		arguments.put(id.getOptionAsString(), 
				      getOption(id, commandLine));
		arguments.put(username.getOptionAsString(), 
				      getOption(username, commandLine));
		arguments.put(password.getOptionAsString(), 
				      getOption(password, commandLine));
		arguments.put(jiraDomain.getOptionAsString(), 
				getOption(jiraDomain, commandLine));
		return arguments;
	}
	
	private static String getOption(final CLOptions option, final CommandLine commandLine) throws IllegalArgumentException {

		if (commandLine.hasOption(option.getOption())) {
			return commandLine.getOptionValue(option.getOption());
		}
	   throw new IllegalArgumentException("-" + option.getOptionAsString() + " (" + option.getText() + ") is mandatory parameter."); 
	}
}
