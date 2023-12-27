package org.firstinspires.ftc.teamcode.trajectories;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.roadrunner.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;

public class TrajectorySequenceBuilder {
    public static org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequenceBuilder buildTrajectorySequence(Pose2d startPose) {
        return new org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequenceBuilder(
                startPose,
                SampleMecanumDrive.VEL_CONSTRAINT,
                SampleMecanumDrive.ACCEL_CONSTRAINT,
                DriveConstants.MAX_ANG_VEL,
                DriveConstants.MAX_ANG_ACCEL

        );
    }
}
