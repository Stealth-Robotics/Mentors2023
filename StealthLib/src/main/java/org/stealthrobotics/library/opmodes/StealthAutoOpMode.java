package org.stealthrobotics.library.opmodes;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandScheduler;

/**
 * Base class for all Stealth auto op-modes.
 */
public abstract class StealthAutoOpMode extends StealthOpMode {
    private Command autoCommand;

    /**
     * Override this to setup your hardware, initialize it, and return your auto command.
     */
    public abstract Command initializeAuto();

    // Just defer to initializeAuto and save the command for later.
    @Override
    public void initialize() {
        autoCommand = initializeAuto();
    }

    // Run the scheduler while waiting to start autos. There are no controller bindings in autos,
    // so it's safe to do this without worrying about someone knocking into a gamepad.
    public void whileWaitingToStart() {
        CommandScheduler.getInstance().run();
    }

    // Use the command we created in initializeAuto
    public Command getAutoCommand() {
        return autoCommand;
    }

}
