package ltts.com.model;

import java.util.regex.Pattern;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "job_descriptions")
public class JobDescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String requiredSkills;
    @Column(columnDefinition = "TEXT")
    private String requiredEducation;
    @Column(columnDefinition = "TEXT")
    private String requiredExperience;

    // Constructors
    public JobDescription() {
    }

    public JobDescription(String title, String requiredSkills, String requiredEducation, String requiredExperience) {
        this.title = title;
        this.requiredSkills = requiredSkills;
        this.requiredEducation = requiredEducation;
        this.requiredExperience = requiredExperience;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(String requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public String getRequiredEducation() {
        return requiredEducation;
    }

    public void setRequiredEducation(String requiredEducation) {
        this.requiredEducation = requiredEducation;
    }

    public String getRequiredExperience() {
        return requiredExperience;
    }

    public void setRequiredExperience(String requiredExperience) {
        this.requiredExperience = requiredExperience;
    }

    @Override
    public String toString() {
        return "JobDescription{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", requiredSkills='" + requiredSkills + '\'' +
                ", requiredEducation='" + requiredEducation + '\'' +
                ", requiredExperience='" + requiredExperience + '\'' +
                '}';
    }

	public Pattern getSkills() {
		// TODO Auto-generated method stub
		return null;
	}


}
