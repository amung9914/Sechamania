let currentPath = window.location.pathname;
let pathSegments = currentPath.split('/').filter(Boolean); // 빈 문자열 제거
let lastPathSegment = pathSegments[pathSegments.length - 1 ]; // 마지막 주소에 담긴 값 == ArticleId

let isLogin = localStorage.getItem("access_token");
if(!isLogin){
    location.replace("/");
}
let articleId = null;
document.addEventListener("DOMContentLoaded", function(){
    // summernote
    $(document).ready(function() {
        $('#summernote').summernote({
            placeholder: 'Hello stand alone ui',
            tabsize: 2,
            height: 300,
            lang: "ko-KR",
            toolbar: [
                ['style', ['style']],
                ['font', ['bold', 'underline', 'clear']],
                ['color', ['color']],
                ['para', ['ul', 'ol', 'paragraph']],
                ['table', ['table']],
                ['insert', ['picture']],
                ['view', ['fullscreen', 'codeview', 'help']]
            ],
            callbacks: {
                onImageUpload : function(files, editor, welEditable){
                    for (let i = 0; i < files.length; i++) {
                        imageUpload(files[i]);

                    }
                }
            }
        });
    });
    function successForCategory(response){
        let selectDiv =  document.getElementById("category");
        let jsonData = response.data;
        jsonData.forEach(function(data){
            // 동적으로 li 요소 생성 및 설정
            let option = document.createElement("option");
            option.setAttribute("value", data.categoryId);
            option.innerText = data.name;
            selectDiv.appendChild(option);
        });
    }
    function failForCategory(){
        alert("카테고리를 정상적으로 불러올 수 없습니다");
    }
    httpRequestWithResponse("GET","/api/category",null,successForCategory,failForCategory);
    /* 기존 게시글 정보 불러오기 START */
    function success(response){
        let jsonData = response.data;
        document.getElementById("category").value = jsonData.categoryId;
        document.getElementById("title").value = jsonData.title;
        $('#summernote').summernote('code',jsonData.content);
        articleId = response.data.articleId;

        let hashtagView = document.getElementById("hashtag_view");
        let hashtagData = response.data.hashtags;
        hashtagData.forEach(function (data){
            let div = document.createElement("div");
            div.className = "p_tag";
            div.innerHTML = '#<p>'+data+'</p>' +
                '<p class="close_btn" onclick="deleteTag(this)">x</p>';
            hashtagView.appendChild(div);
        });

    }
    function fail(){
        alert("게시글 정보를 정상적으로 불러올 수 없습니다");
    }
    httpRequestWtihTokenAndResponse("GET","/article/"+lastPathSegment,null,success,fail);
    /* 기존 게시글 정보 불러오기 END */


    let newHashtag = document.getElementById("hashtag");
    newHashtag.addEventListener('keyup',event => handleEnter(event));

    function handleEnter(event){
        let key = event.key || event.keyCode;
        if(key==='Enter'|| key===13){
            let newHashtag = document.getElementById("hashtag");
            let view = document.getElementById("hashtag_view");
            let div = document.createElement("div");
            div.className = "p_tag";
            div.innerHTML = '#<p class="hash_text">'+newHashtag.value+'</p>' +
                '<p class="close_btn" onclick="deleteTag(this)">x</p>';
            view.appendChild(div);
            newHashtag.value = "";
        }

    }

    // api통신
    document.getElementById("write_article_form").addEventListener("submit",function (event){
       event.preventDefault();
    });

    document.getElementById("submitBtn").addEventListener("click",function (event){
        if(document.getElementById("category").value==="카테고리"){
            alert("카테고리를 확인해주세요");
        }else{
            let markupStr = $('#summernote').summernote('code'); // 본문 내용
            // 해시태그 배열 변환
            let elements = document.getElementsByClassName('hash_text');
            let hashtags = [];
            for(let i = 0; i<elements.length; i++){
                let text = elements[i].innerText;
                hashtags.push(text.substring(text.indexOf('#\n\n')+3));
            }

            let body = JSON.stringify({
                "dto":{
                    "categoryId":document.getElementById("category").value,
                    "title": document.getElementById("title").value,
                    "content":markupStr
                },
                "hashtags":hashtags,
                "articleId":articleId
            })

            function success(){
                alert("게시글 수정이 완료되었습니다.");
                location.replace("/view/article/"+articleId);
            }
            function fail(){
                alert("게시글이 정상적으로 수정되지 않았습니다");
            }

            httpRequestWtihToken("POST","/article",body,success,fail)
        }
    });
})

function imageUpload(file){
    let data = new FormData();
    data.append("file",file);
    function success(response){
        addImg(response.data);
    }
    function fail(){
        alert("이미지 업로드 실패");
    }
    httpRequestForFormDataWithTokenAndResponse("POST","/save/articleImage",data,success,fail);
}

function addImg(imgPath){
    $('#summernote').summernote('editor.insertImage',imgPath);
}

function deleteTag(element){
    element.parentNode.remove();
}
