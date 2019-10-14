package quteshell.commands;

import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;

@Elevation(2)
@Help.Description("The id command prints the ID of the shell.")
public class ID implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.writeln(shell.getID());
    }
}
