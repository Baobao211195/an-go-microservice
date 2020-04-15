package com.an.user.component;

import com.an.common.bean.User;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@Configuration
public class GoogleVerifyToken {

    private static Logger logger = LoggerFactory.getLogger(GoogleVerifyToken.class);

    @Value("#{'${google.signin.clientids}'.split(',')}")
    private List<String> clientIds;

    private static final NetHttpTransport transport = new NetHttpTransport();
    private static final JacksonFactory jsonFactory = new JacksonFactory();

    public User verify(String token) throws Exception {
        User user = null;
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    .setAudience(clientIds)
                    .build();
            logger.info("Google token: " + token);
            GoogleIdToken idToken = GoogleIdToken.parse(verifier.getJsonFactory(), token);
            boolean tokenIsValid = (idToken != null) && verifier.verify(idToken);

            if (tokenIsValid) {
                logger.info("Google token valid");
                GoogleIdToken.Payload payload = idToken.getPayload();
                user = new User();
                user.setSocialId(payload.getSubject());
                user.setEmail(payload.getEmail());
            } else {
                logger.info("Google token invalid");
            }
        } catch (Exception ex){
            logger.error(ex.getMessage(), ex);
        }
        return user;
    }
}
