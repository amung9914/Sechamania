document.addEventListener('DOMContentLoaded', function() {

    let validEmailForRegex = false;

    // 이메일 정규 표현식
    const regexEmail =/^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/;
    const elp = document.getElementById("email_check");
    const email = document.getElementById("email");

    email.addEventListener("input",function(){
        let message = "올바른 이메일 주소를 입력해주세요";
        validEmailForRegex = checkRegex(elp,email.value,regexEmail,message);
    })

    // 로그인 요청
    const login_form = document.getElementById("login_form");
    login_form.addEventListener("submit",function(event){
        event.preventDefault();

        if(validEmailForRegex){
            function success(response){
                const tokenFromHeader = response.headers.get('token');
                if(tokenFromHeader){
                    localStorage.setItem('token',tokenFromHeader);
                }
                const adminHeader = response.headers.get('admin');
                if(adminHeader){
                    localStorage.setItem('admin',adminHeader);
                }
                location.replace("/");
            }

            fetch("/login", {
                method: "POST",
                headers: {
                    'Content-Type':'application/json'
                },
                body: JSON.stringify({
                    "email" : email.value,
                    "password" : document.getElementById("password").value
                }),
            }).then(response =>{
                if(response.status ===200 || response.status ===201){
                    return success(response);
                }else{
                    location.replace("/login/error");
                }

            });
        }
    });


});


// 검증 여부에 따른 결과 메세지 출력
// (표시할요소, 메세지, 검사성공여부)
function showMessage(box,messageP,isCheck){

    if(isCheck){
        let html = '<br>';
        box.innerHTML = html;
    }else{
        let html = '<span class="notice">' + messageP+'</span>';
        box.innerHTML = html;
    }

}

// 정규 표현식 검사
// checkRegex(메세지를 출력할 요소,검사할 값,비교할 정규표현식,출력할 메세지)
function checkRegex(elP,valP,regexP,messageP){
    // 정규표현식 패턴과 사용자가 작성한 내용의 패턴이 일치하지 않음
    if(regexP.test(valP) === false){
        showMessage(elP,messageP,false);
        return false;
    }else if(regexP.test(valP) !== false){
        showMessage(elP,messageP,true);
        return true;
    }
}