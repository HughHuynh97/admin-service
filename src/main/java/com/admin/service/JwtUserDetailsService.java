package com.admin.service;

import com.admin.dao.UserDao;
import com.admin.model.UserInfo;
import lombok.extern.java.Log;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Log
public class JwtUserDetailsService implements UserDetailsService {

    private final UserDao userDao;
    public JwtUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var userInfo = userDao.findByEmail(email);
        if (userInfo != null) {
            var permissions = userDao.getPermissionByUserId(userInfo.getUserId()).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            return new User(userInfo.getEmail(), userInfo.getPassword(), permissions);
        } else {
            throw new UsernameNotFoundException("User not found with username: " + email);
        }
    }

    public void addUser(UserInfo user) {
        userDao.save(user);
    }

}
