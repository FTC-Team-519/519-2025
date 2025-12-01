package org.firstinspires.ftc.teamcode.autos.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.util.OpModeBase;
import org.firstinspires.ftc.teamcode.util.commands.Command;
import org.firstinspires.ftc.teamcode.util.commands.actions.AlignRotator;

@Autonomous(name="ResetRotator",group="Testing")
public class ResetRotator extends OpModeBase {

    Command cmd = new AlignRotator(robot);

    private boolean finished = false;

    public void init() {super.init();}

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
