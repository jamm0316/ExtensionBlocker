package com.flow.extensionBlocker.common.baseException;

import com.flow.extensionBlocker.common.baseResponse.BaseResponse;
import com.flow.extensionBlocker.common.baseResponse.BaseResponseStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //BaseException 발생
    @ExceptionHandler(BaseException.class)
    public BaseResponse<Object> handleBaseException(BaseException e) {
        return new BaseResponse<>(e.getStatus());
    }

    //DTO Validation 실패 시 발생
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<Object> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return new BaseResponse<>(BaseResponseStatus.VALIDATION_ERROR, message);
    }

    //예상하지 못한 오류 발생
    @ExceptionHandler(Exception.class)
    public BaseResponse<Object> handleExeption(Exception e) {
        e.printStackTrace();
        return new BaseResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR);
    }
}
