# 사전과제 #

뿌리기 기능 구현

---

### 개발환경 ###

- Java 1.8.x
- Lombok plugin
- SpringBoot
- H2

---


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




