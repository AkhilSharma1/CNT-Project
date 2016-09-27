import java.util.Scanner;

/**
 * Created by akhil on 27/9/16.
 */
public class UserInputThreadUtil {

    private final UserInputListener receiver;
    private boolean keepRunning = true;


    public UserInputThreadUtil(UserInputListener receiver) {
        this.receiver = receiver;
    }

    public void startListeningToConsoleInput() {

        Scanner userInput = new Scanner(System.in);

        while (keepRunning) {
            String input = userInput.next();
            receiver.onUserInput(input);
        }

        //TODO destroy other threads indirectly.
    }

    public void stopListeningToConsoleInput() {
        keepRunning = false;

    }

    interface UserInputListener {
        void onUserInput(String userInput);
    }


}
