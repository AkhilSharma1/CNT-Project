package worker;

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

    public static void createThreadsForUser(String userName) {


    }


    @Override
    public void addUser(String userName, Socket socket) {
        UserThreadGroup threadGroup = new UserThreadGroup(userName, socket);
        userThreadMap.put(userName, threadGroup);

    }

    @Override
    public void removeUser(String userName) {

        UserThreadGroup userThreadGroup = userThreadMap.get(userName);
        //TODO: signal threads to stop, isDaemon?
        userThreadGroup.destroy();

        userThreadMap.remove(userName);
    }

    @Override
    public void sendMessage(String userName, String message) {
        UserThreadGroup userThreadGroup = userThreadMap.get(userName);
        userThreadGroup.sendMessage(message);
    }


}
