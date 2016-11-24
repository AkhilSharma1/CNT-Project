package worker;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by akhil on 25/9/16.
 */
class UserThreadGroup extends ThreadGroup {


    private static final String RECEIVER_THREAD_NAME = "_receiver";
    private static final String SENDER_THREAD_NAME = "_sender";
    private final ThreadManager threadManager;
    private final String userId;
    private final Socket socket;
    private ReceiverThread receiverThread;
    private SenderThread senderThread;
    private ArrayBlockingQueue arrayBlockingQueue;
    UserThreadGroup(ThreadManager threadManager, String userId, Socket socket) {
        super(userId);
        this.threadManager = threadManager;
        this.userId = userId;
        this.socket = socket;

        try {
            initThreads(userId, socket);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getUserId() {
        return userId;
    }

    private void initThreads(String userName, Socket socket) throws IOException {

        arrayBlockingQueue = new ArrayBlockingQueue(10);

        senderThread = new SenderThread(this, userName + SENDER_THREAD_NAME,
                socket.getOutputStream(), arrayBlockingQueue);
        receiverThread = new ReceiverThread(this, userName + RECEIVER_THREAD_NAME, socket.getInputStream());

        senderThread.start();
        receiverThread.start();
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


    public void sendMessageJson(String jsonString) {
        try {
            arrayBlockingQueue.put(jsonString);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendFile(File file) {
        try {
            arrayBlockingQueue.put(file);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onFileReceived(File file) {
        threadManager.onFileReceived(userId, file);
    }

    public void onMessageReceived(String messageString) {
        threadManager.onMessageReceived(userId, messageString);
    }
}
