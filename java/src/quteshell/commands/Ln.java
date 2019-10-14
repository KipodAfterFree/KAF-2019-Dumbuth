package quteshell.commands;

import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;

@Elevation(3)
@Help.Description("The ln command creates a file link in the file system args=[src, dest]")
public class Ln extends Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        if (shell.getFileSystem()!=null){
            if (arguments!=null){
                String[] paths = arguments.split(" ");
                if (paths.length>1){

                }else{
                    shell.writeln("ln requires 2 arguments");
                }
            }else{
                shell.writeln("Missing arguments");
            }
        }else{
            shell.writeln("Filesystem not mounted");
        }
    }
}
