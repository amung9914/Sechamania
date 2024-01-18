document.addEventListener('DOMContentLoaded',function (){


    const weatherImg = document.getElementById("weather_img");
    let city = null;
    let lat = null;
    let lon = null;
    let isLogin = localStorage.getItem("access_token");
    // 로그인 된 상태
    if(isLogin){
        let API_KEY = document.getElementById("openweather_key").value;
        function success(response){
            city = response.data.city;
            lat = response.data.lat;
            lon = response.data.lon;
            getWeather(lat,lon);
        }

        function fail() {
            alert("회원정보를 불러올 수 없습니다");
        }
        httpRequestWtihTokenAndResponse("GET","/memberInfo",null,success,fail);

        const getWeather = (lat, lon) => {
            fetch(`https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&appid=${API_KEY}&units=metric&lang=kr`)
                .then((response)=>{
                    return response.json();
                })
                .then((data) =>{
                    console.log(data);
                    console.log("날씨는 ",data["weather"][0]["description"],"입니다.");
                    console.log(("현재 온도는 ",data["main"]["temp"]));
                    console.log("최저 기온은 ",data["main"]["temp_min"],"입니다.");
                    console.log("최고 기온은 ",data["main"]["temp_max"],"입니다.");
                })


        }






    } // if weatherImg END


});