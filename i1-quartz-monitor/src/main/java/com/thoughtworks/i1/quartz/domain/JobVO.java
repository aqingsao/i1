package com.thoughtworks.i1.quartz.domain;

import com.google.common.collect.Lists;
import com.thoughtworks.i1.commons.SystemException;
import com.thoughtworks.i1.commons.config.builder.Builder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static org.quartz.JobBuilder.newJob;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class JobVO {
    private JobDetailVO detail;
    private List<TriggerVO> triggers = Lists.newArrayList();

    public JobVO() {
    }

    public JobVO(JobDetailVO jobDetailVO) {
        this.detail = jobDetailVO;
    }

    public String getJobName() {
        return detail.getJobName();
    }

    public String getJobGroupName() {
        return detail.getJobGroupName();
    }

    public String getDescription() {
        return detail.getDescription();
    }


    public String getJobClass() {
        return detail.getJobClass();
    }

    public List<JobDataVO> getJobDatas() {
        return detail.getJobDatas();
    }

    public List<TriggerVO> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<TriggerVO> triggers) {
        this.triggers = triggers;
    }

    public void addTriggerVO(TriggerVO triggerVO) {
        this.triggers.add(triggerVO);
    }

    public JobDetailVO getJobDetailVO(){
        return this.detail;
    }

    public JobDetail getJobDetail() {
        try {
            Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(getJobClass());
            String jobGroupName = getJobGroupName();
            JobBuilder jobBuilder = newJob(jobClass)
                    .withIdentity(getJobName(), jobGroupName.length() == 0 ? "HEREN-JOB-GROUP" : jobGroupName)
                    .withDescription(getDescription())
                    .storeDurably(true);
            List<JobDataVO> jobDatas = getJobDatas();
            for (JobDataVO jobDataVO : jobDatas) {
                String key = jobDataVO.getKey();
                String value = jobDataVO.getValue();
                jobBuilder.usingJobData(key, value);
            }
            return jobBuilder.build();
        } catch (ClassNotFoundException e) {
            throw new SystemException(String.format("Cannot find job with name %s", e.getMessage()), e);
        }
    }
    public static class QuartzVOBuilder implements Builder {

        private JobDetailVO.JobDetailVOBuilder jobDetailVOBuilder;

        private List<TriggerVO.TriggerVOBuilder> triggerVOBuilders = Lists.newArrayList();

        private QuartzVOBuilder() {
        }

        public static QuartzVOBuilder aJobVO() {
            return new QuartzVOBuilder();
        }

        public JobDetailVO.JobDetailVOBuilder jobDetail(JobDetail jobDetail) {
            this.jobDetailVOBuilder = new JobDetailVO.JobDetailVOBuilder(this, jobDetail);
            return this.jobDetailVOBuilder;
        }

        public JobDetailVO.JobDetailVOBuilder jobDetail(String name, String group, String jobClass, String description) {
            this.jobDetailVOBuilder = new JobDetailVO.JobDetailVOBuilder(this, name, group, jobClass, description);
            return this.jobDetailVOBuilder;
        }

        public TriggerVO.TriggerVOBuilder addTrigger(String triggerName, String triggerGroupName) {
            TriggerVO.TriggerVOBuilder triggerVOBuilder = new TriggerVO.TriggerVOBuilder(this, triggerName, triggerGroupName);
            this.triggerVOBuilders.add(triggerVOBuilder);
            return triggerVOBuilder;
        }
        @Override
        public JobVO build() {
            JobVO jobVO = new JobVO(jobDetailVOBuilder.build());
            for (TriggerVO.TriggerVOBuilder triggerVOBuilder : triggerVOBuilders) {
                jobVO.addTriggerVO(triggerVOBuilder.build());
            }
            return jobVO;
        }

    }
}
