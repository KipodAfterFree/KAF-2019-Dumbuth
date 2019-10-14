package quteshell.commands;

import quteshell.Path;
import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;

@Elevation(2)
@Help.Description("FileSySTemCReationtool creates a filesystem and mounts it to the shell.")
public class FsstCr extends Command {

    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.writeln("Creating filesystem...");
        Path.FileSystem fileSystem = Path.FileSystem.createDefault();
        shell.writeln("Mounting filesystem...");
        shell.setFileSystem(fileSystem);
        shell.writeln("Mounted to /");
    }
}
