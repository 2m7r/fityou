// src/services/weatherService.js
import axios from "axios";

const WEATHER_API_URL = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";
const SERVICE_KEY = "j15gUMjkDR3ONh2c6OHuM9HXqkPhnEcUbaYGQ1caYTj3rf4AwOuJ6vPTaAZs6OYkXro2r17cXBPzAsCQ+qwvyw=="; // 기상청 API 서비스 키

export const fetchWeather = async (nx, ny, baseDate, baseTime) => {
    
  try {
    const response = await axios.get(WEATHER_API_URL, {
      params: {
        serviceKey: SERVICE_KEY,
        numOfRows: 10, // 충분한 데이터 수 요청
        pageNo: 1,
        dataType: "JSON",
        base_date: baseDate,
        base_time: baseTime,
        nx: nx,
        ny: ny,
      },
    });

    return response.data.response.body.items.item;
  } catch (error) {
    console.error("기상청 API 호출 실패:", error);
    return null;
  }
};
