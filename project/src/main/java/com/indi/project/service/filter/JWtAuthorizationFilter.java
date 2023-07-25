package com.indi.project.service.filter;

import com.indi.project.entity.User;
import com.indi.project.exception.CustomException;
import com.indi.project.exception.ErrorCode;
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
 *
 * 일단 여기서  Transaction구현 해야할듯하고 Refresh Token도 어떻게 해결해야함
 * 그리고  SecurityConfig에 추가해야돼
 */


@Slf4j
public class JWtAuthorizationFilter extends BasicAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public JWtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  JwtService jwtService
    ) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

            //만약 헤더에 둘다 없으면 바로 리턴 때리기
            if (request.getHeader("Authorization") == null && request.getHeader("RefreshAuthorization") == null) {
                SecurityContextHolder.getContext().setAuthentication(null);

                chain.doFilter(request, response);
                return;
            }

            Optional<String> accessToken = jwtService.getToken(request,JwtProperties.ACCESS_HEADER_PREFIX);
            Optional<String> refreshToken = jwtService.getToken(request,JwtProperties.REFRESH_HEADER_PREFIX);

            if(refreshToken.isPresent()){
                jwtService.checkRefreshTokenValid(refreshToken.get(),response);
            }else{
                throw new CustomException(ErrorCode.ILLEGAL_REFRESH_TOKEN);
            }

            if(accessToken.isPresent()){
                jwtService.checkAccessTokenValid(accessToken.get());
            }else{
                throw new CustomException(ErrorCode.ILLEGAL_ACCESS_TOKEN);
            }

        User userByRefreshToken = jwtService.getUserByRefreshToken(refreshToken.get());
        PrincipalDetails principalsDetails = new PrincipalDetails(userByRefreshToken);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                principalsDetails, null);

        SecurityContextHolder.getContext().setAuthentication(auth);

        chain.doFilter(request,response);
    }


/*    public void returnToClientByExpiredAccessToken(HttpServletResponse response) throws IOException {
        SecurityContextHolder.getContext().setAuthentication(null);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", HttpServletResponse.SC_UNAUTHORIZED);
        jsonObject.put("message","Access token expired. Please get a new token.");
        response.getWriter().write(jsonObject.toString());
    }*/
}
