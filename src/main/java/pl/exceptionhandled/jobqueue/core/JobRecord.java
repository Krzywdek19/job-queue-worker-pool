package pl.exceptionhandled.jobqueue.core;

import pl.exceptionhandled.jobqueue.cli.commands.JobType;

import java.util.UUID;

public record JobRecord (UUID id, JobType type, String payload, JobStatus status, int attempts) {
}
