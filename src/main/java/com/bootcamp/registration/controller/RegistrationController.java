package com.bootcamp.registration.controller;

import com.bootcamp.registration.model.Role;
import com.bootcamp.registration.model.RoleEnum;
import com.bootcamp.registration.model.User;
import com.bootcamp.registration.repository.RoleRepository;
import com.bootcamp.registration.repository.UserRepository;
import com.bootcamp.registration.request.SignupRequest;
import com.bootcamp.registration.response.MessageResponse;
import com.bootcamp.registration.service.CaptchaValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

/**
 * This class will be used for doing the resitration process.
 */
@CrossOrigin(origins ="*",maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class RegistrationController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    CaptchaValidatorService captchaValidatorService;

    /**
     * This method is used for registration process, where user has to provide username, email and password details.
     * This service checks to see if the username already exists or email already exists, if any of them already exists,
     * then it sends out the appropriate message stating that "username is not available" or "email id already registered".
     * After successful validation of username and email it encodes the password and create a user document in user repository.
     * @param signupRequest
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        boolean isvalidCaptcha = captchaValidatorService.validateCaptcha(signupRequest.getCaptchaResp(),null);

        if (!isvalidCaptcha){
            return ResponseEntity.
                    badRequest().
                    body(new MessageResponse("Error: Invalid Captcha not validated"));
        }
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.
                    badRequest().
                    body(new MessageResponse("Error: Username is not available"));
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already registered"));
        }
        User user = new User(signupRequest.getUsername()
                ,signupRequest.getEmail()
                ,passwordEncoder.encode(signupRequest.getPassword()));
        Set<String> strRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet();
        for (String role: strRoles){
            System.out.println("role--->"+role);
        }
        if (strRoles == null){
            logger.error("Error: Roles not found, check if the roles inforamtion initiated in the DB");
        }else{
            strRoles.forEach(role->{
                switch(role){
                    case "admin":
                        Role adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN)
                                .orElseThrow(()-> new RuntimeException("Error: Role is not found"));
                        roles.add(adminRole);
                        break;
                    case "moderator":
                        Role moderatorRole = roleRepository.findByName(RoleEnum.ROLE_MODERAROR)
                                .orElseThrow(()-> new RuntimeException("Error: Role is not found"));
                        roles.add(moderatorRole);
                    break;
                    default:
                        System.out.println(roleRepository.existsByName(RoleEnum.ROLE_USER));
                        System.out.println(roleRepository.findByName(RoleEnum.ROLE_USER).get().getName());

                        Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                                .orElseThrow(()-> new RuntimeException("Error: Role is not found"));
                        roles.add(userRole);
                }
            });

        }
        user.setRoles(roles);
        user.setResetPasswordToken(null);
        user.setNewPassword(null);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User Registered Successfully"));
    }

    /**
     * This method un-resiters the user, by sending out an unregistered event,
     * sending out an email to the user stating the user unregistered successfully
     * and finally that user gets deleted from the user repository.
     * @param user
     * @return
     */

    @DeleteMapping("/de-register")
    public ResponseEntity<MessageResponse> deRegisterUser(@RequestBody User user){
        //TODO: Need to send out an email that user success un-registered.
        // Send out an event message to Email Listener
        logger.info("User De-Reistered Successfully");
        userRepository.findByUsername(user.getUsername()).ifPresent(user_to_remove -> userRepository.delete(user_to_remove));

        return ResponseEntity.ok(new MessageResponse("User De-Registered Successfully"));
    }

}

