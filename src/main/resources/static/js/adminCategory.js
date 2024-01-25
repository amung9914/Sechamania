let isLogin = localStorage.getItem("admin");
if (!isLogin) {
    location.replace("/login");
}

document.addEventListener("DOMContentLoaded", function(){
    function success(response){

        let tbody = document.getElementById("tbody");
        let jsonData = response.data;
        jsonData.forEach(function(data){
           let tr = document.createElement("tr");
           tr.id = data.categoryId;
           tr.setAttribute("data-name",data.name);
           tr.innerHTML = '<td class="category_name">'+data.name+'</td>' +
               '<td>' +
               '          <button type="button" class="btn btn-outline-dark btn-sm" onclick="openUpdate(this)">수정</button>' +
               '          <button type="button" class="btn btn-outline-dark btn-sm" onclick="openDelete(this)">삭제</button>' +
               '</td>';

           tbody.appendChild(tr);

        });

    }
    function fail(){
        console.log("data 로딩 실패");
    }
    httpRequestWtihTokenAndResponse("GET","/api/category",null,success,fail);

    document.getElementById("plus_btn").addEventListener('click',function(){
        if(document.getElementById("new_category")==null){
            let tbody = document.getElementById("tbody");
            let newTr = document.createElement("tr");
            newTr.innerHTML = '<td class="category_name"><input type="text" id="new_category" placeholder="추가하실 이름을 작성해주세요"></td>' +
                '<td>' +
                '          <button type="button" class="btn btn-outline-dark btn-sm" onclick="newCategory()">저장</button>' +
                '</td>';
            tbody.appendChild(newTr);
        }
    })
})

function openUpdate(element){
let tr = element.parentElement.parentElement;
let categoryId = tr.id;
let name = tr.getAttribute("data-name");

let td1 = document.createElement("td");
td1.className = "category_name";
td1.innerHTML = '<input type="text" id="new_name" value="'+name+'">';
let td2 = document.createElement("td");
td2.innerHTML = '<button type="button" class="btn btn-outline-dark btn-sm">저장</button>';
td2.addEventListener("click",function(){
    function success(){
        location.replace("/view/admin/category");
    }
    function fail() {
        alert("수정이 정상적으로 처리되지 않았습니다");
    }
    let body = JSON.stringify({
        "name" : document.getElementById("new_name").value,
        "categoryId" : categoryId
    })
    httpRequestWtihToken("POST","/admin/category",body,success,fail);
});
tr.innerHTML="";
tr.appendChild(td1);
tr.appendChild(td2);

}
function openDelete(element){
    let tr = element.parentElement.parentElement;
    let categoryId = tr.id;
    function success(){
        location.replace("/view/admin/category");
    }
    function fail() {
        alert("게시물이 존재하는 카테고리는 삭제할 수 없습니다");
    }
    let body = JSON.stringify({
        "categoryId" : categoryId
    })
    httpRequestWtihToken("DELETE","/admin/category",body,success,fail);
}

function newCategory(){
    console.log("클릭");
    function success(){
        location.replace("/view/admin/category");
    }
    function fail() {
        alert("카테고리 추가가 정상적으로 처리되지 않았습니다");
    }
    let body = JSON.stringify({
        "name":document.getElementById("new_category").value
    })

    httpRequestWtihToken("POST","/admin/saveCategory",body,success,fail);
}