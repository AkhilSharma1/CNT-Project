package model;

/**
 * Created by akhil on 31/10/16.
 */

/**
 * Model which is used to send data between client and server. If data is text,
 * it is sent with this object. If data is file, filename is sent with this
 * object and then the file is sent separately.
 * If toUser  and excludeUser are null, it is a broadcast.
 * If only toUser is null and excludeUser is not null, it is a blockcast
 */
public class Message {

    private String fromUser;
    private String toUser;
    private String excludeUser;
    private String filePath;
    private String message;

    public Message(String fromUser, String toUser, String excludeUser, String filePath, String message) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.excludeUser = excludeUser;
        this.filePath = filePath;
        this.message = message;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getToUser() {
        return toUser;
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
