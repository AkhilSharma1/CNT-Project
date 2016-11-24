import model.Message;
import worker.ThreadManager;
import worker.WorkerContract;

import java.net.Socket;

/**
 * Created by akhil on 28/9/16.
 */
public abstract class Presenter implements PresenterContract, WorkerContract.onDataReceiveCallback {


    ViewContract view;
    //TODO use a dependency injector such as dagger2
    private WorkerContract worker;

    Presenter(ViewContract view, String userName) {
        this.view = view;
        worker = new ThreadManager(this, userName);
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
        worker.sendMessage(toUserId, message);
    }


    void createNewConnection(Socket socket, String userName) {
        worker.addUser(socket, userName);
    }

    @Override
    final public void onMessageReceived(String fromUserId, Message message) {


        boolean isTextMessage = message.getMessage() != null;

        if (isTextMessage)
            onTextMessageReceived(fromUserId, message);
        else
            onFileMessageReceived(fromUserId, message);
    }
    }
