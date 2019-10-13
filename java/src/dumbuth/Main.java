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
import java.util.Random;

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

        private static final String NAME = "l00t";
        private static final String ELEVATED_HEADER = ":#>";
        private static final String NON_ELEVATED_HEADER = ":$>";

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
        private boolean elevated = false;
        private boolean authenticated = false;
        private String user = null;
        private String password = null;

        // UI
        private boolean header = true;


        Lootshell(Socket socket) {
            id = random(10);
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
            print("New shell");
            transmitln("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            transmitln("Welcome to the mainframe!");
            transmitln("We'll need you to authenticate.");
            transmitln("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            transmit("name> ");
        }

        private void receive(String text) {
            // Evaluate command
            if (authenticated) {
                Command command = new Command(text);
                print("Exec '" + command.getCommand() + "'");
                String[] args = command.getArguments();
                switch (command.getCommand()) {
                    case "hello": {
                        if (args.length > 0) {
                            String name = "";
                            for (String arg : args) name += " " + arg;
                            transmitln("Hi there," + name);
                        } else {
                            transmitln("Hi there!");
                        }
                        break;
                    }
                    case "welcome": {
                        receive("hold-header");
                        receive("clear");
                        transmitln("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                        transmitln("Welcome to lootshell");
                        transmitln("type 'help' to list all commands.");
                        transmitln("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                        break;
                    }
                    case "hold-header": {
                        header = false;
                        break;
                    }
                    case "clear": {
                        transmit("\033[2J\033[H");
                        break;
                    }
                    case "elevate":{
                        elevated = true;
                        break;
                    }
                    case "exit": {
                        receive("hold-header");
                        running = false;
                        break;
                    }
                    default: {
                        transmitln(NAME + ": " + command.getCommand() + ": not handled");
                    }
                }
                if (header)
                    transmit(NAME + (elevated ? ELEVATED_HEADER : NON_ELEVATED_HEADER) + " ");
                if (!command.getCommand().equals("hold-header"))
                    header = true;
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

        private String random(int length) {
            String chars = "0123456789abcdefghijklmnopqrstuvwxyz";
            char c = chars.charAt(new Random().nextInt(chars.length()));
            if (length > 0) {
                return c + random(length - 1);
            }
            return "";
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

        private void print(String text) {
            System.out.println(id + " - " + text);
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
            // TODO change to false
            return true;
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

        private class Command {
            private String command;
            private String[] arguments;

            Command(String text) {
                String[] split = text.split(" ");
                arguments = new String[split.length - 1];
                command = split[0];
                System.arraycopy(split, 1, arguments, 0, arguments.length);
            }

            public String getCommand() {
                return command;
            }

            public String[] getArguments() {
                return arguments;
            }
        }
    }
}
