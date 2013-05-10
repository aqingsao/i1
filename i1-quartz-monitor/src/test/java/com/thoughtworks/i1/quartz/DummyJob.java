package com.thoughtworks.i1.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DummyJob implements Job {
    private static int count = 0;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println(String.format("Dummy job is executed for %d times", ++count));
    }

    public static int getCount() {
        return count;
    }
}
