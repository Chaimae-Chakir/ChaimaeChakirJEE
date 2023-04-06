package ma.enset.jpaenset.service;

import ma.enset.jpaenset.entities.*;

public interface UserService {
    User addNewUser(User user);
    Role addNewRole(Role role);
    User findUserByUserName(String userName);
    Role findRoleByRoleName(String roleName);
    void addRoleToUser(String username,String roleName);
    User authenticate(String userName,String password);
}
