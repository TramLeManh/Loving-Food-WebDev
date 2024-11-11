package session.userInformation;
import org.springframework.web.bind.annotation.*;
@RestController
public class UserInformationController {
    private final UserInformationRepo userInformationRepo;
    public UserInformationController(UserInformationRepo userInformationRepo) {
        this.userInformationRepo = userInformationRepo;
    }
    @GetMapping("/getUserInformation")
    public UserInformation getUserInformation(@RequestParam int id) {
        return userInformationRepo.getUserInformation(id);
    }
    @PostMapping("/saveUserInformation")
    public UserInformation saveUserInformation(@RequestBody UserInformation userInformation) {
        return userInformationRepo.save(userInformation);
    }
}