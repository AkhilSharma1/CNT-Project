import model.Message;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Created by akhil on 26/9/16.
 */
public class ServerPresenter extends Presenter {

    private volatile boolean keepRunning = true;
    private volatile ServerSocket serverSocket;
    private HashMap<String, String> userMap = new HashMap<>();
    private int userIdCounter = 1;
    private ArrayList<Message> waitingList = new ArrayList<>();

    public ServerPresenter(ViewContract view) {
        super(view);
        onStart();
    }


    public void onStart() {
        view.showOutput("Welcome! Server is starting...");
        view.showOutput("Type quit to close server");

        //TODO read port number from a config file
        final int sPort = 8080;
        try {
            serverSocket = new ServerSocket(sPort);
        } catch (IOException e) {
            e.printStackTrace();
        }

        startServerSocketListenerThread();

    }

    @Override
    public void onStop() {
        keepRunning = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        view.showOutput("server shutting down...");
    }


    private void startServerSocketListenerThread() {


        try {
            new Thread() {
                //The server will be listening on this port number

                //TODO learn to interrupt this thread

                @Override
                public void run() {

                    try {
                        while (keepRunning) {
                            Socket socket = serverSocket.accept();
                            ServerPresenter.this.newConnection(socket);
                        }
                    } catch (IOException e) {
                    } finally {
                        try {
                            serverSocket.close();
                        } catch (IOException e) {
                        }
                    }

                }
            }.start();


        } catch (Exception e) {
            //TODO HANDLE
            e.printStackTrace();
        }


    }

    private void newConnection(Socket socket) {
        String newUserId = newUserId();
        createNewConnection(socket, newUserId);
        userMap.put(newUserId, null);

    }

    private void sendWelcomeMessage(String newUserName) {
        String welcomeMessageText = createWelcomeMessage(newUserName,
                new ArrayList<String>(userMap.values()));
        Message welcomeMessage = new Message("server", newUserName, null, null, 0, welcomeMessageText);

        sendMessage(getUserIdFromUserName(newUserName), welcomeMessage);
    }

    private String createWelcomeMessage(String newUserName, ArrayList<String> users) {
        return "Welcome " + newUserName + "! Online userIds are :" + users.toString();
    }


    private String newUserId() {
        return "user" + userIdCounter++;

    }

    @Override
    public void onTextMessageReceived(String fromUserId, Message message) {
        addUserNameIfNotPresent(fromUserId, message);
        processReceivedMessage(message, null);
    }

    private void addUserNameIfNotPresent(String fromUserId, Message message) {
        String userName = userMap.get(fromUserId);
        String fromUserName = message.getFromUser();

        if (userName == null) {
            userMap.put(fromUserId, fromUserName);
            //new user, send welcome message
            sendWelcomeMessage(fromUserName);

        }

    }

    @Override
    public void onFileMessageReceived(String fromUserId, Message message) {
        waitingList.add(message);
    }


    @Override
    public void onFileReceived(String fromUserId, File file) {
        String fileName = file.getName();

        for (ListIterator<Message> iterator = waitingList.listIterator(); iterator.hasNext(); ) {
            Message message = iterator.next();

            String fromId = getUserIdFromUserName(message.getFromUser());
            if (fromId.equalsIgnoreCase(fromUserId) && fileName.equalsIgnoreCase(message.getFileName())) {
                iterator.remove();
                processReceivedMessage(message, file);
            }
        }
    }


    private void processReceivedMessage(Message message, File file) {
        boolean isUnicast;
        boolean isBroadcast;
        boolean isBlockcast;
        boolean isFileMessage = file != null;

        String toUserName = message.getToUser();
        String fromUserName = message.getFromUser();
        String excludeUserName = message.getExcludeUser();

        isUnicast = toUserName != null;
        isBlockcast = toUserName == null && excludeUserName != null;
        isBroadcast = toUserName == null && excludeUserName == null;

        if (isUnicast) {
            sendMessage(getUserIdFromUserName(toUserName), message);
            if (isFileMessage) {
                sendFile(getUserIdFromUserName(toUserName), file);
                view.showOutput("@" + fromUserName + " sending file to @" + toUserName);

            }
            return;
        }

        if (isBlockcast) {
            sendMultipleMessages(fromUserName, excludeUserName, message);
            if (isFileMessage) {
                sendMultipleFiles(fromUserName, excludeUserName, file);
                view.showOutput("@" + fromUserName + " sending file to all users except @" + excludeUserName);

            }
            return;
        }

        if (isBroadcast) {
            System.out.println("in broadcast");
            sendMultipleMessages(fromUserName, null, message);
            if (isFileMessage) {
                sendMultipleFiles(fromUserName, null, file);
                view.showOutput("@" + fromUserName + " sending file to all users");
            }
        }

    }

    private String getUserIdFromUserName(String toUserName) {
        for (Map.Entry<String, String> entry : userMap.entrySet()) {
            if (Objects.equals(toUserName, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void sendMultipleFiles(String fromUserName, String excludeUserName, File file) {

        for (Map.Entry<String, String> entry : userMap.entrySet()) {
            String userId = entry.getKey();
            String userName = entry.getValue();
            if (!(userName.equalsIgnoreCase(fromUserName) || userName.equalsIgnoreCase(excludeUserName))) {
                sendFile(userId, file);
            }
        }
    }

    private void sendMultipleMessages(String fromUserName, String excludeUserName, Message message) {
        //send message to all except sender and blocked userIds
        for (Map.Entry<String, String> entry : userMap.entrySet()) {
            String userId = entry.getKey();
            String userName = entry.getValue();
            if (!(userName.equalsIgnoreCase(fromUserName) || userName.equalsIgnoreCase(excludeUserName))) {
                sendMessage(userId, message);
            }
        }
    }
}
