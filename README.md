# 개인프로젝트 - 세차 커뮤니티 플랫폼 

### 🔗Deploy site

[실제 배포 사이트 바로가기](http://library-env.eba-wgbtarnw.ap-northeast-2.elasticbeanstalk.com/)

##  ✨ 프로젝트 소개
``JPA`` ``RestApi`` ``공공데이터API`` ``CI/CD`` 를 활용하여 세차 커뮤니티 플랫폼을 개발하였습니다.<br>
기간 : 2024.01.10 ~ 2024.01.25 총 16일 소요

## 💡ERD : 
![image](https://github.com/amung9914/Sechamania/assets/137124338/121f046f-42c2-4ebe-901c-7730857cc8ae)

## 시스템 구성도
![시스템구성도](https://github.com/amung9914/book_management/assets/137124338/e78547a8-498a-408c-8d80-2aff703a9893)

## 🛠 Languages and Tools:
- JDK 17
- Spring Boot v3.2.0
- Spring Data JPA v3.2.1
- QueryDSL v5.0.0
- Spring Security v6.2.0
- Spring Cloud AWS Starter v2.2.6
- jjwt v0.9.1
- Oauth2 client v6.2.1
- Jakarta Mail v2.0.2
- Thymeleaf v6.3.1
- Thymeleaf Layout Dialect v3.3.0
- lombok v1.18.28
- Junit v5.10.1
- MySQL v8.0.33
- H2database v2.2.224
- Bootstrap v5.3.1

## 🛠 OPEN Api:
- 기상청 단기예보 조회서비스 v2.0 (공공데이터포털 활용)
- Kakao Map API
- Daum PostCode Service
- Summernote Editor

## 📌 구현 기능 :

### 초기화면 - 현재 날씨 확인 및 세차 제안 코멘트 제공

![image](https://github.com/amung9914/Sechamania/assets/137124338/4e535764-f9c2-45ee-9623-865ae2e00883)

- JWT토큰을 기반으로 자동 로그인 처리
- 등록된 사용자 주소를 기반으로 기상청 단기예보 조회서비스를 이용하여 날씨 호출

![image](https://github.com/amung9914/Sechamania/assets/137124338/d16a2e46-f783-452a-91af-57dfae9efad5)
- 현재 날씨 및 10시간 후 날씨 호출 및 분석
- 날씨에 따라 날씨 이미지 변경
- 날씨와 기온에 따라 세차 제안 코멘트 변경

### 로그인 - 이메일 로그인 및 Oauth2를 이용한 소셜로그인 제공(구글)
![image](https://github.com/amung9914/Sechamania/assets/137124338/739ac93f-da0c-4570-81e8-98a300ff41ee)
- 로그인 정보는 JWT accessToken 및 RefreshToken을 이용하여 로그인 처리

### 소셜 로그인 시 비회원인 경우 추가 정보 등록
![image](https://github.com/amung9914/Sechamania/assets/137124338/1ae8885e-8500-495e-8f99-a3fa9a397ead)
- Oauth2를 이용하여 회원정보를 저장한 후 기존에 회원 정보가 없는 경우 추가 정보를 받는 방식으로 구현

### 회원가입 - 중복확인,이메일인증 및 정규표현식 검증
![image](https://github.com/amung9914/Sechamania/assets/137124338/98a58d2d-91ce-4c8c-8720-d098229f0c29)
![image](https://github.com/amung9914/Sechamania/assets/137124338/ec4a5826-132b-4680-bbec-c05a66408bb6)
- 이메일을 입력 시 중복된 이메일인지 검증 여부 호출
- 닉네임 입력 시 중복된 닉네임인지 검증 여부 호출
- 다음 우편번호 API를 이용하여 주소 검색 및 Kokoa Map api를 통해 위도,경도 호출
![image](https://github.com/amung9914/Sechamania/assets/137124338/67021713-7189-414d-a41c-853e82096558)
- 인증 메일 전송 및 인증번호 확인 후 회원 가입 가능
- 정규표현식을 이용하여 올바른 이메일 형식인지, 정해진 비밀번호 양식 일치여부 검증

### 커뮤니티 - 전체 게시글 목록 최신순 정렬
![image](https://github.com/amung9914/Sechamania/assets/137124338/f7216f07-84df-45ba-ab2f-4f830bf48e44)
- 전체 게시글을 5개씩 최신순 정렬 및 페이징 적용
- 현재 등록된 해시태그 및 카테고리 확인 가능
- 게시글 제목 클릭 시 게시글 상세화면으로 이동

### 커뮤니티 - 해시태그별 게시글 조회
![image](https://github.com/amung9914/Sechamania/assets/137124338/3047aaea-04bb-4821-be0c-db0f4c5c246b)
- 해시태그 클릭 시 해시태그가 등록된 게시글 호출
- 페이징 적용

### 커뮤니티 - 카테고리별 게시글 조회
![image](https://github.com/amung9914/Sechamania/assets/137124338/7821b597-c574-4d77-8f07-c9cbdad404a2)
- 카테고리 클릭 시 카테고리에 해당하는 게시글 호출
- 페이징 적용

### 게시글 작성 - 해시태그 및 이미지 추가

![image](https://github.com/amung9914/Sechamania/assets/137124338/4a3b1717-2a6b-4821-8ba8-65339e7bdbce)
- Summernote Editor를 활용하여 게시글 내에 사진 추가 가능
- 사진은 Amazon S3 버킷에 저장
- 태그입력 칸에서 해시태그 입력 및 기존에 추가된 해시태그 삭제 가능

### 게시글 조회 - 게시글 확인 및 북마크 기능
![image](https://github.com/amung9914/Sechamania/assets/137124338/47fe7281-1ce4-455d-b51a-e626b0c1cbde)
 - 게시글 내용과 북마크 등록 여부 확인 가능
 - 북마크 버튼을 누르면 북마크 등록 / 등록된 북마크를 다시 누르면 북마크 삭제

### 게시글 조회 - 계층형 댓글 기능
![image](https://github.com/amung9914/Sechamania/assets/137124338/023e5dc8-21f2-48f9-a37a-aeac4c3cbf24)
- 댓글의 답글이 가능하도록 댓글 추가와 답댓글 작성 가능
- 댓글 작성자인 경우 댓글 수정/삭제 가능
  

### 마이페이지_계정관리 - 기본정보수정, 비밀번호 변경, 계정삭제

![image](https://github.com/amung9914/Sechamania/assets/137124338/8dade6a2-ed5d-4687-ba59-bb2149da31f2)
- 마이페이지 초기화면
- 계정삭제 버튼 클릭시 계정상태가 WITHDRAW로 전환되고 로그인할 수 없음

![image](https://github.com/amung9914/Sechamania/assets/137124338/0243d8c3-fa26-455d-96be-f16572854ee0)
- 기본정보 수정 버튼 클릭시 동적으로 요소 추가
- 닉네임과 주소, 프로필 이미지를 변경할 수 있고 선택한 프로필 이미지 표시

![image](https://github.com/amung9914/Sechamania/assets/137124338/4370a007-1fa2-4a23-bde1-77bfdec5d898)
- 비밀번호 변경 버튼 클릭시 동적으로 요소 추가

### 마이페이지_북마크
![image](https://github.com/amung9914/Sechamania/assets/137124338/a31ea877-1a25-48e0-9329-b306f6b92243)
- 북마크에 추가한 글 및 페이징 호출
- 제목 클릭 시 게시글 상세 화면으로 이동

### 마이페이지_작성한 글
![image](https://github.com/amung9914/Sechamania/assets/137124338/c222c850-f59b-4ba4-a356-bf43d54c2e12)
- 회원 본인이 작성한 글 및 페이징 호출
  
### 공지사항
![image](https://github.com/amung9914/Sechamania/assets/137124338/97510ec6-cca4-4c31-8178-4683e6f57fa6)
- 카테고리가 공지사항인 글만 따로 조회 및 페이지네이션 추가

### 검색
![image](https://github.com/amung9914/Sechamania/assets/137124338/6b4de97e-df90-4dde-8bba-e288faf3d197)
- 메뉴의 Search 버튼 클릭시 모달 호출

![image](https://github.com/amung9914/Sechamania/assets/137124338/14a1c4ec-f983-44fa-bc2d-3203314eba3c)
- 게시글 내용과 제목을 검색하여 검색 결과를 페이징을 적용하여 호출

### 관리자 - 유입관리
![image](https://github.com/amung9914/Sechamania/assets/137124338/234fb149-0bf0-4482-bf71-5f533ade0b52)
- 최근 3일 최신순으로 유입 경로와 유입시간 호출

### 관리자 - 카테고리 관리
![image](https://github.com/amung9914/Sechamania/assets/137124338/cf734d2f-1950-4741-99a1-20312e41c062)
- 카테고리 추가, 수정, 삭제 기능

### 관리자 - 공지사항 작성
![image](https://github.com/amung9914/Sechamania/assets/137124338/da58a410-47a7-47fd-951f-1292b98d97d6)
- 공지사항을 추가하는 기능
  
### Github Action과 AWS Elastic Beanstalk를 이용한 CI/CD 구현
Github master branch push를 하면 바로 배포될 수 있도록 CI/CD를 구현

<br>

## 🔥 트러블슈팅

## problem 1 : Article의 해시태그가 없는 경우로 인해 N+1 문제 발생

---

### solution:

원래 Article 조회 시 모든 join을 fetchjoin으로 하고 있었어서 문제가 없었는데,

Article의 Hashtag가 없는 경우 fetchjoin으로 조회할 수 없어서 left join을 추가해서 쿼리를 수정.

`@Query("select a from Article a " +
        "join fetch a.category c " +
        "join fetch a.member m " +
        "left join a.articleHashtags ah " +
        "left join ah.hashtag h " +
        "where a.id =:id")
Article findArticleByArticleId(@Param("id") long id);`

이렇게 쿼리를 수정했더니 N+1문제가 발생했습니다.

(해시태그가 만약 2개가 있는 경우 article 조회 1번, articleHashtags 조회 1번 hashtag 조회 2번 발생)

### solution

1. 배치사이즈 이용 - 효과 없음
2. 해시태그가 있는 경우와 없는 경우의 쿼리를 분리
3. queryDsl 이용

### result : 2번째 방법 채택

Service layer에서 해시태그가 있는 경우와 없는 경우를 분리해서 repository 메서드를 호출 했습니다.

articleHashtag에서 해시태그가 존재하는지 먼저 확인 후 해시태그가 있는 경우 모든 join을 fetch join을 이용해서 한 번에 조회할 수 있도록 구현, 해시태그가 없는 경우에는 category와 member까지만 fetch join을 하는 것으로 정리했습니다.

이렇게 하면 해시태그가 있는 경우 articleHashtag테이블 쿼리 1번 article조회쿼리 1번 실행.

해시태그가 없는 경우 articleHashtag테이블 쿼리2번 article조회쿼리 1번 실행됩니다.

---
<br>

## problem 2 : 글을 가져올때 ArticleImg로 인해 패치조인 오류 발생 및 최적화 불가

---

글 정보를 한 번에 가져오려고 하자 MultipleBagFetchException이 발생하였습니다.

컬렉션은 둘 이상 fetch join이 불가능하여 발생하는 오류였습니다.

현재 Article-ArticleHashtag-Hashtag

Article-ArticleImg

이런 형태의 연관관계가 형성중이고 정보를 한 번에 가져오려고 하니 문제가 발생합니다.

### solution:

1. 배치사이즈 이용 - 쿼리 4번(조인테이블 총 4개),
2. 패치조인 사용 및 img 따로 객체 그래프 조회 - 쿼리 2번
3. 패치조인 사용 및 ArticleImg를 엔티티로 관리하지 않는다

### result : 3번째 방법 채택

ArticleImg를 따로 엔티티로 처리하지 않고 Article의 content에 img태그 형태로 보관 & amazon S3에 이미지 파일을 보관하였습니다. 이를 통해 article 조회 쿼리를 1회로 단축했습니다.

---
<br>

## problem 3: JWT기반 인증으로 초기화면에서 Thymeleaf sec:authorize 속성 사용 불가

---

formLogin 기반으로 구현을 하다 JWT를 적용하게 되었는데, 세션기반 인증이 아닌 JWT기반 인증이다보니 초기화면에서 활용했던 코드를 그대로 사용할 수 없었습니다.

```
  <th:block sec:authorize="isAnonymous()">
              <li class="nav-item">
                <a class="nav-link" id="login" th:href="@{/login}">로그인</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" id="signup" th:href="@{/signup}">회원가입</a>
              </li>
            </th:block>

```

특히 문제가 된 부분은 관리자 메뉴입니다.

```
<th:block sec:authorize="hasAnyAuthority('ADMIN')">
              <li class="nav-item">
                <a class="nav-link" th:href="@{/signup}">관리자</a>
              </li>
            </th:block>

```

### solution:

1. Spring security의 LoginSuccessHandler에서 accessToken을 클라이언트에 보낼때 admin권한도 확인
2. admin권한이 있는 경우 이를 클라이언트측에서 알 수 있도록 정보를 포함.
3. 처음에는 response를 Redirect 하면서 쿼리 파라미터에 값을 넣어주려고 했으나, 쿼리 파라미터에 token과 admin을 동시에 넣었더니 클라이언트에서 값을 읽지 못하는 문제가 발생했습니다.
4. 따라서 response Header에 하나씩 값을 추가하여 해결하였습니다

### result: response Header를 이용

1. accessToken이 있는 경우 로그인 된 사용자라고 간주. localstorage에 token이 있는 경우 [로그아웃] 버튼 출력
2. `ROLE_ADMIN`권한이 있는 경우 admin="ture"를 헤더에 넣어서 response 및 localstorage에 보관
3. localstorage에 `admin`이 존재하면 [관리자]버튼 출력
4. SecurityConfig에서 requestMatchers를 통해 권한 확인 하므로 혹시나 관리자가 아닌 사람이 접근해도 문제가 없습니다


