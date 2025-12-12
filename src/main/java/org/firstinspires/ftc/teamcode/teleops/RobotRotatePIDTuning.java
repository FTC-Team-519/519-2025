package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.util.OpModeBase;
import org.firstinspires.ftc.teamcode.util.RobotMath;
import org.firstinspires.ftc.teamcode.util.commands.Command;
import org.firstinspires.ftc.teamcode.util.commands.actions.CorrectForAprilTag;
import org.firstinspires.ftc.teamcode.util.commands.actions.DriveInDirection;
import org.firstinspires.ftc.teamcode.util.commands.actions.RotateRobotCommand;
import org.firstinspires.ftc.teamcode.util.commands.actions.RotateRobotCommandPID;
import org.firstinspires.ftc.teamcode.util.hardware.IntakeColorSensor.pieceType;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static org.firstinspires.ftc.teamcode.teleops.CompetitionTeleOp.RotationSetting.*;

@TeleOp(name = "RotateRobotPIDTuning")
public class RobotRotatePIDTuning extends OpModeBase {

    private Queue<Command> commands_to_run = new LinkedList<>();
    private double d_coef = -0.01;
    private double p_coef = 0.02;
    private double targetAngle = 0.0;

    @Override
    public void init() {
        super.init();
    }

    //we are not solving this right now
    //FIXME: follow the specification in teleopcontrols.txt
    public void loop() {
        try {
            if (gamepad2.aWasReleased()) { // when the a button is pressed
                commands_to_run = new LinkedList<>(); //clear the running commands incase we no longer want
            }

            if (!commands_to_run.isEmpty()) {
                Command command = commands_to_run.peek();
                assert command != null;
                command.run();
                if (command.isDone()) {
                    command.shutdown();
                    commands_to_run.remove();
                    Command next_command = commands_to_run.peek();
                    if (next_command != null) {
                        next_command.init();
                    }
                }
            } else {
                manual_controls();
            }
        } catch (Exception e) {
            telemetry.addData("error", e.toString());
            telemetry.update();
        }

        double currentYaw = robot.getYaw();

        double error = Math.min(RobotMath.trueMod(targetAngle - currentYaw, 360.0), RobotMath.trueMod( currentYaw - targetAngle, 360.0));

        telemetry.addData("Yaw", robot.getYaw());
        telemetry.addData("target", targetAngle);
        telemetry.addData("error", error);
        telemetry.addData("rotation_speed", Math.abs(robot.getLeftBackDrive().getPower()));
        telemetry.addData("p_coef", p_coef);
        telemetry.addData("d_coef", d_coef);
        telemetry.update();
    }


    public void manual_controls() {
        final double rotate_angle = 90;
        if (gamepad1.leftBumperWasReleased()) {

            commands_to_run.add(new RotateRobotCommandPID(-rotate_angle, this.robot, p_coef, d_coef));
            this.targetAngle = RobotMath.trueMod(-rotate_angle + robot.getYaw() + 180.0, 360) - 180;
        }
        if (gamepad1.rightBumperWasReleased()) {
            commands_to_run.add(new RotateRobotCommandPID(rotate_angle, this.robot, p_coef, d_coef));
            this.targetAngle = RobotMath.trueMod(rotate_angle + robot.getYaw() + 180.0, 360) - 180;
        }


        if (gamepad1.dpadUpWasPressed()) {
            p_coef += 0.001;
        }
        if (gamepad1.dpadDownWasPressed()) {
            p_coef -= 0.001;
        }
        if (gamepad1.dpadRightWasPressed()) {
            d_coef += 0.01;
        }
        if (gamepad1.dpadLeftWasPressed()) {
            d_coef -= 0.01;
        }


        p_coef = RobotMath.clamp(p_coef, 0.0, 1.0);
        d_coef = RobotMath.clamp(d_coef, -1.0, 0.0);
    }

}
