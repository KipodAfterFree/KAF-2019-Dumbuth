package dumbuth.commands;

import dumbuth.Path;
import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;
import quteshell.commands.Help;

@Elevation(3)
@Help.Description("ln creates a symbolic link for a file.\ne.g. 'ln source destination' or 'ln /default.txt /files/link.txt'")
public class ln implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        if (shell.getFileSystem() != null) {
            if (arguments != null) {
                String[] paths = arguments.split(" ");
                if (paths.length == 2) {
                    String[] levels = paths[1].split("/");
                    if (levels.length > 0) {
                        Path from = shell.getFileSystem().find(paths[0]);
                        if (from != null) {
                            Path to = from.createLink(levels[levels.length - 1]);
                            Path linkDirectory = shell.getFileSystem().find(paths[1].substring(0, paths[1].length() - (levels[levels.length - 1].length() + 1)));
                            if (linkDirectory!=null) {
                                if (linkDirectory.getType() == Path.Type.Directory) {
                                    linkDirectory.getChildren().add(to);
                                    shell.writeln(paths[0] + "->" + paths[1]);
                                } else {
                                    shell.writeln("Destination exists already");
                                }
                            }else {
                                shell.writeln("Destination does not exist");
                            }
                        } else {
                            shell.writeln("Origin does not exist");
                        }
                    }
                } else {
                    shell.writeln("Missing arguments");
                }
            } else {
                shell.writeln("Missing arguments");
            }
        } else {
            shell.writeln("Filesystem not mounted");
        }
    }
}
