package org.firstinspires.ftc.teamcode.autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.util.OpModeBase;
import org.firstinspires.ftc.teamcode.util.commands.Command;
import org.firstinspires.ftc.teamcode.util.commands.actions.*;
import org.firstinspires.ftc.teamcode.util.commands.command_groups.ParallelCommandGroup;
import org.firstinspires.ftc.teamcode.util.commands.command_groups.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.util.hardware.Robot;

// Jonah was here
public class PickupThreeBalls {

    public static Command init(Robot robot) {
        return new SequentialCommandGroup(
                new AlignRotator(robot),
                new ParallelCommandGroup(
                        new IntakeCommand(5.5, 1.0, robot),
                        new SequentialCommandGroup(
                                new DriveForward(8, 0.1, robot),
                                new ParallelCommandGroup(
                                        new DriveForward(8, 0.1, robot),
                                        new DiskRotatePID(robot, true)
                                ),
                                new ParallelCommandGroup(
                                        new SequentialCommandGroup(
                                                new TimerCommand(0.1),
                                                new DriveForward(8, 0.1, robot)
                                        ),
                                        new DiskRotatePID(robot, true)
                                )
                        )
                )
        );
    }
}
