package org.example.blogsphere.config;

import org.example.blogsphere.entity.Role;
import org.example.blogsphere.service.UserService;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import java.io.IOException;
import java.util.Set;


@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    public CustomAuthenticationSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String username = authentication.getName();
        userService.recordLogin(username);

        Set<String> authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (authorities.contains(Role.ROLE_ADMIN.name())) {
            response.sendRedirect("/admin");
        } else if (authorities.contains(Role.ROLE_BLOGGER.name())) {
            response.sendRedirect("/dashboard");
        } else if (authorities.contains(Role.ROLE_READER.name())) {
            response.sendRedirect("/blogs");
        } else {
            // Default redirect if no specific role is found
            response.sendRedirect("/");
        }
    }
}