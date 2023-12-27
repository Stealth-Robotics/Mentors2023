package org.firstinspires.ftc.teamcode.trajectories;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;

import java.util.function.DoubleSupplier;

public class TrajectoryBuilder {
    public static com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder buildTrajectory(Pose2d startPose, double heading) {
        return new com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder(
                startPose,
                heading,
                SampleMecanumDrive.VEL_CONSTRAINT,
                SampleMecanumDrive.ACCEL_CONSTRAINT
        );
    }

    public static com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder buildTrajectory(Pose2d startPose) {
        return new com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder(
                startPose,
                SampleMecanumDrive.VEL_CONSTRAINT,
                SampleMecanumDrive.ACCEL_CONSTRAINT
        );
    }

    public static com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder buildTrajectory(DoubleSupplier startX, DoubleSupplier startY, DoubleSupplier startHeading) {
        return new com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder(
                new Pose2d(startX.getAsDouble(), startY.getAsDouble(), startHeading.getAsDouble()),
                SampleMecanumDrive.VEL_CONSTRAINT,
                SampleMecanumDrive.ACCEL_CONSTRAINT
        );
    }
}
