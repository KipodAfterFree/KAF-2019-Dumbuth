package dumbuth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Main {

    private static final int PORT = 5387;
    private static final ArrayList<Lootshell> lootshells = new ArrayList<>();

    public static void main(String[] args) {

        try {
            ServerSocket welcomeSocket = new ServerSocket(PORT);
            while (true) {
                Socket connectionSocket = welcomeSocket.accept();
                lootshells.add(new Lootshell(connectionSocket));
            }
        } catch (Exception ignored) {

        }
    }

    private static class Lootshell {

        // ID
        private String id;

        // Comm
        private Thread thread;
        private Socket socket;
        private BufferedReader bin;
        private BufferedWriter bout;

        // Running
        private boolean running = true;

        // Auth
        private boolean authenticated = false;
        private String user = null;
        private String password = null;

        Lootshell(Socket socket) {
            thread = new Thread(() -> {
                try {
                    bin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    bout = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    welcome();
                    while (running) {
                        if (bin.ready()) {
                            receive(bin.readLine());
                        }
                        Thread.sleep(10);
                    }
                    socket.close();
                } catch (Exception ignored) {
                }
                thread.stop();
            });
            thread.start();
        }

        private void welcome() {
            transmitln("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            transmitln("Welcome to the mainframe!");
            transmitln("We'll need you to authenticate.");
            transmitln("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            transmit("name> ");
        }

        private void receive(String text) {
            System.out.println(text);
            // Evaluate command
            if (authenticated) {
                if (text.equals("hello")) {
                    transmitln("Hi there");
                } else if (text.equals("welcome")) {

                }
                transmit("t1ny:$> ");
            } else {
                if (user == null) {
                    user = text;
                    transmit("password> ");
                } else {
                    password = text;
                    authenticated = authenticate(user, password);
                    if (!authenticated) {
                        transmitln("Authentication failed");
                        running = false;
                    } else {
                        receive("welcome");
                    }
                }
            }
        }

        private void transmitln(String text) {
            transmit(text + "\n");
        }

        private void transmit(String text) {
            try {
                bout.write(text);
                bout.flush();
            } catch (Exception ignored) {
            }
        }

        private boolean authenticate(String user, String password) {
            return false;
        }

        public boolean isRunning() {
            return running;
        }
    }
}
