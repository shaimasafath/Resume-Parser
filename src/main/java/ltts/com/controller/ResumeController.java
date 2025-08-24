package ltts.com.controller;

import ltts.com.model.Resume;
import ltts.com.service.ResumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadResume(@RequestParam("file") MultipartFile file) {
        try {
            Resume resume = resumeService.parseResume(file);
            return ResponseEntity.ok(Map.of(
                "id", resume.getId(),
                "name", resume.getName(),
                "email", resume.getEmail(),
                "phone", resume.getPhone(),
                "education", resume.getEducation(),
                "skills", resume.getSkills(),
                "experience", resume.getExperience()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error: " + e.getMessage()));
        }
    }

    @GetMapping("/{resumeId}")
    public ResponseEntity<Resume> getResumeById(@PathVariable Long resumeId) {
        Resume resume = resumeService.getResumeById(resumeId);
        if (resume == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resume);
    }

    @GetMapping("/{resumeId}/extract")
    public ResponseEntity<String> extractResumeData(@PathVariable Long resumeId) {
        Resume resume = resumeService.getResumeById(resumeId);
        if (resume == null) {
            return ResponseEntity.notFound().build();
        }
        StringBuilder extractedData = new StringBuilder();
        extractedData.append("Name: ").append(resume.getName()).append("\n");
        extractedData.append("Phone: ").append(resume.getPhone()).append("\n");
        extractedData.append("Email: ").append(resume.getEmail()).append("\n");
        extractedData.append("Education: ").append(resume.getEducation()).append("\n");
        extractedData.append("Skills: ").append(resume.getSkills()).append("\n");
        extractedData.append("Experience: ").append(resume.getExperience()).append("\n");
        return ResponseEntity.ok(extractedData.toString());
    }

    @GetMapping("/{resumeId}/match/{jobId}")
    public ResponseEntity<?> matchResumeWithJobDescriptions(@PathVariable Long resumeId, @PathVariable Long jobId) {
        try {
            boolean isMatched = resumeService.matchResumeWithJobDescriptions(resumeId, jobId);
            return ResponseEntity.ok(Map.of("match", isMatched));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error: " + e.getMessage()));
        }
    }

    @GetMapping("/shortlist/{jobId}")
    public ResponseEntity<List<Resume>> shortlistResumesForJob(@PathVariable Long jobId) {
        List<Resume> shortlistedResumes = resumeService.shortlistResumesForJob(jobId);
        return ResponseEntity.ok(shortlistedResumes);
    }

    @GetMapping("/{resumeId}/matchinfo/{jobId}")
    public ResponseEntity<?> getMatchInfo(@PathVariable Long resumeId, @PathVariable Long jobId) {
        try {
            Map<String, Object> matchInfo = resumeService.getMatchInfo(resumeId, jobId);
            return ResponseEntity.ok(matchInfo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error: " + e.getMessage()));
        }
    }
}
