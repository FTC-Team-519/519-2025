package org.firstinspires.ftc.teamcode.util.commands.actions;

import org.firstinspires.ftc.teamcode.util.hardware.Robot;
import org.firstinspires.ftc.teamcode.util.commands.Command;

public class DiskRotatePID implements Command {

    private Robot robot;
    private boolean clockwise;

    private TimerCommand waitTimer;

    public DiskRotatePID(Robot robot, boolean clockwise) {
        this.robot = robot;
        this.clockwise = clockwise;
    }

    @Override
    public void init() {
        robot.getRotator().setDiskRotation(clockwise);
    }

    @Override
    public void run() {
        robot.getRotator().runMotorToPositionPID();
    }

    @Override
    public boolean isDone() {
        if (robot.getRotator().isAtPosition() ){
            if (waitTimer == null){
                waitTimer = new TimerCommand(0.001);
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
