let isLogin = localStorage.getItem("admin");
if (!isLogin) {
    location.replace("/login");
}

document.addEventListener("DOMContentLoaded", function(){
    // summernote
    $(document).ready(function() {
        $('#summernote').summernote({
            placeholder: 'Hello stand alone ui',
            tabsize: 2,
            height: 250,
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

    // api통신
    document.getElementById("write_article_form").addEventListener("submit",function (event){
        event.preventDefault();

        let markupStr = $('#summernote').summernote('code'); // 본문 내용

        let body = JSON.stringify({
            "title": document.getElementById("title").value,
            "content":markupStr
        });
        function success(){
            alert("게시글 등록이 완료되었습니다.");
            location.replace("/view/notice");
        }
        function fail(){
            alert("게시글이 정상적으로 작성되지 않았습니다");
        }

        httpRequestWtihToken("POST","/save/article",body,success,fail)
    });

});

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