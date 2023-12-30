package org.stealthrobotics.library.commands;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandBase;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Run a {@link Command}, interrupting it and running a correction {@link Command} when there is
 * too much error.
 * <p>
 * Runs the given Command, evaluating the interrupt predicate after each iteration. If it
 * returns true, the command is interrupted and the correction op is called to generate a correction
 * command, which is then run.
 * <p>
 * Note: the correction op must generate a Command whose requirements are the same as or a subset of
 * the original Command's requirements.
 */
public class CorrectableCommand<T extends Command> extends CommandBase {
    private final T command;
    private final Predicate<T> interruptPredicate;
    private final Function<T, Command> correctionOp;

    private boolean finished = false;
    private Command correctionCommand;

    /**
     * Creates a new CorrectableCommand. The given command will be run until the interrupt predicate
     * determines there is too much error. When that occurs, the command is interrupted and the
     * correction op is called to generate a correction command, which is then run.
     *
     * @param command            the command to run
     * @param interruptPredicate returns true when the command should be interrupted
     * @param correctionOp       function which takes the original command and generates a correction
     */
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
