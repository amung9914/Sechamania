document.addEventListener('DOMContentLoaded',function (){


    const logout = document.getElementById("logout");
    if(logout){
        logout.addEventListener('click',function(){
            localStorage.removeItem("admin"); // 없으면 무시됨
            localStorage.removeItem("token"); // 없으면 무시됨
        })
    }

    // 메뉴 출력
    let isAdmin = localStorage.getItem("admin");
    if(isAdmin){
        const adminBtn = document.getElementById("admin");
        adminBtn.style.display = 'inline-block';
    }
    let isLogin = localStorage.getItem("token");
    if(isLogin){
        const afterLoginElements = document.querySelectorAll(".afterLogin");
        afterLoginElements.forEach(function(element){
            element.style.display = 'inline-block';
        });
        const beforeLoginElements = document.querySelectorAll(".beforeLogin");
        beforeLoginElements.forEach(function(element){
            element.style.display = 'none';
        });
    }

})

// HTTP GET 요청을 보내는 함수
function httpRequestGet(url,success,fail) {
    fetch(url, {
        method: "GET",
        headers: { // 로컬 스토리지에서 액세스 토큰 값을 가져와 헤더에 추가
            Authorization: 'Bearer ' + localStorage.getItem('access_token'),
            'Content-Type': 'application/json',
        }
    }).then(response => {
        if (response.status === 200 || response.status === 201) {
            return response.json();
        } else {
            fail();
        }
    }).then(data => {
        if (data) {
            success(data);
        }
    })
}

// HTTP요청을 보내는 함수
function httpRequest(method,url,body,success,fail){
    fetch(url, {
        method: method,
        headers: {
            'Content-Type':'application/json'
        },
        body: body,
    }).then(response =>{
        if(response.status ===200 || response.status ===201){
            return success();
        }else{
            fail();
        }

    });
}

/**
 * HTTP요청을 보내는 함수 && response 조작
 */
function httpRequestWithResponse(method,url,body,success,fail) {
    fetch(url, {
        method: method,
        headers: {
            'Content-Type':'application/json'
        },
        body: body,
    }).then(response => {
        if (response.status === 200 || response.status === 201) {
            return response.json();
        } else {
            fail();
        }
    }).then(data => {
        if (data) {
            success(data);
        }
    })
}

// HTTP요청을 보내는 함수(FormData)
function httpRequestForFormData(method,url,body,success,fail){
    fetch(url, {
        method: method,
        body: body,
    }).then(response =>{
        if(response.status ===200 || response.status ===201){
            return success();
        }else{
            fail();
        }

    });
}