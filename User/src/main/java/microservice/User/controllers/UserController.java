package microservice.User.controllers;

import lombok.extern.slf4j.Slf4j;
import microservice.User.dto.UserRequest;
import microservice.User.dto.UserResponse;
import microservice.User.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.logging.LoggersEndpoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;
//    private static Logger log = LoggerFactory.getLogger(UserController.class);
//    private final LoggersEndpoint loggersEndpoint;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.ok(userService.fetchAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String id){
        log.info("Request received for user: {}", id);

        log.trace("This is TRACE level - Very detailed logs");
        log.debug("This is DEBUG level - Used for development debugging");
        log.info("This is INFO level - General system information");
        log.warn("This is WARN level - Something might be wrong");
        log.error("This is ERROR level - Something failed");
        return userService.fetchUser(id)
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest){
        userService.addUser(userRequest);
        return ResponseEntity.ok("User Added!!");
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody UserRequest updatedUserRequest){
        boolean updatedOrNot = userService.updateUser(id, updatedUserRequest);
        if (updatedOrNot){
            return ResponseEntity.ok("User Updated");
        }
        return ResponseEntity.notFound().build();
    }
}
