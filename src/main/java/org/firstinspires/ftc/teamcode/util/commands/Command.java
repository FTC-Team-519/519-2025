package org.firstinspires.ftc.teamcode.util.commands;

public interface Command {
    public void init();
    public void run();
    public boolean isDone();
    public void shutdown();
}
