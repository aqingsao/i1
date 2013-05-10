package com.thoughtworks.i1.quartz.domain;

import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

import java.util.Date;

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

    public TriggerVO(SimpleTrigger trigger, Trigger.TriggerState triggerState) {
        this.triggerName = trigger.getKey().getName();
        this.triggerGroupName = trigger.getKey().getGroup();
        this.startTime = trigger.getStartTime();
        this.endTime = trigger.getEndTime();
        this.repeatCount = trigger.getRepeatCount();
        this.repeatInterval = trigger.getRepeatInterval();
        this.triggerState = triggerState.name();
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
}
