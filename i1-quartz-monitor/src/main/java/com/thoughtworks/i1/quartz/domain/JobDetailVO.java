package com.thoughtworks.i1.quartz.domain;

import com.google.common.collect.Lists;
import com.thoughtworks.i1.commons.config.builder.Builder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class JobDetailVO {
    public static final String DEFAULT_GROUP_NAME = "HEREN-JOB-GROUP";
    private String jobName;
    private String jobGroupName = DEFAULT_GROUP_NAME;
    private String jobClass;
//    @XmlTransient
    private List<JobDataVO> jobData = Lists.newArrayList();

    public JobDetailVO(){
    }

    public JobDetailVO(String jobName, String jobGroupName, String jobClass, List jobData) {
        this.jobName = jobName;
        this.jobGroupName = jobGroupName;
        this.jobClass = jobClass;
        this.jobData = jobData;
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

    public List getJobData(){
        return this.jobData;
    }

    public static JobDetailVO fromJobDetail(JobDetail jobDetail) {
        return new JobDetailVO(jobDetail.getKey().getName(), jobDetail.getKey().getGroup(), jobDetail.getJobClass().getName(), getJobDataVOs(jobDetail.getJobDataMap()));
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

        private JobVO.QuartzVOBuilder parent;
        private List jobData = Lists.newArrayList();

        private JobDetailVOBuilder(String jobName, String jobGroupName, String jobClass) {
            this.jobName = jobName;
            this.jobGroupName = jobGroupName;
            this.jobClass = jobClass;
        }

        public JobDetailVOBuilder(JobVO.QuartzVOBuilder parent, String jobName, String jobGroupName, String jobClass) {
            this.jobName = jobName;
            this.jobGroupName = jobGroupName;
            this.jobClass = jobClass;

            this.parent = parent;
        }

        public JobDetailVOBuilder(JobVO.QuartzVOBuilder parent, JobDetail jobDetail) {
            this(parent, jobDetail.getKey().getName(), jobDetail.getKey().getGroup(), jobDetail.getJobClass().getName());
        }

        public static JobDetailVOBuilder aJobDetailVO(String jobName, String jobGroupName, String jobClass) {
            return new JobDetailVOBuilder(jobName, jobGroupName, jobClass);
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
            return new JobDetailVO(jobName, jobGroupName, jobClass, jobData);
        }
    }
}
