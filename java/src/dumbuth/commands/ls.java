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
            Path dir = shell.getFileSystem();
            if (arguments != null)
                dir = dir.find(arguments);
            if (dir != null) {
                ls(shell, dir);
            } else {
                shell.writeln("Path does not exist");
            }
        } else {
            shell.writeln("Filesystem not mounted");
        }
    }

    private void ls(Quteshell shell, Path path) {
        if (path.getType() == Path.Type.Directory) {
            for (Path child : path.getChildren()) {
                shell.writeln(child.getName());
            }
        } else {
            shell.writeln("Not a directory");
        }
    }
}
