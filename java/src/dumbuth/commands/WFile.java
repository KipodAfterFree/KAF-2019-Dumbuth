package dumbuth.commands;

import dumbuth.Path;
import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;
import quteshell.commands.Help;

@Elevation(3)
@Help.Description("WriteFILE is a command that takes args=[file, contents] and writes contents to file.")
public class WFile implements Command {

    @Override
    public void execute(Quteshell shell, String arguments) {
        if (shell.getFileSystem() != null) {
            if (arguments != null) {
                String[] args = arguments.split(" ", 2);
                if (args.length > 1) {
                    Path file = shell.getFileSystem().find(args[0]);
                    file.setContents(args[1]);
                }
            } else {
                shell.writeln("Missing arguments");
            }
        } else {
            shell.writeln("Filesystem not mounted");
        }
    }
}
