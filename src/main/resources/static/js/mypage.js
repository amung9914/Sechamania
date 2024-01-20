let isLogin = localStorage.getItem("access_token");
if (!isLogin) {
    location.replace("/login");
}

document.addEventListener('DOMContentLoaded', function () {

    //기본정보 불러옴
    function success(response) {
        document.getElementById("profile_img").src = "/"+response.data.imgPath;
        document.getElementById("before_update_nickname").innerText = response.data.nickname;
        document.getElementById("nickname").value = response.data.nickname;
        document.getElementById("findAddr").value = response.data.fullAddress;
    }
    function fail() {
        alert("정보를 불러올 수 없습니다");
    }

    httpRequestGet("/updateMember", success, fail);

    /* 이미지 파일 변경 js START */
    document.getElementById("profileImage").addEventListener("change", function () {
        var files = this.files;
        var file = files[0];

        // 사용자 컴퓨터에서 사용자가 선택한 파일이 저장된 실제 위치 정보를 문자열로 반환
        var path = URL.createObjectURL(file);

        // 사용자 컴퓨터에 저장된 이미지 경로로 img 태그 소스 위치 정보를 수정
        document.getElementById("profile_img").src = path;
        document.getElementById("delete_img").style.display = 'block';
    });
    document.getElementById("delete_img").addEventListener('click', function () {
        document.getElementById("profile_img").src = "/img/defaultProfile.jpg";
        document.getElementById("profileImage").value = null;
        document.getElementById("delete_img").style.display = 'none';
    })
    /* 이미지 파일 변경 js END  */

    const openUpdate = document.getElementById("open_update_form");
    openUpdate.addEventListener('click', function () {
        document.getElementById("before_update_nickname").style.display = 'none';
        openUpdate.style.display = 'none';
        document.getElementById("update_form").style.display = 'inline-block';

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

        document.getElementById("update_form").addEventListener('submit',function(event){
            event.preventDefault();

            let formData = new FormData(document.getElementById("update_form"));
            formData.append("lat",lat);
            formData.append("lon",lon);
            formData.append("city",city);

            let body = formData;

            function success() {
                alert("정보 수정이 완료되었습니다.");
                location.replace("/view/mypage");
            }
            function fail() {
                alert("사용할 수 없는 닉네임입니다");
            }
            httpRequestForFormDataWtihToken("POST", "/updateMember", body, success, fail)

        })

        document.getElementById("cancel_update").addEventListener('click', function () {
            openUpdate.style.display = 'inline-block';
            document.getElementById("update_form").style.display = 'none';
        })

    }); // END openUpdate

    const openPassword = document.getElementById("open_password_form");
    openPassword.addEventListener('click', function () {
        openPassword.style.display = 'none';
        document.getElementById("password_form").style.display = 'inline-block';

        let boolPassword = false;
        // 특수문자 포함 비밀번호
        var regexPass = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,16}$/;
        const password = document.getElementById("new_password");
        const elpPw = document.getElementById("password_check");
        password.addEventListener("input", function (){
            let valP = this.value;
            let message = "비밀번호: 특수문자포함 영문/숫자 조합 8~16자리"
            boolPassword = checkRegex(elpPw,valP,regexPass,message);
        });

        document.getElementById("password_form").addEventListener('submit',function(event){
            event.preventDefault();

            let body = JSON.stringify({
                    "password": document.getElementById("password").value,
                    "newPassword": document.getElementById("new_password").value,
            });

            function success() {
                alert("암호 수정이 완료되었습니다.");
                location.replace("/view/mypage");
            }
            function fail() {
                alert("암호가 일치하지 않습니다.");
            }

            if(boolPassword){
                httpRequestWtihToken("POST", "/password", body, success, fail)
            }else{
                alert("비밀번호를 확인해주세요.");
                document.getElementById("password").focus();
            }


        })

        document.getElementById("cancel_update_pw").addEventListener('click', function () {
            openPassword.style.display = 'inline-block';
            document.getElementById("password_form").style.display = 'none';
        })

    }); // END openPassword

});


// 검증 여부에 따른 결과 메세지 출력
// (표시할요소, 메세지, 검사성공여부)
function showMessage(box,messageP,isCheck){
    if(isCheck){
        let html = '<span>' + messageP+'</span>';
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
