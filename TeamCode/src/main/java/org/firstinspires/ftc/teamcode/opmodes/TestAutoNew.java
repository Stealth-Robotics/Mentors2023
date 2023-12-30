package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commands.FollowTrajectorySafelyCommand;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.SimpleServoSubsystem;
import org.firstinspires.ftc.teamcode.trajectories.TestTrajectories;
import org.firstinspires.ftc.teamcode.trajectories.TrajectoryBuilder;
import org.stealthrobotics.library.commands.EndOpModeCommand;
import org.stealthrobotics.library.commands.SaveAutoHeadingCommand;
import org.stealthrobotics.library.opmodes.StealthAutoOpMode;

@SuppressWarnings("unused")
@Autonomous(name = "test auto new", preselectTeleOp = "BLUE | Tele-Op")
public class TestAutoNew extends StealthAutoOpMode {

    private FollowTrajectorySafelyCommand followTrajectorySafelyCommand(DriveSubsystem drive, Trajectory trajectory) {
        return new FollowTrajectorySafelyCommand(this, drive, trajectory);
    }

    @Override
    public Command initializeAuto() {
        // mmmfixme: want to share init w/ teleop and all autos
        //  - no reason not to init the whole robot everywhere. Consistent and less confusing, even if suboptimal for a few modes.
        SampleMecanumDrive mecanumDrive = new SampleMecanumDrive(hardwareMap);
        DriveSubsystem drive = new DriveSubsystem(hardwareMap, mecanumDrive);
        SimpleServoSubsystem servos = new SimpleServoSubsystem(hardwareMap);
        register(drive, servos);

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        // Setup the robot's initial state
        schedule(servos.closeCommand());

        // mmmfixme: initial pose will be different for different opmodes though!
        drive.setPoseEstimate(TestTrajectories.startingPose);

        // Build our entire auto command here. Ensures that everything is loaded and initialized.
        // Anything conditional on, say, a camera needs to be handled with one of:
        //   - ConditionalCommand
        //   - SelectCommand, either form
        return new SequentialCommandGroup(
                new FollowTrajectorySafelyCommand(
                        drive,
                        TestTrajectories.forward,
                        (cmd) -> {
                            double origHeading = cmd.getTrajectory().end().getHeading();
                            Trajectory backUp = TrajectoryBuilder.buildTrajectory(drive.getPoseEstimate())
                                    .lineToLinearHeading(drive.getPoseEstimate().minus(new Pose2d(8, 0, origHeading)))
                                    .build();
                            Trajectory slideOver = TrajectoryBuilder.buildTrajectory(backUp.end())
                                    .strafeRight(6)
                                    .build();
                            Pose2d adj = new Pose2d(0, 6, 0);
                            Trajectory continueOn = TrajectoryBuilder.buildTrajectory(slideOver.end().plus(adj))
                                    .lineToLinearHeading(cmd.getTrajectory().end())
                                    .build();
                            return new SequentialCommandGroup(
                                    new FollowTrajectorySafelyCommand(this, drive, backUp),
                                    new FollowTrajectorySafelyCommand(this, drive, slideOver),
                                    new InstantCommand(() -> drive.setPoseEstimate(drive.getPoseEstimate().plus(adj))),
                                    new FollowTrajectorySafelyCommand(this, drive, continueOn)
                            );
                        }
                ).name("forward"),
                servos.toggleCommand(),
                new FollowTrajectorySafelyCommand(this, drive, TestTrajectories.back).name("back"),
                servos.toggleCommand(),
                new WaitCommand(1000),
                this.followTrajectorySafelyCommand(drive, TestTrajectories.forwardWithTurn).name("forward turn"),
                servos.toggleCommand(),
                this.followTrajectorySafelyCommand(drive, TestTrajectories.backWithTurn).name("back turn"),
                servos.toggleCommand(),
                new SaveAutoHeadingCommand(() -> drive.getPoseEstimate().getHeading()),
                new EndOpModeCommand(this)
        );
    }
}



