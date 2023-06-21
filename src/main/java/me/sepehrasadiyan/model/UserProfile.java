package me.sepehrasadiyan.model;

import lombok.Data;
import lombok.NonNull;
import org.keycloak.representations.AccessToken;
import org.springframework.security.config.web.server.ServerHttpSecurity;

import javax.persistence.Access;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class UserProfile {
    @NonNull
    private String username;

    @NonNull
    private String group;

    @NonNull
    private List<String> roles;

    public UserProfile(String username, List<String> group, List<String> roles) {
        this.username = username;
        this.roles = roles;
        this.group = group.get(0).substring(1);
    }
}