package com.mc.security.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mc.security.token.JwtToken;
import com.mc.security.token.JwtTokenFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Wenyu
 * @since 2/19/17
 */
@Component
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final String TOKEN = "token";
    private static final String REFRESH_TOKEN = "refreshToken";

    private final ObjectMapper mapper = new ObjectMapper();
    private final JwtTokenFactory jwtTokenFactory;

    @Autowired
    public LoginAuthenticationSuccessHandler(JwtTokenFactory jwtTokenFactory) {
        this.jwtTokenFactory = jwtTokenFactory;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException
    {
        // Note: the authentication object here suppose to be the UsernamePasswordAuthenticationToken
        // which is created in the LoginAuthenticationProvider
        String username = (String) authentication.getPrincipal();
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) authentication.getAuthorities();

        JwtToken accessToken = jwtTokenFactory.createAccessToken(username, authorities);
        JwtToken refreshToken = jwtTokenFactory.createRefreshToken(username, authorities);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put(TOKEN, accessToken.getToken());
        tokenMap.put(REFRESH_TOKEN, refreshToken.getToken());

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // json string
        mapper.writeValue(response.getWriter(), tokenMap);

        clearAuthenticationAttributes(request);
    }

    /**
     * TODO: is this necessary?
     * Clear other authentication related attributes.
     *
     * @param request
     */
    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}