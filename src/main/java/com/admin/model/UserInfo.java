package com.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<Long> roleIds;

    public UserInfo(ResultSet rs) throws SQLException {
        this.userId = rs.getLong("user_id");
        this.email = rs.getString("email");
        this.password = rs.getString("password");
        this.displayName = rs.getString("display_name");
        this.status = rs.getInt("status");
        this.roleIds = Arrays.stream(rs.getString("role_ids").split(",")).map(Long::parseLong).collect(Collectors.toList());
    }
}
