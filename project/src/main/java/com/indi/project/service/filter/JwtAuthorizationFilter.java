package com.indi.project.service.filter;
import com.indi.project.entity.User;
import com.indi.project.exception.CustomException;
import com.indi.project.exception.ErrorCode;
import com.indi.project.security.userService.PrincipalDetails;
import com.indi.project.service.json.JsonService;
import com.indi.project.service.jwt.JwtProperties;
import com.indi.project.service.jwt.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
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


@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {


    private final JwtService jwtService;
    private final JsonService jsonService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtService jwtService, JsonService jsonService) {
        super(authenticationManager);
        this.jwtService = jwtService;
        this.jsonService = jsonService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String accessToken = jwtService.getToken(request, JwtProperties.ACCESS_HEADER_PREFIX).orElse(null);

        if (accessToken == null) {              //토큰의 껍질조차 없을 때, 즉 로그인 하지 않았을때
            chain.doFilter(request, response);
            return;
        }

        try {    //accessToken의 유효성 검사
            jwtService.checkAccessTokenValid(accessToken);
            User userByAccessToken = jwtService.getUserByAccessToken(accessToken);
            PrincipalDetails principalDetails = new PrincipalDetails(userByAccessToken);
            Authentication auth = new UsernamePasswordAuthenticationToken(principalDetails, null, null);
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (CustomException e) {
            // In case of token expiration or illegal access token
            if ((e.getErrorCode() == ErrorCode.ACCESS_TOKEN_EXPIRED) || e.getErrorCode() == ErrorCode.ILLEGAL_ACCESS_TOKEN) {
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
            } else if(e.getErrorCode() == ErrorCode.USER_NOT_FOUND){
                jsonService.responseToJson(response, HttpStatus.NOT_FOUND.value(), false, ErrorCode.USER_NOT_FOUND.getCode());
            }
        } catch (Exception e) {
            log.info("처리하지 못한 오류 발생 = {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");
            return;
        }
        chain.doFilter(request, response);
    }
}


//이전 코드
  /*  @Override
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
    }*/

//수정 코드
   /* @Slf4j
    public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
        private final JwtService jwtService;

        public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
            super(authenticationManager);
            this.jwtService = jwtService;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
            String accessToken = jwtService.getToken(request, JwtProperties.ACCESS_HEADER_PREFIX).orElse(null);

            if (accessToken == null) {              //토큰의 껍질조차 없을 때, 즉 로그인 하지 않았을때
                chain.doFilter(request, response);
                return;
            }

            try {
                jwtService.checkAccessTokenValid(accessToken);

                User userByAccessToken = jwtService.getUserByAccessToken(accessToken);
                PrincipalDetails principalDetails = new PrincipalDetails(userByAccessToken);
                Authentication auth = new UsernamePasswordAuthenticationToken(principalDetails, null, null);
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (CustomException e) {
                // In case of token expiration or illegal access token
                if ((e.getErrorCode() == ErrorCode.ACCESS_TOKEN_EXPIRED) || (e.getErrorCode() == ErrorCode.ILLEGAL_ACCESS_TOKEN)) {
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
            } catch (Exception e){
                log.info("처리하지 못한 오류 발생 = {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");
                return;
            }
            chain.doFilter(request, response);
        }
    }

}
*/