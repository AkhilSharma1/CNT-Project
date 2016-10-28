package model;

/**
 * Created by akhil on 1/10/16.
 */
public class FileMessage {

    private String fromUser;
    private String excludeUser;
    private String filePath;
    private String message;

    public FileMessage(String fromUser, String touser, String excludeUser, String message, String filePath) {
        this.fromUser = fromUser;
        this.touser = touser;
        this.excludeUser = excludeUser;
        this.message = message;
        this.filePath = filePath;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getTouser() {
        return touser;
    }

    public String getExcludeUser() {
        return excludeUser;
    }


    public String getFilePath() {
        return filePath;
    }

    public String getMessage() {
        return message;
    }
}
