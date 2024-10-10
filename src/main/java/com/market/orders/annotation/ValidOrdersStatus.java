package com.market.orders.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OrdersStatusValidator.class)
public @interface ValidOrdersStatus {

    String message() default "Invalid ordersStatus format, check API DOCS";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
