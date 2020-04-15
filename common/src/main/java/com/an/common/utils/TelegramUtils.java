package com.an.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class TelegramUtils {

    private static Logger logger = LoggerFactory.getLogger(TelegramUtils.class);

    public static String sendMessage(String apiToken, String chatId, String content) throws Exception {
        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
        urlString = String.format(urlString, apiToken, chatId, content);

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        StringBuilder sb = new StringBuilder();
        InputStream is = new BufferedInputStream(conn.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String inputLine = "";
        while ((inputLine = br.readLine()) != null) {
            sb.append(inputLine);
        }
        String response = sb.toString();
        logger.info("send message response: " + response);
        return response;
    }

//    https://api.telegram.org/bot960577024:AAEfi2n-nkqn1T-USC__rdadlOailcP6Udk/getUpdates
}
