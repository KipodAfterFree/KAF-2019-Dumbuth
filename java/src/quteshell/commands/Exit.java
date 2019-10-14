package quteshell.commands;

import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;

@Elevation(1)
@Help.Description("The exit command closes the shell and disconnects.")
public class Exit extends Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.finish();
    }
}
