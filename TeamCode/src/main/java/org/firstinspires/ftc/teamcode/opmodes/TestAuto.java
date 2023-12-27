package org.firstinspires.ftc.teamcode.opmodes;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandScheduler;
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
import org.stealthrobotics.library.opmodes.StealthOpMode;

@SuppressWarnings("unused")
@Autonomous(name = "test auto", preselectTeleOp = "BLUE | Tele-Op")
public class TestAuto extends StealthOpMode {
    DriveSubsystem drive;
    SampleMecanumDrive mecanumDrive;
    SimpleServoSubsystem servos;

    @Override
    public void initialize() {
        mecanumDrive = new SampleMecanumDrive(hardwareMap);
        drive = new DriveSubsystem(hardwareMap, mecanumDrive);
        servos = new SimpleServoSubsystem(hardwareMap);
        register(drive, servos);
    }

    @Override
    public void whileWaitingToStart() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public Command getAutoCommand() {
        drive.setPoseEstimate(TestTrajectories.startingPose);

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



