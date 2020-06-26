package com.kp.test.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kp.test.KpMoneySpreadApplication;
import com.kp.test.application.dto.CreatMoneySpread;
import com.kp.test.application.dto.MoneySpreadToken;
import com.kp.test.application.resolver.RoomUserHeaderArgumentResolver;
import com.kp.test.infrastructure.config.advice.ExceptionAdivce;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@ExtendWith(value = {SpringExtension.class})
@SpringBootTest(classes = KpMoneySpreadApplication.class)
public class MoneySpreadControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MoneySpreadController moneySpreadController;

    private MockMvc mockMvc;

    @BeforeEach
    private void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(moneySpreadController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .setCustomArgumentResolvers(new RoomUserHeaderArgumentResolver())
                .setControllerAdvice(new ExceptionAdivce())
                .build();
    }

    @Order(1)
    @DisplayName("뿌리기 생성 api controller 호출 테스트")
    @Test
    public void create() throws Exception {

        CreatMoneySpread createMoneySpread = new CreatMoneySpread(
                new BigDecimal("20000")
                , 5
        );

        String body = objectMapper.writeValueAsString(createMoneySpread);

        Long userId = 101L;
        Long roomId = 10001L;

        createCall(body, userId, roomId)
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"))
                .andReturn();
    }

    private ResultActions createCall(String body, Long userId, Long roomId) throws Exception {

        HttpHeaders headers = new HttpHeaders();

        if(userId != null) {
            headers.add("x-user-id", String.valueOf(userId));
        }

        if(roomId != null) {
            headers.add("x-room-id", String.valueOf(roomId));
        }


        return mockMvc.perform(
                    post("/spreads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .headers(headers)
                );
    }

    @Order(2)
    @DisplayName("뿌리기 생성 api controller 호출 예외 테스트 (헤더 누락)")
    @Test
    public void createError1() throws Exception {

        CreatMoneySpread createMoneySpread = new CreatMoneySpread(
                new BigDecimal("20000")
                , 5
        );

        String body = objectMapper.writeValueAsString(createMoneySpread);

        Long userId = 101L;
        Long roomId = null;

        createCall(body, userId, roomId)
                .andExpect(status().isBadRequest());

    }

    @Order(3)
    @DisplayName("뿌리기 생성 api controller 호출 예외 테스트 (body 항목 누락)")
    @Test
    public void createError2() throws Exception {

        CreatMoneySpread createMoneySpread = new CreatMoneySpread(
                null
                , 5
        );

        String body = objectMapper.writeValueAsString(createMoneySpread);

        Long userId = 101L;
        Long roomId = 10001L;

        createCall(body, userId, roomId)
                .andExpect(status().isBadRequest());

    }

    @Order(4)
    @DisplayName("뿌리기 생성 후 받기 테스트")
    @Test
    public void receive() throws Exception {

        CreatMoneySpread createMoneySpread = new CreatMoneySpread(
                new BigDecimal("20000")
                , 5
        );
        String body = objectMapper.writeValueAsString(createMoneySpread);
        Long createUserId = 101L;
        Long receiveUserId = 201L;
        Long roomId = 10001L;

        MvcResult createResult = createCall(body, createUserId, roomId).andReturn();
        byte[] responseBody = createResult.getResponse().getContentAsByteArray();
        MoneySpreadToken tokenBody = objectMapper.readValue(responseBody, MoneySpreadToken.class);

        String token = tokenBody.getToken();

        receiveCall(token, receiveUserId, roomId)
                .andExpect(status().isOk());

    }

    private ResultActions receiveCall(String token, Long userId, Long roomId) throws Exception {
        return mockMvc.perform(
                post("/spreads/receive")
                        .queryParam("token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-user-id", userId)
                        .header("x-room-id", roomId)
        );
    }

    @Order(5)
    @DisplayName("뿌리기 보기 테스트")
    @Test
    public void show() throws Exception {

        CreatMoneySpread createMoneySpread = new CreatMoneySpread(
                new BigDecimal("20000")
                , 5
        );
        String body = objectMapper.writeValueAsString(createMoneySpread);
        Long createUserId = 101L;
        Long roomId = 10001L;

        MvcResult createResult = createCall(body, createUserId, roomId).andReturn();
        byte[] responseBody = createResult.getResponse().getContentAsByteArray();
        MoneySpreadToken tokenBody = objectMapper.readValue(responseBody, MoneySpreadToken.class);
        String location = createResult.getResponse().getHeader("location");

        String token = tokenBody.getToken();

        receiveCall(token, 201L, roomId)
                .andExpect(status().isOk());

        receiveCall(token, 202L, roomId)
                .andExpect(status().isOk());

        receiveCall(token, 203L, roomId)
                .andExpect(status().isOk());

        mockMvc.perform(
                get(location)
                        .header("x-user-id", createUserId)
                        .header("x-room-id", roomId)
        )
                .andExpect(status().isOk());

    }

}
