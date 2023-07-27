package com.indi.project.service.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.indi.project.dto.Token.TokenDto;
import com.indi.project.entity.User;
import com.indi.project.exception.CustomException;
import com.indi.project.exception.ErrorCode;
import com.indi.project.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@Getter
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JwtService implements JwtProperties {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getUserByRefreshToken(String refreshToken) {
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return user;
    }



    @Transactional
    public void setRefreshToken(String loginId, String refreshJwt) {
        userRepository.findByLoginId(loginId)
                .ifPresent(user -> user.setRefreshToken(refreshJwt));
    }

    @Transactional
    public void removeRefreshToken(String token) {
        userRepository.findByRefreshToken(token)
                .ifPresent(u->u.deleteRefreshToken());
    }

    @Transactional
    public void logout(HttpServletRequest request) {
        try {
            checkHeaderValid(request);
            String refreshJwtToken = request
                    .getHeader(JwtProperties.REFRESH_HEADER_PREFIX)
                    .replace(JwtProperties.TOKEN_PREFIX, "");
            removeRefreshToken(refreshJwtToken);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
    }

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

    public void checkHeaderValid(HttpServletRequest request) {
        String accessJwt = request.getHeader(JwtProperties.ACCESS_HEADER_PREFIX);
        String refreshJwt = request.getHeader(JwtProperties.REFRESH_HEADER_PREFIX);

        if (accessJwt == null && refreshJwt == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
    }

    @Transactional
    public void checkAccessTokenValid(String token) {
        try{
            JWT.require(Algorithm.HMAC512(JwtProperties.SECRET_KEY))
                    .build()
                    .verify(token);
        }catch (JWTVerificationException e){
            throw new CustomException(ErrorCode.ILLEGAL_ACCESS_TOKEN);
        }
    }


    @Transactional
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

    @Transactional
    void updateRefreshToken(String refreshToken, HttpServletResponse response) {
        User userByRefreshToken = getUserByRefreshToken(refreshToken);
        String newRefreshToken = createRefreshToken();

        response.setHeader(JwtProperties.REFRESH_HEADER_PREFIX, JwtProperties.TOKEN_PREFIX
                + newRefreshToken);
        setRefreshToken(userByRefreshToken.getLoginId(),newRefreshToken);
    }

    @Transactional
    public void setNewAccessToken(TokenDto tokenDto, HttpServletResponse response) {
        String accessToken = createAccessToken(tokenDto.getLoginId(), tokenDto.getUserName());
        response.setHeader(JwtProperties.ACCESS_HEADER_PREFIX, JwtProperties.TOKEN_PREFIX
                + accessToken);
    }
    @Transactional
    public Optional<String> getToken(HttpServletRequest request, String prefix) {
        String headerValue = request.getHeader(prefix);
        if (headerValue != null) {
            String token = headerValue.replace(JwtProperties.TOKEN_PREFIX, "");
            return Optional.of(token);
        } else {
            return Optional.empty();
        }
    }

    public User getUserByAccessToken(String token) {
        // Decode token and extract user id
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET_KEY))
                .build()
                .verify(token);
        String loginId = decodedJWT.getClaim(JwtProperties.LOGIN_ID).asString();
        log.info("loginID = {}", loginId);

        // Get user from database
        Optional<User> optionalUser = userRepository.findByLoginId(loginId);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
    }
}
