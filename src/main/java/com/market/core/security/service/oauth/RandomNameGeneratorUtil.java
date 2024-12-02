package com.market.core.security.service.oauth;

import java.util.Random;

public class RandomNameGeneratorUtil {

    private static final String[] ADJECTIVES = {
            "신나는", "바쁜", "조용한", "행복한", "슬픈",
            "용감한", "귀여운", "똑똑한", "멋진", "화려한",
            "정직한", "친절한", "영리한", "활발한", "강한"
    };

    private static final String[] NOUNS = {
            "코끼리", "바나나", "호랑이", "토끼", "사자",
            "사과", "달팽이", "나무", "구름", "고양이",
            "햇살", "물고기", "도서관", "바람", "강아지"
    };

    /**
     * 랜덤 이름을 생성합니다.
     */
    public static String generateRandomName() {
        Random random = new Random();
        String adjective = ADJECTIVES[random.nextInt(ADJECTIVES.length)];
        String noun = NOUNS[random.nextInt(NOUNS.length)];
        return adjective + " " + noun;
    }
}
