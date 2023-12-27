package org.firstinspires.ftc.teamcode.trajectories;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;

public class TestTrajectories {

    public static Pose2d startingPose = new Pose2d(6, 6, Math.toRadians(0));
    public static Trajectory forward = TrajectoryBuilder.buildTrajectory(startingPose)
            .forward(12)
            .build();
    public static Trajectory back = TrajectoryBuilder.buildTrajectory(forward.end())
            .back(12)
            .build();

    public static Trajectory forwardWithTurn = TrajectoryBuilder.buildTrajectory(startingPose)
            .lineToLinearHeading(new Pose2d(18, 6, Math.toRadians(180)))
            .build();

    public static Trajectory backWithTurn = TrajectoryBuilder.buildTrajectory(forwardWithTurn.end())
            .lineToLinearHeading(startingPose)
            .build();
}