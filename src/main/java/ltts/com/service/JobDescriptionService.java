package ltts.com.service;

import ltts.com.model.JobDescription;

import java.util.List;

public interface JobDescriptionService {

    JobDescription saveJobDescription(JobDescription jobDescription);

    JobDescription getJobDescriptionById(Long id);

    List<JobDescription> getAllJobDescriptions();

    JobDescription updateJobDescription(Long id, JobDescription jobDescription);

    void deleteJobDescription(Long id);

}


