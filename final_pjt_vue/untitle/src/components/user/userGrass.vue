잔디

<template>
  <div class="calendar">
    <div class="calendar-header">
      <h4 class="month-title">{{ currentMonth }}월의 기록</h4>
    </div>
    <div class="calendar-body">
      <div class="calendar-grid">
        <div v-for="(date, index) in calendarDates" :key="index" class="calendar-date">
          <div v-if="date !== ''" :class="['calendar-date-item', getDayStatus(date)]" @click="onDateClick(date)">
            {{ date }}
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="calendar-footer">
    <div class="legend">
      <div class="legend-item">
        <span class="legend-circle diet"></span>
        식단일기
      </div>
      <div class="legend-item">
        <span class="legend-circle workout"></span>
        운동일기
      </div>
      <div class="legend-item">
        <span class="legend-circle both"></span>
        둘 다
      </div>
    </div>
  </div>
</template>

<script setup>
import apiClient from "../api/apiClient"; // apiClient import
import { ref, onMounted } from "vue";
import { defineEmits } from 'vue';

const emit = defineEmits(); // 부모에게 이벤트를 전달

// 상태 변수들
const currentMonth = ref(""); // 현재 월
const currentYear = ref(""); // 현재 연도
const calendarDates = ref([]); // 날짜들 (월별 날짜들)
const workoutData = ref([]); // 운동일기 작성된 날짜
const dietData = ref([]); // 식단일기 작성된 날짜

// sessionStorage에서 'user' 키에 저장된 유저 데이터를 가져옵니다.
const userData = sessionStorage.getItem("user");
const userId = ref(userData ? JSON.parse(userData).userId : null); // 유저 ID를 가져옵니다.

// 날짜 클릭 시 해당 날짜를 부모에게 전달
const onDateClick = (date) => {
  const formattedDate = `${currentYear.value}-${currentMonth.value}-${date}`;
  emit("dateSelected", formattedDate); // 부모에게 선택된 날짜 전달
};

const workoutLogsForSelectedDate = ref([]);  // 선택된 날짜의 운동일기 저장
const dietLogsForSelectedDate = ref([]);     // 선택된 날짜의 식단일기 저장

// 선택한 날짜의 운동일기 불러오기
const getWorkoutLogsForDate = async (date) => {
  try {
    const response = await apiClient.get(`/api-workout/feed/${userId.value}`, {
      params: { date: date } // 날짜를 쿼리 파라미터로 전달
    });
    workoutLogsForSelectedDate.value = response.data;
    console.log("Selected Workout Logs:", workoutLogsForSelectedDate.value);
  } catch (error) {
    console.error("운동일기 가져오기 실패", error);
  }
};

// 선택한 날짜의 식단일기 불러오기
const getDietLogsForDate = async (date) => {
  try {
    const response = await apiClient.get('/api-diet/date', {
      params: {
         date: date,
         userId: userId.value
        }
    });
    dietLogsForSelectedDate.value = response.data;
    console.log("Selected Diet Logs:", dietLogsForSelectedDate.value);
  } catch (error) {
    console.error("식단일기 가져오기 실패", error);
  }
};

// API로부터 운동일기 날짜 데이터 가져오기
const getWorkoutDates = async () => {
  if (userId.value) {
    try {
      const response = await apiClient.get(`/api-workout/feed/${userId.value}`);
      workoutData.value = response.data.map((item) => item.recordDate);
      console.log("Workout Dates:", workoutData.value); // workoutData 확인
    } catch (error) {
      console.error("운동일기 데이터 가져오기 실패", error);
    }
  }
};

// API로부터 식단일기 날짜 데이터 가져오기
const getDietDates = async () => {
  if (userId.value) {
    try {
      const response = await apiClient.get(`/api-diet/feed/${userId.value}`);
      dietData.value = response.data.map((item) => item.recordDate);
      console.log("Diet Dates:", dietData.value); // dietData 확인
    } catch (error) {
      console.error("식단일기 데이터 가져오기 실패", error);
    }
  }
};

onMounted(() => {
  if (userId.value) {
    getWorkoutDates(); // 유저 ID가 있을 경우 운동일기 날짜 데이터 가져오기
    getDietDates(); // 유저 ID가 있을 경우 식단일기 날짜 데이터 가져오기
  } else {
    console.log("유저 ID가 없습니다. 로그인 페이지로 이동합니다.");
    // 로그인 페이지로 이동
    window.location.href = "/login";
  }
  getCurrentMonthDates(); // 현재 월의 날짜를 계산하여 캘린더에 표시
});

// 날짜 비교 형식을 맞추기 위한 함수
// 날짜 포맷 변환 함수
const formatDate = (date) => {
  const d = new Date(date);
  const year = d.getFullYear();
  const month = (d.getMonth() + 1).toString().padStart(2, "0"); // 월을 2자리로
  const day = d.getDate().toString().padStart(2, "0"); // 일을 2자리로
  return `${year}-${month}-${day}`;
};

// 날짜 비교 함수
const getDayStatus = (date) => {
  const year = currentYear.value;
  const month =
    currentMonth.value < 10 ? `0${currentMonth.value}` : currentMonth.value; // 월을 2자리로 맞추기
  const formattedDate = `${year}-${month}-${date < 10 ? `0${date}` : date}`; // 날짜 형식: "YYYY-MM-DD"

  // API에서 가져온 날짜 배열
  const workoutDates = workoutData.value ? workoutData.value : [];
  const dietDates = dietData.value ? dietData.value : [];

  // 운동일기와 식단일기 날짜 비교
  const workout = workoutDates.some((d) => d === formattedDate); // 운동일기 날짜 비교
  const diet = dietDates.some((d) => d === formattedDate); // 식단일기 날짜 비교

  // console.log(formattedDate + " 운동: " + workout);  // 디버그 출력
  // console.log(formattedDate + " 식단: " + diet);    // 디버그 출력

  // 상태 반환
  if (workout && diet) return "both"; // 운동과 식단일기 모두 작성된 경우
  if (workout) return "workout"; // 운동일기만 작성된 경우
  if (diet) return "diet"; // 식단일기만 작성된 경우
  return ""; // 두 개의 일기가 모두 없으면 빈 문자열
};

// 현재 월의 날짜들을 생성하는 함수
const getCurrentMonthDates = () => {
  const date = new Date();
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, "0"); // 월을 2자리로 처리 (1~12)
  const firstDay = new Date(year, date.getMonth(), 1); // 첫날
  const lastDay = new Date(year, date.getMonth() + 1, 0); // 마지막 날

  const daysInMonth = lastDay.getDate();
  const firstDayOfWeek = firstDay.getDay(); // 첫날의 요일

  const dates = [];
  // 첫날 전의 빈 날짜 처리
  for (let i = 0; i < firstDayOfWeek; i++) {
    dates.push(""); // 빈 날짜는 문자열로 추가
  }

  // 실제 날짜 추가
  for (let i = 1; i <= daysInMonth; i++) {
    dates.push(i); // 1일부터 말일까지 날짜 추가
  }

  calendarDates.value = dates;
  currentMonth.value = month; // `currentMonth`를 2자리 숫자로 설정
  currentYear.value = year;
};
</script>

<style scoped>
.calendar-footer {
  margin-top: 20px;
  font-size: 14px;
}

.legend {
  display: flex;
  justify-content: space-around;
  align-items: center;
}

.legend-item {
  display: flex;
  align-items: center;
}

.legend-circle {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  margin-right: 5px;
}

.legend-circle.diet {
  background-color: #b8d69f; /* 식단일기 색상 */
}

.legend-circle.workout {
  background-color: #98cfba; /* 운동일기 색상 */
}

.legend-circle.both {
  background-color: #519472; /* 운동과 식단 둘 다 작성한 경우 */
}

.calendar {
  display: flex;
  flex-direction: column;
  padding: 0 10px; /* 왼쪽, 오른쪽 여백 */
  max-width: 500px; /* 최대 너비 500px */
}

.calendar-header {
  font-size: 20px;
  font-weight: bold !important; /* 글씨 두껍게 */
  margin-bottom: 10px;
}

.calendar-body {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr); /* 7개의 열 */
  grid-auto-rows: 1fr; /* 날짜 셀의 비율을 1:1로 설정 */
  gap: 1px; /* 셀 간의 간격을 좁히기 */
  width: 100%;
  max-width: 500px; /* 캘린더 너비 제한 */
}

.calendar-date {
  text-align: center;
  padding: 4px; /* 각 셀의 안쪽 여백을 줄임 */
  cursor: pointer;
  border-radius: 10px;
  transition: background-color 0.2s ease;
}

.calendar-date-item {
  font-size: 12px; /* 글씨 크기 줄이기 */
  background-color: #f5f5f5; /* 기본적으로 아주 연한 회색 */
  width: 100%;
  display: inline-block;
  aspect-ratio: 1; /* 1:1 비율로 유지 */
  border-radius: 8px; /* 둥글게 모서리 처리 */
  position: relative;
}

.calendar-date-item.today {
  background-color: #54a673;
  color: white;
  border-radius: 50%;
}

.calendar-date-item.workout {
  background-color: #98cfba; /* 운동일기 색상 */
}

.calendar-date-item.diet {
  background-color: #b8d69f; /* 식단일기 색상 */
}

.calendar-date-item.both {
  background-color: #519472; /* 운동과 식단 둘 다 작성한 경우 */
}

.calendar-date:hover {
  background-color: #f0f0f0;
}

.day-of-week {
  font-weight: bold;
  color: #333;
  padding: 5px;
  text-align: center;
}

.month-title {
  font-weight: bold;
  font-size: 30px;
}
</style>
