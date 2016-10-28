package worker;

import com.google.gson.Gson;
import model.TextMessage;

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


    public void addUser(Socket socket, String userName) {
        UserThreadGroup threadGroup = new UserThreadGroup(userName, socket);
        userThreadMap.put(userName, threadGroup);

    }

    public void removeUser(String userName) {

        UserThreadGroup userThreadGroup = userThreadMap.get(userName);
        //TODO: signal threads to stop, isDaemon?
        userThreadGroup.destroy();

        userThreadMap.remove(userName);
    }


    @Override
    public void sendData(String newUser, String dataString) {

    }


    @Override
    public void sendData(TextMessage data) {
        String userName = data.getFromUser();
        UserThreadGroup userThreadGroup = userThreadMap.get(userName);

        Gson gson = new Gson();
        String string = gson.toJson(data);
        userThreadGroup.sendString(string);

    }


}
