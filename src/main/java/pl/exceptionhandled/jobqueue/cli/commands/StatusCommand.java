package pl.exceptionhandled.jobqueue.cli.commands;

import java.util.UUID;

public record StatusCommand(UUID jobId) implements Command {
}
