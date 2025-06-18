package com.flow.extensionBlocker.common.baseResponse;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    /**
     * 1000: 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    /**
     * 2000: Validation Error
     */
    VALIDATION_ERROR(false, 2000, "유효성 검증에 실패하였습니다."),
    EXTENSION_NOT_FOUND(false, 2001, "요청 데이터가 유효하지 않습니다."),
    EXTENSION_NAME_LENGTH_EXCEEDED(false, 2002, "확장자은 20자 내로 입력가능합니다."),
    EXTENSION_LIMIT_EXCEEDED(false, 2003, "확장자 차단은 200개까지 활성화 가능합니다."),
    EXTENSION_NAME_DUPLICATED(false, 2004, "해당 확장자가 이미 존재합니다."),

    /**
     * 4000: Internal Server Error
     */
    INTERNAL_SERVER_ERROR(false, 4000, "서버 오류가 발생하였습니다.")
    ;

    private final boolean isSuccess;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
