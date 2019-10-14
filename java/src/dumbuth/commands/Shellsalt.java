package dumbuth.commands;

import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;
import quteshell.commands.Help;

import java.util.Random;

@Elevation(2)
@Help.Description("shellsalt returns the hash and salt for the (5 char) OTP.")
public class Shellsalt implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        if (shell.getElevation() == 2) {
            String password = random(5);
            String salt = random(10);
            String hashed = Auth.duthHash(password, salt, 10);
            shell.setOTP(hashed, salt);
            shell.writeln("duthHash(10) - " + hashed + " - " + salt);
        }
    }

    private String random(int length) {
        final String charset = "abcdefghijklmnopqrstuvwxyz";
        if (length > 0) {
            return charset.charAt(new Random().nextInt(charset.length())) + random(length - 1);
        }
        return "";
    }
}
