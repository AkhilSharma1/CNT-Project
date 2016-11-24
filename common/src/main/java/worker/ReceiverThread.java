package worker;

import com.google.gson.Gson;
import model.Message;
import org.apache.commons.io.IOUtils;

import java.io.*;

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
    private String toUser;
    private String fileName;


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
                    System.out.println("1111111111111111111");
                    byte[] bytes = new byte[(int) fileLength];
                    IOUtils.readFully(inputStream, bytes);
                    fileLength = 0;


                    fileReceived(bytes);
                } else {
                    System.out.println("22222222222222");

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
            String path = "Received/";

            if (this.getName().equalsIgnoreCase("server_receiver")) //client side receiver thread
                path += toUser + "/";
            else
                path += "server/";    //server side receiver thread

            File file1 = new File(path);
            file1.mkdirs();


            path += fileName;

            FileOutputStream fileOuputStream = new FileOutputStream(path);
            fileOuputStream.write(bytes);
            fileOuputStream.flush();
            fileOuputStream.close();
            System.out.println("33333333333333333");

            File file = new File(path);
            System.out.println(file.getCanonicalPath());


            threadGroup.onFileReceived(file);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    void textMessageReceived(String messageString) {
        System.out.println("message string :" + messageString);
        Gson gson = new Gson();
        Message message = gson.fromJson(messageString, Message.class);
        fileLength = message.getFileLength();
        toUser = message.getToUser();
        fileName = message.getFileName();
        threadGroup.onMessageReceived(messageString);
    }

}
