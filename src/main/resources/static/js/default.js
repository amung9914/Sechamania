document.addEventListener('DOMContentLoaded', function () {


    document.getElementById("modal_input").addEventListener("keydown", function (event) {
        let key = event.key;
        if (key === 'Enter') {
            event.preventDefault();
            document.getElementById("model_btn").click();
        }
    })
    /* 검색 */
    document.getElementById("model_btn").addEventListener("click", function () {
        let searchText = document.getElementById("modal_input").value;
        location.replace("/view/search/" + searchText);
    })


    const logout = document.getElementById("logout");
    if (logout) {
        logout.addEventListener('click', function () {
            if (localStorage.getItem("access_token") != null) {
                let body = JSON.stringify({
                    "accessToken": localStorage.getItem("access_token")
                })

                function success() {
                    alert("토큰삭제완료");
                }

                httpRequestWtihToken("POST", "/api/delete/token", body, success, null);
            }
            localStorage.removeItem("admin"); // 없으면 무시됨
            localStorage.removeItem("access_token"); // 없으면 무시됨
        })
    }

    // 메뉴 출력
    let isAdmin = localStorage.getItem("admin");
    if (isAdmin) {
        const adminBtn = document.getElementById("admin");
        adminBtn.style.display = 'inline-block';
    }
    let isLogin = localStorage.getItem("access_token");
    if (isLogin) {
        const afterLoginElements = document.querySelectorAll(".afterLogin");
        afterLoginElements.forEach(function (element) {
            element.style.display = 'inline-block';
        });
        const beforeLoginElements = document.querySelectorAll(".beforeLogin");
        beforeLoginElements.forEach(function (element) {
            element.style.display = 'none';
        });
    }

});

// HTTP GET 요청을 보내는 함수 + Token
function httpRequestGet(url, success, fail) {
    fetch(url, {
        method: "GET",
        headers: { // 로컬 스토리지에서 액세스 토큰 값을 가져와 헤더에 추가
            Authorization: 'Bearer ' + localStorage.getItem('access_token'),
            'Content-Type': 'application/json',
        }
    }).then(response => {
        if (response.status === 200 || response.status === 201) {
            return response.json();
        }
        if (response.status === 401) {
            fetch('/api/token', {
                method: 'POST',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('access_token'),
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    accessToken: localStorage.getItem('access_token'),
                }),
            })
                .then(res => {
                    if (res.ok) {
                        return res.json();
                    }
                })
                .then(result => { // 재발급이 성공하면 로컬 스토리지값을 새로운 액세스 토큰으로 교체
                    localStorage.setItem('access_token', result.accessToken);
                    httpRequestGet(url, success, fail);
                })
                .catch(error => fail());
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
function httpRequest(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: body,
    }).then(response => {
        if (response.status === 200 || response.status === 201) {
            return success();
        } else {
            fail();
        }

    });
}

/**
 * HTTP요청을 보내는 함수 && response 조작
 */
function httpRequestWithResponse(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
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
function httpRequestForFormData(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        body: body,
    }).then(response => {
        if (response.status === 200 || response.status === 201) {
            return success();
        } else {
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
        if (response.status === 401) {
            fetch('/api/token', {
                method: 'POST',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('access_token'),
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    accessToken: localStorage.getItem('access_token'),
                }),
            })
                .then(res => {
                    if (res.ok) {
                        return res.json();
                    }
                })
                .then(result => { // 재발급이 성공하면 로컬 스토리지값을 새로운 액세스 토큰으로 교체
                    localStorage.setItem('access_token', result.accessToken);
                    httpRequestWtihTokenAndResponse(method, url, body, success, fail);
                })
                .catch(error => fail());
        } else {
            return fail();
        }
    });
}


// HTTP요청을 보낸다.
function httpRequestForFormDataWtihToken(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        headers: { // 로컬 스토리지에서 액세스 토큰 값을 가져와 헤더에 추가
            Authorization: 'Bearer ' + localStorage.getItem('access_token'),
        },
        body: body,
    }).then(response => {
        if (response.status === 200 || response.status === 201) {
            return success();
        }
        if (response.status === 401) {
            fetch('/api/token', {
                method: 'POST',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('access_token'),
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    accessToken: localStorage.getItem('access_token'),
                }),
            })
                .then(res => {
                    if (res.ok) {
                        return res.json();
                    }
                })
                .then(result => { // 재발급이 성공하면 로컬 스토리지값을 새로운 액세스 토큰으로 교체
                    localStorage.setItem('access_token', result.accessToken);
                    httpRequestWtihTokenAndResponse(method, url, body, success, fail);
                })
                .catch(error => fail());
        } else {
            return fail();
        }
    });
}

function httpRequestForFormDataWithTokenAndResponse(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        headers: { // 로컬 스토리지에서 액세스 토큰 값을 가져와 헤더에 추가
            Authorization: 'Bearer ' + localStorage.getItem('access_token'),
        },
        body: body,
    }).then(response => {
        if (response.status === 200 || response.status === 201) {
            return response.json();
        }
        if (response.status === 401) {
            fetch('/api/token', {
                method: 'POST',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('access_token'),
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    accessToken: localStorage.getItem('access_token'),
                }),
            })
                .then(res => {
                    if (res.ok) {
                        return res.json();
                    }
                })
                .then(result => { // 재발급이 성공하면 로컬 스토리지값을 새로운 액세스 토큰으로 교체
                    localStorage.setItem('access_token', result.accessToken);
                    httpRequestWtihTokenAndResponse(method, url, body, success, fail);
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
        if (response.status === 401) {
            fetch('/api/token', {
                method: 'POST',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('access_token'),
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    accessToken: localStorage.getItem('access_token'),
                }),
            })
                .then(res => {
                    if (res.ok) {
                        return res.json();
                    }
                })
                .then(result => { // 재발급이 성공하면 로컬 스토리지값을 새로운 액세스 토큰으로 교체
                    localStorage.setItem('access_token', result.accessToken);
                    httpRequestWtihTokenAndResponse(method, url, body, success, fail);
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
function getCookie(key) {
    var result = null;
    var cookie = document.cookie.split(';');
    // some  함수로 배열의 각 요소(쿠키)에 대해 함수를 실행한다.
    cookie.some(function (item) {
        item = item.replace(' ', '');
        var dic = item.split("="); // key와 value 분리
        if (key === dic[0]) {
            result = dic[1];
            return true;
        }
    });
    return result;
}
