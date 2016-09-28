/**
 * Created by akhil on 28/9/16.
 */
public abstract class Program implements UserInputThreadUtil.UserInputListener {

    private UserInputThreadUtil userInputThreadUtil;


    public abstract void processUserInput(String userInput);

    public abstract void onStart();

    public abstract void onStop();

    @Override
    public void onUserInput(String userInput) {
        //parse user input here
        if (userInput.equalsIgnoreCase("quit")) {
            stop();
        }
        processUserInput(userInput);
    }

    public final void init() {
        onStart();

        //start listening to user input
        userInputThreadUtil = new UserInputThreadUtil(this);
        userInputThreadUtil.startListeningToConsoleInput();
    }

    protected void stop() {
        userInputThreadUtil.stopListeningToConsoleInput();

        onStop();
    }

}
