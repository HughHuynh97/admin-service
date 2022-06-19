package com.admin.dao;

import com.admin.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbc;
    private static final PasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();
    public static final String GET_BY_EMAIL = """
            SELECT u.user_id, u.email, u.display_name, u.password, u.status FROM user_info u WHERE u.email = ?;
            """;
    public static final String SAVE_USER = """
            INSERT INTO user_info(email, display_name, password, image, status) VALUES (?,?,?,?,?)
            ON DUPLICATE KEY UPDATE display_name = VALUES(display_name), password = VALUES(password), image = VALUES(image), status = VALUES(status)
            """;

    public UserInfo findByEmail(String email) {
        return jdbc.queryForObject(GET_BY_EMAIL, (rs, i) -> new UserInfo(rs), email);
    }

    public void save(UserInfo userInfo) {
        jdbc.update(SAVE_USER, userInfo.getEmail(), userInfo.getDisplayName(), bcryptEncoder.encode(userInfo.getPassword()), userInfo.getImage(), userInfo.getStatus());
    }
}
