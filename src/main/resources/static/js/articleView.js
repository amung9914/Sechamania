let currentPath = window.location.pathname;
let pathSegments = currentPath.split('/').filter(Boolean); // 빈 문자열 제거
let lastPathSegment = pathSegments[pathSegments.length - 1 ]; // 마지막 주소에 담긴 값 == ArticleId

document.addEventListener("DOMContentLoaded", function(){
    function success(response){
        console.log(response);
    }
    function fail(){
        alert("게시글을 정상적으로 불러올 수 없습니다");
        location.replace("/view/article");
    }
    httpRequestWtihTokenAndResponse("GET","/api/article/"+lastPathSegment,null,success,fail);
});
