package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commands.FollowTrajectory;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.SimpleServoSubsystem;
import org.firstinspires.ftc.teamcode.trajectories.TestTrajectories;
import org.stealthrobotics.library.commands.EndOpModeCommand;
import org.stealthrobotics.library.commands.SaveAutoHeadingCommand;
import org.stealthrobotics.library.opmodes.StealthAutoOpMode;

@SuppressWarnings("unused")
@Autonomous(name = "test auto new", preselectTeleOp = "BLUE | Tele-Op")
public class TestAutoNew extends StealthAutoOpMode {
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
                new FollowTrajectory(drive, TestTrajectories.forward),
                servos.toggleCommand(),
                new FollowTrajectory(drive, TestTrajectories.back),
                servos.toggleCommand(),
                new WaitCommand(1000),
                new FollowTrajectory(drive, TestTrajectories.forwardWithTurn),
                servos.toggleCommand(),
                new FollowTrajectory(drive, TestTrajectories.backWithTurn),
                servos.toggleCommand(),
                new SaveAutoHeadingCommand(() -> drive.getPoseEstimate().getHeading()),
                new EndOpModeCommand(this)
        );
    }
}



