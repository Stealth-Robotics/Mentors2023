package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

public class DriveSubsystem extends SubsystemBase {
    private final SampleMecanumDrive roadrunnerDrive;

    public DriveSubsystem(HardwareMap hardwareMap, SampleMecanumDrive roadrunnerDrive) {
        this.roadrunnerDrive = roadrunnerDrive;


    }

    public void followTrajectoryAsync(Trajectory trajectory) {
        roadrunnerDrive.followTrajectoryAsync(trajectory);
    }

    public void followTrajectorySequenceAsync(TrajectorySequence trajectory) {
        roadrunnerDrive.followTrajectorySequenceAsync(trajectory);
    }

    public void update() {
        roadrunnerDrive.update();
    }

    public boolean isBusy() {
        return roadrunnerDrive.isBusy();
    }

    public Pose2d getLastError() {
        return roadrunnerDrive.getLastError();
    }

    public void abortTrajectorySequenceRunner() {
        roadrunnerDrive.abortTrajectorySequenceRunner();
    }

    public void setPoseEstimate(Pose2d poseEstimate) {
        roadrunnerDrive.setPoseEstimate(poseEstimate);
    }

    public Pose2d getPoseEstimate() {
        return roadrunnerDrive.getPoseEstimate();
    }

    public void stop() {
        roadrunnerDrive.setMotorPowers(0, 0, 0, 0);
    }

}
