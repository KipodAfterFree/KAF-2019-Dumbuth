package dumbuth.commands;

import dumbuth.Path;
import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;
import quteshell.commands.Help;

@Elevation(3)
@Help.Description("cmfs (CreateMountFileSystem) creates a filesystem and mounts it to /.")
public class cmfs implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.writeln("Creating filesystem...");
        Path.FileSystem fileSystem = Path.FileSystem.createDefault();
        shell.writeln("Mounting filesystem...");
        shell.setFileSystem(fileSystem);
        shell.writeln("Mounted on /, as root/");
    }
}
