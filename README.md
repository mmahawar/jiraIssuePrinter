# jiraIssuePrinter

Introduction
============
This application can print a single Jira Issue or can print all the issues in a given sprint.

Parameters to pass
==================
This application takes four parameters
* -s : Indicator to specify single issue vs. printing entire sprint issues. 
  * TRUE indicates Print sprint issue
  * FALSE indicate Print single issue 
* -i : Sprint ID or Issue Key  
  * Please provide [sprint Id](http://braintwitter.blogspot.com/2016/02/finding-jira-sprintid-for-rest-api-call.html) if the above indicator is TRUE
  * Please provide Issue Key if the above indicator is FALSE 
* -u : GAP Jira username i.e. Ma3l3k8 
* -p : GAP Jira password xxxxxx 

How to run the program for Printing Issue/s
=======================================================
* Clone the git repo locally
* Install maven commandline version
* Install jre 1.8
* Update the arguments in pom.xml with correct credentials
  * <argument>-sTRUE</argument>
  * <argument>-iPRTR-57</argument>
  * <argument>-uMa3l3K8</argument>
  * <argument>-pxxxx</argument>
* Run  mvn clean compile
* Run mvn exec:java


