## 트러블슈팅

### problem1 : 글을 가져올때 패치조인 오류 발생으로 최적화 불가
<hr>

글 정보를 한 번에 가져오려고 하자 MultipleBagFetchException이 발생하였습니다.<br>
컬렉션은 둘 이상 fetch join이 불가능하여 발생하는 오류였습니다.<br>

현재 Article-ArticleHashtag-Hashtag<br>
    Article-ArticleImg

이런 형태의 연관관계가 형성중이고 정보를 한 번에 가져오려고 하니 문제가 발생합니다. 

### solution: 

1. 배치사이즈 이용 - 쿼리 4번(조인테이블 총 4개),<br>
2. 패치조인 사용 및 img 따로 객체 그래프 조회 - 쿼리 2번<br>

### result : 저는 2번째 방법을 선택하였습니다. 
리스트 형태로 받아오는게 아니라 <br>
글을 조회했을때 쿼리가 나가기에 크게 부담이 가지 않을 것으로 예상했습니다.

<hr>

### problem2: JWT기반 인증으로 초기화면에서 Thymeleaf sec:authorize 속성 사용 불가
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
1. localstorage에 관리자는 admin을 넣어준다.
2. admin이 있는 경우 관리자 메뉴 표시
2. js를 이용해서 메뉴를 동적으로 구현한다.
3. access token의 duration이 하루라서 괜찮을 듯. access token이 없으면 로그인/회원가입 버튼 출력


