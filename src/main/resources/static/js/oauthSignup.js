document.addEventListener('DOMContentLoaded', function() {

    document.getElementById("profileImage").addEventListener("change", function () {
        var files = this.files;
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

            if (!boolNickname) {
                alert("닉네임을 확인해주세요.");
                document.getElementById("nickname").focus();
            }else if (lat === null) {
                alert("주소를 확인해주세요.");
                document.getElementById("findAddr").focus();
            }else {
                httpRequestForFormDataWtihToken("POST", "/oauthSignup", body, success, fail);
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

