package com.flow.extensionBlocker.common.baseResponse;

public class BaseResponse<T> {
    private boolean isSuccess;
    private int code;
    private String message;
    private T result;

    //요청 성공 시
    public BaseResponse(T result) {
        this.isSuccess = BaseResponseStatus.SUCCESS.isSuccess();
        this.code = BaseResponseStatus.SUCCESS.getCode();
        this.message = BaseResponseStatus.SUCCESS.getMessage();
        this.result = result;
    }

    //요청 실패 시
    public BaseResponse(BaseResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.code = status.getCode();
        this.message = status.getMessage();
        this.result = null;
    }

    public BaseResponse(BaseResponseStatus status, String message) {
        this.isSuccess = status.isSuccess();
        this.code = status.getCode();
        this.message = message;
        this.result = null;
    }
}
