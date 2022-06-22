package com.admin.dao;

import com.admin.model.UserInfo;
import lombok.extern.java.Log;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

@Repository
@Log
public class UserDao {

    private final JdbcTemplate jdbc;
    private static final PasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();
    public static final String GET_BY_EMAIL = """
            SELECT u.user_id, u.email, u.display_name, u.password, u.status, GROUP_CONCAT(r.role_id, ',') AS role_ids
            FROM user_info u 
                INNER JOIN user_role r ON u.user_id = r.role_id
            WHERE u.email = ?;
            """;
    private static final String GET_PERMISSION_BY_USER_ID = """
            SELECT p.code 
            FROM permission p 
                INNER JOIN role_permission rp ON rp.permission_id = p.permission_id
                INNER JOIN role r ON r.role_id = rp.role_id
                INNER JOIN user_role ur ON ur.role_id = r.role_id
                INNER JOIN user_info u ON u.user_id = ur.user_id
            WHERE u.user_id = ?
            """;
    public static final String SAVE_USER = """
            INSERT INTO user_info(email, display_name, password, image, status) VALUES (?,?,?,?,?)
            ON DUPLICATE KEY UPDATE display_name = VALUES(display_name), password = VALUES(password), image = VALUES(image), status = VALUES(status)
            """;
    private static final String UPDATE_ROLE = """
            INSERT IGNORE INTO user_role(user_id, role_id) VALUES (?,?)
            """;

    public UserDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public UserInfo findByEmail(String email) {
        return jdbc.queryForObject(GET_BY_EMAIL, (rs, i) -> new UserInfo(rs), email);
    }

    public void save(UserInfo userInfo) {
        try {
            var keyHolder = new GeneratedKeyHolder();
            jdbc.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SAVE_USER, new String[]{"userId"});
                ps.setString(1, userInfo.getEmail());
                ps.setString(2, userInfo.getDisplayName());
                ps.setString(3, bcryptEncoder.encode(userInfo.getPassword()));
                ps.setString(4, userInfo.getImage());
                ps.setInt(5, 1);
                return ps;
            }, keyHolder);
            long userId;
            if (keyHolder.getKeyList().size() > 1) {
                userId = Long.parseLong(String.valueOf(keyHolder.getKeyList().get(0).get("GENERATED_KEY")));
            } else {
                userId = Objects.requireNonNull(keyHolder.getKey()).longValue();
            }
            jdbc.update(UPDATE_ROLE, userId, 1);
        } catch (Exception exception) {
            log.log(Level.WARNING, "UserDao >> save >> exception >> ", exception);
        }
    }

    public List<String> getPermissionByUserId(Long userId) {
        try {
            return jdbc.queryForList(GET_PERMISSION_BY_USER_ID, String.class, userId);
        } catch (Exception exception) {
            log.log(Level.WARNING, "UserDao >> getPermissionByUserId >> exception >> ", exception);
        }
        return Collections.emptyList();
    }
}
