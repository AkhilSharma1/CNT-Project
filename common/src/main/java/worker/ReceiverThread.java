package worker;

import model.Message;

import java.io.*;

/**
 * Created by akhil on 25/9/16.
 */
public class ReceiverThread extends Thread {

    public static long TIME_OUT = 3000;
    private final UserThreadGroup threadGroup;
    private ObjectInputStream objectInputStream;
    private volatile boolean stopped = false;
    private long fileLength;
    private String toUser;
    private String fileName;


    public ReceiverThread(UserThreadGroup threadGroup, String receiver, InputStream inputStream) {
        super(threadGroup, receiver);
        this.threadGroup = threadGroup;
        try {
            objectInputStream = new ObjectInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!stopped) {
            Message message = null;

            try {
                message = (Message) objectInputStream.readObject();
                fileName = message.getFileName();

                textMessageReceived(message);
                if (fileName != null) {
                    fileLength = message.getFileLength();
                    toUser = message.getToUser();
                    fileReceived(message.getFileData());
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);

            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);

            } finally {
            }


        }
    }


    void fileReceived(byte[] bytes) {


        try {
            String path = "Received" + File.separator;

            if (this.getName().equalsIgnoreCase("server_receiver")) //client side receiver thread
                path += ThreadManager.userName + File.separator;
            else
                path += "server" + File.separator;    //server side receiver thread

            File file1 = new File(path);
            file1.mkdirs();


            path += fileName;

            FileOutputStream fileOuputStream = new FileOutputStream(path);
            fileOuputStream.write(bytes);
            fileOuputStream.flush();
            fileOuputStream.close();

            File file = new File(path);
            System.out.println(file.getCanonicalPath());


            threadGroup.onFileReceived(file);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    void textMessageReceived(Message message) {

        threadGroup.onMessageReceived(message);
    }

}
