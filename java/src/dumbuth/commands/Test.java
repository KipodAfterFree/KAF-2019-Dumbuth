package dumbuth.commands;

import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;

@Elevation(2)
public class Test implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.setElevation(2);
    }
}
