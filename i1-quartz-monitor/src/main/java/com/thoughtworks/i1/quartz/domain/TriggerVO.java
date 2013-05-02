package com.thoughtworks.i1.quartz.domain;

import java.util.Date;
import java.util.Map;

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

    public TriggerVO(String triggerName, String triggerGroupName, Date startTime, Date endTime, String triggerState, int repeatCount, long repeatInterval) {
        this.triggerName = triggerName;
        this.triggerGroupName = triggerGroupName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.triggerState = triggerState;
        this.repeatCount = repeatCount;
        this.repeatInterval = repeatInterval;
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
