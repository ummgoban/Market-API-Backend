package com.market.orders.annotation;

import com.market.orders.entity.OrdersStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class OrdersStatusValidator implements ConstraintValidator<ValidOrdersStatus, String> {

    @Override
    public boolean isValid(String input, ConstraintValidatorContext context) {

        for (OrdersStatus status : OrdersStatus.values()) {
            if (status.name().equals(input)) return true;
        }

        context.disableDefaultConstraintViolation(); // 기본 메시지 비활성화
        context.buildConstraintViolationWithTemplate("주문 상태 값이 잘못되었습니다. API 문서를 확인해주세요.")
                .addPropertyNode(input)
                .addConstraintViolation();

        return false;
    }
}
