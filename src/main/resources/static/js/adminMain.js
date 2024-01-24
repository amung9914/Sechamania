let isLogin = localStorage.getItem("admin");
if (!isLogin) {
    location.replace("/login");
}

document.addEventListener("DOMContentLoaded", function(){
    function success(response){
        if(response.data.length===0) {
            document.getElementById("inner_box").innerHTML =
                '<h4>로그</h4>' +
                '<p>최근3일 최신순</p>' +
                '<h5>유입 경로 데이터가 없습니다</h5>';
        }else{
            let tbody = document.getElementById("tbody");
            let jsonData = response.data;
            jsonData.forEach(function(data){
               let tr = document.createElement("tr");
               tr.innerHTML = '<td>'+data.path+'</td>' +
                   '                            <td>'+transDate(data.createdTime)+'</td>';
               tbody.appendChild(tr);
            });
        }
        console.log(response);
    }
    function fail(){
        console.log("data 로딩 실패");
    }
    httpRequestWtihTokenAndResponse("GET","/admin/referer",null,success,fail);
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