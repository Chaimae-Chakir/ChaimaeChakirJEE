package org.sid.ebankingbackend.security.services;


import org.sid.ebankingbackend.security.entities.AppRole;
import org.sid.ebankingbackend.security.entities.AppUser;
import org.sid.ebankingbackend.security.repositories.AppRoleRepository;
import org.sid.ebankingbackend.security.repositories.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;
    private PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.appRoleRepository = appRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUser addNewUser(AppUser appUser) {
        String pw = appUser.getPassword();
        appUser.setPassword(passwordEncoder.encode(pw));
        return appUserRepository.save(appUser);
    }

    @Override
    public AppRole addNewRole(AppRole appRole) {
        return appRoleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String rolename) {
        AppUser appUser = appUserRepository.findAppUserByUsername(username);
        AppRole appRole = appRoleRepository.findAppRoleByRoleName(rolename);

        if (appUser != null && appRole != null) {
            appUser.getRoles().add(appRole);
        } else {
            //
        }
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findAppUserByUsername(username);
    }

    @Override
    public List<AppUser> listUsers() {
        return appUserRepository.findAll();
    }
}
