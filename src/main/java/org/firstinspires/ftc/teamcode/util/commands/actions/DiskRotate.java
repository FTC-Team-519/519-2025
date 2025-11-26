package org.firstinspires.ftc.teamcode.util.commands.actions;

import com.qualcomm.robotcore.hardware.*;
import org.firstinspires.ftc.teamcode.util.Robot;
import org.firstinspires.ftc.teamcode.util.commands.Command;

public class DiskRotate implements Command {

    private final Robot robot;
    private final boolean clockwise;
    private final double power;

    private TimerCommand waitTimer;

    public DiskRotate(Robot robot, boolean clockwise, double power) {
        this.robot = robot;
        this.clockwise = clockwise;
        this.power = power;
    }

    @Override
    public void init() {
        robot.getRotator().setDiskRotation(clockwise);
    }

    @Override
    public void run() {
        robot.getRotator().runMotorToPosition(power);
    }

    @Override
    public boolean isDone() {
        if (robot.getRotator().isAtPosition() ){
            if (waitTimer == null){
                waitTimer = new TimerCommand(0.01);
            }else{
                return waitTimer.isDone();
            }
        }
        return false;
    }

    @Override
    public void shutdown() {
        robot.getRotator().runMotor(0.0);
    }
}
