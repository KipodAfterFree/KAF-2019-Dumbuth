package quteshell.command;

import quteshell.Quteshell;

public interface Command {
    void execute(Quteshell shell, String arguments);
}