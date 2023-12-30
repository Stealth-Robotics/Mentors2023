package org.stealthrobotics.library.commands;

import com.arcrobotics.ftclib.command.Command;

import org.stealthrobotics.library.opmodes.StealthOpMode;

import java.util.function.Predicate;

/**
 * Stop the OpMode if the given command has too much error.
 * <p>
 * Runs the given Command, evaluating the interrupt predicate after each iteration. If it
 * returns true, the command is interrupted and the OpMode is stopped gracefully.
 */
public class StopOnErrorCommand<T extends Command> extends CorrectableCommand<T> {
    /**
     * Creates a new StopOnErrorCommand. The given command will be run until the interrupt predicate
     * determines there is too much error. When that occurs, the command is interrupted and the
     * OpMode is stopped gracefully.
     *
     * @param opMode             the OpMode to end
     * @param command            the command to run and watch for error
     * @param interruptPredicate the function to evaluate the error state of the command
     */
    public StopOnErrorCommand(StealthOpMode opMode, T command, Predicate<T> interruptPredicate) {
        super(command, interruptPredicate, (cmd) -> new EndOpModeCommand(opMode));
    }
}
