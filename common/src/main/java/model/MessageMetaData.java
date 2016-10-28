package model;

/**
 * Created by akhil on 24/10/16.
 */
public class MessageMetaData {

    private Enum<DataTypes> dataType;
    private String dataString;
    private String toUser;

    public MessageMetaData(Enum<DataTypes> dataType, String dataString, String toUser) {
        this.dataType = dataType;
        this.dataString = dataString;
        this.toUser = toUser;
    }

    public enum DataTypes {
        WELCOMEMESSAGE, TEXTMESSAGE, FILEMESSAGE
    }
}
