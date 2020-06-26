package com.kp.test.infrastructure.config.resolver;

import com.kp.test.application.dto.RoomUserHeader;
import com.kp.test.infrastructure.config.exception.KpCustomException;
import com.kp.test.infrastructure.config.exception.KpExceptionCode;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

public class RoomUserHeaderArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(RoomUserHeader.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        try {
            Long roomId = Long.parseLong(request.getHeader("x-room-id"));

            if(roomId == null){
                throw new KpCustomException(KpExceptionCode.INVALID_HEADER);
            }

            Long userId = Long.parseLong(request.getHeader("x-user-id"));

            if(userId == null){
                throw new KpCustomException(KpExceptionCode.INVALID_HEADER);
            }


            return new RoomUserHeader(roomId, userId);
        } catch (Exception e) {
            throw new KpCustomException(KpExceptionCode.INVALID_HEADER);
        }

    }
}
