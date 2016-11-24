import model.Message;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by akhil on 29/9/16.
 */
public class ClientPresenter extends Presenter {

    public final String SERVER_NAME = "server";
    private final String userName;

    public ClientPresenter(ClientConsoleView clientConsoleView, String userName) {
        super(clientConsoleView, userName);
        this.userName = userName;

        onStart();
    }

    public void onStart() {
        //initiate a connection to Server
        try {
            Socket requestSocket = new Socket("localhost", 8080);
            createNewConnection(requestSocket, SERVER_NAME);
            //send a broadcast message to all other users
            Message joinBroadcastMessage = new Message(userName, null, null, null, 0, "@" + userName + " joined!");
            sendMessage(SERVER_NAME, joinBroadcastMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        System.exit(0);
    }


    @Override
    public void onUserInput(String userInput) {
        super.onUserInput(userInput);
        Message message = processUserInput(userInput);
        if (message == null) {
            view.showOutput("Invalid command, please try again");
            return;
        }
        sendData(message);
    }

    public void sendData(Message message) {

        if (message.getFileName() != null) {
            File file = new File(message.getFileName());
            try {
                message.setFileData(FileUtils.readFileToByteArray(file));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        sendMessage(SERVER_NAME, message);
    }

    @Override
    public void onTextMessageReceived(String fromUserId, Message message) {
        String fromUser = message.getFromUser();
        String textMessage = message.getMessage();

        String consoleOutput = "@" + fromUser + " : " + textMessage;
        view.showOutput(consoleOutput);
    }

    @Override
    public void onFileMessageReceived(String fromUserId, Message message) {
        String fromUser = message.getFromUser();
        String fileName = message.getFileName();

        String consoleOutput = "@" + fromUser + " : " + "incoming file " + fileName;
        view.showOutput(consoleOutput);
    }


    @Nullable
    private Message processUserInput(String userInput) {
        boolean isUnicast = false;
        boolean isBroadcast = false;
        boolean isBlockcast = false;

        boolean isFile = false;
        boolean isText;

        String messageText = null;
        String filePath = null;
        File file = null;

        String clientName = null;

        Message message = null;


        String[] inputArray = userInput.trim().split(" ");
        switch (inputArray[0]) {
            case "unicast":
                isUnicast = true;
                break;
            case "broadcast":
                isBroadcast = true;
                break;
            case "blockcast":
                isBlockcast = true;
                break;
            default:
                return null;
        }

        switch (inputArray[1]) {
            case "file":
                isFile = true;
                isText = false;
                filePath = inputArray[2];
                file = new File(filePath);
                break;

            case "message":
                isText = true;
                isFile = false;
                messageText = inputArray[2];
                break;
            default:
                return null;

        }

        if (!isBroadcast) {
            //client to send message if unicast, excluded client if blockcast
            clientName = inputArray[3];
        }

        if (isBroadcast) {
            if (isFile) {
                message = new Message(userName, null, null, file.getName(), file.length(), null);
            } else if (isText) {
                message = new Message(userName, null, null, null, 0, messageText);
            }

        } else if (isBlockcast) {
            if (isFile) {
                message = new Message(userName, null, clientName, file.getName(), file.length(), null);
            } else if (isText) {
                message = new Message(userName, null, clientName, null, 0, messageText);
            }

        } else if (isUnicast) {
            if (isFile) {
                message = new Message(userName, clientName, null, file.getName(), file.length(), null);
            } else if (isText) {
                message = new Message(userName, clientName, null, null, 0, messageText);
            }

        }

        return message;
    }


    @Override
    public void onFileReceived(String fromUserId, File file) {
        String consoleOutput = "@" + fromUserId + " : " + " file " + file.getName() +
                " received successfully";
        view.showOutput(consoleOutput);

    }


}
