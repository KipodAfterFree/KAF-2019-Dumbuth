package dumbuth.commands;

import org.json.JSONArray;
import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;
import quteshell.commands.Help;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

@Elevation(1)
@Help.Description("auth is used to authenticate, and if authenticated elevates you to :2\ne.g. 'auth user password'")
public class auth implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        if (shell.getElevation() == 1) {
            if (arguments != null) {
                String[] creds = arguments.split(" ");
                if (creds.length >= 2) {
                    String user = creds[0];
                    String password = creds[1];
                    if (authenticate(user, password)) {
                        shell.writeln("Authentication OK");
                        shell.setElevation(2);
                    } else {
                        shell.writeln("Authentication failed");
                        shell.finish();
                    }
                }
            }
        } else {
            shell.writeln("Can't authenticate at elevation " + shell.getElevation());
        }
    }

    private boolean authenticate(String user, String password) {
        try {
            String fileContents = Files.readAllLines(new File("/var/www/html/files/dumbuth/private/users.json").toPath()).get(0);
            JSONArray usersArray = new JSONArray(fileContents);
            for (int i = 0; i < usersArray.length(); i++) {
                JSONArray userArray = usersArray.getJSONArray(i);
                if (userArray.getString(0).equals(user)) {
                    if (userArray.getString(1).equals(duthHash(password, userArray.getString(2), 10)))
                        return true;
                }
            }
        } catch (Exception ignored) {
        }
        // TODO change to false
        return true;
    }

    static String duthHash(String secret, String salt, int rounds) {
        if (rounds == 0)
            return sha1(secret + salt);
        return sha1(salt + duthHash(secret, salt, rounds - 1));
    }

    private static String sha1(String password) {
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

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
