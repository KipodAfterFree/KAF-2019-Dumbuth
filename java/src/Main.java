import quteshell.Quteshell;

import java.net.ServerSocket;
import java.util.ArrayList;

public class Main {

    //    private static final int PORT = 5386;
    private static final int PORT = 5387;
    public static final ArrayList<Quteshell> quteshells = new ArrayList<>();

    private static boolean listening = true;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (listening) {
                quteshells.add(new Quteshell(serverSocket.accept()).siblings(quteshells).begin());
            }
        } catch (Exception e) {
            System.out.println("Host - " + e.getMessage());
        }
    }
}
