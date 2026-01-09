package org.firstinspires.ftc.teamcode.autos;

import org.firstinspires.ftc.teamcode.util.commands.Command;
import org.firstinspires.ftc.teamcode.util.commands.actions.*;
import org.firstinspires.ftc.teamcode.util.commands.command_groups.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.util.hardware.IntakeColorSensor;
import org.firstinspires.ftc.teamcode.util.hardware.IntakeColorSensor.*;
import org.firstinspires.ftc.teamcode.util.hardware.Robot;

import static org.firstinspires.ftc.teamcode.util.hardware.IntakeColorSensor.pieceType.*;

public class ShootThreeArtifacts implements Command {
    private Command command;

    private Robot robot;

    private double shoot_power;

    private boolean rotate_clockwise = true;

    public ShootThreeArtifacts(Robot robot, double shoot_power){
        this.robot = robot;
        this.shoot_power = shoot_power;
    }

    //command stuff

    @Override
    public void run() {
        this.command.run();
    }

    @Override
    public void init(){
        this.command =  new SequentialCommandGroup(
                new Instant(() -> {
                    IntakeColorSensor.pieceType[] motif = robot.getMotif();
                    if (motif == null){
                        motif = IntakeColorSensor.defaultMotif();
                    }
                    if (motif.equals(new pieceType[]{PURPLE, PURPLE, GREEN})){
                        this.rotate_clockwise = true;
                    } else if (motif.equals(new pieceType[]{PURPLE, GREEN, PURPLE})) {
                        this.rotate_clockwise = false;
                    } else if (motif.equals(new pieceType[]{GREEN, PURPLE, PURPLE})){
                        this.rotate_clockwise = true;
                        return new DiskRotatePIDF(robot, false);
                    }
                    return null;
                }),

                //first shot
                new OuttakeCommand(robot, shoot_power),
                new TimerCommand(0.75),
                new EngageDisengageKicker(robot, 1.0, true),
                new TimerCommand(0.75),
                new EngageDisengageKicker(robot, 1.0, false),
                new DiskRotatePIDF(robot, true),

                //second shot
                new TimerCommand(0.75),
                new EngageDisengageKicker(robot, 1.0, true),
                new TimerCommand(0.75),
                new EngageDisengageKicker(robot, 1.0, false),
                new DiskRotatePIDF(robot, true),

                //third shot
                new TimerCommand(0.75),
                new EngageDisengageKicker(robot, 1.0, true),
                new TimerCommand(0.75),
                new EngageDisengageKicker(robot, 1.0, false),
                new DiskRotatePIDF(robot, true),

                new OuttakeCommand(robot, 0.0)
        );
    };

    @Override
    public boolean isDone() {
        return this.command.isDone();
    }

    @Override
    public void shutdown() {
        this.command.shutdown();
    }
}
