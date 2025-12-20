package org.firstinspires.ftc.teamcode.util.commands.actions;

import org.firstinspires.ftc.teamcode.util.hardware.Robot;
import org.firstinspires.ftc.teamcode.util.commands.Command;

public class DiskRotate implements Command {

    private final Robot robot;
    private final int rotate_sectors;
    private final double power;

    private TimerCommand waitTimer;

    public DiskRotate(Robot robot, boolean clockwise, double power) {
        this.robot = robot;
        this.rotate_sectors = clockwise?1:-1;
        this.power = power;
    }

    public DiskRotate(Robot robot, int rotate_sectors, double power) {
        this.robot = robot;
        this.rotate_sectors = rotate_sectors;
        this.power = power;
    }

    @Override
    public void init() {
        robot.getRotator().setDiskRotation(rotate_sectors);
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
