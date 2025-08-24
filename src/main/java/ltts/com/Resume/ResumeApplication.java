package ltts.com.Resume;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//import ltts.com.Shadow_project.config.ComponentScan;
//import ltts.com.Shadow_project.config.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "ltts.com")
@ComponentScan(basePackages = "ltts.com")
@EnableJpaRepositories(basePackages = "ltts.com.repository")
public class ResumeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResumeApplication.class, args);
        System.out.println("started");
    }
}
