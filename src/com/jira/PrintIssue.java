package com.jira;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PrintIssue {

	public static void main(String[] args) throws IOException, ParseException, org.apache.commons.cli.ParseException {
		Map<String, String> commandLineArguments = verifyCommandLineArguments(args);
		// TODO: print usage
		boolean isSingleIssue = Boolean.valueOf(commandLineArguments.get(CommandLineOptions.sprintIssues.toString()));
		if (isSingleIssue) {
			String jiraRestURLForSingleIssue = "https://jira.gapinc.com/rest/agile/1.0/issue/"
					+ commandLineArguments.get(CommandLineOptions.id.toString());
			printIssues(commandLineArguments.get(CommandLineOptions.username.toString()),
					commandLineArguments.get(CommandLineOptions.password.toString()), jiraRestURLForSingleIssue);
		} else {
			String jiraRestURLForSprintIssues = "https://jira.gapinc.com/rest/agile/1.0/sprint/"
					+ commandLineArguments.get(CommandLineOptions.id.toString()) + "/issue";
			printIssues(commandLineArguments.get(CommandLineOptions.username.toString()),
					commandLineArguments.get(CommandLineOptions.password.toString()), jiraRestURLForSprintIssues);
		}
	}

	private static Map<String, String> verifyCommandLineArguments(String[] args)
			throws ParseException, org.apache.commons.cli.ParseException {
		final CommandLineParser parser = new DefaultParser();

		final Options options = new Options();
		options.addOption("s", true, "Print Sprint Issues TRUE/FALSE");
		options.addOption("i", true, "Jira Issue Id or Sprint Id");
		options.addOption("u", true, "Jira User Name");
		options.addOption("p", true, "Jira Password");

		OptionGroup optionGroup = new OptionGroup();
		optionGroup.setRequired(true);
		
		
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "help", options );

		final CommandLine commandLine = parser.parse(options, args, true);
		
		Map<String, String> arguments = new HashMap<String, String>();
		arguments.put(CommandLineOptions.sprintIssues.toString(), getOption('s', commandLine));
		arguments.put(CommandLineOptions.id.toString(), getOption('i', commandLine));
		arguments.put(CommandLineOptions.username.toString(), getOption('u', commandLine));
		arguments.put(CommandLineOptions.password.toString(), getOption('p', commandLine));
		
		return arguments;
	}

	public static String getOption(final char option, final CommandLine commandLine) {

		if (commandLine.hasOption(option)) {
			return commandLine.getOptionValue(option);
		}

		return ""; // User StringUtils class EMPTY method.
	}

	private static void printIssues(String userName, String password, String jiraRestApiUrl)
			throws IOException, ParseException {
		JSONObject jsonObj = findJiraIssues(userName, password, jiraRestApiUrl);
		JSONArray jsonArray = (JSONArray) jsonObj.get("issues");
		String pdfFilename = "sprintIssues.pdf";
		
		if (jsonArray != null) {
			@SuppressWarnings("unchecked")
			List<Issue> issues = (List<Issue>) jsonArray.stream().map(value_returnIssue).collect(Collectors.toList());
			// //Print the PDF
			PDFBuilder.createPDF(pdfFilename, issues);
		} else {
			List<Issue> issues = new ArrayList<>();
			Issue issue = value_returnIssue.apply(jsonObj);
			issues.add(issue);
			// //Print the PDF
			PDFBuilder.createPDF(pdfFilename, issues);
		}
	}

	private static JSONObject findJiraIssues(String userName, String password, String jiraRestApiUrl)
			throws IOException, ParseException {
		ProcessBuilder pb = new ProcessBuilder("curl", "--show-error", "--request", "GET", "--header",
				"Accept: application/json", "--user", userName + ":" + password, jiraRestApiUrl);
		Process p = pb.start();

		InputStream is = p.getInputStream();
		JSONParser parser = new JSONParser();
		JSONObject jsonObj = (JSONObject) parser.parse(read(is));
		return jsonObj;
	}

	public static String read(InputStream input) throws IOException {
		try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
			return buffer.lines().collect(Collectors.joining("\n"));
		}
	}

	private static Function<JSONObject, Issue> value_returnIssue = json -> {
		Issue issue = new Issue();
		issue.setKey((String) json.get("key"));

		JSONObject fields = (JSONObject) json.get("fields");
		// System.out.println(fields.toString());
		issue.setDescription((String) fields.get("description"));
		issue.setSummary((String) fields.get("summary"));
		// issue.setIssueType((String) fields.get("issuetype"));
		issue.setAcs((String) fields.get("customfield_10010"));
		issue.setStoryPoints((Double) fields.get("customfield_10003"));
		return issue;
	};
}