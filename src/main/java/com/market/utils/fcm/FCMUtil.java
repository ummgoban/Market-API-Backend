package com.market.utils.fcm;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FCMUtil {

    private static final String PRODUCT_CREATED_ALARM_TITLE = "찜 가게 새로운 메뉴 등록";
    private static final String PRODUCT_CREATED_ALARM_BODY = "가게의 새로운 메뉴가 등록되었어요.";
    private static final String PRODUCT_UPDATED_ALARM_TITLE = "찜 가게 메뉴 재고 변경";
    private static final String PRODUCT_UPDATED_ALARM_BODY = "가게의 메뉴 재고가 변경되었어요.";


    private final FCMAsyncUtil fcmAsyncUtil;

    /**
     * 가게를 찜한 유저들에게 메뉴 등록 알림을 보낸다.
     */
    public void sendCreatedAlarms(String marketName, List<String> tokens) {

        try {

            MulticastMessage multicastMessage = MulticastMessage.builder()
                    .setNotification(Notification.builder()
                            .setTitle(PRODUCT_CREATED_ALARM_TITLE)
                            .setBody(marketName + PRODUCT_CREATED_ALARM_BODY)
                            .build())
                    .addAllTokens(tokens)
                    .build();

            fcmAsyncUtil.sendReservationRemindMessages(multicastMessage);
        } catch (FirebaseMessagingException ex) {
            logErrorMessage(ex);
        }
    }

    /**
     * 가게를 찜한 유저들에게 재고 변경 알림을 보낸다.
     */
    public void sendUpdatedAlarms(String marketName, List<String> tokens) {

        try {

            MulticastMessage multicastMessage = MulticastMessage.builder()
                    .setNotification(Notification.builder()
                            .setTitle(PRODUCT_UPDATED_ALARM_TITLE)
                            .setBody(marketName + PRODUCT_UPDATED_ALARM_BODY)
                            .build())
                    .addAllTokens(tokens)
                    .build();

            fcmAsyncUtil.sendReservationRemindMessages(multicastMessage);
        } catch (FirebaseMessagingException ex) {
            logErrorMessage(ex);
        }
    }

    private void logErrorMessage(FirebaseMessagingException ex) {
        MessagingErrorCode errorCode = ex.getMessagingErrorCode();
        log.error("메시지 전송 과정에서 에러 발생. 원인 == {}", errorCode.name());
    }
}