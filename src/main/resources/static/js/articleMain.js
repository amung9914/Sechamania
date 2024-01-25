document.addEventListener('DOMContentLoaded',function (){

    function successForHashtag(response){
        const hashtagNav =  document.getElementById("hashtag_nav");
        let jsonData = response.data;
        jsonData.forEach(function(data){
            // 동적으로 li 요소 생성 및 설정
            let li = document.createElement("li");
            li.className = "nav-item";
            let button = document.createElement("button");
            button.type = "button";
            button.className = "btn btn-outline-dark btn-sm";
            button.innerText = data.name;
            button.setAttribute("data-user-id", data.hashtagId);
            button.addEventListener('click',function(){
                    if(document.getElementsByClassName("btn btn-outline-warning btn-sm").length===0){
                        button.className = "btn btn-outline-warning btn-sm";
                        searchHashtag(data.hashtagId);
                    }else{
                        button.className = "btn btn-outline-dark btn-sm";
                        roadAllArticle();
                    }

            })
            li.appendChild(button);
            hashtagNav.appendChild(li);
        });
    }
    function failForHashtag(){
        alert("해시태그를 정상적으로 불러올 수 없습니다");
    }

    httpRequestWithResponse("GET","/api/hashtag",null,successForHashtag,failForHashtag);

    function successForCategory(response){
        const categoryNav =  document.getElementById("category_nav");
        let jsonData = response.data;
        jsonData.forEach(function(data){
            if(data.name!=="공지사항"){
                // 동적으로 li 요소 생성 및 설정
                let li = document.createElement("li");
                li.className = "nav-item category";
                li.setAttribute("data-user-id", data.categoryId);
                let paragraph = document.createElement("p");
                paragraph.textContent = data.name;
                li.addEventListener('click',function(){
                    if(document.getElementsByClassName("nav-item category notice").length===0){
                        li.className = "nav-item category notice";
                        searchCategory(data.categoryId);
                    }else{
                        li.className = "nav-item category";
                        roadAllArticle();
                    }
                })
                // li에 paragraph를 자식 요소로 추가
                li.appendChild(paragraph);
                categoryNav.appendChild(li);
            }
        });
    }
    function failForCategory(){
        alert("카테고리를 정상적으로 불러올 수 없습니다");
    }

    httpRequestWithResponse("GET","/api/category",null,successForCategory,failForCategory);

    roadAllArticle();

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
                '                        <p class="date">'+transDate(data.createdTime)+' '+data.nickname+'</p>' +
                '                        <img class="profile_img" src="'+data.profileImg+'">' +
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
    httpRequestWithResponse("GET","/api/articleList/"+path,null,success,fail);
}

function scrollToTop() {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
}

function roadAllArticle(){
    function successForArticle(response){
        let articleList = document.getElementById("article_List");
        articleList.innerHTML ="";
        let content = response.content;
        content.forEach(function(data){
            let div = document.createElement("div");
            div.className = "article";
            div.innerHTML = '<div class="author">' +
                '                        <p class="date">'+transDate(data.createdTime)+' '+data.nickname+'</p>' +
                '                        <img class="profile_img" src="'+data.profileImg+'">' +
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
    function failForArticle(){
        let h3 = document.createElement("h3");
        h3.innerText = "게시글이 존재하지 않습니다";
        document.getElementById("article_List").appendChild(h3);
    }
    httpRequestWithResponse("GET","/api/articleList",null,successForArticle,failForArticle)

}
