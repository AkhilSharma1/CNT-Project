package model;

/**
 * Created by akhil on 31/10/16.
 */

import java.io.Serializable;

/**
 * Model which is used to send data between client and server. If data is text,
 * it is sent with this object. If data is file, filename is sent with this
 * object and then the file is sent separately.
 * If toUser  and excludeUser are null, it is a broadcast.
 * If only toUser is null and excludeUser is not null, it is a blockcast
 */
public class Message implements Serializable {

    private String fromUser;
    private String toUser;
    private String excludeUser;
    private String fileName;
    private long fileLength;
    private String message;
    private byte[] fileData;


    public Message(String fromUser, String toUser, String excludeUser, String fileName, long fileLength, String message) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.excludeUser = excludeUser;
        this.fileName = fileName;
        this.fileLength = fileLength;
        this.message = message;
    }

    public long getFileLength() {
        return fileLength;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getExcludeUser() {
        return excludeUser;
    }

    public String getFileName() {
        return fileName;
    }

    public String getMessage() {
        return message;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
}
