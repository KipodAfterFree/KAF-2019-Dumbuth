package quteshell.commands;

import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;

@Elevation(2)
@Help.Description("Lists all files in a directory (including subdirs")
public class Find extends Command {
    @Override
    public void execute(Quteshell shell, String arguments) {

    }
}
