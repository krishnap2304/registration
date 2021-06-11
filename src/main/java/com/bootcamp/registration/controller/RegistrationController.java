package com.bootcamp.registration.controller;

import com.bootcamp.registration.model.Role;
import com.bootcamp.registration.model.RoleEnum;
import com.bootcamp.registration.model.User;
import com.bootcamp.registration.repository.RoleRepository;
import com.bootcamp.registration.repository.UserRepository;
import com.bootcamp.registration.request.SignupRequest;
import com.bootcamp.registration.response.MessageResponse;
import com.bootcamp.registration.service.CaptchaValidatorService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class will be used for doing the resitration process.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
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
     *
     * @param signupRequest
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        boolean isvalidCaptcha = captchaValidatorService.validateCaptcha(signupRequest.getCaptchaResp(), null);
        if (!isvalidCaptcha) {
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
                , signupRequest.getEmail()
                , passwordEncoder.encode(signupRequest.getPassword()));
        Set<String> strRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet();
        if (strRoles == null) {
            throw new RuntimeException("Error: Roles  not found");
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(adminRole);
                        break;
                    case "moderator":
                        Role moderatorRole = roleRepository.findByName(RoleEnum.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(moderatorRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
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

    public ResponseEntity<MessageResponse> signupEmpty(){
       return  ResponseEntity.ok(new MessageResponse("Sign up down, please try after some time..."));
    }
    @HystrixCommand(fallbackMethod="getRolesEmpty")
    @GetMapping("roles")
    @Timed(value = "rolesloading.time", description = "Time taken to return roles")
    public List<Role> getAllRoles() {
        logger.info("Roles retrieved successfully");
        return roleRepository.findAll();
    }
    public List<Role> getRolesEmpty(){return Arrays.asList(new Role(RoleEnum.ROLE_USER));}

    @HystrixCommand(fallbackMethod="getRoleDefault")
    @GetMapping("role")
    @Timed(value = "roleloading.time", description = "Time taken to return roles")
    public Role getRoles(@RequestParam String name) {
        logger.info("Role info retrieved successfully");

        if (name.equals("user")) {
            return roleRepository.findByName(RoleEnum.ROLE_USER).get();
        } else if (name.equals("admin")) {
            return roleRepository.findByName(RoleEnum.ROLE_ADMIN).get();
        } else if(name.equals("moderator")){
            return roleRepository.findByName(RoleEnum.ROLE_MODERATOR).get();
        }
        return null;
    }
    public Role getRoleDefault(@RequestParam String name) {
        return new Role(RoleEnum.ROLE_MODERATOR);
    }
    @HystrixCommand(fallbackMethod="getUsersEmpty")
    @GetMapping("users")
    @Timed(value = "usersloading.time", description = "Time taken to return users")
    public List<User> getAllUsers() {
        logger.info("Role info retrieved successfully");

        return userRepository.findAll();
    }
    public List<User> getUsersEmpty() {
        return Arrays.asList(new User());
    }

    /**
     * This method un-resiters the user, by sending out an unregistered event,
     * sending out an email to the user stating the user unregistered successfully
     * and finally that user gets deleted from the user repository.
     *
     * @param username
     * @return
     */

    @DeleteMapping("/de-register")
    public ResponseEntity<MessageResponse> deRegisterUser(@RequestParam String username) {
        //TODO: Need to send out an email that user success un-registered.
        // Send out an event message to Email Listener
        logger.info("User De-Reistered Successfully");
        userRepository.findByUsername(username).ifPresent(user_to_remove -> userRepository.delete(user_to_remove));

        return ResponseEntity.ok(new MessageResponse("User De-Registered Successfully"));
    }

}


