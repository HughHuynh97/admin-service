package com.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private Long userId;
    private String email;
    private String password;
    private String displayName;
    private String image;
    private Integer status;

    public UserInfo(ResultSet rs) throws SQLException {
        this.userId = rs.getLong("user_id");
        this.email = rs.getString("email");
        this.password = rs.getString("password");
        this.displayName = rs.getString("display_name");
        this.status = rs.getInt("status");
    }
}
