package dumbuth.commands;

import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;

@Elevation(2)
public class Elevate implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.writeln("Elevating to level 3...");
        shell.setElevation(3);
        shell.writeln("Elevated to level 3.");
    }
}
