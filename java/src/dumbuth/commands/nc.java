package dumbuth.commands;

import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;
import quteshell.commands.Help;

@Elevation(2)
@Help.Description("nc (NetCat) connects to given ShellID with a given OTP, then runs the given command as :2.\ne.g. 'nc ShellID OTP Command' or 'nc vy4eqeamoglrqt wkcxl echo Hi!'")
public class nc implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        String[] split = arguments.split(" ", 3);
        if (split.length == 3) {
            for (Quteshell quteshell : shell.getSiblings()) {
                if (quteshell.getID().equals(split[0])) {
                    shell.write("\033[1;31m");
                    shell.writeln("Shell found!");
                    shell.writeln("Connected to shell, authenticating...");
                    if (quteshell.getHash().equals(auth.duthHash(split[1], quteshell.getSalt(), 10))) {
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
