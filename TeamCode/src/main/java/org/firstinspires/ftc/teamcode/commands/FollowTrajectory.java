package org.firstinspires.ftc.teamcode.commands;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;

public class FollowTrajectory extends CommandBase {
    private final DriveSubsystem driveSubsystem;
    private final Trajectory trajectory;

    public FollowTrajectory(DriveSubsystem driveSubsystem, Trajectory trajectory) {
        this.driveSubsystem = driveSubsystem;
        this.trajectory = trajectory;
        addRequirements(driveSubsystem);
    }

    @Override
    public void initialize() {
        driveSubsystem.followTrajectoryAsync(trajectory);
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

    public Pose2d getLastError() {
        return driveSubsystem.getLastError();
    }

    public Trajectory getTrajectory() {
        return trajectory;
    }
}
