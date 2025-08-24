package ltts.com.service;

import org.springframework.web.multipart.MultipartFile;

import ltts.com.model.Resume;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ResumeService {
	 Resume parseResume(MultipartFile file);
	
	 Resume getResumeById(Long resumeId);
	boolean matchResumeWithJobDescriptions(Long resumeId, Long jobId);
    List<Resume> shortlistResumesForJob(Long jobId);
    Map<String, Object> getMatchInfo(Long resumeId, Long jobId);
}