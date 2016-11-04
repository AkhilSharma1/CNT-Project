import com.google.gson.Gson;
import model.Message;
import worker.ThreadManager;
import worker.WorkerContract;

import java.io.File;
import java.net.Socket;

/**
 * Created by akhil on 28/9/16.
 */
public abstract class Presenter implements PresenterContract, WorkerContract.onDataReceiveCallback {


    private final Gson gson;
    ViewContract view;
    //TODO use a dependency injector such as dagger2
    private WorkerContract worker;

    Presenter(ViewContract view) {
        gson = new Gson();
        this.view = view;
        worker = new ThreadManager(this);
    }


    public abstract void onTextMessageReceived(String userId, Message message);

    public abstract void onFileMessageReceived(String userId, Message message);


    public abstract void onStop();


    @Override
    public void onUserInput(String userInput) {
        //parse user input here
        if (userInput.equalsIgnoreCase("quit")) {
            stop();
        }
    }

    private void stop() {
        view.stopListeningToInput();
        //TODO signal all threads to stop
        onStop();
    }

    void sendMessage(String toUserId, Message message) {
        String messageJsonString = gson.toJson(message);
        worker.sendMessage(toUserId, messageJsonString);

    }

    void sendFile(String toUserId, File file) {
        worker.sendFile(toUserId, file);
    }


    void createNewConnection(Socket socket, String userName) {
        worker.addUser(socket, userName);
    }

    @Override
    final public void onMessageReceived(String fromUserId, String messageString) {

        Message message = gson.fromJson(messageString, Message.class);

        boolean isTextMessage = message.getMessage() != null;

        if (isTextMessage)
            onTextMessageReceived(fromUserId, message);
        else
            onFileMessageReceived(fromUserId, message);
    }
    }
