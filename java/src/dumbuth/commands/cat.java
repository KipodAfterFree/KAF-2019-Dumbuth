package dumbuth.commands;

import dumbuth.Path;
import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;
import quteshell.commands.Help;

@Elevation(2)
@Help.Description("cat reads a file and outputs it to console.\ne.g. 'cat /default.txt'")
public class cat implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        try {
            if (shell.getElevation() == 2) {
                if (shell.getFileSystem() != null) {
                    if (arguments != null) {
                        Path file = shell.getFileSystem().find(arguments);
                        cat(shell, file);
                    } else {
                        shell.writeln("Missing argument");
                    }
                } else {
                    shell.writeln("Filesystem not mounted");
                }
            } else {
                shell.writeln("Can't cat at elevation " + shell.getElevation());
            }
        } catch (Exception e) {
            shell.writeln(e.getMessage());
        }
    }

    private void cat(Quteshell shell, Path file) {
        if (file != null) {
            if (file.getType() == Path.Type.File) {
                shell.writeln(file.getContents());
            } else if (file.getType() == Path.Type.Link) {
                cat(shell, file.getDestination());
            } else {
                shell.writeln("Can't cat directories");
            }
        } else {
            shell.writeln("Path does not exist");
        }
    }
}
