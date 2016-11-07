package worker;

import com.google.gson.Gson;
import model.Message;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Random;

/**
 * Created by akhil on 25/9/16.
 */
public class ReceiverThread extends Thread {

    public static long TIME_OUT = 3000;
    private final UserThreadGroup threadGroup;
    private final InputStream inputStream;
    private BufferedInputStream bufferedInputStream = null;
    private volatile boolean stopped = false;
    private long fileLength;


    public ReceiverThread(UserThreadGroup threadGroup, String receiver, InputStream inputStream) {
        super(threadGroup, receiver);
        this.threadGroup = threadGroup;
        bufferedInputStream = new BufferedInputStream(inputStream);
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        while (!stopped) {
            System.out.println("receiver loop");
            try {
                if (fileLength != 0) {
                    byte[] bytes = new byte[(int) fileLength];
                    IOUtils.readFully(inputStream, bytes);
                    fileLength = 0;
                    fileReceived(bytes);
                } else {
                    java.util.Scanner s = new java.util.Scanner(bufferedInputStream).useDelimiter("\n");
                    String receivedString = s.hasNext() ? s.next() : "";
                    textMessageReceived(receivedString);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }


    void fileReceived(byte[] bytes) {
        System.out.println(this.getName());
        try {
            Random random = new Random();
            String path = "/home/akhil/cnt/Received/test";

            if (this.getName().equalsIgnoreCase("server_receiver"))
                path += "server_receiver";

            FileOutputStream fileOuputStream = new FileOutputStream(path);
            fileOuputStream.write(bytes);
            fileOuputStream.flush();
            fileOuputStream.close();
            File file = new File(path);

            System.out.println("file received");
            threadGroup.onFileReceived(file);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    void textMessageReceived(String messageString) {
        Gson gson = new Gson();
        Message message = gson.fromJson(messageString, Message.class);
        fileLength = message.getFileLength();
        threadGroup.onMessageReceived(messageString);
    }

}
