package dumbuth.commands;

import dumbuth.Path;
import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;
import quteshell.commands.Help;

@Elevation(3)
@Help.Description("touch creates a new file.\ne.g. 'touch myfile.txt'")
public class touch implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        if (shell.getFileSystem() != null) {
            if (arguments != null) {
                String[] levels = arguments.split("/");
                if (levels.length > 0) {
                    Path to = new Path(levels[levels.length - 1], "");
                    Path fileDirectory = shell.getFileSystem().find(arguments.substring(0, arguments.length() - (levels[levels.length - 1].length() + 1)));
                    if(fileDirectory!=null){
                        if (fileDirectory.getType() == Path.Type.Directory) {
                            fileDirectory.getChildren().add(to);
                            shell.writeln("File created");
                        }
                    }else{
                        shell.writeln("Directory does not exist");
                    }
                }
            } else {
                shell.writeln("Missing argument");
            }
        } else {
            shell.writeln("Filesystem not mounted");
        }
    }
}
