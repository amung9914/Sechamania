document.addEventListener("DOMContentLoaded", function(){

    function successForCategory(response){
        let selectDiv =  document.getElementById("category");
        let jsonData = response.data;
        jsonData.forEach(function(data){
            if(data.name!=="공지사항") {
                // 동적으로 li 요소 생성 및 설정
                let option = document.createElement("option");
                option.setAttribute("value", data.categoryId);
                option.innerText = data.name;
                selectDiv.appendChild(option);
            }
        });
    }
    function failForCategory(){
        alert("카테고리를 정상적으로 불러올 수 없습니다");
    }

    httpRequestWithResponse("GET","/api/category",null,successForCategory,failForCategory);


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
                hashtags.push(text);
            }

            let body = JSON.stringify({
                "addArticleDto":{
                    "categoryId":document.getElementById("category").value,
                    "title": document.getElementById("title").value,
                    "content":markupStr
                },
                "hashtags":hashtags,
            })
            function success(){
                alert("게시글 등록이 완료되었습니다.");
                location.replace("/view/article");
            }
            function fail(){
                alert("게시글이 정상적으로 작성되지 않았습니다");
            }

            httpRequestWtihToken("POST","/save/article",body,success,fail)
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

