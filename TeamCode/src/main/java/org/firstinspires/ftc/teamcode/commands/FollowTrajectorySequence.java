package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;

public class FollowTrajectorySequence extends CommandBase {
    private final DriveSubsystem driveSubsystem;
    private final TrajectorySequence trajectorySequence;

    public FollowTrajectorySequence(DriveSubsystem driveSubsystem, TrajectorySequence trajectorySequence) {
        this.driveSubsystem = driveSubsystem;
        this.trajectorySequence = trajectorySequence;
        addRequirements(driveSubsystem);
    }

    @Override
    public void initialize() {
        driveSubsystem.followTrajectorySequenceAsync(trajectorySequence);
    }

    @Override
    public void execute() {
        // TODO: log the trajectory we're following
        driveSubsystem.update();
    }

    @Override
    public boolean isFinished() {
        return !driveSubsystem.isBusy();
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted) {
            driveSubsystem.abortTrajectorySequenceRunner();
        }
        driveSubsystem.stop();
    }
}
