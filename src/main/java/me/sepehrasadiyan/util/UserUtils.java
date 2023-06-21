package me.sepehrasadiyan.util;


import me.sepehrasadiyan.exception.IncompleteProfileException;
import me.sepehrasadiyan.model.UserProfile;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;

public class UserUtils {

  private static AccessToken getAccessToken() {
    KeycloakAuthenticationToken authenticationToken = (KeycloakAuthenticationToken) SecurityContextHolder.getContext()
            .getAuthentication();

    KeycloakPrincipal<KeycloakSecurityContext> principal = (KeycloakPrincipal<KeycloakSecurityContext>) authenticationToken.getPrincipal();
    return principal.getKeycloakSecurityContext().getToken();
  }

  public static UserProfile getProfile() throws IncompleteProfileException {
    AccessToken token = getAccessToken();
    Map<String, Object> claims = token.getOtherClaims();

    try {
      return new UserProfile(
              token.getPreferredUsername(),
              (List<String>) claims.get("group"),
              (List<String>) claims.get("role")
      );
    } catch (NullPointerException exception) {
      throw new IncompleteProfileException();
    }

  }
}
