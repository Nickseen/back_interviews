package hr.recruitment.controller;

import hr.recruitment.dto.UserRegistrationDto;
import hr.recruitment.model.User;
import hr.recruitment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserRegistrationController {
    
    private final UserService userService;
    
    @PostMapping("/{role}")
    public ResponseEntity<User> registerUser(@PathVariable String role, @RequestBody UserRegistrationDto registrationDto) {
        try {
            User user = userService.registerUser(registrationDto, role);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
