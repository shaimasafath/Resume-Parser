package ltts.com.service;

import ltts.com.model.JobDescription;
import ltts.com.repository.JobDescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class JobDescriptionServiceImpl implements JobDescriptionService {

    private final JobDescriptionRepository jobDescriptionRepository;

    @Autowired
    public JobDescriptionServiceImpl(JobDescriptionRepository jobDescriptionRepository) {
        this.jobDescriptionRepository = jobDescriptionRepository;
    }

    @Override
    public JobDescription saveJobDescription(JobDescription jobDescription) {
        return jobDescriptionRepository.save(jobDescription);
    }

    @Override
    public JobDescription getJobDescriptionById(Long id) {
        Optional<JobDescription> optionalJobDescription = jobDescriptionRepository.findById(id);
        return optionalJobDescription.orElse(null);
    }

    @Override
    public List<JobDescription> getAllJobDescriptions() {
        return jobDescriptionRepository.findAll();
    }

    @Override
    public JobDescription updateJobDescription(Long id, JobDescription jobDescription) {
        Optional<JobDescription> optionalJobDescription = jobDescriptionRepository.findById(id);
        if (optionalJobDescription.isPresent()) {
            jobDescription.setId(id);
            return jobDescriptionRepository.save(jobDescription);
        }
        return null;
    }

    @Override
    public void deleteJobDescription(Long id) {
        jobDescriptionRepository.deleteById(id);
    }
}


