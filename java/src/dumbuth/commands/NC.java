package dumbuth.commands;

import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;
import quteshell.commands.Help;

@Elevation(2)
@Help.Description("The NetCat command takes as args=[ShellID, OTP, Command], runs command as :2 in ShellID.\nRequires the correct OneTimePassword!")
public class NC implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        String[] split = arguments.split(" ", 3);
        if (split.length > 2) {
            for (Quteshell quteshell : shell.getSiblings()) {
                if (quteshell.getID().equals(split[0])) {
                    shell.write("\033[1;31m");
                    shell.writeln("Shell found!");
                    shell.writeln("Connected to shell, authenticating...");
                    if (quteshell.getHash().equals(Auth.duthHash(split[1], quteshell.getSalt(), 10))) {
                        shell.writeln("Authentication OK");
                        shell.writeln("Executing '" + split[2] + "' as :2 in shell '" + split[0] + "'");
                        int prev = quteshell.getElevation();
                        quteshell.setElevation(2);
                        quteshell.execute(split[2]);
                        quteshell.setElevation(prev);
                        quteshell.setOTP("", "");
                        shell.writeln("Execution complete.");
                    } else {
                        shell.writeln("Authentication failure");
                    }
                    shell.writeln("Disconnected from shell");
                    shell.write("\033[0m");
                }
            }
        }
    }
}
