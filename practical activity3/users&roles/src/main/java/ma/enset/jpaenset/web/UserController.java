package ma.enset.jpaenset.web;

import ma.enset.jpaenset.entities.User;
import ma.enset.jpaenset.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public class UserController {
    private UserService userService;
    @GetMapping("/users/{usersname}")
    public User user(@PathVariable String username){
       User user=userService.findUserByUserName(username);
       return user;
    }

}
