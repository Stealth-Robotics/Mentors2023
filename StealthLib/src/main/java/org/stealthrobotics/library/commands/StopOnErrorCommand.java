package org.stealthrobotics.library.commands;

import com.arcrobotics.ftclib.command.Command;

import org.stealthrobotics.library.opmodes.StealthOpMode;

import java.util.function.Predicate;

/**
 * Stop the OpMode if the given command has too much error.
 * <p>
 * mmmfixme: docs
 */
public class StopOnErrorCommand<T extends Command> extends CorrectableCommand<T> {
    /**
     * Creates a new command to end the OpMode if the given command has too much error.
     *
     * @param opMode             the OpMode to end
     * @param command            the command to run and watch for error
     * @param interruptPredicate the function to evaluate the error state of the command
     */
    public StopOnErrorCommand(StealthOpMode opMode, T command, Predicate<T> interruptPredicate) {
        super(command, interruptPredicate, (cmd) -> new EndOpModeCommand(opMode));
    }
}
