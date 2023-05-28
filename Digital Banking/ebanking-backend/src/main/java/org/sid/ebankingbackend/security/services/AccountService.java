package org.sid.ebankingbackend.security.services;


import org.sid.ebankingbackend.security.entities.AppRole;
import org.sid.ebankingbackend.security.entities.AppUser;

import java.util.List;

public interface AccountService {
    AppUser addNewUser(AppUser appUser);
    AppRole addNewRole(AppRole appRole);
    void addRoleToUser(String username, String rolename);
    AppUser loadUserByUsername(String username);
    List<AppUser> listUsers();

}
