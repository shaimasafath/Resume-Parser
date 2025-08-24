package ltts.com.controller;

import ltts.com.model.JobDescription;
import ltts.com.service.JobDescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/job-descriptions")
public class JobDescriptionController {

    private final JobDescriptionService jobDescriptionService;

    public JobDescriptionController(JobDescriptionService jobDescriptionService) {
        this.jobDescriptionService = jobDescriptionService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createJobDescription(@RequestBody JobDescription jobDescription) {
        jobDescriptionService.saveJobDescription(jobDescription);
        return ResponseEntity.ok("Job description created successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<JobDescription>> getAllJobDescriptions() {
        List<JobDescription> jobDescriptions = jobDescriptionService.getAllJobDescriptions();
        return ResponseEntity.ok(jobDescriptions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDescription> getJobDescriptionById(@PathVariable Long id) {
        JobDescription jobDescription = jobDescriptionService.getJobDescriptionById(id);
        if (jobDescription == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(jobDescription);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<JobDescription> updateJobDescription(@PathVariable Long id, @RequestBody JobDescription jobDescription) {
        JobDescription updatedJobDescription = jobDescriptionService.updateJobDescription(id, jobDescription);
        if (updatedJobDescription == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedJobDescription);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteJobDescription(@PathVariable Long id) {
        jobDescriptionService.deleteJobDescription(id);
        return ResponseEntity.ok("Job description deleted successfully");
    }
}

