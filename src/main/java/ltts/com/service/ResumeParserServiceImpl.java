package ltts.com.service;

import ltts.com.model.JobDescription;
import ltts.com.model.Resume;
import ltts.com.repository.JobDescriptionRepository;
import ltts.com.repository.ResumeRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class ResumeParserServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final JobDescriptionRepository jobDescriptionRepository;
    private static final Logger logger = LoggerFactory.getLogger(ResumeParserServiceImpl.class);
   // private static final Pattern NAME_PATTERN = Pattern.compile("Name:\\s*(.*)", Pattern.DOTALL);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\(?([0-9]{3})\\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})");
      private static final Pattern EDUCATION_PATTERN = Pattern.compile("\\b(Education|EDUCATION|Educational Qualification |B-Tech|BE |Diploma|DIPLOMA)(.*?)(?=\\b(Experience|EXPERIENCE|SkillS|Career Journey|DECLARATION|Functional / Technical Experience|2015|2010|SKILL)\\b|$)", Pattern.DOTALL);
   // private static final Pattern EXPERIENCE_PATTERN = Pattern.compile("\\b(Experience|Experience Summary|EXPERIENCE|Work Experience|WORK EXPERIENCE)(.*?)(?=\\b(Skill|Work Summary|SKILL|Education|EDUCATION)\\b|$)", Pattern.DOTALL);
    private static final Pattern EXPERIENCE_PATTERN = Pattern.compile("\\b(Experience|Experience Summary|EXPERIENCE|Key Expertise|Work Experience|WORK EXPERIENCE).*?(?=\\b|$)(.*?(\\d+[+]?\\s*years?\\s*of\\s*experience\\b))", Pattern.DOTALL);
   // private static final Pattern SKILLS_PATTERN = Pattern.compile("\\b(Skill|Skills :|SKILL|Technical Skills|Tools|Technical Knowledge|TECHNICAL SKILLS|Computer Skills|COMPUTER SKILLS)(.*?)(?=\\b(Experience|EXPERIENCE|Education|EDUCATION)\\b|$)", Pattern.DOTALL);
    private static final Pattern SKILLS_PATTERN = Pattern.compile("\\b(Skill|Skills:|SKILL|Technical Skills|Technical Knowledge|TECHNICAL SKILLS|Computer Skills|COMPUTER SKILLS)(.*?)(?=\\b(Experience|EXPERIENCE|Project1|Education|SonarQube|EDUCATION)\\b|$)", Pattern.DOTALL);
    public ResumeParserServiceImpl(ResumeRepository resumeRepository, JobDescriptionRepository jobDescriptionRepository) {
        this.resumeRepository = resumeRepository;
        this.jobDescriptionRepository = jobDescriptionRepository;
    }

    @Override
    public Resume parseResume(MultipartFile file) {
        logger.info("Parsing resume content...");
        String content = parseResumeContent(file);
        logger.debug("Parsed content: {}", content);

     
        content = cleanText(content);

        String name = extractName(content);
        String email = extractEmail(content);
        String phone = extractPhone(content);
        String education = extractEducation(content);
        Set<String> skills = extractSkills(content);
        String experience = extractExperience(content);

        Resume resume = new Resume();
        resume.setName(name);
        resume.setEmail(email);
        resume.setPhone(phone);
        resume.setEducation(education);
        resume.setSkills(skills.stream().collect(Collectors.joining(", ")));
        resume.setExperience(experience);

        resumeRepository.save(resume);
        logger.info("Resume parsed and saved with ID: {}", resume.getId());
        return resume;
    }

    private String parseResumeContent(MultipartFile file) {
        String content = "";
        try {
            String fileType = file.getContentType();
            if (fileType.equals("application/pdf")) {
                PDDocument document = PDDocument.load(file.getInputStream());
                PDFTextStripper pdfStripper = new PDFTextStripper();
                content = pdfStripper.getText(document);
                document.close();
            } else if (fileType.equals("application/msword")) {
                HWPFDocument document = new HWPFDocument(file.getInputStream());
                WordExtractor wordExtractor = new WordExtractor(document);
                content = wordExtractor.getText();
            } else if (fileType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                XWPFDocument document = new XWPFDocument(file.getInputStream());
                XWPFWordExtractor wordExtractor = new XWPFWordExtractor(document);
                content = wordExtractor.getText();
            } else {
                throw new RuntimeException("Unsupported file type: " + fileType);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file content", e);
        }
        return content;
    }

    private String cleanText(String text) {
       
        return text.replaceAll("\\r?\\n", " ");
    }
   

//    
//    private static String extractName(String text) {
//        String[] lines = text.split("\\n");
//        for (String line : lines) {
//            if (line.contains(":")) {
//                String[] parts = line.split(":");
//                if (parts[0].trim().equalsIgnoreCase("Name") || parts[0].trim().equalsIgnoreCase("FullName")) {
//                    String name = parts[1].trim();
//                    int endIndex = name.lastIndexOf(' '); // stop at the last occurrence of a space character in the same line
//                    if (endIndex!= -1) {
//                        name = name.substring(0, endIndex).trim();
//                    }
//                    return name;
//                }
//            }
//        }
//       
//        if (lines.length > 0) {
//            String firstLine = lines[0].trim();
//            char lastChar = firstLine.charAt(firstLine.length() - 1);
//            return firstLine.substring(0, firstLine.length() - 1) + String.valueOf(lastChar).toLowerCase();
//        }
//        return "";
//    }
  private static String extractName(String text) {
  String[] lines = text.split("\\n");
  for (String line : lines) {
      if (line.contains(":")) {
          String[] parts = line.split(":");
          if (parts[0].trim().equalsIgnoreCase("Name") || parts[0].trim().equalsIgnoreCase("FullName")) {
              String name = parts[1].trim();
              int endIndex = name.lastIndexOf(' '); // stop at the last occurrence of a space character in the same line
              if (endIndex!= -1) {
                  name = name.substring(0, endIndex).trim();
              }
              return name;
          }
      }
  }
  return "Not Found";
}
    private String extractEmail(String content) {
        Matcher matcher = EMAIL_PATTERN.matcher(content);
        return matcher.find() ? matcher.group() : "Not Found";
    }

    private String extractPhone(String content) {
        Matcher matcher = PHONE_PATTERN.matcher(content);
        return matcher.find() ? matcher.group() : "Not Found";
    }

    private String extractEducation(String content) {
        Matcher matcher = EDUCATION_PATTERN.matcher(content);
        return matcher.find() ? matcher.group(2).trim() :"Not Found";
    }

    private Set<String> extractSkills(String content) {
        Matcher matcher = SKILLS_PATTERN.matcher(content);
        if (matcher.find()) {
            String skillsText = matcher.group(2).trim();
            return Arrays.stream(skillsText.split(",")).map(String::trim).collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    private String extractExperience(String content) {
        Matcher matcher = EXPERIENCE_PATTERN.matcher(content);
        return matcher.find() ? matcher.group(2).trim() : "Not Found";
    }

    @Override
    public Resume getResumeById(Long resumeId) {
        return resumeRepository.findById(resumeId).orElse(null);
    }

    @Override
    public boolean matchResumeWithJobDescriptions(Long resumeId, Long jobId) {
        Resume resume = getResumeById(resumeId);
        JobDescription jobDescription = jobDescriptionRepository.findById(jobId).orElse(null);
        if (resume == null || jobDescription == null) {
            throw new RuntimeException("Resume or Job Description not found");
        }

        boolean skillsMatch = matchSkills(resume.getSkills(), jobDescription.getRequiredSkills());
        boolean educationMatch = matchEducation(resume.getEducation(), jobDescription.getRequiredEducation());
        boolean experienceMatch = matchExperience(resume.getExperience(), jobDescription.getRequiredExperience());

        return skillsMatch && educationMatch && experienceMatch;
    }

    private boolean matchSkills(String resumeSkills, String requiredSkills) {
        Set<String> resumeSkillsSet = Arrays.stream(resumeSkills.split(",")).map(String::trim).collect(Collectors.toSet());
        Set<String> requiredSkillsSet = Arrays.stream(requiredSkills.split(",")).map(String::trim).collect(Collectors.toSet());

        // Calculate matching percentage
        int totalRequiredSkills = requiredSkillsSet.size();
        int matchedSkillsCount = 0;

        for (String requiredSkill : requiredSkillsSet) {
            if (resumeSkillsSet.contains(requiredSkill)) {
                matchedSkillsCount++;
            }
        }

        double matchingPercentage = (double) matchedSkillsCount / totalRequiredSkills * 100;
        return matchingPercentage >= 60.0; 
    }

    private boolean matchEducation(String resumeEducation, String requiredEducation) {
        return resumeEducation.equalsIgnoreCase(requiredEducation);
    }

    private boolean matchExperience(String resumeExperience, String requiredExperience) {
        return resumeExperience.equalsIgnoreCase(requiredExperience);
    }

    @Override
    public List<Resume> shortlistResumesForJob(Long jobId) {
        JobDescription jobDescription = jobDescriptionRepository.findById(jobId).orElse(null);
        if (jobDescription == null) {
            throw new RuntimeException("Job Description not found");
        }

        String[] jobSkills = jobDescription.getRequiredSkills().split(",");
        List<Resume> allResumes = resumeRepository.findAll();
        List<Resume> shortlistedResumes = new ArrayList<>();

        for (Resume resume : allResumes) {
            boolean isMatched = matchSkills(resume.getSkills(), jobDescription.getRequiredSkills()) &&
                    matchEducation(resume.getEducation(), jobDescription.getRequiredEducation()) &&
                    matchExperience(resume.getExperience(), jobDescription.getRequiredExperience());

            if (isMatched) {
                shortlistedResumes.add(resume);
            }
        }
        return shortlistedResumes;
    }

    @Override
    public Map<String, Object> getMatchInfo(Long resumeId, Long jobId) {
        Resume resume = getResumeById(resumeId);
        JobDescription jobDescription = jobDescriptionRepository.findById(jobId).orElse(null);
        if (resume == null || jobDescription == null) {
            throw new RuntimeException("Resume or Job Description not found");
        }

        Map<String, Object> matchInfo = new HashMap<>();
        matchInfo.put("resume", resume);
        matchInfo.put("jobDescription", jobDescription);

        boolean skillsMatch = matchSkills(resume.getSkills(), jobDescription.getRequiredSkills());
        boolean educationMatch = matchEducation(resume.getEducation(), jobDescription.getRequiredEducation());
        boolean experienceMatch = matchExperience(resume.getExperience(), jobDescription.getRequiredExperience());

        matchInfo.put("skillsMatch", skillsMatch);
        matchInfo.put("educationMatch", educationMatch);
        matchInfo.put("experienceMatch", experienceMatch);

        return matchInfo;
    }
}




