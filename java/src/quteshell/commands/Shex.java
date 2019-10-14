package quteshell.commands;

import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;

@Elevation(3)
@History.Exclude
@Help.Description("shex runs the command you give it as a passthrough.")
public class Shex extends Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.execute(arguments);
    }
}
