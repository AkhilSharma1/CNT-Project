package worker;

import model.Message;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by akhil on 25/9/16.
 */
class UserThreadGroup extends ThreadGroup {


    private static final String RECEIVER_THREAD_NAME = "_receiver";
    private static final String SENDER_THREAD_NAME = "_sender";
    private final ThreadManager threadManager;
    private final Socket socket;
    private ReceiverThread receiverThread;
    private SenderThread senderThread;


    UserThreadGroup(ThreadManager threadManager, String userName, Socket socket) {
        super(userName);
        this.threadManager = threadManager;
        this.socket = socket;

        try {
            initThreads(userName, socket);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initThreads(String userName, Socket socket) throws IOException {

        senderThread = new SenderThread(this, userName + SENDER_THREAD_NAME, socket.getOutputStream());
        receiverThread = new ReceiverThread(this, userName + RECEIVER_THREAD_NAME, socket.getInputStream());
    }

    @Override
    public synchronized boolean isDestroyed() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.isDestroyed();

    }


    @Override
    protected void finalize() throws Throwable {
        socket.close();
        super.finalize();
    }


    public void sendMessage(Message data) {
        senderThread.sendMessage(data);
    }

    public void sendFile(File file) {
        senderThread.sendFile(file);
    }


    public void onFileReceived(File file) {
        threadManager.onFileReceived(file);


    }

    public void onMessageReceived(Message message) {
        threadManager.onMessageReceived(message);


    }
}
