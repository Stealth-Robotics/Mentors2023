package org.firstinspires.ftc.teamcode.commands;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.arcrobotics.ftclib.command.Command;

import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.stealthrobotics.library.commands.CorrectableCommand;
import org.stealthrobotics.library.commands.EndOpModeCommand;
import org.stealthrobotics.library.opmodes.StealthOpMode;

import java.util.function.Function;

public class FollowTrajectorySafelyCommand extends CorrectableCommand<FollowTrajectory> {
    private static final Pose2d allowableError = new Pose2d(2.0, 2.0, Math.toRadians(10.0));

    public FollowTrajectorySafelyCommand(StealthOpMode opMode,
                                         DriveSubsystem driveSubsystem,
                                         Trajectory trajectory) {
        super(
                new FollowTrajectory(driveSubsystem, trajectory),
                (cmd) -> {
                    Pose2d lastError = cmd.getLastError();
                    return Math.abs(lastError.getX()) > allowableError.getX() ||
                            Math.abs(lastError.getY()) > allowableError.getY() ||
                            Math.abs(lastError.getHeading()) > allowableError.getHeading();
                },
                (cmd) -> new EndOpModeCommand(opMode)
        );
    }

    public FollowTrajectorySafelyCommand(StealthOpMode opMode,
                                         DriveSubsystem driveSubsystem,
                                         Trajectory trajectory,
                                         Function<FollowTrajectory, Command> correctionOp) {
        super(
                new FollowTrajectory(driveSubsystem, trajectory),
                (cmd) -> {
                    Pose2d lastError = cmd.getLastError();
                    return Math.abs(lastError.getX()) > allowableError.getX() ||
                            Math.abs(lastError.getY()) > allowableError.getY() ||
                            Math.abs(lastError.getHeading()) > allowableError.getHeading();
                },
                correctionOp
        );
    }
}
