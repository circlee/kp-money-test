package com.kp.test.application.controller;

import com.kp.test.application.dto.CreatMoneySpread;
import com.kp.test.application.dto.MoneySpreadDetail;
import com.kp.test.application.dto.MoneySpreadToken;
import com.kp.test.application.dto.RoomUserHeader;
import com.kp.test.application.service.MoneySpreadService;
import com.kp.test.domain.vo.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/spreads")
@RestController
public class MoneySpreadController {

    private static final String TOKEN_QUERY_PARAM_NAME = "token";

    private final MoneySpreadService moneySpreadService;

    @PostMapping
    public ResponseEntity createMoneySpread(@RequestBody @Valid CreatMoneySpread creatMoneySpread
            , RoomUserHeader roomUserHeader) {

        Token token = moneySpreadService.createMoneySpreadInRoom(
                roomUserHeader.getRoomId()
                , roomUserHeader.getUserId()
                , creatMoneySpread);

        URI location = UriComponentsBuilder.fromPath("/spreads")
                .queryParam(TOKEN_QUERY_PARAM_NAME, token.getValue())
                .build()
                .toUri();
        return ResponseEntity.created(location)
                .body(MoneySpreadToken.from(token));
    }

    @PostMapping(value = "/receive", params = TOKEN_QUERY_PARAM_NAME)
    public ResponseEntity receiveMoneySpread(@RequestParam(TOKEN_QUERY_PARAM_NAME) String token, RoomUserHeader roomUserHeader){

         moneySpreadService.receiveMoneySpread(token
                , roomUserHeader.getRoomId()
                , roomUserHeader.getUserId());

        return ResponseEntity.ok().build();
    }

    @GetMapping(params = TOKEN_QUERY_PARAM_NAME)
    public ResponseEntity getMoneSpreadDetail(@RequestParam(TOKEN_QUERY_PARAM_NAME) String token, RoomUserHeader roomUserHeader){

        MoneySpreadDetail detail = moneySpreadService.getMoneySpreadDetail(token
                , roomUserHeader.getRoomId()
                , roomUserHeader.getUserId());

        return ResponseEntity.of(Optional.ofNullable(detail));
    }

}
