package com.assignment.security.oauth2;

import com.assignment.entity.User;
import com.assignment.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {


    private UserService userService;

    @Autowired
    public OAuth2LoginSuccessHandler(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication
    ) throws ServletException, IOException {

        DefaultOidcUser oAuth2User = (DefaultOidcUser) authentication.getPrincipal();
        String email = oAuth2User.getEmail();
        System.out.println("OAuth2 fullname: " + oAuth2User.getFullName());
        System.out.println("OAuth2 email: " + email);

        User user = userService.findByEmail(email);

        if(user != null) {
            System.out.println("User account already exists in database");
        } else {
            System.out.println("New user. About to add a new entry in database...");
        }

//        getRedirectStrategy().sendRedirect(request, response, "/cat");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
