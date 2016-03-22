package com.jira;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PrintIssue {

	public static void main(String[] args) throws IOException, ParseException {

		List<String> commandLineArgs = new ArrayList<String>();
		commandLineArgs.addAll(Arrays.asList(args));
		boolean isSingleIssue = Boolean.valueOf(commandLineArgs.get(0));

		String userName = commandLineArgs.get(1);
		String password = commandLineArgs.get(2);

		if (isSingleIssue) {
			String jiraRestURLForSingleIssue = "https://jira.gapinc.com/rest/agile/1.0/issue/" + commandLineArgs.get(3);
			printIssues(userName, password, jiraRestURLForSingleIssue);
		} else {
			String jiraRestURLForSprintIssues = "https://jira.gapinc.com/rest/agile/1.0/sprint/"
					+ commandLineArgs.get(3) + "/issue";
			printIssues(userName, password, jiraRestURLForSprintIssues);
		}
	}

	private static void printIssues(String userName, String password, String jiraRestApiUrl)
			throws IOException, ParseException {

		ProcessBuilder pb = new ProcessBuilder("curl", "--show-error", "--request", "GET", "--header",
				"Accept: application/json", "--user", userName + ":" + password, jiraRestApiUrl);

		Process p = pb.start();

		InputStream is = p.getInputStream();
		JSONParser parser = new JSONParser();
		JSONObject jsonObj = (JSONObject) parser.parse(read(is));

		JSONArray jsonArray = (JSONArray) jsonObj.get("issues");
		if (jsonArray != null) {
			@SuppressWarnings("unchecked")
			List<Issue> issues = (List<Issue>) jsonArray.stream().map(value_returnIssue).collect(Collectors.toList());
			// //Print the PDF
			PDFBuilder.createPDF("sprintIssues.pdf", issues);
		} else {
			List<Issue> issues = new ArrayList<>();
			Issue issue = value_returnIssue.apply(jsonObj);
			issues.add(issue);
			// //Print the PDF
			PDFBuilder.createPDF("sprintIssues.pdf", issues);

		}

	}

	public static String read(InputStream input) throws IOException {
		try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
			return buffer.lines().collect(Collectors.joining("\n"));
		}
	}

	private static Function<JSONObject, String> key_issue = i -> (String) i.get("key");

	private static Function<JSONObject, Issue> value_returnIssue = json -> {
		Issue issue = new Issue();
		issue.setKey((String) json.get("key"));

		JSONObject fields = (JSONObject) json.get("fields");

		issue.setDescription((String) fields.get("description"));
		issue.setSummary((String) fields.get("summary"));
		// issue.setIssueType((String) fields.get("issuetype"));
		issue.setAcs((String) fields.get("customfield_10010"));
		issue.setStoryPoints((Double) fields.get("customfield_10003"));
		return issue;
	};

}