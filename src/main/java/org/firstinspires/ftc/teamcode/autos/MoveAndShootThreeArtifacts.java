package org.firstinspires.ftc.teamcode.autos;

import org.firstinspires.ftc.teamcode.util.hardware.Robot;
import org.firstinspires.ftc.teamcode.util.commands.Command;
import org.firstinspires.ftc.teamcode.util.commands.actions.*;
import org.firstinspires.ftc.teamcode.util.commands.command_groups.SequentialCommandGroup;

public class MoveAndShootThreeArtifacts {
    public static Command init(Robot robot) {
        final double shoot_power = 0.52; //58 is good for teleop

        return new SequentialCommandGroup(
                new DriveInDirection(36 * 6.0 / 5.0, 3.0 * Math.PI / 4.0, robot),
                new AlignRotator(robot),
                ShootThreeArtifacts.init(robot, shoot_power)
        );
    }
}
