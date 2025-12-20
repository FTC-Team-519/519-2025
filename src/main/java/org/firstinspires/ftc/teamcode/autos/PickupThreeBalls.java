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
        final double drive_speed = 0.25;
        return new ParallelCommandGroup(
                new IntakeCommand(5.5, 1.0, robot),
                new DriveForward(27, drive_speed, robot),
                new DiskRotatePID(robot, 9)
        );
    }
}
