package dumbuth.commands;

import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;
import quteshell.command.Exclude;
import quteshell.commands.Help;
import quteshell.commands.History;

@Elevation(3)
@Exclude
@Help.Description("shex runs the command you give it as a passthrough.")
public class Shex implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.execute(arguments);
    }
}
