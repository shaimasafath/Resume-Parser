package ltts.com.service;

import ltts.com.model.Authentication;

public interface AuthService {
    Authentication registerAuthentication(Authentication authentication);
    boolean loginAuthentication(String email, String password);
}
