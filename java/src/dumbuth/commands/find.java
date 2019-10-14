package dumbuth.commands;

import dumbuth.Path;
import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;
import quteshell.commands.Help;

@Elevation(2)
@Help.Description("find prints a list of all the files in all directories.\ne.g. 'find /' or 'find files'")
public class find implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        if (shell.getElevation() == 2) {
            if (shell.getFileSystem() != null) {
                String path = "/";
                if (arguments != null) {
                    path = arguments;
                }
                Path initial = shell.getFileSystem().find(path);
                if (initial != null) {
                    try {
                        shell.write("Find result:");
                        shell.writeln(find(initial, 0, true));
                    } catch (StackOverflowError e) {
                        shell.setElevation(0);
                        shell.writeln();
                        shell.writeln("╔══════════════════════════════════════════════════╗");
                        shell.writeln("║ KAF{d1dnt_expect_1t_t0_b3_4n_0verfl0w_3xc3ption} ║");
                        shell.writeln("╚══════════════════════════════════════════════════╝");
                    }
                }
            } else {
                shell.writeln("Filesystem not mounted");
            }
        } else {
            shell.writeln("Can't find at elevation " + shell.getElevation());
        }
    }

    private String find(Path p, int tabs, boolean whitespace) {
        String tree = "";
        if (whitespace) {
            tree += "\n";
            for (int i = 0; i < tabs; i++)
                tree += "\t";
        }
        if (p.getType() == Path.Type.Link) {
            tree += p.getName();
            tree += "->";
            tree += find(p.getDestination(), tabs, false);
        } else {
            tree += p.getName();
            if (p.getType() == Path.Type.Directory) {
                tree += "/";
                for (Path child : p.getChildren()) {
                    tree += find(child, tabs + 1, true);
                }
            }
        }
        return tree;
    }
}
