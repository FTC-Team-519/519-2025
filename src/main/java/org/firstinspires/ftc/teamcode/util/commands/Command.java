package org.firstinspires.ftc.teamcode.util.commands;

public interface Command {
    void init();
    void run();
    boolean isDone();
    void shutdown();
}
