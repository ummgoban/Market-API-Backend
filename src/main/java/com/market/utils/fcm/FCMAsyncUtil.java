package com.market.utils.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FCMAsyncUtil {

    private final FirebaseMessaging firebaseMessaging;

    /**
     * 여러 명의 고객에게 알림을 보낸다.
     */
    @Async("FCMAsyncBean")
    public void sendReservationRemindMessages(MulticastMessage messages) throws FirebaseMessagingException {
        firebaseMessaging.sendEachForMulticast(messages);
    }
}