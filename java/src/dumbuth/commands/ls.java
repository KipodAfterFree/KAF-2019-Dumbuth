package dumbuth.commands;

import dumbuth.Path;
import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;
import quteshell.commands.Help;

@Elevation(2)
@Help.Description("ls lists the files and folders in a given path.\ne.g. 'ls /'")
public class ls implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        if (shell.getFileSystem() != null) {
            String path = "/";
            if (arguments != null)
                path = arguments;
            Path dir = shell.getFileSystem().find(path);
            ls(shell, dir);
        } else {
            shell.writeln("Filesystem not mounted");
        }
    }

    private void ls(Quteshell shell, Path path) {
        if (path != null) {
            if (path.getType() == Path.Type.Directory) {
                for (Path child : path.getChildren()) {
                    shell.writeln(child.getName());
                }
            } else if (path.getType() == Path.Type.Link) {
                ls(shell, path.getDestination());
            }
        }
    }
}
