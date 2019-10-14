package quteshell.commands;

import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;

@Elevation(1)
@Help.Description("The welcome command displays a welcome message.")
public class Welcome implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.writeln("╔═══════════════════════════════════╗");
        shell.writeln("║        Welcome to Dumbuth!        ║");
        shell.writeln("║ You can type 'help' for commands. ║");
        shell.writeln("║    Use 'auth' to authenticate.    ║");
        shell.writeln("╚═══════════════════════════════════╝");
    }
}
