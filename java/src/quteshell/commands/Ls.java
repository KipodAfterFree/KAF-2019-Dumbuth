package quteshell.commands;

import quteshell.Path;
import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;

@Elevation(2)
public class Ls extends Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        if (shell.getFileSystem()!=null) {
            String path = "/";
            if (arguments != null)
                path = arguments;
            Path dir = shell.getFileSystem().find(path);
            if (dir.getType() == Path.Type.Directory) {
                for (Path child : dir.getChildren()) {
                    shell.writeln(child.getName());
                }
            }
        }else {
            shell.writeln("Filesystem not mounted");
        }
    }
}
