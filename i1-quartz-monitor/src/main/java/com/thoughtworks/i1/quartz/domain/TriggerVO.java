package com.thoughtworks.i1.quartz.domain;

import com.thoughtworks.i1.commons.config.builder.Builder;
import org.quartz.*;

import java.util.Date;

import static org.quartz.TriggerBuilder.newTrigger;

public class TriggerVO {

    private String triggerName;
    private String triggerGroupName;
    private Date startTime;
    private Date endTime;
    private String triggerState;
    private int repeatCount;
    private long repeatInterval;

    public TriggerVO() {
    }

    public TriggerVO(String name, String groupName, Date startTime, Date endTime, int repeatCount, long repeatInterval, String triggerState) {
        this.triggerName = name;
        this.triggerGroupName = groupName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.repeatCount = repeatCount;
        this.repeatInterval = repeatInterval;
        this.triggerState = triggerState;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerGroupName() {
        return triggerGroupName;
    }

    public void setTriggerGroupName(String triggerGroupName) {
        this.triggerGroupName = triggerGroupName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getTriggerState() {
        return triggerState;
    }

    public void setTriggerState(String triggerState) {
        this.triggerState = triggerState;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public long getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(long repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public static TriggerVO fromTrigger(SimpleTrigger trigger, String triggerStateName) {
        return new TriggerVO(trigger.getKey().getName(), trigger.getKey().getGroup(), trigger.getStartTime(), trigger.getEndTime(), trigger.getRepeatCount(), trigger.getRepeatInterval(), triggerStateName);
    }

    public Trigger toTrigger(JobKey jobKey) {
        String triggerGroupName = getTriggerGroupName();
        TriggerBuilder<Trigger> triggerBuilder = newTrigger()
                .withIdentity(getTriggerName(), triggerGroupName.length() == 0 ? "HEREN-TRIGGER-GROUP" : triggerGroupName)
                .startAt(getStartTime())
                .endAt(getEndTime())
                .forJob(jobKey);
        triggerBuilder.withSchedule(
                SimpleScheduleBuilder
                        .simpleSchedule()
                        .withRepeatCount(getRepeatCount())
                        .withIntervalInMilliseconds(getRepeatInterval())
        );
        return triggerBuilder.build();
    }

    public static class TriggerVOBuilder implements Builder {
        private String triggerName ;
        private String triggerGroupName;
        private Date startTime;
        private Date endTime;
        private int repeatCount;
        private long repeatInterval;
        private String triggerState;

        private JobVO.QuartzVOBuilder parent;

        public TriggerVOBuilder(JobVO.QuartzVOBuilder parent, String triggerName, String triggerGroupName) {
            this.triggerName =triggerName;
            this.triggerGroupName = triggerGroupName;
            this.parent = parent;
        }

        public TriggerVOBuilder time(Date startTime, Date endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
            return this;
        }

        public TriggerVOBuilder repeat(int interval, int repeatCount) {
            this.repeatInterval = interval;
            this.repeatCount = repeatCount;
            return this;
        }

        public JobVO.QuartzVOBuilder end(){
            return parent;
        }

        public TriggerVO build() {
            return new TriggerVO(triggerName, triggerGroupName, startTime, endTime, repeatCount, repeatInterval, triggerState);
        }
    }
}
