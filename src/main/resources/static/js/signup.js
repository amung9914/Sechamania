document.addEventListener('DOMContentLoaded', function() {

    document.getElementById("profileImage").addEventListener("change", function () {
        var files = this.files;
        console.log(files);
        var file = files[0];

        // 사용자 컴퓨터에서 사용자가 선택한 파일이 저장된 실제 위치 정보를 문자열로 반환
        var path = URL.createObjectURL(file);

        // 사용자 컴퓨터에 저장된 이미지 경로로 img 태그 소스 위치 정보를 수정
        document.getElementById("profile_img").src = path;
        document.getElementById("delete_img").style.display = 'block';
    });

    document.getElementById("delete_img").addEventListener('click',function(){
        document.getElementById("profile_img").src = "/img/defaultProfile.jpg";
        document.getElementById("profileImage").value = null;
        document.getElementById("delete_img").style.display = 'none';
    })

    /* 회원가입 검증 시작*/

    let validEmailForRegex = false; // 정규식 통과 여부
    let validEmail = false; // 중복검증 통과 여부
    // 이메일 정규 표현식
    const regexEmail =/^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/;
    const elp = document.getElementById("email_check");
    const email = document.getElementById("email");

    email.addEventListener("input",function(){
        let message = "올바른 이메일 주소를 입력해주세요";
        validEmailForRegex = checkRegex(elp,email.value,regexEmail,message);
        if(validEmailForRegex){
            // 아이디 중복검사 결과 요청
            function success(){
                showMessage(elp,"이메일: 사용할 수 있는 이메일입니다",true);
                validEmail = true;
                document.getElementById("mail_btn").disabled = false;
            }
            function fail(){
                showMessage(elp,"이메일: 사용할 수 없는 이메일입니다",false);
                document.getElementById("mail_btn").disabled = true;
                validEmail = false;
            }

            httpRequestGet("/signup/email/"+email.value,success,fail);
        }
    });

    document.getElementById("mail_btn").addEventListener('click',function(){
        document.getElementById("email_code_line").style.display='flex';
    })

    let boolPassword = false;
    // 특수문자 포함 비밀번호
    var regexPass = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,16}$/;
    const password = document.getElementById("password");
    const elpPw = document.getElementById("password_check");
    password.addEventListener("input", function (){
        let valP = this.value;
        let message = "비밀번호: 특수문자포함 영문/숫자 조합 8~16자리"
        boolPassword = checkRegex(elpPw,valP,regexPass,message);
    });

    let boolNickname = false; // 닉네임 검증
    const elpNickname = document.getElementById("nickname_check");
    const nickname = document.getElementById("nickname");
    nickname.addEventListener("input",function(){
        // 닉네임 중복검사 결과 요청
        function success(){
            showMessage(elpNickname,"사용할 수 있는 닉네임입니다",true);
            boolNickname = true;
            document.getElementById("mail_btn").disabled = false;
        }
        function fail(){
            showMessage(elpNickname,"사용중인 닉네임입니다",false);
            document.getElementById("mail_btn").disabled = true;
            boolNickname = false;
        }
        httpRequestGet("/signup/nickname/"+nickname.value,success,fail);
    });

    /* 이메일 인증 */
    let confirmCode = null;
    const mailBtn = document.getElementById("mail_btn");
    mailBtn.addEventListener("click",function (){
        if(validEmailForRegex){
            data = JSON.stringify({
                "mail" : document.getElementById("email").value
            });
            function success(response){
                alert("인증번호를 발송하였습니다");
                confirmCode = response.data;
            }
            function fail(){
                alert("메일전송실패");
            }
            httpRequestWithResponse("POST","/mail",data,success,fail);
        }
    });

    //이메일 검증
    let boolEmailCode = false;
    document.getElementById("emailAcceptBtn").addEventListener("click", function(){
        let userCode = document.getElementById("emailCode").value;
        if(confirmCode == userCode){
            alert("이메일 인증이 완료되었습니다.")
            boolEmailCode = true;
            document.getElementById("mail_btn").disabled = true;
        }else{
            boolEmailCode = false;
            alert("인증코드를 다시 확인해주세요.");
        }

    });
    /* 이메일 인증 END */

    /**
     * 우편번호 서비스
     */
    let lat = null;
    let lon = null;
    let city = null;
    document.getElementById("daumPostBtn").addEventListener('click',function(){
        daumPostcode();
    })
    function daumPostcode() {
        //주소-좌표 변환 객체를 생성
        var geocoder = new daum.maps.services.Geocoder();

        new daum.Postcode({
            oncomplete: function(data) {
                var addr = data.address; // 최종 주소 변수
                city = data.sigungu;

                // 주소 정보를 해당 필드에 넣는다.
                document.getElementById("findAddr").value = addr;
                // 주소로 상세 정보를 검색
                geocoder.addressSearch(data.address, function(results, status) {
                    // 정상적으로 검색이 완료됐으면
                    if (status === daum.maps.services.Status.OK) {

                        var result = results[0]; //첫번째 결과의 값을 활용

                        // 해당 주소에 대한 좌표를 받아서 좌표로 변환한다
                        var coords = new daum.maps.LatLng(result.y, result.x);
                        lat = coords.getLat();
                        lon = coords.getLng();

                    }
                });
            }
        }).open();
    }





    /* 회원 가입 */
    const joinForm = document.getElementById('join_form');
    if(joinForm){
        joinForm.addEventListener('submit',function (event) {
            event.preventDefault();

            let formData = new FormData(joinForm);
            formData.append("lat",lat);
            formData.append("lon",lon);
            formData.append("city",city);

            const body = formData;

            function success() {
                alert("회원 가입이 완료되었습니다.");
                location.replace("/");
            }

            function fail() {
                alert("회원 가입이 실패했습니다.");
                location.replace("/signup");
            }

            if (!validEmail) {
                // 이메일 유효성 검사 완료 여부
                alert("이메일을 확인해주세요.");
                document.getElementById("memberName").focus();
            } else if(!boolEmailCode){
                alert("이메일 인증을 완료해주세요.");
                document.getElementById("memberName").focus();
            } else if (!boolPassword) {
                alert("비밀번호를 확인해주세요.");
                document.getElementById("password").focus();
            } else if (!boolNickname) {
                alert("닉네임을 확인해주세요.");
                document.getElementById("nickname").focus();
            }else if (lat === null) {
                alert("주소를 확인해주세요.");
                document.getElementById("findAddr").focus();
            }else {
                httpRequestForFormData("POST", "signup", body, success, fail);
            }
        });
    } // joinForm end






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

