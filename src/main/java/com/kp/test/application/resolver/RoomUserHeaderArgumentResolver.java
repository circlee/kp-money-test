package com.kp.test.application.resolver;

import com.kp.test.application.dto.RoomUserHeader;
import com.kp.test.infrastructure.config.exception.KpCustomException;
import com.kp.test.infrastructure.config.exception.KpExceptionCode;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Component
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
            Long userId = Long.parseLong(request.getHeader("x-user-id"));

            return new RoomUserHeader(roomId, userId);
        } catch (Exception e) {
            throw new KpCustomException(KpExceptionCode.INVALID_HEADER);
        }

    }
}
