package com.tpe.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMessage<E> {

    private E object;
    private String message;
    private HttpStatus httpStatus;

    public <T> ResponseMessage(boolean b, String message, T data) {
    }

    public static <T> ResponseMessage<T> success(String message, T data) {
        return new ResponseMessage<>(true, message, data);
    }

    public static <T> ResponseMessage<T> error(String message, T data) {
        return new ResponseMessage<>(false, message, data);
    }

}