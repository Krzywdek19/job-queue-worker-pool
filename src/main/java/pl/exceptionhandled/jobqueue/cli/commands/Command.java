package pl.exceptionhandled.jobqueue.cli.commands;

public sealed interface Command permits ExitCommand, HelpCommand, InvalidCommand, ListCommand, NoOpCommand, ResultCommand, ShutdownCommand, StatusCommand, SubmitCommand, UnknownCommand {
}
