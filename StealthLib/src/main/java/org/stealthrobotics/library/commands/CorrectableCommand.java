package org.stealthrobotics.library.commands;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandBase;

import java.util.function.Function;
import java.util.function.Predicate;

// todo: docs
//  - correctionOp generates a Command whose requirements must be a subset of the original

public class CorrectableCommand<T extends Command> extends CommandBase {
    private final T command;
    private final Predicate<T> interruptPredicate;
    private final Function<T, Command> correctionOp;

    private boolean finished = false;
    private Command correctionCommand;

    public CorrectableCommand(T command, Predicate<T> interruptPredicate, Function<T, Command> correctionOp) {
        this.command = command;
        this.interruptPredicate = interruptPredicate;
        this.correctionOp = correctionOp;
        m_requirements.addAll(command.getRequirements());
    }

    @Override
    public void initialize() {
        command.initialize();
    }

    // mmmfixme: logging
    @Override
    public void execute() {
        if (correctionCommand == null) {
            command.execute();
            if (command.isFinished()) {
                command.end(false);
                finished = true;
            } else if (interruptPredicate.test(command)) {
                System.out.printf("CorrectableCommand: error exceeded, interrupting original command (%s) and failing over to the correction.",
                        command.getName());
                System.out.flush();
                command.end(true);
                correctionCommand = correctionOp.apply(command);
                correctionCommand.initialize();
            }
        } else {
            correctionCommand.execute();
            if (correctionCommand.isFinished()) {
                correctionCommand.end(false);
                finished = true;
            }
        }
    }

    @Override
    public void end(boolean interrupted) {
        if (!finished) {
            if (correctionCommand != null) {
                correctionCommand.end(true);
            } else {
                command.end(true);
            }
            finished = true;
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

}
