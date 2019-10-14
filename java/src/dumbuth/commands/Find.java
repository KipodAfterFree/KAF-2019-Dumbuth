package dumbuth.commands;

import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;
import quteshell.commands.Help;

@Elevation(2)
@Help.Description("Lists all files in a directory (including subdirs")
public class Find implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        if (shell.getElevation() == 2) {

        } else {
            shell.writeln("Can't find at elevation " + shell.getElevation());
        }
    }
}
