/* URL 쿼리 문자열에 파라미터로 받은 토큰이 있다면 토큰을 로컬스토리지(브라우저)에 저장한다 */
const token = searchParam('token')
if(token){
    localStorage.setItem("access_token",token)
}
function searchParam(key){
    return new URLSearchParams(location.search).get(key);
}