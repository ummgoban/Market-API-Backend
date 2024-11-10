package com.market.utils.fcm;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FCMUtil {

    private static final String REGISTER_TITLE = "찜 가게 메뉴 재고 변경";
    private static final String REGISTER_BODY = "찜한 가게의 메뉴 재고가 업데이트 되었어요.";
    private final FCMAsyncUtil fcmAsyncUtil;

    /**
     * 가게를 찜한 유저들에게 알림을 보낸다.
     */
    public void sendReservationRemindMessages(List<String> tokens) throws FirebaseMessagingException {

        MulticastMessage multicastMessage = MulticastMessage.builder()
                .setNotification(Notification.builder()
                        .setTitle(REGISTER_TITLE)
                        .setBody(REGISTER_BODY)
                        .build())
                .addAllTokens(tokens)
                .build();

        fcmAsyncUtil.sendReservationRemindMessages(multicastMessage);
    }

}