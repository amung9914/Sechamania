
// HTTP GET 요청을 보내는 함수
function httpRequestGet(url,success,fail) {
    fetch(url, {
        method: "GET",
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