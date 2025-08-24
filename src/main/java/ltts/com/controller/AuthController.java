package ltts.com.controller;
  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
 
import ltts.com.model.Authentication;
import ltts.com.service.AuthService;
 
@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {
 
    @Autowired
    private AuthService authenticationService;
 
    @PostMapping("/register")
    public boolean registerAuthentication(@RequestBody Authentication authentication) {
        Authentication registeredAuthentication = authenticationService.registerAuthentication(authentication);
        if(registeredAuthentication!=null) {
         return true;
        }
        return false;
    }
 
    @PostMapping("/login")
    public boolean loginAuthentication(@RequestBody Authentication authentication) {
         return authenticationService.loginAuthentication(authentication.getEmail(), authentication.getPassword());
    }
}