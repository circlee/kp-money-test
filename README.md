# 사전과제 #

뿌리기 기능 구현

---

### 개발환경 ###

- Java 1.8.x
- Lombok plugin
- SpringBoot
- H2

---

### 문제 해결전략

```
MoneySpreadToken 
- 난수 Token 값과 RoomId 의 맵핑 엔티티입니다.

MoneySpread
- 뿌리기 데이터 엔티티입니다.

MoneySpreadDistribution
- 뿌리기의 분배된 금액 데이터 엔티티입니다.

```


- token은 문자열 3자리의 난수로 생성됩니다.
  작성한 소스의 TokenRandomGenerator 에서는 apache common-lang 의 RandomStringUtils 유틸을 사용하여
  3자리(알파벳+숫자) 랜덤생성 하고 있습니다.
  
  MoneySpreadToken entity(Room Token 맵핑 저장소)에서 roomId와 token 이 unique 구성됩니다.
  난수의 제한적인 자릿수로 Room에 대한 중복값이 발생하여 unique violation이 밣생할 수 있습니다.
  중복 발생의 경우 spring-retry 등으로 제한된 숫자만큼 재시도 하도록 처리하는 것도 좋을 것 같습니다.
  또는 만료된 토큰의 삭제 및 백업처리로도 보완이 가능할것 같습니다.
  제한된 갯수의 토큰이라면 Room 별로 미리 토큰을 생성햇 관리해도 좋을 것 같습니다. 

- 뿌리기 생성시 7일의 유효기간을 갖는 토큰을 생성하고 
  해당 토큰을 이용하여 뿌리기(10분 유효기간)를 생성합니다.
  
- 뿌리기 받기를 할 때, roomId와 토큰값을 이용하여 유효한 맵핑토큰을 획득하고
  유효한 뿌리기에 대해 "받기"를 합니다.
  뿌리기 받기 시도에 대한 동시성 접근을 고려하여 룸과 토큰으로 조회시 비관적락(LockModeType.PESSIMISTIC_FORCE_INCREMENT)을 적용하였습니다. 
  
- 뿌리기 생성자가 뿌리기 보려할 때, roomId와 토큰값을 이용하여 유효한 맵핑토큰을 획득하고
  유효한 뿌리기를 조회하여 응답합니다.
  

### 실행방법

```

$> gradle bootRun  
 -- (서버포트 10080 로 지정되어있습니다.)

```

--- 

### API

> 뿌리기 생성 API

```
curl -X POST \
  http://localhost:10080/spreads \
  -H 'content-type: application/json' \
  -H 'X-ROOM-ID: {roomId}' \
  -H 'X-USER-ID: {userID}' \
  -d '{"distributionSize":5,"depositPrice":20000}'
```

> 뿌리기 받기 API
```
curl --location --request POST 'http://localhost:10080/spreads/{token}/receive' \
--header 'X-ROOM-ID: {roomId}' \
--header 'X-USER-ID: {userID}' \
--header 'Content-Type: application/json' \
--data-raw ''
```

> 뿌리기 확인 API

```
curl --location --request GET 'http://localhost:10080/spreads/{token}' \
--header 'X-ROOM-ID: {roomId}' \
--header 'X-USER-ID: {userID}' \
--header 'Content-Type: application/json' \
--data-raw ''
```




