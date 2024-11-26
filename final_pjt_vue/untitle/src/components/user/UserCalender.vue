<template>
  <div class="calendar">
    <div class="calendar-header">
      <h4> {{ currentYear }} {{ currentMonth }} </h4>
    </div>
    <div class="calendar-body">
      <div class="calendar-grid">
        <!-- 요일 헤더 -->
        <div class="calendar-date day-of-week">Sun</div>
        <div class="calendar-date day-of-week">Mon</div>
        <div class="calendar-date day-of-week">Tue</div>
        <div class="calendar-date day-of-week">Wed</div>
        <div class="calendar-date day-of-week">Thu</div>
        <div class="calendar-date day-of-week">Fri</div>
        <div class="calendar-date day-of-week">Sat</div>

        <!-- 날짜 출력 부분 -->
        <div v-for="(date, index) in calendarDates" :key="index" class="calendar-date" @click="goToCalendarView(date)">
          <div :class="['calendar-date-item', isToday(date) ? 'today' : '']">
            {{ date }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const currentMonth = ref('')
const currentYear = ref('')
const calendarDates = ref([])

const getCurrentMonthDates = () => {
  const date = new Date()
  const year = date.getFullYear()
  const month = date.getMonth()
  const firstDay = new Date(year, month, 1)
  const lastDay = new Date(year, month + 1, 0)

  const daysInMonth = lastDay.getDate()
  const firstDayOfWeek = firstDay.getDay()

  const dates = []
  for (let i = 0; i < firstDayOfWeek; i++) {
    dates.push('')
  }
  for (let i = 1; i <= daysInMonth; i++) {
    dates.push(i)
  }

  calendarDates.value = dates
  currentMonth.value = date.toLocaleString('default', { month: 'long' })
  currentYear.value = year
}

const goToCalendarView = (date) => {
  if (date) {
    router.push({ name: 'calendar-view', params: { date } })
  }
}

const isToday = (date) => {
  const today = new Date()
  return date === today.getDate() && today.getMonth() === new Date().getMonth() && today.getFullYear() === new Date().getFullYear()
}

onMounted(() => {
  getCurrentMonthDates()
})
</script>

<style scoped>
.calendar {
  display: flex;
  flex-direction: column;
  align-items: center;
  overflow: hidden;
  height: 100%;
  padding-left: 20px; /* 왼쪽 여백을 조금 더 추가 */
  padding-right: 20px; /* 오른쪽 여백을 조금 더 추가 */
}

.calendar-header {
  font-size: 20px;
  font-family: 'Bold';
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
  grid-auto-rows: 50px; /* 날짜 셀의 높이 줄이기 */
  gap: 3px; /* 셀 간의 간격을 조금 늘리기 */
  max-width: 90%; /* 캘린더 너비 줄이기 */
}

.calendar-date {
  text-align: center;
  padding: 8px;
  cursor: pointer;
  border-radius: 10px;
  transition: background-color 0.2s ease;
}

.calendar-date-item {
  font-size: 14px;
}

.calendar-date-item.today {
  background-color: #54a673;
  color: white;
  border-radius: 50%;
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
</style>
