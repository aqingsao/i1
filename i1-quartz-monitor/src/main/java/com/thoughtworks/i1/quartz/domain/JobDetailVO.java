package com.thoughtworks.i1.quartz.domain;

import com.google.common.collect.Lists;
import com.thoughtworks.i1.commons.config.builder.Builder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class JobDetailVO {
    public static final String DEFAULT_GROUP_NAME = "HEREN-JOB-GROUP";
    private String jobName;
    private String jobGroupName = DEFAULT_GROUP_NAME;
    private String jobClass;
    private String description;
//    @XmlTransient
    private List<JobDataVO> jobDatas = Lists.newArrayList();

    public JobDetailVO(){
    }

    public JobDetailVO(String jobName, String jobGroupName, String jobClass, String description, List jobDatas) {
        this.jobName = jobName;
        this.jobGroupName = jobGroupName;
        this.jobClass = jobClass;
        this.description = description;
        this.jobDatas = jobDatas;
    }

    public String getJobName() {
        return jobName;
    }

    public String getJobGroupName() {
        return jobGroupName;
    }

    public String getJobClass() {
        return jobClass;
    }

    public String getDescription() {
        return description;
    }

    public List getJobDatas(){
        return this.jobDatas;
    }

    public static JobDetailVO fromJobDetail(JobDetail jobDetail) {
        return new JobDetailVO(jobDetail.getKey().getName(), jobDetail.getKey().getGroup(), jobDetail.getJobClass().getName(),jobDetail.getDescription(), getJobDataVOs(jobDetail.getJobDataMap()));
    }

    private static List<JobDataVO> getJobDataVOs(JobDataMap jobDataMap) {
        List<JobDataVO> jobDataVOList = Lists.newArrayList();
        for (String key : jobDataMap.getKeys()) {
            jobDataVOList.add(new JobDataVO(key, jobDataMap.get(key).toString()));
        }
        return jobDataVOList;
    }

    public static class JobDetailVOBuilder implements Builder{
        private String jobName;
        private String jobGroupName;
        private String jobClass;
        private String description;

        private JobVO.QuartzVOBuilder parent;
        private List jobData = Lists.newArrayList();

        private JobDetailVOBuilder(String jobName, String jobGroupName, String jobClass, String description) {
            this.jobName = jobName;
            this.jobGroupName = jobGroupName;
            this.jobClass = jobClass;
            this.description = description;
        }

        public JobDetailVOBuilder(JobVO.QuartzVOBuilder parent, String jobName, String jobGroupName, String jobClass, String description) {
            this.jobName = jobName;
            this.jobGroupName = jobGroupName;
            this.jobClass = jobClass;
            this.description = description;
            this.parent = parent;
        }

        public JobDetailVOBuilder(JobVO.QuartzVOBuilder parent, JobDetail jobDetail) {
            this(parent, jobDetail.getKey().getName(), jobDetail.getKey().getGroup(), jobDetail.getJobClass().getName(), jobDetail.getDescription());
        }

        public static JobDetailVOBuilder aJobDetailVO(String jobName, String jobGroupName, String jobClass, String description) {
            return new JobDetailVOBuilder(jobName, jobGroupName, jobClass, description);
        }

        public JobDetailVOBuilder addJobData(String key, String value) {
            this.jobData.add(new JobDataVO(key, value));
            return this;
        }

        public JobVO.QuartzVOBuilder end(){
            return parent;
        }

        @Override
        public JobDetailVO build() {
            return new JobDetailVO(jobName, jobGroupName, jobClass, description, jobData);
        }
    }
}
