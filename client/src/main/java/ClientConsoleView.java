import java.util.Scanner;

/**
 * Created by akhil on 29/10/16.
 */
public class ClientConsoleView implements ViewContract {


    private boolean keepRunning = true;
    private Presenter presenter;


    public ClientConsoleView(String userName) {

        presenter = new ClientPresenter(this, userName);

        startListeningToInput();
    }

    @Override
    public void showOutput(String output) {
        System.out.println(output);
    }


    public void startListeningToInput() {
        Scanner userInput = new Scanner(System.in);

        while (keepRunning) {
            String input = userInput.nextLine();
            presenter.onUserInput(input);
        }
        //TODO destroy other threads indirectly.
    }

    @Override
    public void stopListeningToInput() {
        keepRunning = false;

    }

}
