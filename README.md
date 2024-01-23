## 트러블슈팅

### problem 1 : 글을 가져올때 ArticleImg로 인해 패치조인 오류 발생 및 최적화 불가
<hr>

글 정보를 한 번에 가져오려고 하자 MultipleBagFetchException이 발생하였습니다.<br>
컬렉션은 둘 이상 fetch join이 불가능하여 발생하는 오류였습니다.<br>

현재 Article-ArticleHashtag-Hashtag<br>
    Article-ArticleImg

이런 형태의 연관관계가 형성중이고 정보를 한 번에 가져오려고 하니 문제가 발생합니다. 

### solution: 

1. 배치사이즈 이용 - 쿼리 4번(조인테이블 총 4개),<br>
2. 패치조인 사용 및 img 따로 객체 그래프 조회 - 쿼리 2번<br>
3. 패치조인 사용 및 ArticleImg를 엔티티로 관리하지 않는다

### result : 3번째 방법 채택
ArticleImg를 따로 엔티티로 처리하지 않고 Article의 content에 img태그 형태로 보관 & amazon S3에 이미지 파일을 보관하였습니다.
이를 통해 article 조회 쿼리를 1회로 단축했습니다.
<hr>

### problem 2: JWT기반 인증으로 초기화면에서 Thymeleaf sec:authorize 속성 사용 불가
<hr>
formLogin 기반으로 구현을 하다 JWT를 적용하게 되었는데, 
세션기반 인증이 아닌 JWT기반 인증이다보니 초기화면에서 활용했던 코드를 그대로 사용할 수 없었습니다.

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
1. Spring security의 LoginSuccessHandler에서 accessToken을 클라이언트에 보낼때 admin권한도 확인한다.
2. admin권한이 있는 경우 이를 클라이언트측에서 알 수 있도록 정보를 포함시킨다.
3. 처음에는 response를 Redirect 하면서 쿼리 파라미터에 값을 넣어주려고 했으나, 쿼리 파라미터에 token과 admin을 동시에 넣었더니 클라이언트에서 값을 읽지 못하는 문제가 발생했다.
4. 따라서 response Header에 하나씩 값을 추가하여 해결하였다.

### result: response Header를 이용
1. accessToken이 있는 경우 로그인 된 사용자라고 간주. localstorage에 token이 있는 경우 [로그아웃] 버튼 출력
2. ``ROLE_ADMIN``권한이 있는 경우 admin="ture"를 헤더에 넣어서 response 및 localstorage에 보관
3. localstorage에 ``admin``이 존재하면 [관리자]버튼 출력
3. SecurityConfig에서 requestMatchers를 통해 권한 확인 하므로 혹시나 관리자가 아닌 사람이 접근해도 문제 없음. 

### 이게 맞나? springsecurity requestMatcher에 view를 다 열어 놓고 api만 막는게 맞나?


