package ltts.com.repository;
 
import ltts.com.model.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface AuthRepository extends JpaRepository<Authentication, Integer> {
    Authentication findByEmail(String email);
}
