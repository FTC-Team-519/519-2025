package org.firstinspires.ftc.teamcode.util.commands.actions;

import org.firstinspires.ftc.teamcode.util.hardware.Robot;
import org.firstinspires.ftc.teamcode.util.commands.Command;

public class DiskRotatePID implements Command {

    private Robot robot;
    private int rotate_sectors;

    private TimerCommand waitTimer;

    public DiskRotatePID(Robot robot, int rotate_sectors) {
        this.robot = robot;
        this.rotate_sectors = rotate_sectors;
    }

    public DiskRotatePID(Robot robot, boolean clockwise) {
        this(robot, clockwise?1:-1);
    }

    @Override
    public void init() {
        robot.getRotator().setDiskRotation(rotate_sectors);
    }

    @Override
    public void run() {
        robot.getRotator().runMotorToPositionPID();
    }

    @Override
    public boolean isDone() {
        return robot.getRotator().isAtPosition();
//        if (robot.getRotator().isAtPosition() ){
//            if (waitTimer == null){
//                waitTimer = new TimerCommand(0.001);
//            }else{
//                return waitTimer.isDone();
//            }
//        }
//        return false;
    }

    @Override
    public void shutdown() {
        robot.getRotator().runMotor(0.0);
    }
}
