let currentPath = window.location.pathname;
let pathSegments = currentPath.split('/').filter(Boolean); // 빈 문자열 제거
let lastPathSegment = pathSegments[pathSegments.length - 1 ]; // 마지막 주소에 담긴 값 == ArticleId

let articleId = null;
let isMarked = false;
document.addEventListener("DOMContentLoaded", function(){
    function success(response){
        console.log(response);
        isMarked = response.isMarked; // 북마크여부
        let isAuthor = response.isAuthor; // 작성자여부
        articleId = response.data.articleId;
        document.getElementById("title").innerText = response.data.title;
        document.getElementById("category").innerText = response.data.categoryName;
        document.getElementById("content").innerHTML = response.data.content;
        document.getElementById("date&author").innerText = transDate(response.data.createdTime)+' | '+response.data.nickname;
        document.getElementById("author_profile").src = response.data.profileImg;
        if(isMarked){
            document.getElementById("bookmark_img").src = "/img/bookmark_checked.png";
        }
        let hashtagView = document.getElementById("hashtag_view");
        let hashtagData = response.data.hashtags;
        hashtagData.forEach(function (data){
            let div = document.createElement("div");
            div.className = "p_tag";
            div.innerHTML = '#<p>'+data+'</p>';
            hashtagView.appendChild(div);
        });
        if(isAuthor){
            let update = document.createElement("div");
            update.className = "author";
            update.innerHTML = '<p class="comment_p" id="ubdate_btn" onclick="isValidUpdateBtn()">수정</p>' +
                '           <p class="comment_p" id="delete_btn" onclick="isValidDeleteBtn()">삭제</p>';
            document.getElementById("author_line").appendChild(update);
        }

    }
    function fail(){
        alert("게시글을 정상적으로 불러올 수 없습니다");
        location.replace("/view/article");
    }
    let isLogin = localStorage.getItem("access_token");
    if(isLogin){
        httpRequestWtihTokenAndResponse("GET","/api/article/"+lastPathSegment,null,success,fail);
    }else{
        httpRequestWithResponse("GET","/api/article/"+lastPathSegment,null,success,fail);
    }

    /* 북마크 클릭 이벤트 START */
    let bookmark = document.getElementById("bookmark_img");
    bookmark.addEventListener('click',function(){
        console.log(bookmark.src);
       if(isLogin && !isMarked){
           function success(){
            bookmark.src= '/img/bookmark_checked.png';
            isMarked = true;
           }
           function fail(){
               alert("북마크가 정상적으로 등록되지 않았습니다");
           }
           httpRequestWtihToken("POST","/bookmark/"+articleId,null,success,fail);
       }else if(isLogin && isMarked){
           function success(){
               bookmark.src= '/img/bookmark.png';
               isMarked = false;
           }
           function fail(){
               alert("북마크가 정상적으로 삭제되지 않았습니다");
           }
           httpRequestWtihToken("DELETE","/bookmark/"+articleId,null,success,fail);
       }
    }); /* 북마크 클릭 이벤트 END */

});


function transDate(data){
    // Date 객체 생성
    let date = new Date(data);

// 년, 월, 일 정보 추출
    let year = date.getFullYear().toString().substr(-2);
    let month = ("0" + (date.getMonth() + 1)).slice(-2);
    let day = ("0" + date.getDate()).slice(-2);
    let hour = ("0" + (date.getHours() + 1)).slice(-2);
    let min = ("0" + (date.getMinutes() + 1)).slice(-2);

// 변환된 형식으로 조합
    let formattedDate = year + "." + month + "." + day +" "+hour+"시 "+min+"분";
    return formattedDate;
}

function isValidUpdateBtn(){
        location.replace("/view/updateArticle/"+articleId);
}

function isValidDeleteBtn(){
    let isConfirmed = confirm("게시글을 삭제하시겠습니까?");
    if(isConfirmed){
        function success(){
            alert("삭제가 완료되었습니다");
            location.replace("/view/article");
        }
        function fail(){
            alert("삭제가 정상적으로 완료되지 않았습니다");
        }
        httpRequestWtihToken("DELETE","/article/"+articleId,null,success,fail);

    }
}