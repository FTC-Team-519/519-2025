package org.firstinspires.ftc.teamcode.util.commands.actions;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.util.commands.Command;

public class TimerCommand implements Command {

    private final double seconds;
    private final ElapsedTime timer;

    public TimerCommand(double seconds) {
        timer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
        this.seconds = seconds;
    }

    @Override
    public void init() {
        timer.reset();
    }

    @Override
    public void run() {

    }

    @Override
    public boolean isDone() {
        return timer.time() >= seconds;
    }

    @Override
    public void shutdown() {

    }
}
