package org.firstinspires.ftc.teamcode.autos.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.util.OpModeBase;
import org.firstinspires.ftc.teamcode.util.commands.Command;
import org.firstinspires.ftc.teamcode.util.commands.actions.AlignRotator;
import org.firstinspires.ftc.teamcode.util.hardware.Robot;

@Autonomous(name="ResetRotator",group="Testing")
public class ResetRotator extends OpModeBase {

    Command cmd;

    private boolean finished = false;

    public void init() {
        robot = new Robot(hardwareMap);
        robot.resumeMotifStreaming();
        cmd = new AlignRotator(robot);
    }

    @Override
    public void loop() {
        if(!cmd.isDone()) {
            cmd.run();
        } else if(cmd.isDone() && !finished) {
            cmd.shutdown();
            finished = true;
        }
    }
}
