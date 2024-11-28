package com.market.core.code.error;


import com.market.core.response.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 결제 에러 코드를 관리하는 enum 클래스입니다.
 */
@Getter
public enum PaymentErrorCode implements BaseErrorCode {

    ALREADY_PROCESSED_PAYMENT(400, "이미 처리된 결제 입니다.", HttpStatus.BAD_REQUEST),
    EXCEED_MAX_CARD_INSTALLMENT_PLAN(400, "설정 가능한 최대 할부 개월 수를 초과했습니다.", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST(400, "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    NOT_ALLOWED_POINT_USE(400, "포인트 사용이 불가한 카드로 카드 포인트 결제에 실패했습니다.", HttpStatus.BAD_REQUEST),
    INVALID_REJECT_CARD(400, "카드 사용이 거절되었습니다. 카드사 문의가 필요합니다.", HttpStatus.BAD_REQUEST),
    BELOW_MINIMUM_AMOUNT(400, "신용카드는 결제금액이 100원 이상, 계좌는 200원이상부터 결제가 가능합니다.", HttpStatus.BAD_REQUEST),
    INVALID_CARD_EXPIRATION(400, "카드 정보를 다시 확인해주세요. (유효기간)", HttpStatus.BAD_REQUEST),
    INVALID_STOPPED_CARD(400, "정지된 카드 입니다.", HttpStatus.BAD_REQUEST),
    EXCEED_MAX_DAILY_PAYMENT_COUNT(400, "하루 결제 가능 횟수를 초과했습니다.", HttpStatus.BAD_REQUEST),
    NOT_SUPPORTED_INSTALLMENT_PLAN_CARD_OR_MERCHANT(400, "할부가 지원되지 않는 카드 또는 가맹점 입니다.", HttpStatus.BAD_REQUEST),
    INVALID_CARD_INSTALLMENT_PLAN(400, "할부 개월 정보가 잘못되었습니다.", HttpStatus.BAD_REQUEST),
    NOT_SUPPORTED_MONTHLY_INSTALLMENT_PLAN(400, "할부가 지원되지 않는 카드입니다.", HttpStatus.BAD_REQUEST),
    EXCEED_MAX_PAYMENT_AMOUNT(400, "하루 결제 가능 금액을 초과했습니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND_TERMINAL_ID(400, "단말기번호(Terminal Id)가 없습니다. 토스페이먼츠로 문의 바랍니다.", HttpStatus.BAD_REQUEST),
    INVALID_CARD_LOST_OR_STOLEN(400, "분실 혹은 도난 카드입니다.", HttpStatus.BAD_REQUEST),
    RESTRICTED_TRANSFER_ACCOUNT(400, "계좌는 등록 후 12시간 뒤부터 결제할 수 있습니다. 관련 정책은 해당 은행으로 문의해주세요.", HttpStatus.BAD_REQUEST),
    INVALID_CARD_NUMBER(400, "카드번호를 다시 확인해주세요.", HttpStatus.BAD_REQUEST),
    INVALID_UNREGISTERED_SUBMALL(400, "등록되지 않은 서브몰입니다. 서브몰이 없는 가맹점이라면 안심클릭이나 ISP 결제가 필요합니다.", HttpStatus.BAD_REQUEST),
    NOT_REGISTERED_BUSINESS(400, "등록되지 않은 사업자 번호입니다.", HttpStatus.BAD_REQUEST),
    EXCEED_MAX_ONE_DAY_WITHDRAW_AMOUNT(400, "1일 출금 한도를 초과했습니다.", HttpStatus.BAD_REQUEST),
    EXCEED_MAX_ONE_TIME_WITHDRAW_AMOUNT(400, "1회 출금 한도를 초과했습니다.", HttpStatus.BAD_REQUEST),
    EXCEED_MAX_AMOUNT(400, "거래금액 한도를 초과했습니다.", HttpStatus.BAD_REQUEST),
    INVALID_ACCOUNT_INFO_RE_REGISTER(400, "유효하지 않은 계좌입니다. 계좌 재등록 후 시도해주세요.", HttpStatus.BAD_REQUEST),
    NOT_AVAILABLE_PAYMENT(400, "결제가 불가능한 시간대입니다", HttpStatus.BAD_REQUEST),
    UNAPPROVED_ORDER_ID(400, "아직 승인되지 않은 주문번호입니다.", HttpStatus.BAD_REQUEST),
    EXCEED_MAX_MONTHLY_PAYMENT_AMOUNT(400, "EXCEED_MAX_MONTHLY_PAYMENT_AMOUNT", HttpStatus.BAD_REQUEST),
    REJECT_ACCOUNT_PAYMENT(400, "잔액부족으로 결제에 실패했습니다.", HttpStatus.BAD_REQUEST),
    REJECT_CARD_PAYMENT(400, "한도초과 혹은 잔액부족으로 결제에 실패했습니다.", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(400, "결제 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),


    UNAUTHORIZED_KEY(401, "인증되지 않은 시크릿 키 혹은 클라이언트 키 입니다.", HttpStatus.UNAUTHORIZED),


    REJECT_CARD_COMPANY(403, "결제 승인이 거절됐습니다.", HttpStatus.FORBIDDEN),
    FORBIDDEN_REQUEST(403, "허용되지 않은 요청입니다.", HttpStatus.FORBIDDEN),
    REJECT_TOSSPAY_INVALID_ACCOUNT(403, "선택하신 출금 계좌가 출금이체 등록이 되어 있지 않아요. 계좌를 다시 등록해 주세요.", HttpStatus.FORBIDDEN),
    EXCEED_MAX_AUTH_COUNT(403, "최대 인증 횟수를 초과했습니다. 카드사로 문의해주세요.", HttpStatus.FORBIDDEN),
    EXCEED_MAX_ONE_DAY_AMOUNT(403, "일일 한도를 초과했습니다.", HttpStatus.FORBIDDEN),
    NOT_AVAILABLE_BANK(403, "은행 서비스 시간이 아닙니다.", HttpStatus.FORBIDDEN),
    FDS_ERROR(403, "위험거래가 감지되어 결제가 제한됩니다. 발송된 문자에 포함된 링크를 통해 본인인증 후 결제가 가능합니다. (고객센터: 1644-8051)", HttpStatus.FORBIDDEN),


    NOT_FOUND_PAYMENT(404, "존재하지 않는 결제 정보 입니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_PAYMENT_SESSION(404, "결제 시간이 만료되어 결제 진행 데이터가 존재하지 않습니다.", HttpStatus.NOT_FOUND),


    FAILED_PAYMENT_INTERNAL_SYSTEM_PROCESSING(500, "결제가 완료되지 않았어요. 다시 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR),
    FAILED_INTERNAL_SYSTEM_PROCESSING(500, "내부 시스템 처리 작업이 실패했습니다. 잠시 후 다시 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR),
    PROVIDER_ERROR(500, "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_API_KEY(500, "잘못된 시크릿키 연동 정보 입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_AUTHORIZE_AUTH(500, "유효하지 않은 인증 방식입니다.", HttpStatus.BAD_REQUEST),
    CARD_PROCESSING_ERROR(500, "카드사에서 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INCORRECT_BASIC_AUTH_FORMAT(500, "잘못된 요청입니다. ':' 를 포함해 인코딩해주세요.", HttpStatus.INTERNAL_SERVER_ERROR),
    UNKNOWN_PAYMENT_ERROR(500, "결제에 실패했어요. 같은 문제가 반복된다면 은행이나 카드사로 문의해주세요.", HttpStatus.INTERNAL_SERVER_ERROR),

    Payment_Method_NOT_FOUND(500,"일치하는 결제 방법이 없습니다.",HttpStatus.INTERNAL_SERVER_ERROR);

    private final int errorCode;
    private final String errorMessage;
    private final HttpStatus status;

    PaymentErrorCode(int errorCode, String errorMessage, HttpStatus status) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return new ErrorResponse(this.errorCode, this.errorMessage);
    }
}
