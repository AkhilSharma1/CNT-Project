package worker;

import model.Message;

import java.io.File;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by akhil on 25/9/16.
 */
public class ThreadManager implements WorkerContract {

    private static ThreadManager instance = null;

    private static HashMap<String, UserThreadGroup> userThreadMap = new HashMap<>();


    public ThreadManager() {
        //avoid instantiation
    }

    public static ThreadManager getInstance() {
        if (instance == null) {
            instance = new ThreadManager();
        }
        return instance;
    }


    @Override
    public void sendMessage(String toUser, Message message) {

    }

    @Override
    public void sendFile(String toUser, File file) {

    }

    public void addUser(Socket socket, String userName) {
        UserThreadGroup threadGroup = new UserThreadGroup(this, userName, socket);
        userThreadMap.put(userName, threadGroup);

    }

    public void removeUser(String userName) {

        UserThreadGroup userThreadGroup = userThreadMap.get(userName);
        //TODO: signal threads to stop, isDaemon?
        userThreadGroup.destroy();

        userThreadMap.remove(userName);
    }


    public void onFileReceived(File file) {

    }

    public void onMessageReceived(Message message) {


    }
}
