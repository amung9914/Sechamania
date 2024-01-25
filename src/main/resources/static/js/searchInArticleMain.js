
function searchHashtag(hashtagId){
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
        if(response.first!==true){
            newHTML +=
                '<li class="page-item">' +
                '                                <a class="page-link" onclick="callPageForHashtag('+before+',hashtagId)" aria-label="Previous">' +
                '                                    <span aria-hidden="true">&laquo;</span>' +
                '                                </a>' +
                '                            </li>';

        }
        for (let i = startRange; i <= endRange; i++) {
            if(i===currentPage){
                newHTML +=  '<li class="page-item"><a class="page-link notice" onclick="callPageForHashtag('+i+',hashtagId)">'+i+'</a></li>';
            }else if(i>=response.totalPages){
                break;
            }else{
                newHTML +=  '<li class="page-item"><a class="page-link" onclick="callPageForHashtag('+i+',hashtagId)">'+i+'</a></li>';
            }
        }
        let next = endRange + 1;
        if(response.last!==true){
            newHTML +=
                '                            <li class="page-item">' +
                '                                <a class="page-link" onclick="callPageForHashtag('+next+',hashtagId)" aria-label="Next">' +
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
    let body = JSON.stringify({
        "hashtagId" : hashtagId
    })
    httpRequestWithResponse("POST","/api/articleListWithHashtag",body,success,fail);
}


function callPageForHashtag(page,hashtagId){
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
        if(response.first!==true){
            newHTML +=
                '<li class="page-item">' +
                '                                <a class="page-link" onclick="callPageForHashtag('+before+',hashtagId)" aria-label="Previous">' +
                '                                    <span aria-hidden="true">&laquo;</span>' +
                '                                </a>' +
                '                            </li>';

        }
        for (let i = startRange; i <= endRange; i++) {
            if(i===currentPage){
                newHTML +=  '<li class="page-item"><a class="page-link notice" onclick="callPageForHashtag('+i+',hashtagId)">'+i+'</a></li>';
            }else if(i>=response.totalPages){
                break;
            }else{
                newHTML +=  '<li class="page-item"><a class="page-link" onclick="callPageForHashtag('+i+',hashtagId)">'+i+'</a></li>';
            }
        }
        let next = endRange + 1;
        if(response.last!==true){
            newHTML +=
                '                            <li class="page-item">' +
                '                                <a class="page-link" onclick="callPageForHashtag('+next+',hashtagId)" aria-label="Next">' +
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

    let body = JSON.stringify({
        "hashtagId":hashtagId,
        "page":path
    })
    httpRequestWithResponse("POST","/api/articleListWithHashtag",body,success,fail);
}


function searchCategory(categoryId){
    function success(response){
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
        if(response.first!==true){
            newHTML +=
                '<li class="page-item">' +
                '                                <a class="page-link" onclick="callPageForCategory('+before+',categoryId)" aria-label="Previous">' +
                '                                    <span aria-hidden="true">&laquo;</span>' +
                '                                </a>' +
                '                            </li>';

        }
        for (let i = startRange; i <= endRange; i++) {
            if(i===currentPage){
                newHTML +=  '<li class="page-item"><a class="page-link notice" onclick="callPageForCategory('+i+',categoryId)">'+i+'</a></li>';
            }else if(i>=response.totalPages){
                break;
            }else{
                newHTML +=  '<li class="page-item"><a class="page-link" onclick="callPageForCategory('+i+',categoryId)">'+i+'</a></li>';
            }
        }
        let next = endRange + 1;
        if(response.last!==true){
            newHTML +=
                '                            <li class="page-item">' +
                '                                <a class="page-link" onclick="callPageForCategory('+next+',categoryId)" aria-label="Next">' +
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
    let body = JSON.stringify({
        "categoryId" : categoryId
    })
    httpRequestWithResponse("POST","/api/articleListWithCategory",body,success,fail);
}


function callPageForCategory(page,categoryId){
    let path = page-1;
    function success(response){
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
        if(response.first!==true){
            newHTML +=
                '<li class="page-item">' +
                '                                <a class="page-link" onclick="callPageForCategory('+before+',categoryId)" aria-label="Previous">' +
                '                                    <span aria-hidden="true">&laquo;</span>' +
                '                                </a>' +
                '                            </li>';

        }
        for (let i = startRange; i <= endRange; i++) {
            if(i===currentPage){
                newHTML +=  '<li class="page-item"><a class="page-link notice" onclick="callPageForCategory('+i+',categoryId)">'+i+'</a></li>';
            }else if(i>=response.totalPages){
                break;
            }else{
                newHTML +=  '<li class="page-item"><a class="page-link" onclick="callPageForCategory('+i+',categoryId)">'+i+'</a></li>';
            }
        }
        let next = endRange + 1;
        if(response.last!==true){
            newHTML +=
                '                            <li class="page-item">' +
                '                                <a class="page-link" onclick="callPageForCategory('+next+',categoryId)" aria-label="Next">' +
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

    let body = JSON.stringify({
        "categoryId":categoryId,
        "page":path
    })
    httpRequestWithResponse("POST","/api/articleListWithCategory",body,success,fail);
}