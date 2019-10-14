package quteshell.commands;

import quteshell.Path;
import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;

@Elevation(3)
@Help.Description("The ln command creates a file link in the file system args=[src, dest]")
public class Ln extends Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        if (shell.getFileSystem() != null) {
            if (arguments != null) {
                String[] paths = arguments.split(" ");
                if (paths.length > 1) {
                    String[] levels = paths[1].split("/");
                    Path from = shell.getFileSystem().find(paths[0]);
                    Path to = from.createLink(levels[levels.length - 1]);
                    String dirPath = "";
                    for (int i = 0; i < levels.length - 1; i++) {
                        if (dirPath.length() > 0)
                            dirPath += "/";
                        dirPath += levels[i];
                    }
                    Path dir = shell.getFileSystem().find(dirPath);
                    if (dir.getType() == Path.Type.Directory) {
                        dir.getChildren().add(to);
                        shell.writeln(paths[0] + "->" + paths[1]);
                    }
                } else {
                    shell.writeln("ln requires 2 arguments");
                }
            } else {
                shell.writeln("Missing arguments");
            }
        } else {
            shell.writeln("Filesystem not mounted");
        }
    }
}
