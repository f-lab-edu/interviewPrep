# 🧑🏻‍💻 InterviewPrep

### 웹사이트 URL
http://www.interviewprep.kr/

### 👥  팀원
|                                         Backend                                          |                                         Backend                                          |                                         Backend                                          |                                       Frontend                                        |                                        Frontend                                         |
| :--------------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------------: | :-----------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------------: | 
| [이종복](https://github.com/LeeJongbokz) | [진은혜](https://github.com/Jineh) | [임정택](https://github.com/wjdxor) | [도윤](https://github.com/N3theri9N) | [이지홍](https://github.com/lee-ji-hong) |
<br>

### 🧚‍♀️ 로컬 세팅 가이드
1) Repository에서 프로젝트를 다운 받습니다.
```
   git clone https://github.com/f-lab-edu/interviewPrep.git
```

2) Redis를 설정합니다.    
(1) Docker를 설치합니다. 
```
   https://docs.docker.com/desktop/install/windows-install/
```
(2) Redis Image를 받아옵니다.
```
   docker image pull redis
```
(3) Redis Network를 생성하고, 확인합니다.  
```
   docker network create redis-network
   docker network ls  
```
(4) Redis 서버를 실행합니다. 
```
   docker run --name local-redis -p 6379:6379 --network redis-network -v redis_temp:/data -d redis:latest redis-server --appendonly yes
```
(5) 현재 실행중인 Redis에 Redis-cli로 접속합니다.
```
    docker run -it --network redis-network --rm redis:latest redis-cli -h local-redis
```
