import model.Message;
import worker.ThreadManager;
import worker.WorkerContract;

import java.io.File;
import java.net.Socket;

/**
 * Created by akhil on 28/9/16.
 */
public abstract class Presenter implements PresenterContract, WorkerContract.onDataReceiveCallback {


    ViewContract view;
    //TODO use a dependency injector such as dagger2
    private WorkerContract worker;

    Presenter(ViewContract view) {
        this.view = view;
        worker = new ThreadManager();
        onStart();

    }

    WorkerContract getWorker() {
        return worker;
    }

    public abstract void sendData(Message message);

    public abstract void onStart();

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

    void sendMessage(String toUser, Message message) {
        worker.sendMessage(toUser, message);

    }

    void sendFile(String toUser, File file) {
        worker.sendFile(toUser, file);
    }


    void createNewConnection(Socket socket, String userName) {
        worker.addUser(socket, userName);
    }


    @Override
    public void onFileReceived(String user, File file) {

    }

    @Override
    public void onMessageReceived(Message message) {

    }

}
