<template>
  <div>
    <div class="marginplz"></div>
    <div class="mp-container">
      <div class="mp-profile">
        <div class="profile-container">
          <!-- 왼쪽: 사용자 프로필 사진 -->
          <div class="profile">
            <img :src="userProfileImage" alt="User Profile" class="profile-img" />
          </div>

          <!-- 중간: 사용자 이름과 설명 -->
          <div class="user-info">
            <h3 class="user-name">{{ userStore.userName || '이름 없음' }}</h3>
            <p class="user-description">{{ userStore.userDescription || '설명 없음' }}</p>

            <!-- 팔로워 수와 팔로잉 수 -->
            <div class="follow-info">
              <span class="follow-count" @click="openFollowerList">
                팔로워: {{ followerCount }}
              </span> |
              <span class="follow-count" @click="openFollowingList">
                팔로잉: {{ followingCount }}
              </span>
            </div>
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

      <!-- 팔로워와 팔로잉 목록 모달 -->
      <FollowerListModal v-if="isFollowerListOpen" @close="closeFollowerList" :followers="followers" />
      <FollowingListModal v-if="isFollowingListOpen" @close="closeFollowingList" :following="following" />

      <div class="next-container">
        <!-- 왼쪽: UserGrass -->
        <div class="usergrass">
          <UserGrass @dateSelected="onDateSelected" />
        </div>

        <!-- 오른쪽: 선택된 날짜의 운동일기와 식단일기 -->
        <div class="logs-container">
          <div v-if="selectedDate">
            <h3 class="slt-date">{{ selectedDate }}</h3>

            <!-- 운동일기 -->
            <div v-if="workoutLogsForSelectedDate" class="log-card">
              <h4 class="card-title">운동일기</h4>
              <div v-for="exercise in workoutLogsForSelectedDate.exercises" class="exc-content">
                <strong>{{ exercise.exerciseName }}</strong>
                {{ exercise.weight }} kg {{ exercise.reps }} 회
                {{ exercise.sets }} 세트
              </div>
              <p class="workout-content">{{ workoutLogsForSelectedDate.description }}</p>
            </div>

            <!-- 식단일기 -->
            <div v-if="dietLogsForSelectedDate" class="log-card">
              <h4 class="card-title">식단일기</h4>
              <div class="meal-images">
                <img
                  :src="breakfastImg || 'https://via.placeholder.com/150'"
                  alt="Breakfast Image"
                  class="meal-img"
                  width="50px"
                />
                <img
                  :src="lunchImg || 'https://via.placeholder.com/150'"
                  alt="Lunch Image"
                  class="meal-img"
                  width="50px"
                />
                <img
                  :src="dinnerImg || 'https://via.placeholder.com/150'"
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
  </div>
</template>


<script setup>
import { ref, onMounted, computed } from "vue";
import { useUserStore } from "@/stores/userStore";
import UserGrass from "@/components/user/userGrass.vue";
import defaultprofileImage from '@/assets/profile.jpg'
import apiClient from "@/components/api/apiClient";
import UserPagecopy from "@/components/user/UserPagecopy.vue";
import FollowerListModal from "@/components/user/FollowerListModal.vue";
import FollowingListModal from "@/components/user/FollowingListModal.vue";

const isEditModalOpen = ref(false); // 모달 상태
const isFollowerListOpen = ref(false); // 팔로워 목록 모달 상태
const isFollowingListOpen = ref(false); // 팔로잉 목록 모달 상태

// 사용자 상태
const userStore = useUserStore();

// sessionStorage에서 'user' 키에 저장된 유저 데이터를 가져옵니다.
const userData = sessionStorage.getItem("user");
const userId = ref(userData ? JSON.parse(userData).userId : null); // 유저 ID를 가져옵니다.


// 기본 이미지 경로 지정
const defaultImage = defaultprofileImage;

const userProfileImage = computed(() => {
  const profileImage = userStore.user?.profileImage; // userStore에 있는 프로필 정보
  if (profileImage && profileImage.trim() !== '') {
    return 'http://localhost:8080/' + profileImage.replace(/\\/g, '/');
  }
  return defaultprofileImage; // 프로필 이미지가 없으면 기본 이미지 반환
});

// 팔로워 수와 팔로잉 수
const followerCount = ref(0);
const followingCount = ref(0);
const followers = ref([]);
const following = ref([]);

// 팔로워, 팔로잉 데이터 불러오기
const fetchFollowData = async () => {
  try {
    const followerResponse = await apiClient.get(`/api-follow/follower`, {params: {userId: userId.value}});
    followerCount.value = followerResponse.data.length;
    followers.value = followerResponse.data;

    const followingResponse = await apiClient.get(`/api-follow/following`, {params: {userId: userId.value}});
    followingCount.value = followingResponse.data.length;
    following.value = followingResponse.data;

  } catch (error) {
    console.error("팔로워/팔로잉 데이터 불러오기 실패", error);
  }
};

// 팔로워 목록 보기
const openFollowerList = () => {
  isFollowerListOpen.value = true;
};

// 팔로잉 목록 보기
const openFollowingList = () => {
  isFollowingListOpen.value = true;
};

// 모달 닫기
const closeFollowerList = () => {
  isFollowerListOpen.value = false;
};

const closeFollowingList = () => {
  isFollowingListOpen.value = false;
};

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

const breakfastImg = dietLogsForSelectedDate.breakfastImagePath?  'http://localhost:8080/' + dietLogsForSelectedDate.breakfastImagePath : null
const lunchImg = dietLogsForSelectedDate.lunchImagePath?  'http://localhost:8080/' + dietLogsForSelectedDate.lunchImagePath : null
const dinnerImg = dietLogsForSelectedDate.dinnerImagePath? 'http://localhost:8080/' + dietLogsForSelectedDate.dinnerImagePath : null

// 컴포넌트가 마운트될 때 팔로워, 팔로잉 데이터 가져오기
onMounted(() => {
  fetchFollowData();
});
</script>

<style scoped>
.mp-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-content: center;
}



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
  flex-wrap: wrap; /* 화면 크기에 맞춰서 줄 바꿈 가능 */
  justify-content: space-between;
  align-items: center;
  width: 100%;
  height: 250px;
  max-width: 1500px;
  padding: 20px; /* 상하 여백 20px */
  padding-left: 60px; /* 좌측 여백 추가 */
  padding-right: 60px; /* 우측 여백 추가 */
  background-color: #f8f9fa;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  gap: 20px; /* 아이템 간격 설정 */
}

/* 왼쪽: 프로필 사진 */
.profile {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  min-width: 150px; /* 최소 너비 설정 */
}

/* 중간: 사용자 이름 및 설명 */
.user-info {
  flex: 2;
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-width: 230px; /* 최소 너비 설정 */
  padding-left: 20px;
}

.user-name {
  font-size: 1.5rem;
  font-weight: bold;
}

.user-description {
  font-size: 1rem;
  color: #555;
  margin-top: 5px;
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


/* 추가된 문구 스타일 */
.extra-st {
  flex: 1; /* 비율 맞추기 */
  padding: 0 20px; /* 좌우 여백 */
  text-align: center;
  font-size: 4rem;
  color: #8ea59b;
  font-style: italic;
  margin-top: 10px;
  margin-right: 100px;
  font-weight: bold;
  font-family: 'Bold';
  letter-spacing: 15px;
}




.my-2 {
  margin-top: 15px;
}

.btn-lg {
  font-size: 16px;
  padding: 12px 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  margin-top: 3%;
  margin-bottom: 10%;
  height: 50px;
}

.btn-edit {
  background-color: #54a673;
  color: white;
  max-width: 150px;
}

.btn-edit:hover {
  background-color: #4a8e5c;
}

.rounded-full {
  border-radius: 50px !important;
}

.my-3 {
  margin-top: 20px;
}

.marginplz {
  margin-top: 180px;
}


.usergrass {
  width: 500px;
  margin: 10px 0;
}


.next-container {
  display: flex;
  justify-content: center;
  width: 100%;
  max-width: 1500px;
  gap: 100px;
  margin: 0 auto;
  margin-top: 20px;
  margin-bottom: 20px;
}

/* 부모 요소에 flexbox 스타일을 추가 */
.challenge-wrapper {
  display: flex;
  justify-content: center; /* 수평 중앙 정렬 */
  align-items: center; /* 수직 중앙 정렬 */
  height: 100vh; /* 부모의 높이를 100%로 설정하여 중앙 배치가 잘 되도록 */
  margin-top: 50px;
}

/* 기존 .challenge-box 스타일 */
.challenge-box {
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 이미지 크기 조정 */
.challenge-img {
  max-width: 500px;
  height: auto;
  margin-left: -60%;
  margin-top: 50px;
}



/* 잔디가 이동하는 효과 */
.usergrass {
  flex:1;
  transition: transform 0.7s ease-out;
  width: 500px;
  margin: 10px 0;
}



.logs-container {
  flex: 2;
  margin-right: 20px;
  animation: fadeIn 0.8s ease-out forwards; /* 투명도가 서서히 나타나는 애니메이션 */
}

.log-card {
  background-color: #f9f9f9;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 10px;  /* 모서리 둥글게 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 그림자 추가 */
  transition: box-shadow 0.3s ease;
  max-width: 800px;
}

.log-card:hover {
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2); /* 마우스 오버 시 그림자 효과 */
}


.workout-content, .diet-content {
  font-size: 16px;
  color: #666;
  line-height: 1.5;
  margin-top: 10px;
  background-color: #fff;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border-radius: 20px;
}

.exc-content {
  border: 1px solid #c7c7c7;
  padding: 10px 20px;
  margin: 10px 0;
  border-radius: 20px;
}

.exc-content strong {
  display: block;
  font-weight: bold;
  margin-bottom: 5px;
}

.meal-images {
  display: flex;
  justify-content: space-between;
  margin: 20px 10px;
  gap: 15px;
}


.meal-img {
  width: 230px;
  height: 230px;
  object-fit: cover; /* 1:1 비율 유지 */
  border-radius: 15%; 
  box-shadow: 3px 2px 3px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease; /* 호버 시 크기 및 그림자 효과 부드럽게 */
}

.meal-img:hover {
  transform: scale(1.07); /* 5% 확대 */
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3); /* 호버 시 그림자 강도 증가 */
}


.workout-content {
  font-size: 20px;
}


.diet-content {
  font-size: 20px;
}

p {
  font-family: 'Light';
  font-size: 20px;
  color: #777;
}


.log-card p {
  color: #000000;
  margin-top: 20px;
}

.slt-date {
  margin-left: 10px;

}


.card-title {
  font-size: 1.7rem;
  font-family: 'SemiBold';
}

/* 반응형 스타일 */
@media (max-width: 1024px) {
  .usergrass {
    width: 100%; /* 화면 크기에 맞춰 크기 조정 */
  }
  .logs-container {
    width: 100%;
  }
}

</style>