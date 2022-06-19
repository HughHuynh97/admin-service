package com.admin.service;

import com.admin.dao.UserDao;
import com.admin.model.UserInfo;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Log
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var userInfo = userDao.findByEmail(email);
        if (userInfo != null) {
            return new User(userInfo.getEmail(), userInfo.getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + email);
        }
    }

    public void addUser(UserInfo user) {
        userDao.save(user);
    }
}
