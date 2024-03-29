package com.hangoutwithus.hangoutwithus.config.oauth;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2FailureHandler  extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

            String redirectUrl = "hangoutwithus://"
                    +"?state=error"
                    +"&error_code="
                    +exception.getMessage();


            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
