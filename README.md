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


2) h2를 설치하고, 접속합니다. 
   (1) h2 웹 사이트에서 h2를 다운 받습니다. 
   ```
      http://h2database.com/html/main.html
   ```
   (2) h2의 JDBC URL을 다음과 같이 설정합니다.  
   ```
     jdbc:h2:tcp://localhost/~/interviewPrep
   ```
   (3) h2에 접속합니다.
   
3) Redis를 설정합니다.    

   (1) Docker를 설치합니다. 
   ```
      https://docs.docker.com/desktop/install/windows-install/
   ```
   (2) Redis Image를 받아옵니다.
   ```
      docker pull redis
   ```
   (3) docker-compose로 컨테이너를 생성하고 실행합니다. 
   ```
      docker-compose up -d  
   ```
   (4) Redis-cli로 접속해서, 확인합니다. 
   ```
      docker exec -it redis_test redis-cli
   ```
