package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.DefaultMecanumDriveCommand;
import org.firstinspires.ftc.teamcode.subsystems.SimpleMecanumDriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.SimpleServoSubsystem;
import org.stealthrobotics.library.gamepad.Gamepad;
import org.stealthrobotics.library.opmodes.StealthOpMode;


// TODO
//   - StealthOpMode changes for next time
//     - base class for Teleop vs. auto
//       - auto can reset the auto-to-tele storage, ask for the auto cmd, pump the scheduler loop during init, etc.
//       - tele can have a separate method to bind controller buttons vs. init, so we can schedule cmds during init but not bind buttons.


public abstract class Teleop extends StealthOpMode {

    // Subsystems
    SimpleMecanumDriveSubsystem drive;
    SimpleServoSubsystem servos;

    @Override
    public void initialize() {
        // Setup and register all of your subsystems here
        drive = new SimpleMecanumDriveSubsystem(hardwareMap);
        servos = new SimpleServoSubsystem(hardwareMap);
        register(drive, servos);

        // Note: I don't recommend leaving this enabled. As of release 0.4.6 there is a bad
        // memory leak if you disable the dashboard via the opmode, which will cause your opmode
        // to crash after a short period of time. Use with caution until there's a new release.
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        // Game controllers
        Gamepad driveGamepad = new Gamepad(gamepad1);
        Gamepad mechGamepad = new Gamepad(gamepad2);

        // Init various subsystems
        schedule(servos.closeCommand());

        // A subsystem's default command runs all the time. Great for drivetrains and such.
        drive.setDefaultCommand(
                new DefaultMecanumDriveCommand(
                        drive,
                        driveGamepad.left_stick_y,
                        driveGamepad.left_stick_x,
                        driveGamepad.right_stick_x,
                        driveGamepad.right_trigger
                )
        );

        // Setup all of your controllers' buttons and triggers here
        driveGamepad.back.whenActive(() -> drive.togglefieldcentric());
        driveGamepad.y.whenActive(() -> drive.resetHeading());
    }

    /**
     * Ideally your red vs. blue opmodes are nothing more than this. Keep code shared between
     * them, and take different actions based on the alliance color.
     *
     * @see org.stealthrobotics.library.Alliance
     */

    @SuppressWarnings("unused")
    @TeleOp(name = "RED | Tele-Op", group = "Red")
    public static class RedTeleop extends Teleop {
    }

    @SuppressWarnings("unused")
    @TeleOp(name = "BLUE | Tele-Op", group = "Blue")
    public static class BlueTeleop extends Teleop {
    }
}
