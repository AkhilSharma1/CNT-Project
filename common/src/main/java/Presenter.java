import worker.ThreadManager;
import worker.UserInputThreadUtil;
import worker.WorkerContract;

/**
 * Created by akhil on 28/9/16.
 */
public abstract class Presenter implements UserInputThreadUtil.UserInputListener {

    private UserInputThreadUtil userInputThreadUtil;

    //TODO use a dependency injector such as dagger2
    private WorkerContract worker;


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

        worker = new ThreadManager();

    }

    protected void stop() {
        userInputThreadUtil.stopListeningToConsoleInput();
        //TODO signal all threads to stop
        onStop();
    }

    public WorkerContract getWorker() {
        return worker;
    }
}
