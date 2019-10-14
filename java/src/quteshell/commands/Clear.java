package quteshell.commands;

import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;

@Elevation(1)
@Help.Description("The clear command clears the client's terminal.")
public class Clear extends Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.clear();
    }
}
