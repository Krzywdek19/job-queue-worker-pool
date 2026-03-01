package pl.exceptionhandled.jobqueue.cli.commands;

public sealed interface Command permits ExitCommand, HelpCommand, InvalidCommand, NoOpCommand, SubmitCommand, UnknownCommand {
}
