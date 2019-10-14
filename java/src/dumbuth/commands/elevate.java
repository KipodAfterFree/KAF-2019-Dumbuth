package dumbuth.commands;

import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;
import quteshell.commands.Help;

@Elevation(2)
@Help.Description("elevate elevates you from :2 to :3.")
public class elevate implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.write("Elevating to level 3");
        for (int i = 0;i<3;i++){
            try{
                Thread.sleep(200);
            }catch (InterruptedException ignored){

            }
            shell.write(".");
        }
        shell.writeln();
        shell.setElevation(3);
    }
}
