package worker;

import java.io.File;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by akhil on 25/9/16.
 */
public class ThreadManager implements WorkerContract {


    private static HashMap<String, UserThreadGroup> userIdThreadMap = new HashMap<>();
    private final onDataReceiveCallback presenter;


    public ThreadManager(WorkerContract.onDataReceiveCallback presenter) {
        this.presenter = presenter;
    }


    @Override
    public void sendMessage(String toUserId, String messageJsonString) {
        UserThreadGroup userThreadGroup = userIdThreadMap.get(toUserId);
        userThreadGroup.sendMessageJson(messageJsonString);
    }

    @Override
    public void sendFile(String toUserId, File file) {
        UserThreadGroup userThreadGroup = userIdThreadMap.get(toUserId);
        userThreadGroup.sendFile(file);
    }

    public void addUser(Socket socket, String userId) {
        UserThreadGroup threadGroup = new UserThreadGroup(this, userId, socket);
        userIdThreadMap.put(userId, threadGroup);
    }


    public void onFileReceived(String userId, File file) {
        presenter.onFileReceived(userId, file);
    }

    public void onMessageReceived(String userId, String messageString) {
        presenter.onMessageReceived(userId, messageString);
    }
}
