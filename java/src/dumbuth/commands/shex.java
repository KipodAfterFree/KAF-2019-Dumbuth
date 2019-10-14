package dumbuth.commands;

import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;
import quteshell.command.Exclude;
import quteshell.commands.Help;
import quteshell.commands.History;

@Elevation(3)
@Exclude
@Help.Description("shex runs the command given as arguments.\ne.g. 'shex echo Hi'")
public class shex implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.execute(arguments);
    }
}
