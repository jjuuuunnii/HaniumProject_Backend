package com.indi.project.service.filter;

import com.indi.project.entity.User;
import com.indi.project.exception.CustomException;
import com.indi.project.exception.ErrorCode;
import com.indi.project.exception.jwt.JwtErrorCode;
import com.indi.project.exception.jwt.JwtException;
import com.indi.project.security.userService.PrincipalDetails;
import com.indi.project.service.jwt.JwtProperties;
import com.indi.project.service.jwt.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * TODO
 * <p>
 * 일단 여기서  Transaction구현 해야할듯하고 Refresh Token도 어떻게 해결해야함
 * 그리고  SecurityConfig에 추가해야돼
 */


@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {


    private final JwtService jwtService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (request.getHeader("Authorization") == null && request.getHeader("ReAuthorization") == null) {
            SecurityContextHolder.getContext().setAuthentication(null);
            chain.doFilter(request, response);
            return;
        }

        //만약 헤더에 둘다 없으면 바로 리턴 때리기

        Optional<String> accessToken = jwtService.getToken(request, JwtProperties.ACCESS_HEADER_PREFIX);
        Optional<String> refreshToken = jwtService.getToken(request, JwtProperties.REFRESH_HEADER_PREFIX);

        if (refreshToken.isPresent()) {
            jwtService.checkRefreshTokenValid(refreshToken.get(), response);
        } else {
            throw new CustomException(ErrorCode.ILLEGAL_REFRESH_TOKEN);
        }

        if (accessToken.isPresent()) {
            jwtService.checkAccessTokenValid(accessToken.get());
        } else {
            throw new CustomException(ErrorCode.ILLEGAL_ACCESS_TOKEN);
        }

        User userByRefreshToken = jwtService.getUserByRefreshToken(refreshToken.get());

        PrincipalDetails principalDetails = new PrincipalDetails(userByRefreshToken);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                principalDetails,null,null);

        SecurityContextHolder.getContext().setAuthentication(auth);

        chain.doFilter(request, response);
    }

    /**
     *
     *
     *
     * TODO
     *
     * 이부분 수정
     */
    @Slf4j
    public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
        private final JwtService jwtService;

        public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
            super(authenticationManager);
            this.jwtService = jwtService;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
            String accessToken = jwtService.getToken(request, JwtProperties.ACCESS_HEADER_PREFIX).orElse(null);

            if (accessToken == null) {
                chain.doFilter(request, response);
                return;
            }

            try {
                jwtService.checkAccessTokenValid(accessToken);

                User userByAccessToken = jwtService.getUserByAccessToken(accessToken);
                PrincipalDetails principalDetails = new PrincipalDetails(userByAccessToken);
                Authentication auth = new UsernamePasswordAuthenticationToken(principalDetails, null, null);
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (JwtException e) {
                // In case of token expiration or illegal access token
                if ((e.getJwtErrorCode() == JwtErrorCode.ACCESS_TOKEN_EXPIRED) || (e.getJwtErrorCode() == JwtErrorCode.ILLEGAL_ACCESS_TOKEN)) {
                    String refreshToken = jwtService.getToken(request, JwtProperties.REFRESH_HEADER_PREFIX).orElse(null);

                    if (refreshToken != null) {
                        jwtService.checkRefreshTokenValid(refreshToken, response);

                        User userByRefreshToken = jwtService.getUserByRefreshToken(refreshToken);
                        PrincipalDetails principalDetails = new PrincipalDetails(userByRefreshToken);
                        Authentication auth = new UsernamePasswordAuthenticationToken(principalDetails, null, null);
                        SecurityContextHolder.getContext().setAuthentication(auth);

                        // regenerate access token
                        String newAccessToken = jwtService.createAccessToken(userByRefreshToken.getLoginId(), userByRefreshToken.getName());
                        response.addHeader(JwtProperties.ACCESS_HEADER_PREFIX, newAccessToken);
                    } else {
                        throw new CustomException(ErrorCode.ILLEGAL_REFRESH_TOKEN);
                    }
                } else {
                    throw e;
                }
            }

            chain.doFilter(request, response);
        }
    }

}
