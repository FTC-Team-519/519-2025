package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.util.OpModeBase;
import org.firstinspires.ftc.teamcode.util.RobotMath;
import org.firstinspires.ftc.teamcode.util.hardware.Rotator;
import org.firstinspires.ftc.teamcode.util.commands.Command;
import org.firstinspires.ftc.teamcode.util.commands.actions.CorrectForAprilTag;
import org.firstinspires.ftc.teamcode.util.commands.actions.DriveInDirection;
import org.firstinspires.ftc.teamcode.util.hardware.IntakeColorSensor.pieceType;

import java.util.LinkedList;
import java.util.Queue;

import java.util.Arrays;

import static org.firstinspires.ftc.teamcode.teleops.CompetitionTeleOp.RotationSetting.*;

@TeleOp(name = "Competition TeleOp")
public class CompetitionTeleOp extends OpModeBase {
    private boolean driving_field_centric = false;
    private boolean intaking = false;
    private boolean hasDetectedMotif = false;
    private pieceType[] motif = null;
    private double outtake_power = 0.0;
    private RotationSetting rotationSetting = AutoRotate;

    private Queue<Command> commands_to_run = new LinkedList<>();

    enum RotationSetting{
        AutoAlignment,
        AutoRotate,
        ManualRotate,
        ConstantRotate,
    }

    @Override
    public void init() {
        super.init();
    }

    //we are not solving this right now
    //FIXME: follow the specification in teleopcontrols.txt
    public void loop() {
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
    }


    public void manual_controls() {
        camera();

        //outtake flywheels
        outtake();

        //kicker
        kicking();

        //rotating
        rotating();

        //intake
        intake();

        //driving
        driving();

        //telemetry
        telemetry();
    }

    private void camera() {
        //save cpu using camera
        if (gamepad2.leftBumperWasPressed()) {
            robot.stopStreaming();
        } else if (gamepad2.rightBumperWasPressed()) {
            robot.resumeStreaming();
        }

        if(!hasDetectedMotif) {
            pieceType[] detMotif = robot.getMotif();
            if (detMotif != null) {
                motif = detMotif;
                hasDetectedMotif = true;
            }
        }
    }

    private void outtake() {
        if (gamepad2.dpadUpWasReleased()) {
            outtake_power += 0.02;
        }

        if (gamepad2.dpadDownWasReleased()) {
            outtake_power -= 0.02;
        }
        if (gamepad2.xWasReleased()) {
            outtake_power = 0.0;
        }
        if (gamepad2.yWasReleased()) {
            outtake_power = 0.6;
        }
        if(gamepad2.bWasPressed() && Arrays.stream(robot.getIds()).anyMatch((i)-> i==20 || i ==24)) {
            outtake_power = RobotMath.outPower(robot.getDistancesFromAprilTag()[1]); // 0.016 being our untested distance {FIXME: Set to a final variable}
        }
        outtake_power = RobotMath.clamp(outtake_power, 0.0, 1.0);
        robot.runOuttake(outtake_power);


    }

    private void kicking() {
        if (gamepad2.aWasPressed()) {
            robot.changeKicking();
        }
    }

    private void rotating() {
        switch (this.rotationSetting){
            case AutoAlignment:
                robot.getRotator().runMotor(-0.1);
                if(robot.isRotatorAligned()){
                    this.rotationSetting = AutoRotate;
                    robot.getRotator().resetEncoder();
                    robot.getRotator().getMotor().setTargetPosition(robot.getRotatorPosition());
                }
                break;
            case AutoRotate:
                robot.getRotator().runMotorToPositionPID();
//                if (robot.getRotator().isAtPosition()) {
//                    this.rotationSetting = NoRotate;
//                }
                break;
            case ManualRotate:
                if (gamepad1.left_trigger == 0 && gamepad1.right_trigger == 0) {
                    this.rotationSetting = AutoRotate;
                    robot.getRotator().resetEncoder();
                    robot.getRotator().getMotor().setTargetPosition(robot.getRotatorPosition());
                }
                break;
            case ConstantRotate:
                robot.getRotator().runMotor(-0.3);
                break;
        }

        if (gamepad1.yWasPressed()) {
            if(this.rotationSetting == ConstantRotate){
                this.rotationSetting = AutoRotate;
            }else{
                this.rotationSetting = ConstantRotate;
            }
            if (this.rotationSetting != ConstantRotate) {
                robot.getRotator().setDiskRotation(true);
            }
        }

        //rotating the disk to a specific position
        if (gamepad1.leftBumperWasReleased()) {
            //clockwise
            robot.getRotator().setDiskRotation(false);
            this.rotationSetting = AutoRotate;
        } else if (gamepad1.rightBumperWasReleased()) {
            //counter clock wise
            robot.getRotator().setDiskRotation(true);
            this.rotationSetting = AutoRotate;
        }

        //manual rotation
        if (gamepad1.left_trigger != 0 || gamepad1.right_trigger != 0) {
            this.rotationSetting = ManualRotate;
            robot.getRotator().runMotor((gamepad1.left_trigger - gamepad1.right_trigger));//*Rotator.MAX_SPEED);
        }

        //will align to the magnet
        if(gamepad1.right_stick_button && gamepad1.left_stick_button){
            this.rotationSetting = AutoAlignment;
        }
    }

    private void intake() {
        if (gamepad1.bWasReleased()) {
            intaking = !intaking;
        }

        if (gamepad1.x) {
            robot.runIntake(-1.0);
        } else {
            if (intaking) {
                robot.runIntake(1.0);
            } else {
                robot.runIntake(0.0);
            }
        }


    }

    private void driving() {
        if (gamepad1.aWasReleased()) {
            driving_field_centric = !driving_field_centric;
            robot.resetYaw();
        }

        //regular driving
        if (driving_field_centric) {
            double x = gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y;
            double mag = Math.hypot(x, y);
            double drive_angle = Math.atan2(y, x);
            double robot_angle = robot.getYaw() * Math.PI / 180.0;
            double angle = drive_angle - robot_angle;

            raw_driving(mag * Math.cos(angle), mag * Math.sin(angle), gamepad1.right_stick_x);
        } else {
            raw_driving(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
        }

        //we want regular driving to be overwritten by dpad driving
        if (gamepad1.dpad_down || gamepad1.dpad_up || gamepad1.dpad_left || gamepad1.dpad_right) {
            double x = 0;
            double y = 0;
            if (gamepad1.dpad_down) {
                y -= 1;
            }
            if (gamepad1.dpad_up) {
                y += 1;
            }
            if (gamepad1.dpad_left) {
                x -= 1;
            }
            if (gamepad1.dpad_right) {
                x += 1;
            }
            double angle = -3.0 * Math.PI / 4.0; // we want it rotated by 135 deg so that we can line up the shot easier
            raw_driving(x * Math.cos(angle) - y * Math.sin(angle), x * Math.sin(angle) + y * Math.cos(angle), 0.0);
        }

        if (gamepad2.leftBumperWasPressed()) {
            commands_to_run.add(new CorrectForAprilTag(robot));
            double dist = robot.getDistancesFromAprilTag()[0];
            if(dist>0) {
                commands_to_run.add(new DriveInDirection(dist, 1.0 / 4 * Math.PI, robot));
            } else {
                commands_to_run.add(new DriveInDirection(dist, -3.0 / 4 * Math.PI,robot));
            }
        }


    }

    private void raw_driving(double x, double y, double rot) {
        double lf_power = y + x + rot;
        double rf_power = y - x - rot;
        double lb_power = y - x + rot;
        double rb_power = y + x - rot;

        double max = 0.0;
        max = Math.max(Math.abs(lf_power), Math.abs(rf_power));
        max = Math.max(max, Math.abs(lb_power));
        max = Math.max(max, Math.abs(rb_power));

        if (max > 1.0) {
            lf_power  /= max;
            rf_power /= max;
            lb_power   /= max;
            rb_power  /= max;
        }

        robot.setLeftFrontPower(lf_power);
        robot.setRightFrontPower(rf_power);
        robot.setLeftBackPower(lb_power);
        robot.setRightBackPower(rb_power);
    }


    private void telemetry() {

        double[] distanceArray = robot.getDistancesFromAprilTag();

        telemetry.addData("magnet seeing", robot.getRotator().isAligned());

        telemetry.addData("X Offset", distanceArray[0]);
        telemetry.addData("Y Offset", distanceArray[1]);
        telemetry.addData("Z Offset", distanceArray[2]);
        telemetry.addData("Yaw", distanceArray[3]);

        telemetry.addData("AprilTag Ids found", Arrays.toString(robot.getIds()));

        telemetry.addData("Field Centric:", driving_field_centric);
        telemetry.addData("Rotation setting:", this.rotationSetting);
        telemetry.addData("outtake power:", robot.getOuttake().getLeftMotor().getPower());
        try {
            //if (robot.getRotator().getPieceColor() != null) {
            telemetry.addData("sensing:", robot.getRotator().getPieceColor().toString());
            telemetry.addData("color sensor 1 red", robot.getRotator().getColorSensor1().get_rgb()[0]);
            telemetry.addData("color sensor 1 green", robot.getRotator().getColorSensor1().get_rgb()[1]);
            telemetry.addData("color sensor 1 blue", robot.getRotator().getColorSensor1().get_rgb()[2]);
            telemetry.addData("color sensor 1 rgb", Arrays.toString(robot.getRotator().getColorSensor1().get_rgb()));
            telemetry.addData("color sensor 1 hsv", Arrays.toString(robot.getRotator().getColorSensor1().get_hsv()));
            telemetry.addData("color sensor 1 distance", robot.getRotator().getColorSensor1().get_distance_inch());
            telemetry.addData("color sensor 1 sensing", robot.getRotator().getColorSensor1().get_piece());
            telemetry.addData("color sensor 2 red", robot.getRotator().getColorSensor2().get_rgb()[0]);
            telemetry.addData("color sensor 2 green", robot.getRotator().getColorSensor2().get_rgb()[1]);
            telemetry.addData("color sensor 2 blue", robot.getRotator().getColorSensor2().get_rgb()[2]);
            telemetry.addData("color sensor 2 distance", robot.getRotator().getColorSensor2().get_distance_inch());
            telemetry.addData("color sensor 2 sensing", robot.getRotator().getColorSensor2().get_piece());
            //}
        } catch (NullPointerException ignored) {

        }
        telemetry.addData("current disk pos: ", robot.getRotator().getEncoderPosition());
        telemetry.addData("desired pos:", robot.getRotator().getMotor().getTargetPosition());
        telemetry.addData("current disk pos(normalized): ", robot.getRotator().getEncoderPosition() / (double) Rotator.CLICKS_PER_ROTATION);
        telemetry.addData("position error", robot.getRotator().getMotor().getTargetPosition() - robot.getRotator().getEncoderPosition());
        telemetry.addData("Current Motif",Arrays.toString(motif));
        telemetry.update();
    }
}
