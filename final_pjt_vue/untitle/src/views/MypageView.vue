<template>
  <div>
    <div class="marginplz"></div>
    <div class="mp-profile">
      <div class="profile-container">
        <!-- 왼쪽: 사용자 프로필 사진 -->
        <div class="profile">
          <img :src="userProfileImage" class="profile-img" />
        </div>

        <!-- 중간: 사용자 이름과 설명 -->
        <div class="user-info">
          <h3 class="user-name">{{ userStore.userName || '이름 없음' }}</h3>
          <p class="user-description">{{ userStore.userDescription || '설명 없음' }}</p>
        </div>

        <!-- 문구 추가 -->
        <div class="extra-st">
          <p>FITYOU!</p>
        </div>

        <!-- 오른쪽: 내 정보 수정 버튼 -->
        <div class="edit-button-container">
          <button
            class="btn btn-lg btn-edit my-2 d-flex align-items-center rounded-full"
            @click="openEditModal"
          >
            <i class="bi bi-pencil-square"></i> 편집
          </button>
        </div>
      </div>
    </div>

    <div class="next-container">
      <!-- 왼쪽: UserGrass -->
      <div class="usergrass">
        <UserGrass @dateSelected="onDateSelected" />
      </div>

      <!-- 오른쪽: 선택된 날짜의 운동일기와 식단일기 -->
      <div class="logs-container">
        <div v-if="selectedDate">
          <h3>선택된 날짜: {{ selectedDate }}</h3>

          <!-- 운동일기 -->
          <div v-if="workoutLogsForSelectedDate" class="log-card">
            <h4>운동일기</h4>
            <p class="workout-content">{{ workoutLogsForSelectedDate.description }}</p>
            <div v-for="exercise in workoutLogsForSelectedDate.exercises" class="exc-content">
              <strong>{{ exercise.exerciseName }}</strong>
              {{ exercise.weight }} kg {{ exercise.reps }} 회
              {{ exercise.sets }} 세트
            </div>
          </div>

          <!-- 식단일기 -->
          <div v-if="dietLogsForSelectedDate" class="log-card">
            <h4>식단일기</h4>
            <div class="meal-images">
              <img
                :src="'http://localhost:8080/' + dietLogsForSelectedDate.breakfastImagePath"
                alt="Breakfast Image"
                class="meal-img"
                width="50px"
              />
              <img
                :src="'http://localhost:8080/' + dietLogsForSelectedDate.lunchImagePath"
                alt="Lunch Image"
                class="meal-img"
                width="50px"
              />
              <img
                :src="'http://localhost:8080/' + dietLogsForSelectedDate.dinnerImagePath"
                alt="Dinner Image"
                class="meal-img"
                width="50px"
              />
            </div>
            <p class="diet-content">{{ dietLogsForSelectedDate.content }}</p>
          </div>

          <!-- 기록이 없을 때만 "해당 날짜에 기록된 내용이 없습니다." 메시지 표시 -->
          <div v-if="!workoutLogsForSelectedDate && !dietLogsForSelectedDate">
            <p>해당 날짜에 기록된 내용이 없습니다.</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 챌린지 부분 -->
    <RouterLink :to="{ name: 'challenge' }">
      <div class="challenge-box">
        <img src="@/assets/challenge.png" alt="챌린지하세요" class="challenge-img" />
      </div>
    </RouterLink>

    <!-- UserPage 모달 열기 -->
    <UserPagecopy v-if="isEditModalOpen" @close="closeEditModal" />

  </div>
</template>

<script setup>
import { ref } from "vue";
import { useUserStore } from "@/stores/userStore";
import UserGrass from "@/components/user/userGrass.vue";
import defaultprofileImage from '@/assets/profile.jpg'
import apiClient from "@/components/api/apiClient";
import UserPagecopy from "@/components/user/UserPagecopy.vue";

const isEditModalOpen = ref(false); // 모달 상태

// 사용자 상태
const userStore = useUserStore();

// sessionStorage에서 'user' 키에 저장된 유저 데이터를 가져옵니다.
const userData = sessionStorage.getItem("user");
const userId = ref(userData ? JSON.parse(userData).userId : null); // 유저 ID를 가져옵니다.


// 기본 이미지 경로 지정
const defaultImage = defaultprofileImage;

// userProfileImage 계산 로직
const userProfileImage = userStore.userProfileImage && userStore.userProfileImage.trim()
  ? 'http://localhost:8080/' + userStore.userProfileImage.replace(/\\/g, '/') 
  : defaultImage;

// 모달 열기
const openEditModal = () => {
  isEditModalOpen.value = true;
};

// 모달 닫기
const closeEditModal = () => {
  isEditModalOpen.value = false;
};

// 선택된 날짜
const selectedDate = ref(null);
const workoutLogsForSelectedDate = ref(null);  
const dietLogsForSelectedDate = ref(null);    

// 날짜가 선택되었을 때 해당 날짜의 기록을 가져오는 함수
const onDateSelected = async (date) => {
  selectedDate.value = date;
  fetchLogsForSelectedDate(date);
};

// 해당 날짜의 운동일기 가져오기
const fetchWorkoutLogs = async (date) => {
  try {
    const response = await apiClient.get('/api-workout/date', {
      params: {
        date: date,
        userId: userId.value
      }
    });
    workoutLogsForSelectedDate.value = response.data ? response.data : null; // 첫 번째 운동일기만 가져옴
  } catch (error) {
    workoutLogsForSelectedDate.value = null;
    console.error("운동일기 가져오기 실패", error);
  }
};

// 해당 날짜의 식단일기 가져오기
const fetchDietLogs = async (date) => {
  try {
    const response = await apiClient.get('/api-diet/date', {
      params: { date: date, userId: userId.value }
    });
    dietLogsForSelectedDate.value = response.data ? response.data : null; // 첫 번째 식단일기만 가져옴
  } catch (error) {
    dietLogsForSelectedDate.value = null;
    console.error("식단일기 가져오기 실패", error);
  }
};

// 날짜에 맞는 로그들 불러오기
const fetchLogsForSelectedDate = (date) => {
  fetchWorkoutLogs(date);
  fetchDietLogs(date);
};
</script>

<style scoped>
/* 프로필 사진 크기 */
.profile-img {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
  border: 4px solid #54a673;
}

/* 프로필 영역의 Flexbox */
.mp-profile {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

/* 3개의 영역을 가로로 배치 (왼쪽, 중간, 오른쪽) */
.profile-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  height: 250px;
  max-width: 1500px;
  padding: 20px;
  gap: 20px;
}

/* 왼쪽: 프로필 사진 */
.profile {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  min-width: 150px;
}

/* 중간: 사용자 이름 및 설명 */
.user-info {
  flex: 2;
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-width: 230px;
  padding-left: 20px;
}

/* 문구 스타일 */
.extra-st {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
  font-size: 4rem;
  color: #6c757d;
  font-style: italic;
  margin-top: 10px;
  font-weight: bold;
  font-family: Arial, Helvetica, sans-serif;
  letter-spacing: 15px;
}

/* 오른쪽: 수정 버튼 */
.edit-button-container {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  min-width: 150px;
}

/* 아이콘과 텍스트 간격 조정 */
.bi {
  margin-right: 8px;
}

/* 일기 표시 영역 */
.next-container {
  display: flex;
  justify-content: space-between;
  margin-top: 30px;
}

.usergrass {
  width: 40%;
}

.logs-container {
  width: 55%;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 로그 카드 */
.log-card {
  padding: 20px;
  margin: 15px 0;
  border-radius: 12px;
  background-color: #ffffff;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
}

h3 {
  font-size: 1.6rem;
}

h4 {
  font-size: 1.3rem;
  font-weight: 600;
  color: #333;
}

.workout-content,
.diet-content {
  font-size: 1.1rem;
  color: #555;
}

.exc-content {
  font-size: 0.9rem;
  color: #777;
}

.meal-img {
  border-radius: 5px;
  margin-right: 10px;
}

/* 챌린지 박스 */
.challenge-box {
  margin-top: 40px;
  width: 100%;
  text-align: center;
}

.challenge-img {
  width: 100%;
  max-width: 900px;
  height: auto;
  border-radius: 10px;
}

/* 반응형 스타일 */
@media (max-width: 1024px) {
  .next-container {
    flex-direction: column;
  }

  .usergrass {
    width: 100%;
  }

  .logs-container {
    width: 100%;
  }
}
</style>
