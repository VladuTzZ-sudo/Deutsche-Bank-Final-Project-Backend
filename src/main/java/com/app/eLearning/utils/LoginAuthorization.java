package com.app.eLearning.utils;

import com.app.eLearning.exceptions.WrongTokenException;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACVerifier;
import org.springframework.data.util.Pair;

public class LoginAuthorization {

    public static Pair<Integer, String> validateAuthorization(String token) throws WrongTokenException {

        Verifier verifier = HMACVerifier.newVerifier("my secret key to verify if token is legit ;)");

        int userId;
        String userRole;

        try {
            JWT jwt = JWT.getDecoder().decode(token, verifier);
            userId = Integer.parseInt(jwt.subject.split(":")[0]);
            userRole = (String) jwt.audience;
        } catch (Exception e) {
            throw new WrongTokenException();
        }

       return Pair.of(userId, userRole);


    }
}
