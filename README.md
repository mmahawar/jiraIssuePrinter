# jiraIssuePrinter

Introduction
============
This application can print a single Jira Issue or can print all the issues in a given sprint.

Parameters to pass
==================
This application takes four parameters
* -s : Indicator to specify single issue vs. printing entire sprint issues. 
  * TRUE indicates Print single issue
  * FALSE indicate Print sprint issues 
* -i : Sprint ID or Issue Key  
  * Please provide [sprint Id](http://braintwitter.blogspot.com/2016/02/finding-jira-sprintid-for-rest-api-call.html) if the above indicator is FALSE
  * Please provide Issue Key if the above indicator is TRUE 
* -u : Jira username i.e. Ma3l3k8 
* -p : Jira password xxxxxx 
* -d : Jira Domain name without HTTP protocol

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
  * <argument>-dxxxx.jira.com</argument>
* Run  mvn clean compile
* Run mvn exec:java


