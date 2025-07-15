package com.baseproject.config.security;

import com.baseproject.exception.exceptions.ValidationException;
import com.baseproject.model.User;
import com.baseproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BaseOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final UserService userService;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException
  {
    OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

    String email = oAuth2User.getAttribute("email");
    String name = oAuth2User.getAttribute("name");

    User user;
    try {
      user = userService.findByEmail(email);
      userService.updateLastActiveTime(user);
    } catch (ValidationException e) {
      user = userService.create(email, name, "google");
    }

    List<GrantedAuthority> authorities = user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
        .collect(Collectors.toList());

    return new DefaultOAuth2User(
        authorities,
        oAuth2User.getAttributes(),
        "email"
    );
  }
}
