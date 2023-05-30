package JiraAdapter;

public class JiraSettings {


    /**
     * SECTION 1 - Project Settings
     **/
    public static String siteName = "testresmig";
    public static String projectName = "TRM";
    public static String userName = "testresmig@gmail.com";
    public static String userToken = "uSCAvyWz5pNRsXmlZ7eIB831";
    public static String issueOpenStatus1 = "To Do";
    public static String issueOpenStatus2 = "In Progress";
    public static String issueOpenStatus3 = "Testing";
    public static int transitionIdDone = 31;
    public static boolean isNewIssueShouldBeMovedToSprint = false;


    /**
     * SECTION 2 - Jira API Settings
     * do not change until global Jira API settings changed
     **/
    public static String jiraEndpointIssue = ".atlassian.net/rest/api/3/issue/";
    //public static String jiraEndpointSearch = ".atlassian.net/rest/api/3/search?jql=summary~";
    public static String jiraEndpointSearch = ".atlassian.net/rest/api/3/search?jql=issuetype=Bug%20AND%20summary~";
    //"%20AND%20issuetype=Test"


    public static String jiraEndpointSprint = ".atlassian.net/rest/agile/1.0/sprint/";
    public static String jiraEndpointGreenhopper = ".atlassian.net/rest/greenhopper/latest/integration/teamcalendars/sprint/list?jql=project=";
    public static String jiraEndpointSprintSearch = "%20AND%20sprint%20IN%20openSprints()%20AND%20sprint%20NOT%20IN%20futureSprints()%20AND%20sprint%20NOT%20IN%20closedSprints()";


}
