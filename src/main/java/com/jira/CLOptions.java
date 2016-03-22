package com.jira;

public enum CLOptions {
	sprintIssues("Print Sprint Issues TRUE/FALSE", 's'), 
	id("Jira Issue Id or Sprint Id",'i'), 
	username("Jira User Name", 'u'), 
	password("Jira Password", 'p');

	private final String text;
	private final char option;

	CLOptions(String text, char option) {
		this.text = text;
		this.option = option;
	}

	public String getText() {
		return text;
	}

	public String getOptionAsString() {
		return String.valueOf(option);
	}

	public char getOption() {
		return option;
	}
}
