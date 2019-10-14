package dumbuth.commands;

import dumbuth.Path;
import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;
import quteshell.commands.Help;

@Elevation(2)
@Help.Description("Lists all files in a directory (including subdirs")
public class Find implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        if (shell.getElevation() == 2) {
            if (shell.getFileSystem() != null) {
                String path = "/";
                if (arguments != null) {
                    path = arguments;
                }
                Path initial = shell.getFileSystem().find(path);
                try {
                    shell.writeln(find(initial, 0));
                } catch (StackOverflowError e) {
                    shell.setElevation(0);
                    shell.writeln();
                    shell.writeln("╔══════════════════════════════════════════════════╗");
                    shell.writeln("║ KAF{d1dnt_expect_1t_t0_b3_4n_0verfl0w_3xc3ption} ║");
                    shell.writeln("╚══════════════════════════════════════════════════╝");
                }
            } else {
                shell.writeln("Filesystem not mounted");
            }
        } else {
            shell.writeln("Can't find at elevation " + shell.getElevation());
        }
    }

    private String find(Path p, int tabs) {
        String s = "\n";
        for (int i = 0; i < tabs; i++) s += "\t";
        if (p.getType() == Path.Type.Directory) {
            s += p.getName() + "/";
            for (Path child : p.getChildren()) {
                s += find(child, tabs + 1);
            }
        } else if (p.getType() == Path.Type.File) {
            s += p.getName();
        } else if (p.getType() == Path.Type.Link) {
            s += find(p.getDestination(), tabs + 1);
        }
        return s;
    }
}
