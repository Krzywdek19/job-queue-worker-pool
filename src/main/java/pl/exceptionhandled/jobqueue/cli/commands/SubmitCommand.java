package pl.exceptionhandled.jobqueue.cli.commands;

public record SubmitCommand (JobType type, String payload) implements Command {
}
