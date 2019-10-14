package dumbuth.commands;

import dumbuth.Path;
import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;
import quteshell.commands.Help;

@Elevation(3)
@Help.Description("wfile (WriteFILE) writes contents to a file.\ne.g. 'wfile file contents' or 'wfile myfile.txt Hello World'")
public class wfile implements Command {

    @Override
    public void execute(Quteshell shell, String arguments) {
        if (shell.getFileSystem() != null) {
            if (arguments != null) {
                String[] args = arguments.split(" ", 2);
                if (args.length == 2) {
                    Path file = shell.getFileSystem().find(args[0]);
                    if (file.getType() == Path.Type.File)
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
