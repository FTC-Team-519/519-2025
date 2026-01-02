package org.firstinspires.ftc.teamcode.util.commands.actions;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.util.hardware.Robot;
import org.firstinspires.ftc.teamcode.util.RobotMath;
import org.firstinspires.ftc.teamcode.util.commands.Command;

//the distance that the motors report they have driven might not be the actual amount we have driven due to slippage
//I am not sure if this will drive exactly the prescribed amount so we will need to test it out
public class DriveInDirection implements Command {
    private final double distance;

    //from 0 to 2PI
    private final double direction;
    private final Robot robot;

    public DriveInDirection(double inches, double direction, Robot robot) {
        this.direction = RobotMath.trueMod(direction, (Math.PI * 2));
        this.robot = robot;
        this.distance = inches * Robot.getCountsPerInchForDriveMotors();
    }

    @Override
    public void init() {
        this.robot.setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.robot.setDriveTargetPosition((int) distance); //we might need to tweak this value a bit
    }

    @Override
    public void run() {
        double y = Math.sin(direction);
        double x = Math.cos(direction);

        double lf_power = y + x;
        double rf_power = y - x;
        double lb_power = y - x;
        double rb_power = y + x;

        double scale = Math.abs(y) + Math.abs(x);

        lf_power /= scale;
        rf_power /= scale;
        lb_power /= scale;
        rb_power /= scale;

        robot.setLeftFrontPower(lf_power);
        robot.setRightFrontPower(rf_power);
        robot.setLeftBackPower(lb_power);
        robot.setRightBackPower(rb_power);
    }

    @Override
    public boolean isDone() {
        return Math.abs(robot.getLeftFrontDrive().getCurrentPosition()) >= Math.abs(robot.getLeftFrontDrive().getTargetPosition()) ||
                Math.abs(robot.getRightFrontDrive().getCurrentPosition()) >= Math.abs(robot.getRightFrontDrive().getTargetPosition()) ||
                Math.abs(robot.getLeftBackDrive().getCurrentPosition()) >= Math.abs(robot.getLeftBackDrive().getTargetPosition()) ||
                Math.abs(robot.getRightBackDrive().getCurrentPosition()) >= Math.abs(robot.getRightBackDrive().getTargetPosition());
    }

    @Override
    public void shutdown() {
        this.robot.setAllDrivePower(0.0);
        this.robot.setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public final static double FORWARDS = (Math.PI / 2.0);
    public final static double BACKWARDS = (Math.PI * 3.0 / 2.0);
    public final static double LEFT = (0.0);
    public final static double RIGHT = (Math.PI);

}
