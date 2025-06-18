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
    VALIDATION_ERROR(false, 2000, "요청 데이터가 유효하지 않습니다."),

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
