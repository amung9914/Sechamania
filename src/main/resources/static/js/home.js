document.addEventListener('DOMContentLoaded',function (){

    let lat = null;
    let lon = null;
    let isLogin = localStorage.getItem("access_token");
    // 로그인 된 상태
    if(isLogin){
        let API_KEY = document.getElementById("weather_key").value;
        function success(response){
            document.getElementById("city").innerText = response.data.city;
            lat = response.data.lat;
            lon = response.data.lon;
            let grid = dfs_xy_conv("toXY",lat,lon);

            getWeather(grid.x,grid.y);
        }

        function fail() {
            alert("회원정보를 불러올 수 없습니다");
        }
        httpRequestWtihTokenAndResponse("GET","/memberInfo",null,success,fail);

        /* 기상청 날씨 api START */
        /*
        *  날짜 변수
        */
        let now = new Date();

        let year = now.getFullYear();
        let month = ('0' + (now.getMonth() + 1)).slice(-2);
        let day = ('0' + now.getDate()).slice(-2);

        /*
        * 시간 변수
        */
        let hours = now.getHours();
        let minutes = ('0' + now.getMinutes()).slice(-2);
        let isYesterday = false;

        /*
        * - Base_time : 0200, 0500, 0800, 1100, 1400, 1700, 2000, 2300 (1일 8회)
        * - API 제공 시간(~이후) : 02:10, 05:10, 08:10, 11:10, 14:10, 17:10, 20:10, 23:10
        */
        const baseTimeArr = [2,5,8,11,14,17,20,23];

        function time(){
            // 2시 이전인 경우 어제 23시로 호출함
            if(hours<baseTimeArr[0]){
                isYesterday = true;
                return '2300';
            }
            for (let i = 1; i < baseTimeArr.length; i++) {
                if(hours < baseTimeArr[i]){
                    return ('0' + baseTimeArr[i-1]).slice(-2)+'00';
                }else if(hours === baseTimeArr[i] && minutes > '10'){
                    return ('0' + baseTimeArr[i]).slice(-2)+'00';
                }
            }
        }

        function calcDateString(isYesterday){
            if(isYesterday){
                let yesterday = new Date(now);
                yesterday.setDate(now.getDate()-1);
                day = ('0' + yesterday.getDate()).slice(-2);
                return year+ month + day;
            }else{
                return year+ month + day;
            }
        }

        const getWeather = (gridX,gridY) => {
            let url = new URL("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst");
            let params = {
                serviceKey: `${API_KEY}`,
                pageNo: '1',
                numOfRows: '112',
                dataType: 'JSON',
                base_time: time(),
                base_date: calcDateString(isYesterday),
                nx: gridX,
                ny: gridY
            };
            url.search = new URLSearchParams(params).toString();
            fetch(decodeURI(url))
                .then(res => {
                    return res.json();
                })
                .then(data => {
                   weather_data(data);
                }).catch(err => {
                // error 처리
                console.log('Fetch Error', err);
            });
        };
        /* 기상청 날씨 api END */


    } // 로그인된 상태 END


});