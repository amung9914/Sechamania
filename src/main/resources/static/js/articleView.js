let currentPath = window.location.pathname;
let pathSegments = currentPath.split('/').filter(Boolean); // 빈 문자열 제거
let lastPathSegment = pathSegments[pathSegments.length - 1 ]; // 마지막 주소에 담긴 값 == ArticleId

let articleId = null;
let isMarked = false;
let userNickname = null;
document.addEventListener("DOMContentLoaded", function(){

    /* 게시글 로딩 START */
    function success(response){
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
    /* 게시글 로딩 END */

    /* 댓글 로딩 START - renderComment 함수와 연결 */
    function successForComment(response){
        console.log(response);
        userNickname = response.nickname;
        let jsonData = response.data;
        renderComment(jsonData,0);
    }
    function failForComment(){
        console.log("댓글로딩실패");
    }
    if(isLogin){
        document.getElementById("comment_form").style.display = 'block';
        httpRequestWtihTokenAndResponse("GET","/api/comment/"+lastPathSegment,null,successForComment,failForComment);
    }else{
        httpRequestWithResponse("GET","/api/comment/"+lastPathSegment,null,successForComment,failForComment);
    }
    /* 댓글 로딩 END */

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

    /* 댓글 추가 */
    document.getElementById("comment_form").addEventListener('submit',function (event){
        event.preventDefault();
        let newComment = document.getElementById("new_comment");
        if(newComment.value!==""){
            let body = JSON.stringify({
                "content": newComment.value,
                "articleId": articleId
            })
            function success(){
                location.replace("/view/article/"+articleId);
            }
            function fail(){
                alert("댓글이 정상적으로 등록되지 않았습니다");
            }
            httpRequestWtihToken("POST","/save/comment",body,success,fail);
        }
    });

    // 뒤로 가기
    document.getElementById("back_btn").addEventListener('click',function () {
        window.history.back();
    })

}); // END DOMLoaded


function transDate(data){
    // Date 객체 생성
    let date = new Date(data);

// 년, 월, 일 정보 추출
    let year = date.getFullYear().toString().substr(-2);
    let month = ("0" + (date.getMonth() + 1)).slice(-2);
    let day = ("0" + date.getDate()).slice(-2);
    let hour = ("0" + (date.getHours())).slice(-2);
    let min = ("0" + (date.getMinutes())).slice(-2);

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


/**
 * jsonData, 반복횟수 전달함(댓글 깊이 계산)
 */
function renderComment(jsonData,count){
    let commentSection = document.getElementById("comment_section");
    jsonData.forEach(function (data) {
        let root = document.createElement("div");
        root.className = "comment";
        if(data.parentId!=null){
            for(let i=0; i<count;i++){
                let countDiv = document.createElement('div');
                countDiv.className = "blank";
                countDiv.innerText = '  ';
                root.appendChild(countDiv);
            }
        }
        let div = document.createElement("div");
        div.className = "comment_content";
        div.id = data.commentId;
        div.innerHTML = '                        <div class="author">' +
            '                            <img class="profile_img" src="'+data.profileImg+'">' +
            '                            <p class="date">'+data.author+' | '+transDate(data.createdTime)+'</p>'+
            '                        </div>' +
            '                        <div id="content'+data.commentId+'">'+data.content+'</div>';

        if(userNickname!==null){
            let div2 = document.createElement("div");
            div2.className = "author";
            div2.innerHTML = '<p class="comment_p" onclick="addChild(this)">댓글쓰기</p>';
            if(userNickname===data.author){
                div2.innerHTML +='<p class="comment_p" onclick="openUpdate(this)">수정</p>' +
                    '           <p class="comment_p" onclick="deleteComment(this)">삭제</p>';
            }
            div.appendChild(div2);
        }

        root.appendChild(div);
        commentSection.appendChild(root);
        if(data.children.length>0){
            count +=1;
            renderComment(data.children,count);
        }
    });
}

/**
 * 댓글 수정
 */
function openUpdate(element){
    let targetDiv = element.parentElement.parentElement;
    let commentId = targetDiv.id;
    let previousValue = document.getElementById("content"+commentId).innerText;
    let textarea = document.createElement("textarea");
    textarea.className = "comment_text";
    textarea.id = "update_comment";
    textarea.value = previousValue;
    let button = document.createElement("button");
    button.className = "btn btn-outline-secondary";
    button.id = "update_btn";
    button.innerText = "등록";

    button.addEventListener('click',function (){
        let body = JSON.stringify({
            "commentId": commentId,
            "content": document.getElementById("update_comment").value
        })
        function success(){
            location.replace("/view/article/"+articleId);
        }
        function fail(){
            alert("댓글 수정이 정상적으로 처리되지 않았습니다");
        }
        httpRequestWtihToken("POST","/update/comment",body,success,fail);
    })

    targetDiv.appendChild(textarea);
    targetDiv.appendChild(button);

}

/**
 * 댓글 삭제
 */
function deleteComment(element){
    let targetDiv = element.parentElement.parentElement;
    let commentId = targetDiv.id;

    let isConfirmed = confirm("댓글을 삭제하시겠습니까?");
    if(isConfirmed){
        function success(){
            alert("삭제가 완료되었습니다");
            location.replace("/view/article/"+articleId);
        }
        function fail(){
            alert("삭제가 정상적으로 완료되지 않았습니다");
        }
        httpRequestWtihToken("DELETE","/comment/"+commentId,null,success,fail);

    }
}

/**
 * 답글을 등록하는 함수
 */
function addChild(element){
    let targetDiv = element.parentElement.parentElement;
    let commentId = targetDiv.id;
    let textarea = document.createElement("textarea");
    textarea.className = "comment_text";
    textarea.id = "child_comment";
    textarea.placeholder = "여러분의 소중한 댓글을 입력해주세요";
    let button = document.createElement("button");
    button.className = "btn btn-outline-secondary";
    button.id = "child_btn";
    button.innerText = "등록";

    button.addEventListener('click',function (){
        let body = JSON.stringify({
            "parentId": commentId,
            "content": document.getElementById("child_comment").value,
            "articleId": articleId
        })
        function success(){
            location.replace("/view/article/"+articleId);
        }
        function fail(){
            alert("댓글 등록이 정상적으로 처리되지 않았습니다");
        }
        httpRequestWtihToken("POST","/save/comment",body,success,fail);
    })

    targetDiv.appendChild(textarea);
    targetDiv.appendChild(button);

}