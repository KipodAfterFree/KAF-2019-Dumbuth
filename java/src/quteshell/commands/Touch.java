package quteshell.commands;

import quteshell.Path;
import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;

@Elevation(3)
@Help.Description("The touch command creates a new file. args=[file]")
public class Touch extends Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        if (shell.getFileSystem() != null) {
            if (arguments != null) {
                String[] levels = arguments.split("/");
                Path to = new Path(levels[levels.length - 1], "");
                String dirPath = "";
                for (int i = 0; i < levels.length - 1; i++) {
                    if (dirPath.length() > 0)
                        dirPath += "/";
                    dirPath += levels[i];
                }
                Path dir = shell.getFileSystem().find(dirPath);
                if (dir.getType() == Path.Type.Directory) {
                    dir.getChildren().add(to);
                    shell.writeln("File created");
                }
            } else {
                shell.writeln("Missing argument");
            }
        } else {
            shell.writeln("Filesystem not mounted");
        }
    }
}
