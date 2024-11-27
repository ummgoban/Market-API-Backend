package com.market.utils.random;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OrdersIdUtils {

    private static String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
    private static Random random = new Random();

    public String generateOrdersId() {

        int length = random.nextInt(59) + 6; // 6 ~ 64 자리의 랜덤 길이 추출

        // 문자열 생성
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
    }
}
