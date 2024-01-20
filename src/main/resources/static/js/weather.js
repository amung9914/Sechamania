/**
 * 기준시간 기준 10시간 이후에 data까지 확인 가능
 *  10시간 이후 data로 forecate 출력
 */
function calcTime(time_weather,state){
    if ('06' < time_weather && time_weather < '12') {
        return '내일 오전 : '+state;
    }else if('12' <= time_weather && time_weather < '20'){
        return '오늘 오후 : '+state;
    }else{
        return '오늘 밤 : '+state;
    }
}

/**
 * SKY: 하늘 상태 맑음(1), 구름많음(3), 흐림(4)
 * PTY:강수형태 없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4)
 */
function weather_data(data){
    console.log(data);
    const currentSKY = data.response.body.items.item[5].fcstValue;
    const currentPTY = data.response.body.items.item[6].fcstValue;
    document.getElementById("sky").innerText = make_weather(currentSKY,currentPTY);
    const currentTMP = data.response.body.items.item[0].fcstValue;
    document.getElementById("temp").innerText = '현재 기온 '+currentTMP+'°C';
    let after10HourSKY = null;
    let after10HourPTY = null;

    if(data.response.body.items.item[102].category==='SKY'){
        after10HourSKY = data.response.body.items.item[102];
        after10HourPTY = data.response.body.items.item[103];
    }else{
        after10HourSKY = data.response.body.items.item[101];
        after10HourPTY = data.response.body.items.item[102];
    }

    const time_weather = after10HourSKY.fcstTime.slice(0, -2);

    document.getElementById("forecast").innerText =
        calcTime(time_weather,make_forecastWeather(after10HourSKY.fcstValue,after10HourPTY.fcstValue));
    document.getElementById("summary").innerText =
        make_summary(currentTMP,currentPTY,after10HourPTY.fcstValue);
}

function make_weather(sky,pty){
    const weatherImg = document.getElementById("weather_img");
    switch(pty){
        case '0':
            if(sky==='1'){
                return '맑음';
            }else if(sky==='3'){
                weatherImg.src = '/img/clouds.jpg';
                return '구름많음';
            }else{
                weatherImg.src = '/img/brokenClouds.jpg';
                return '흐림';
            }
        case '1':
            weatherImg.src = '/img/rain.jpg';
            return '비';
        case '2':
            weatherImg.src = '/img/rain.jpg';
            return '비/눈';
        case '3':
            weatherImg.src = '/img/snow.jpg';
            return '눈';
        case '4':
            weatherImg.src = '/img/rain.jpg';
            return '소나기';
    }
}

function make_forecastWeather(sky,pty){
    switch(pty){
        case '0':
            if(sky==='1'){
                return '맑음';
            }else if(sky==='3'){
                return '구름많음';
            }else{
                return '흐림';
            }
        case '1':
            return '비';
        case '2':
            return '비/눈';
        case '3':
            return '눈';
        case '4':
            return '소나기';
    }
}

// 강수 예보로 1차 판단 (PTY : 강수형태)
// 맑은 날씨일 경우
// 현재 기온 기준으로 차가 어는지 안어는지 판단
function make_summary(currentTMP,pty,afterPty){
    switch(pty){
        case '0':
            if(currentTMP >'1'&& afterPty==='0'){
                return '오늘은 세차하기 좋은 날이에요';
            }else if(currentTMP <='1'){
                return '오늘은 차가 얼어버릴 수 있어요';
            }else{
                return '세차는 다음에 하는 게 어떨까요?'
            }
        case '1':
            return '오늘은 자연세차의 날';
        case '2':
            return '세차는 다음 기회에';
        case '3':
            return '세차는 다음 기회에';
        case '4':
            return '오늘은 자연세차의 날';
    }
}