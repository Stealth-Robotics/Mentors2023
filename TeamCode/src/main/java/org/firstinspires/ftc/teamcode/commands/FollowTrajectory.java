package org.firstinspires.ftc.teamcode.commands;

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
        // TODO: safe version that gives us an error and stops the opmode if we don't reach the end post within some margin of error.
        return !driveSubsystem.isBusy();
    }

    @Override
    public void end(boolean interrupted) {
        // TODO: insufficient if interrupted I bet. Check. RR will still be trying to follow the path, all this is doing is stopping the motors
        driveSubsystem.stop();
    }
}
