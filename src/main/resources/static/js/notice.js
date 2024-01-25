document.addEventListener('DOMContentLoaded', function () {

    function success(response){
        console.log(response);
        if(response.empty===true){
           document.getElementById("article_List").innerHTML =
               '<h4>아직 북마크가 없습니다</h4>';
        }else{
            let articleList = document.getElementById("article_List");
            let content = response.content;
            content.forEach(function(data){

                let div = document.createElement("div");
                div.className = "article";
                div.innerHTML = '<div class="author">' +
                    '                        <p class="date">'+transDate(data.createdTime)+' | '+data.nickname+'</p>' +
                    '                    </div>' +
                    '                    <div>' +
                    '                        <h2>' +
                    '                          <a href="/view/article/'+data.id+'">' +
                    '                                '+data.title+
                    '                            </a>' +
                    '                       </h2>' +
                    '                       <hr/>' +
                    '                      </div>';
                articleList.appendChild(div);
            });

            let currentPage = response.number+1;
            // 페이지 범위 계산
            let startRange = Math.floor((currentPage - 1) / 5) * 5 + 1;
            let endRange = startRange + 4;

            let paginationUl = document.getElementById("pagination");
            let newHTML = "";
            let before = startRange-1;
            if(response.pageable.pageNumber>4){
                newHTML +=
                    '<li class="page-item">' +
                    '                                <a class="page-link" onclick="callPage('+before+')" aria-label="Previous">' +
                    '                                    <span aria-hidden="true">&laquo;</span>' +
                    '                                </a>' +
                    '                            </li>';

            }

            for (let i = startRange; i <= endRange; i++) {

                if(i===currentPage){
                    newHTML +=  '<li class="page-item"><a class="page-link notice" onclick="callPage('+i+')">'+i+'</a></li>';
                }else if(i>=response.totalPages){
                    break;
                }else{
                    newHTML +=  '<li class="page-item"><a class="page-link" onclick="callPage('+i+')">'+i+'</a></li>';
                }
            }
            let next = endRange+1;
            if(response.totalPages-response.pageable.pageNumber>5){
                newHTML +=
                    '                            <li class="page-item">' +
                    '                                <a class="page-link" onclick="callPage('+next+')" aria-label="Next">' +
                    '                                    <span aria-hidden="true">&raquo;</span>' +
                    '                                </a>' +
                    '                            </li>';
            }
            paginationUl.innerHTML = newHTML;
        }
    }
    function fail(){
        console.log("bookmark를 정상적으로 불러올 수 없습니다");
    }
    httpRequestWithResponse("GET","/api/notice",null,success,fail);
})


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

function callPage(page){
    let path = page-1;
    function success(response){
        console.log(response);
        let articleList = document.getElementById("article_List");
        articleList.innerHTML="";
        let content = response.content;
        content.forEach(function(data){

            let div = document.createElement("div");
            div.className = "article";
            div.innerHTML = '<div class="author">' +
                '                        <p class="date">'+transDate(data.createdTime)+' | '+data.nickname+'</p>' +
                '                    </div>' +
                '                    <div>' +
                '                        <h2>' +
                '                          <a href="/view/article/'+data.id+'">' +
                '                                '+data.title+
                '                            </a>' +
                '                       </h2>' +
                '                       <hr/>' +
                '                      </div>';
            articleList.appendChild(div);
        });

        let currentPage = response.number+1;
        // 페이지 범위 계산
        let startRange = Math.floor((currentPage - 1) / 5) * 5 + 1;
        let endRange = startRange + 4;

        let paginationUl = document.getElementById("pagination");
        let newHTML = "";
        let before = startRange-1;
        if(response.pageable.pageNumber>4){
            newHTML +=
                '<li class="page-item">' +
                '                                <a class="page-link" onclick="callPage('+before+')" aria-label="Previous">' +
                '                                    <span aria-hidden="true">&laquo;</span>' +
                '                                </a>' +
                '                            </li>';

        }

        for (let i = startRange; i <= endRange; i++) {

            if(i===currentPage){
                newHTML +=  '<li class="page-item"><a class="page-link notice" onclick="callPage('+i+')">'+i+'</a></li>';
            }else if(i>=response.totalPages){
                break;
            }else{
                newHTML +=  '<li class="page-item"><a class="page-link" onclick="callPage('+i+')">'+i+'</a></li>';
            }
        }
        let next = endRange+1;
        if(response.totalPages-response.pageable.pageNumber>5){
            newHTML +=
                '                            <li class="page-item">' +
                '                                <a class="page-link" onclick="callPage('+next+')" aria-label="Next">' +
                '                                    <span aria-hidden="true">&raquo;</span>' +
                '                                </a>' +
                '                            </li>';
        }
        paginationUl.innerHTML = newHTML;
        scrollToTop();
    }
    function fail(){
        let h3 = document.createElement("h3");
        h3.innerText = "게시글이 존재하지 않습니다";
        document.getElementById("article_List").appendChild(h3);
    }
    httpRequestWithResponse("GET","/api/notice/"+path,null,success,fail);
}

function scrollToTop() {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
}