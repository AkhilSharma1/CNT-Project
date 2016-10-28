package model;

/**
 * Created by akhil on 2/10/16.
 */
public class WelcomeMessage {
    private String assignedUserName;
    private String welcomeMessage;

    public WelcomeMessage(String assignedUserName, String welcomeMessage) {
        this.assignedUserName = assignedUserName;
        this.welcomeMessage = welcomeMessage;
    }

    public String getAssignedUserName() {
        return assignedUserName;
    }
}
