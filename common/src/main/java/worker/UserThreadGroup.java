package worker;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by akhil on 25/9/16.
 */
class UserThreadGroup extends ThreadGroup {


    private static final String RECEIVER_THREAD_NAME = "_receiver";
    private static final String SENDER_THREAD_NAME = "_sender";
    private final Socket socket;
    private ReceiverThread receiverThread;
    private SenderThread senderThread;


    UserThreadGroup(String userName, Socket socket) {
        super(userName);
        this.socket = socket;

        initThreads(userName, socket);

    }

    private void initThreads(String userName, Socket socket) {


        senderThread = new SenderThread(this, userName + SENDER_THREAD_NAME, socket);


        receiverThread = new ReceiverThread(this, userName + RECEIVER_THREAD_NAME, socket);


    }

    public ReceiverThread getReceiverThread() {
        return receiverThread;
    }

    public SenderThread getSenderThread() {
        return senderThread;
    }

    public void sendMessage(String message) {

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


}
