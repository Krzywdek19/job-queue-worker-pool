package pl.exceptionhandled.jobqueue.cli.commands;

import java.util.UUID;

public record ResultCommand(UUID jobId) implements Command {
}
