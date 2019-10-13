package dumbuth;

import org.json.JSONArray;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;

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
                transmit("l00t:$> ");
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
            try {
                String filecontents = Files.readAllLines(new File("/var/www/html/files/dumbuth/private/users.json").toPath()).get(0);
                JSONArray usersArray = new JSONArray(filecontents);
                for (int i = 0; i < usersArray.length(); i++) {
                    JSONArray userArray = usersArray.getJSONArray(i);
                    if (userArray.getString(0).equals(user)) {
                        if (userArray.getString(1).equals(duth_hash(password, userArray.getString(2), 100)))
                            return true;
                    }
                }
            } catch (Exception ignored) {
            }
            return false;
        }

        private String duth_hash(String secret, String salt, int rounds) {
            if (rounds == 0)
                return sha1(secret + salt);
            return sha1(salt + duth_hash(secret, salt, rounds - 1));
        }

        private String sha1(String password) {
            String sha1 = "";
            try {
                MessageDigest crypt = MessageDigest.getInstance("SHA-1");
                crypt.reset();
                crypt.update(password.getBytes(StandardCharsets.UTF_8));
                sha1 = byteToHex(crypt.digest());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return sha1;
        }

        private String byteToHex(final byte[] hash) {
            Formatter formatter = new Formatter();
            for (byte b : hash) {
                formatter.format("%02x", b);
            }
            String result = formatter.toString();
            formatter.close();
            return result;
        }

        public boolean isRunning() {
            return running;
        }
    }
}
