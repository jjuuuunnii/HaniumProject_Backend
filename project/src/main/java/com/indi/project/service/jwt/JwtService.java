package com.indi.project.service.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.indi.project.dto.Token.TokenDto;
import com.indi.project.entity.User;
import com.indi.project.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@Getter
@RequiredArgsConstructor
public class JwtService implements JwtProperties{

    private final UserRepository userRepository;

    public String createAccessToken(String loginId, String userName){
        return JWT.create()
                .withSubject(JwtProperties.ACCESS_TOKEN)
                .withExpiresAt(new Date(System.currentTimeMillis() + (JwtProperties.EXPIRATION_TIME)))
                .withClaim(JwtProperties.LOGIN_ID, loginId)
                .withClaim(JwtProperties.USERNAME, userName)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET_KEY));
    }

    public String createRefreshToken(){
        return JWT.create()
                .withSubject(JwtProperties.REFRESH_TOKEN)
                .withExpiresAt(new Date(System.currentTimeMillis() + (JwtProperties.REFRESH_EXPIRATION_TIME)))
                .sign(Algorithm.HMAC512(JwtProperties.SECRET_KEY));
    }

    public void setRefreshToken(String loginId, String refreshJwt) {
        userRepository.findByLoginId(loginId).ifPresent(user -> user.setRefreshToken(refreshJwt));
    }


    public void checkAccessTokenValid(String token) {
        JWT.require(Algorithm.HMAC512(JwtProperties.SECRET_KEY))
                .build()
                .verify(token);
    }

    public User getUserByRefreshToken(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with refreshToken: " + refreshToken));
    }


    public void checkRefreshTokenValid(String refreshToken, HttpServletResponse response) {
        try {
            Date expiresAt = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET_KEY))
                    .build()
                    .verify(refreshToken)
                    .getExpiresAt();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(System.currentTimeMillis()));
            calendar.add(Calendar.DATE, 3);

            Date after3dayFromToday = calendar.getTime();

            if(expiresAt.before(after3dayFromToday)){
                updateRefreshToken(refreshToken, response);
            }

        } catch (TokenExpiredException e) {
            updateRefreshToken(refreshToken, response);
        }
    }

    private void updateRefreshToken(String refreshToken, HttpServletResponse response) {
        User userByRefreshToken = getUserByRefreshToken(refreshToken);
        String newRefreshToken = createRefreshToken();

        response.setHeader(JwtProperties.REFRESH_HEADER_PREFIX, JwtProperties.TOKEN_PREFIX
                + newRefreshToken);
        setRefreshToken(userByRefreshToken.getLoginId(),newRefreshToken);
    }

    public void setAccessToken(TokenDto tokenDto, HttpServletResponse response) {
        String accessToken = createAccessToken(tokenDto.getLoginId(), tokenDto.getUserName());
        response.setHeader(JwtProperties.ACCESS_HEADER_PREFIX, JwtProperties.TOKEN_PREFIX
                + accessToken);
    }

    public Optional<String> getToken(HttpServletRequest request, String prefix) {
        String headerValue = request.getHeader(prefix);
        if (headerValue != null) {
            String token = headerValue.replace(JwtProperties.TOKEN_PREFIX, "");
            return Optional.of(token);
        } else {
            return Optional.empty();
        }
    }



}
