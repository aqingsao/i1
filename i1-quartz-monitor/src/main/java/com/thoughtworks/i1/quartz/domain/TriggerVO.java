package com.thoughtworks.i1.quartz.domain;

import com.thoughtworks.i1.commons.config.builder.Builder;
import org.quartz.*;

import java.util.Date;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class TriggerVO {

    private Integer triggerFlag;
    private String triggerName;
    private String triggerGroupName;
    private Date startTime;
    private Date endTime;
    private String triggerState;
    private int repeatCount;
    private long repeatInterval;
    private String cron;

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

    public TriggerVO(Integer triggerFlag, String triggerName, String triggerGroupName, Date startTime, Date endTime, String triggerState, int repeatCount, long repeatInterval, String cron) {
        this.triggerFlag = triggerFlag;
        this.triggerName = triggerName;
        this.triggerGroupName = triggerGroupName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.triggerState = triggerState;
        this.repeatCount = repeatCount;
        this.repeatInterval = repeatInterval;
        this.cron = cron;
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

    public Integer getTriggerFlag() {
        return triggerFlag;
    }

    public void setTriggerFlag(Integer triggerFlag) {
        this.triggerFlag = triggerFlag;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public static TriggerVO fromTrigger(SimpleTrigger trigger, String triggerStateName) {
        TriggerVO triggerVO = new TriggerVO(trigger.getKey().getName(), trigger.getKey().getGroup(), trigger.getStartTime(), trigger.getEndTime(), trigger.getRepeatCount(), trigger.getRepeatInterval(), triggerStateName);
        triggerVO.setTriggerFlag(0);
        triggerVO.setCron("");
        return triggerVO;
    }

    public static TriggerVO fromTrigger(CronTrigger trigger, String triggerStateName) {
        TriggerVO triggerVO = new TriggerVO(trigger.getKey().getName(), trigger.getKey().getGroup(), trigger.getStartTime(), trigger.getEndTime(), 0, 0, triggerStateName);
        triggerVO.setTriggerFlag(1);
        triggerVO.setCron(trigger.getCronExpression());
        return triggerVO;
    }


    public Trigger toTrigger(JobKey jobKey) {
        String triggerGroupName = getTriggerGroupName();
        if(getTriggerFlag() == 0){
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
        } else {
            return newTrigger()
                    .withIdentity(getTriggerName(), triggerGroupName.length() == 0 ? "HEREN-TRIGGER-GROUP" : triggerGroupName)
                    .withSchedule(cronSchedule(getCron()))
                    .startAt(getStartTime())
                    .endAt(getEndTime())
                    .forJob(jobKey)
                    .build() ;
        }

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

        public TriggerVOBuilder repeat(long interval, int repeatCount) {
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
