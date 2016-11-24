package worker;

import model.Message;

import java.io.File;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by akhil on 25/9/16.
 */
public class ThreadManager implements WorkerContract {


    public static String userName;
    private static HashMap<String, UserThreadGroup> userIdThreadMap = new HashMap<>();
    private final onDataReceiveCallback presenter;


    public ThreadManager(onDataReceiveCallback presenter, String userName) {
        this.presenter = presenter;
        ThreadManager.userName = userName;
    }


    @Override
    public void sendMessage(String toUserId, Message message) {
        UserThreadGroup userThreadGroup = userIdThreadMap.get(toUserId);
        userThreadGroup.sendMessage(message);
    }



    public void addUser(Socket socket, String userId) {
        UserThreadGroup threadGroup = new UserThreadGroup(this, userId, socket);
        userIdThreadMap.put(userId, threadGroup);
    }


    public void onFileReceived(String userId, File file) {
        presenter.onFileReceived(userId, file);
    }

    public void onMessageReceived(String userId, Message message) {
        presenter.onMessageReceived(userId, message);
    }
}
