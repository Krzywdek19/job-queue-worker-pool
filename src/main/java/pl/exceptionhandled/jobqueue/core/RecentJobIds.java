package pl.exceptionhandled.jobqueue.core;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecentJobIds {
    private final ArrayDeque<JobRecord> recentJobs;
    private final int capacity;

    public RecentJobIds(int capacity) {
        this.capacity = capacity;
        recentJobs = new ArrayDeque<>(capacity);
    }

    public synchronized void add(JobRecord jobRecord) {
        if (recentJobs.size() == capacity) {
            recentJobs.removeFirst();
        }
        recentJobs.addLast(jobRecord);
    }

    public synchronized List<JobRecord> latest(int limit){
        if(limit <= 0){
            return List.of();
        }
        int n = Math.min(limit, recentJobs.size());
        List<JobRecord> out = new ArrayList<>(n);

        var it = recentJobs.descendingIterator();
        while (it.hasNext() && out.size() < n) {
            out.add(it.next());
        }
        return out;
    }
}
