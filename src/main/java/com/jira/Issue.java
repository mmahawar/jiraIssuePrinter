package com.jira;

public class Issue {

	private String key;
	private String description;
	private String summary;
	private String acs;
	private String issueType;
	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	private Double storyPoints;
	

	public String getAcs() {
		return acs;
	}

	public void setAcs(String acs) {
		this.acs = acs;
	}

	public String getSummary() {
		return summary;
	}

	public Double getStoryPoints() {
		return storyPoints;
	}

	public void setStoryPoints(Double storyPoints) {
		this.storyPoints = storyPoints;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
}