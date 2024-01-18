document.addEventListener('DOMContentLoaded',function (){


    const logout = document.getElementById("logout");
    if(logout){
        logout.addEventListener('click',function(){
            localStorage.removeItem("admin"); // 없으면 무시됨
            localStorage.removeItem("access_token"); // 없으면 무시됨
        })
    }

    // 메뉴 출력
    let isAdmin = localStorage.getItem("admin");
    if(isAdmin){
        const adminBtn = document.getElementById("admin");
        adminBtn.style.display = 'inline-block';
    }
    let isLogin = localStorage.getItem("access_token");
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

    // chekcAuthority
    document.getElementById("checkAuthority").addEventListener('click',function (){
        function success(response){
            console.log(response);
        }
        httpRequestWtihTokenAndResponse("GET","/test/1",null,success,null);
    })

});

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


// HTTP요청을 보낸다.
function httpRequestWtihToken(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        headers: { // 로컬 스토리지에서 액세스 토큰 값을 가져와 헤더에 추가
            Authorization: 'Bearer ' + localStorage.getItem('access_token'),
            'Content-Type': 'application/json',
        },
        body: body,
    }).then(response => {
        if (response.status === 200 || response.status === 201) {
            return success();
        }
        const refresh_token = getCookie('refresh_token');
        if (response.status === 401 && refresh_token) {

            fetch('/api/token', {
                method: 'POST',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('access_token'),
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    refreshToken: getCookie('refresh_token'),
                }),
            })
                .then(res => {
                    if (res.ok) {
                        return res.json();
                    }
                })
                .then(result => { // 재발급이 성공하면 로컬 스토리지값을 새로운 액세스 토큰으로 교체
                    localStorage.setItem('access_token', result.accessToken);
                    httpRequest(method, url, body, success, fail);
                })
                .catch(error => fail());
        } else {
            return fail();
        }
    });
}

// HTTP요청을 보낸다.
function httpRequestWtihTokenAndResponse(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        headers: { // 로컬 스토리지에서 액세스 토큰 값을 가져와 헤더에 추가
            Authorization: 'Bearer ' + localStorage.getItem('access_token'),
            'Content-Type': 'application/json',
        },
        body: body,
    }).then(response => {
        if (response.status === 200 || response.status === 201) {
            return response.json();
        }
        const refresh_token = getCookie('refresh_token');
        if (response.status === 401 && refresh_token) {

            fetch('/api/token', {
                method: 'POST',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('access_token'),
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    refreshToken: getCookie('refresh_token'),
                }),
            })
                .then(res => {
                    if (res.ok) {
                        return res.json();
                    }
                })
                .then(result => { // 재발급이 성공하면 로컬 스토리지값을 새로운 액세스 토큰으로 교체
                    localStorage.setItem('access_token', result.accessToken);
                    httpRequest(method, url, body, success, fail);
                })
                .catch(error => fail());
        } else {
            return fail();
        }
    }).then(data => {
        if (data) {
            success(data);
        }
    });
}


//쿠키를 가져오는 함수
function getCookie(key){
    var result = null;
    var cookie = document.cookie.split(';');
    // some  함수로 배열의 각 요소(쿠키)에 대해 함수를 실행한다.
    cookie.some(function (item){
        item = item.replace(' ','');
        var dic = item.split("="); // key와 value 분리
        if(key === dic[0]){
            result = dic[1];
            return true;
        }
    });
    return result;
}

