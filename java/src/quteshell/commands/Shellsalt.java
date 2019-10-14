package quteshell.commands;

import quteshell.Quteshell;
import quteshell.Toolbox;
import quteshell.command.Command;
import quteshell.command.Elevation;

@Elevation(2)
@Help.Description("shellsalt returns the hash and salt for the (5 char a-z) OTP.")
public class Shellsalt extends Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        if (shell.getElevation() == 2) {
            String password = Toolbox.random(5);
            String salt = Toolbox.random(10);
            String hashed = Auth.duthHash(password, salt, 10);
            shell.setOTP(hashed, salt);
            shell.writeln("duthHash(10) - " + hashed + " - " + salt);
        }
    }
}
