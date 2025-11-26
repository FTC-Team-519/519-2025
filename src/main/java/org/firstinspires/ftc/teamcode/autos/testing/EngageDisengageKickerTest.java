package org.firstinspires.ftc.teamcode.autos.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.util.OpModeBase;
import org.firstinspires.ftc.teamcode.util.commands.actions.EngageDisengageKicker;
import org.firstinspires.ftc.teamcode.util.commands.actions.TimerCommand;
import org.firstinspires.ftc.teamcode.util.commands.command_groups.SequentialCommandGroup;

@Autonomous(name="EngageDisengageKickerTest", group="test")
public class EngageDisengageKickerTest extends OpModeBase {

    private SequentialCommandGroup seq;

    public void init() {
        super.init();

        this.seq = new SequentialCommandGroup(
                new EngageDisengageKicker(robot, 0.5, true),
                new TimerCommand(5),
                new EngageDisengageKicker(robot, 0.5, false)
        );
    }
    @Override
    public void loop() {
        if (!seq.isDone()) {
            seq.run();
        }
    }
}