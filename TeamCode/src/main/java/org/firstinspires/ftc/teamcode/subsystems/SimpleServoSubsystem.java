package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


@Config
public class SimpleServoSubsystem extends SubsystemBase {
    private final Servo servo;

    public static double OPEN_POSITION = 0.75;
    public static double CLOSE_POSITION = 0.25;
    public static int OPEN_CLOSE_TIME = 300; // ms

    private boolean open = false;

    public SimpleServoSubsystem(HardwareMap hardwareMap) {
        servo = hardwareMap.get(Servo.class, "gripper");
    }

    public Command openCommand() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> {
                    servo.setPosition(OPEN_POSITION);
                    open = true;
                }, this),
                new WaitCommand(OPEN_CLOSE_TIME)
        );
    }

    public Command closeCommand() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> {
                    servo.setPosition(CLOSE_POSITION);
                    open = false;
                }, this),
                new WaitCommand(OPEN_CLOSE_TIME)
        );
    }

    public Command toggleCommand() {
        return new ConditionalCommand(openCommand(), closeCommand(), () -> !open);
    }

}
