package org.firstinspires.ftc.teamcode.commands;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.stealthrobotics.library.commands.CorrectableCommand;
import org.stealthrobotics.library.commands.EndOpModeCommand;
import org.stealthrobotics.library.opmodes.StealthOpMode;

import java.util.function.Function;

/**
 * Follow a Trajectory while watching it for error. If the positional error exceeds a set bounds,
 * abort the trajectory. Optionally, a correction op may generate a new command to try to recover.
 * <p>
 * This logs the maximum error on each axis, to help determine how well the bot is tracking and
 * how close it is to erroring out.
 * <p>
 * TODO:
 *  - the error bounds are hardcoded for now. Overloads to take them as a param would be nice.
 *  -
 */
public class FollowTrajectorySafelyCommand extends SequentialCommandGroup {
    private static final Pose2d allowableError = new Pose2d(2.0, 2.0, Math.toRadians(5.0));

    private double maxErrorX = 0.0;
    private double maxErrorY = 0.0;
    private double maxErrorHeading = 0.0;

    /**
     * Creates a new FollowTrajectorySafelyCommand. The trajectory will be followed, and interrupted
     * if there is too much positional error along the way. If so, the OpMode will be stopped.
     */
    public FollowTrajectorySafelyCommand(StealthOpMode opMode,
                                         DriveSubsystem driveSubsystem,
                                         Trajectory trajectory) {
        this(driveSubsystem, trajectory, (cmd) -> new EndOpModeCommand(opMode));
    }

    /**
     * Creates a new FollowTrajectorySafelyCommand. The trajectory will be followed, and interrupted
     * if there is too much positional error along the way. If so, correction op will be called
     * to generate a new command to run which may attempt to correct the error and follow an
     * updated trajectory.
     *
     * @param driveSubsystem the drivetrain subsystem
     * @param trajectory     the Trajectory to follow
     * @param correctionOp   the correction op to generate a replacement Command
     */
    public FollowTrajectorySafelyCommand(DriveSubsystem driveSubsystem,
                                         Trajectory trajectory,
                                         Function<FollowTrajectory, Command> correctionOp) {
        addCommands(
                new CorrectableCommand<>(new FollowTrajectory(driveSubsystem, trajectory), this::evaluateError, correctionOp),
                new InstantCommand(() -> System.out.println(this))
        );
    }

    private boolean evaluateError(FollowTrajectory cmd) {
        Pose2d lastError = cmd.getLastError();
        // TODO: uncomment if you are having trouble determining which part of the trajectory has
        //  the most error.
        //System.out.println("Current " + cmd.getPoseEstimate() + " -- err " + lastError);
        double x = Math.abs(lastError.getX());
        double y = Math.abs(lastError.getY());
        double heading = Math.abs(lastError.getHeading());
        maxErrorX = Math.max(maxErrorX, x);
        maxErrorY = Math.max(maxErrorY, y);
        maxErrorHeading = Math.max(maxErrorHeading, heading);
        return x > allowableError.getX() || y > allowableError.getY() || heading > allowableError.getHeading();
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("FollowTrajectorySafelyCommand [%s] max errors=(%.3f, %.3f, %.3f°)",
                getName(), maxErrorX, maxErrorY, Math.toDegrees(maxErrorHeading));
    }

    // TODO: the original setName method isn't chainable...
    public FollowTrajectorySafelyCommand name(String name) {
        super.setName(name);
        return this;
    }
}
