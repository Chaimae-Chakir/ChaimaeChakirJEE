package ma.enset.jpaenset.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import ma.enset.jpaenset.entities.*;
import ma.enset.jpaenset.repositories.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Override
    public User addNewUser(User user) {
        user.setUserId(UUID.randomUUID().toString());
        return userRepository.save(user);
    }

    @Override
    public Role addNewRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public User findUserByUserName(String userName) {
        return userRepository.findByUsername(userName);
    }

    @Override
    public Role findRoleByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        User user = this.userRepository.findByUsername(username);
        Role role = this.roleRepository.findByRoleName(roleName);
        if (user.getRoles()!=null){
            user.getRoles().add(role);
            role.getUsers().add(user);
        }
        //userRepository.save(user);
    }

    @Override
    public User authenticate(String userName, String password) {
        User user=userRepository.findByUsername(userName);
        if(user==null) throw new RuntimeException("Bad credentials");
        if(user.getPassword().equals(password)) return user;
        throw new RuntimeException("Bad credencials");
    }
}
