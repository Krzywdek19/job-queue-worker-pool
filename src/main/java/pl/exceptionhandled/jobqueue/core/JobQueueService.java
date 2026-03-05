package pl.exceptionhandled.jobqueue.core;

import pl.exceptionhandled.jobqueue.cli.commands.JobType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class JobQueueService {
    private final ConcurrentHashMap<UUID, JobRecord> jobs = new ConcurrentHashMap<>();
    private final LinkedBlockingQueue<UUID> queue;
    private final RecentJobIds recent;

    public JobQueueService(int capacity, int historyCapacity) {
        this.recent = new RecentJobIds(historyCapacity);
        this.queue = new LinkedBlockingQueue<>(capacity);
    }

    public SubmitResponse submit(JobType type, String payload) {
        UUID id = UUID.randomUUID();
        if(!queue.offer(id)){
            return new SubmitResponse.Rejected("Queue full");
        }
        JobRecord record = new JobRecord(id, type, payload, JobStatus.QUEUED, 0, null, null);
        jobs.put(id, record);
        recent.add(record);
        return new SubmitResponse.Accepted(id);
    }

    public Optional<JobRecord> get(UUID id){
        return Optional.ofNullable(jobs.get(id));
    }

    public Optional<JobStatus> getStatus(UUID id){
        return get(id).map(JobRecord::status);
    }

    public List<JobRecord> list(int limit){
        return recent.latest(limit);
    }

    public Optional<JobResult> result(UUID id) {
        return get(id)
                .filter(r -> r.status() == JobStatus.SUCCESS)
                .map(JobRecord::result);
    }
}
