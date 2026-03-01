package pl.exceptionhandled.jobqueue.core;

import pl.exceptionhandled.jobqueue.cli.commands.JobType;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class JobQueueService {
    private final ConcurrentHashMap<UUID, JobRecord> jobs = new ConcurrentHashMap<>();

    public UUID submit(JobType type, String payload) {
        UUID id = UUID.randomUUID();
        JobRecord record = new JobRecord(id, type, payload, JobStatus.QUEUED, 0);
        jobs.put(id, record);
        return id;
    }
}
