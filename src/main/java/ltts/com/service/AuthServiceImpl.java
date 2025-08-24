package ltts.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ltts.com.model.Authentication;
import ltts.com.repository.AuthRepository;

@Service
public class AuthServiceImpl implements AuthService {
 
    @Autowired
    private AuthRepository authenticationRepository;
 
    @Override
    public Authentication registerAuthentication(Authentication authentication) {
        return authenticationRepository.save(authentication);
    }
 
    @Override
    public boolean loginAuthentication(String email, String password) {
        Authentication authentication = authenticationRepository.findByEmail(email);
        if(authentication != null && authentication.getPassword().equals(password)) {
            return true;
        }
        return false;
    }




}
