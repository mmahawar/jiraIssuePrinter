package com.jira;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PrintIssue {

	private final static String PDF_FILENAME = "sprintIssues.pdf";
	public static void main(String[] args) throws IOException, ParseException, org.apache.commons.cli.ParseException {
		Map<String, String> clArgs = CommandLineParameterValidator.verifyCommandLineArguments(args);
		
		boolean isSingleIssue = Boolean.valueOf(clArgs.get(CLOptions.sprintIssues.getOptionAsString()));
		if (isSingleIssue) 
			printSingleIssue(clArgs);
		else // Print Entire Sprint
			printEntireSprintIssues(clArgs);
	}

	private static void printEntireSprintIssues(Map<String, String> clArgs) throws IOException, ParseException {
		String jiraRestURLForSprintIssues = "https://"+ clArgs.get(CLOptions.jiraDomain.getOptionAsString()) + "/rest/agile/1.0/sprint/"
				+ clArgs.get(CLOptions.id.getOptionAsString()) + "/issue";
		JSONObject jsonObj = findJiraIssues(clArgs.get(CLOptions.username.getOptionAsString()), clArgs.get(CLOptions.password.getOptionAsString()), jiraRestURLForSprintIssues);
		JSONArray jsonArray = (JSONArray) jsonObj.get("issues");
		
		@SuppressWarnings("unchecked")
		List<Issue> issues = (List<Issue>) jsonArray.stream().map(value_returnIssue).collect(Collectors.toList());
		PDFBuilder.createPDF(PDF_FILENAME, issues);
	}

	private static void printSingleIssue(Map<String, String> clArgs) throws IOException, ParseException {
		String jiraRestURLForSingleIssue = "https://"+ clArgs.get(CLOptions.jiraDomain.getOptionAsString()) + "/rest/agile/1.0/issue/"
				+ clArgs.get(CLOptions.id.getOptionAsString());
		
		JSONObject jsonObj = findJiraIssues(clArgs.get(CLOptions.username.getOptionAsString()), clArgs.get(CLOptions.password.getOptionAsString()), jiraRestURLForSingleIssue);
		
		List<Issue> issues = new ArrayList<>();
		Issue issue = value_returnIssue.apply(jsonObj);
		issues.add(issue);

		PDFBuilder.createPDF(PDF_FILENAME, issues);
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