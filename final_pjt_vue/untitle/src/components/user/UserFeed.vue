<template>
  <div class="user-view">
    <!-- 검색창 추가 -->
    <div class="search-bar">
      <div class="search-box">
        <input 
          type="text" 
          v-model="searchUserId" 
          placeholder="유저 ID를 입력하세요"
          @keyup.enter="searchUser"
        />
        <button @click="searchUser">
          <i class="bi bi-search"></i> <!-- 검색 아이콘 -->
        </button>
        <button @click="resetSearch">
          <i class="bi bi-arrow-clockwise"></i> <!-- 초기화 아이콘 -->
        </button>
      </div>
    </div>

    <!-- 검색된 유저 목록 -->
    <div v-if="searchedUsers.length > 0" class="user-list">
      <h3>검색된 유저</h3>
      <div 
        v-for="user in searchedUsers" 
        :key="user.userId" 
        class="user-card" 
        @click="selectUser(user)"
      >
        <p>{{ user.username }}</p>

        <!-- 팔로우 버튼 표시 -->
        <div v-if="user.userId !== userId.value">
          <button 
            v-if="!isFollowing(user.userId)"
            @click="followUser(user)"
            class="follow-btn"
          >
            <i class="bi bi-plus"></i> <!-- 팔로우 버튼을 + 아이콘으로 -->
          </button>
          <button 
            v-if="isFollowing(user.userId)"
            @click="unfollowUser(user)"
            class="follow-btn"
          >
            <i class="bi bi-check"></i> <!-- 팔로우 취소 버튼 -->
          </button>
        </div>
      </div>
    </div>

    <!-- 탭 버튼 -->
   
<div class="tabs">
  <input type="radio" id="diet-tab" v-model="currentTab" value="diet" class="tab-toggle" />
  <label for="diet-tab" class="tab-label">
    <i class="bi tab-icon">🥗</i>
  </label>
  
  <input type="radio" id="workout-tab" v-model="currentTab" value="workout" class="tab-toggle" />
  <label for="workout-tab" class="tab-label">
    <i class="bi tab-icon">🏃‍➡️</i>
  </label>
</div>



    <!-- 식단일기 탭 -->
    <div v-if="currentTab === 'diet'" class="diet-tab">
      <!-- 나의 최근 식단일기 하나만 출력 -->
      <div class="my-diet">
        <div v-if="myDietLogs.length > 0" class="log-card">
          <h3>나의 최근 식단</h3>
          <p class="diet-date">{{ myDietLogs[0].recordDate }}</p>
          

          
           <!-- 식사 이미지 탭 -->
           <div class="meal-tabs">
            <input type="radio" id="breakfast-tab" v-model="mealTab" value="breakfast" class="meal-toggle" />
            <label for="breakfast-tab" class="meal-label">아침</label>

            <input type="radio" id="lunch-tab" v-model="mealTab" value="lunch" class="meal-toggle" />
            <label for="lunch-tab" class="meal-label">점심</label>

            <input type="radio" id="dinner-tab" v-model="mealTab" value="dinner" class="meal-toggle" />
            <label for="dinner-tab" class="meal-label">저녁</label>
          </div>

          <!-- 아침 이미지 -->
          <div v-if="mealTab === 'breakfast' && myDietLogs[0].breakfastImagePath">
            <img :src="'http://localhost:8080/'+myDietLogs[0].breakfastImagePath" alt="Breakfast Image" class="meal-img" />
          </div>

          <!-- 점심 이미지 -->
          <div v-if="mealTab === 'lunch' && myDietLogs[0].lunchImagePath">
            <img :src="'http://localhost:8080/'+myDietLogs[0].lunchImagePath" alt="Lunch Image" class="meal-img" />
          </div>

          <!-- 저녁 이미지 -->
          <div v-if="mealTab === 'dinner' && myDietLogs[0].dinnerImagePath">
            <img :src="'http://localhost:8080/'+myDietLogs[0].dinnerImagePath" alt="Dinner Image" class="meal-img" />
          </div>


          <p class="diet-content">{{ myDietLogs[0].content }}</p>


          <button @click="openDietLogModal(myDietLogs[0])" class="edit-btn">수정</button>
        </div>
      </div>
      <hr>
      <div v-for="log in dietLogs" :key="log.diet_id" class="log-card">
        <h3>{{log.name}}</h3>
        <p><strong>{{ log.recordDate }}</strong></p>
        <div>{{ log.content }}</div>
        <div class="meal-tabs">
          <input type="radio" id="breakfast-tab-{{ log.diet_id }}" v-model="mealTab" value="breakfast" class="meal-toggle" />
          <label for="breakfast-tab-{{ log.diet_id }}" class="meal-label">아침</label>

          <input type="radio" id="lunch-tab-{{ log.diet_id }}" v-model="mealTab" value="lunch" class="meal-toggle" />
          <label for="lunch-tab-{{ log.diet_id }}" class="meal-label">점심</label>

          <input type="radio" id="dinner-tab-{{ log.diet_id }}" v-model="mealTab" value="dinner" class="meal-toggle" />
          <label for="dinner-tab-{{ log.diet_id }}" class="meal-label">저녁</label>
        </div>

        <!-- 아침 이미지 -->
        <div v-if="mealTab === 'breakfast' && log.breakfastImagePath">
          <img :src="'http://localhost:8080/'+log.breakfastImagePath" alt="Breakfast Image" class="meal-img" />
        </div>

        <!-- 점심 이미지 -->
        <div v-if="mealTab === 'lunch' && log.lunchImagePath">
          <img :src="'http://localhost:8080/'+log.lunchImagePath" alt="Lunch Image" class="meal-img" />
        </div>

        <!-- 저녁 이미지 -->
        <div v-if="mealTab === 'dinner' && log.dinnerImagePath">
          <img :src="'http://localhost:8080/'+log.dinnerImagePath" alt="Dinner Image" class="meal-img" />
        </div>
      </div>
    </div>

    <!-- 운동일기 탭 -->
    <div v-if="currentTab === 'workout'" class="workout-tab">
      <div v-if="myWorkoutLogs.length > 0" class="log-card">
        <h3>나의 최근 운동</h3>
        <p><strong>{{ myWorkoutLogs[0].recordDate }}</strong></p>
        <p>{{ myWorkoutLogs[0].description }}</p>
        <div v-for="exercise in myWorkoutLogs[0].exercises" :key="exercise.id">
          <strong>{{ exercise.exerciseName }}</strong>
          {{ exercise.weight }} kg
          {{ exercise.reps }} 회
          {{ exercise.sets }} 세트
        </div>
      </div>
      <hr>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import apiClient from '@/components/api/apiClient';

// sessionStorage에서 'user' 키로 객체를 가져오기
const user = ref(JSON.parse(sessionStorage.getItem('user')));

// 현재 선택된 탭
const currentTab = ref('diet');

// 유저 데이터 처리
const userId = ref(user.value ? user.value.userId : null);

const searchUserId = ref('');  // 검색한 userId
const searchedUsers = ref([]);  // 검색된 유저 목록

const dietLogs = ref([]);  // 팔로우한 유저들의 식단일기 배열
const myDietLogs = ref([]);  // 내 식단일기 배열
const workoutLogs = ref([]);  // 팔로우한 유저들의 운동일기 배열
const myWorkoutLogs = ref([]);  // 내 운동일기 배열

const selectedDietLog = ref(null);  // 선택한 식단일기
const isDietLogModalOpen = ref(false);  // 모달 열림 여부




// 팔로우 상태 확인 함수
const isFollowing = (otherUserId) => {
  // 팔로우한 유저 목록에서 다른 유저가 팔로우된 상태인지 확인
  return dietLogs.value.some(log => log.userId === otherUserId) || workoutLogs.value.some(log => log.userId === otherUserId);
};

// 팔로우하기
const followUser = async (user) => {
  console.log(user)
  try {
    await apiClient.post('/api-follow/follow', {
      followerId: userId.value,
      followingId: user.userId
      });
      resetSearch();
  } catch (error) {
    console.error('팔로우 실패', error);
  }
};

// 팔로우 취소하기
const unfollowUser = async (user) => {
  try {
    await apiClient.delete('/api-follow/unfollow', {
      params: {
        followerId: userId.value,
        followingId: user.userId
      }
    });
    resetSearch();
  } catch (error) {
    console.error('팔로우 취소 실패', error);
  }
};





// 탭 선택 함수
const selectTab = (tab) => {
  currentTab.value = tab;
  fetchLogs(tab);
};




// 유저 검색 처리
const searchUser = async () => {
  if (searchUserId.value) {
    try {
      // 유저 ID와 비슷한 유저들을 찾는 API 요청
      const response = await apiClient.get(`/api-user/search/${searchUserId.value}`);
      searchedUsers.value = response.data;
    } catch (error) {
      console.error('유저 검색 실패', error);
    }
  }
};

// 선택한 유저의 일기 정보 불러오기
const selectUser = async (user) => {
  userId.value = user.userId;  // 선택한 유저 ID로 설정
  searchUserId.value = user.userId;  // 검색창에 선택한 유저 ID 표시
  fetchLogs(currentTab.value);  // 현재 탭에 해당하는 일기 불러오기
  searchedUsers.value = [];  // 검색된 유저 목록 초기화
};

// 로그 데이터 불러오기
const fetchLogs = async (tab) => {
  try {
    if (!userId.value) {
      console.error("User ID가 존재하지 않습니다.");
      return;
    }

    if (tab === 'diet') {
      const response = await apiClient.get(`/api-diet/follow/${userId.value}`);
      dietLogs.value = response.data;
      const myresponse = await apiClient.get(`/api-diet/feed/${userId.value}`);
      myDietLogs.value = myresponse.data;
    } else if (tab === 'workout') {
      const response = await apiClient.get(`/api-workout/follow/${userId.value}`);
      workoutLogs.value = response.data;
      const myresponse = await apiClient.get(`/api-workout/feed/${userId.value}`);
      myWorkoutLogs.value = myresponse.data;
    }
  } catch (error) {
    console.error('로그 불러오기 실패', error);
  }
};

// 검색 초기화 함수
const resetSearch = () => {
  searchUserId.value = '';  // 검색어 초기화
  searchedUsers.value = [];  // 검색된 유저 목록 초기화
  userId.value = user.value.userId;  // 내 ID로 돌아가기
  fetchLogs(currentTab.value);  // 현재 탭에 해당하는 내 로그 불러오기
};




// 식단일기 수정 모달 열기
const openDietLogModal = (dietLog) => {
  selectedDietLog.value = dietLog;
  isDietLogModalOpen.value = true;
};

// 모달 닫기
const closeDietLogModal = () => {
  isDietLogModalOpen.value = false;
};



const mealTab = ref('breakfast'); // 식사 이미지 탭



onMounted(() => {
  // 컴포넌트가 마운트될 때 기본 탭의 로그를 불러옵니다.
  if (userId.value) {
    fetchLogs(currentTab.value);  
  }
});
</script>

<style scoped>



/* 토글 스타일 */
.tabs {
  display: flex;
  gap: 10px;
  justify-content: center;
  margin: 20px 0;
}

.tab-toggle {
  display: none; /* radio input을 숨깁니다. */
}

.tab-label {
  padding: 5px 10px;
  cursor: pointer;
  border-radius: 50px;
  background-color: #f0f0f0;
  transition: background-color 0.3s ease;
  font-weight: bold;
  color: #333;
}

.tab-toggle:checked + .tab-label {
  background-color: #54a673;
  color: white;
  transition: background-color 0.3s ease;
}

/* 슬라이드 효과 */
.tab-toggle:checked + .tab-label {
  background-color: #54a673;
  color: white;
}


.my-diet {
  padding: 15px 40px;
  background-color: #ffffff;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  border-radius: 15px; /* 모서리 둥글게 */
  max-width: 1000px;
  margin: 0 auto;
}

.diet-tab .log-card {
  margin-bottom: 30px; /* 카드 간격 추가 */
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 15px;
}

button {
  padding: 10px;
  border-radius: 5px;
  cursor: pointer;
}

button.active {
  background-color: #54a673;
  color: white;
}

.log-card {
  padding: 20px;
  margin: 20px 0; /* 카드 바깥쪽에 여백 추가 */
  border-radius: 8px;
  background-color: #ffffff;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  gap: 20px;
}



img {
  max-width: 200px;
  max-height: 200px;
  object-fit: cover;
}

/* 검색창 스타일 */
.search-bar {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.search-box {
  display: flex;
  align-items: center;
  background-color: transparent; /* 배경 투명 */
  padding: 10px;
  border-radius: 20px;
  border: 1px solid #ddd; /* 테두리 */
}

.search-box input {
  padding: 10px;
  border: none;
  border-radius: 15px;
  width: 400px;
}

.search-box button {
  background: transparent;
  border: none;
  padding: 10px;
  cursor: pointer;
  margin-left: 5px;
}

.search-box button i {
  font-size: 20px;
  color: #54a673;
}


.user-list {
  margin-top: 20px;
  border-radius: 20px; /* 유저 목록 박스 둥글게 */
  border: 1px solid #ddd;
  padding: 20px;
  background-color: #ffffff;
}



/* 팔로우 버튼 */
.follow-btn {
  background: transparent;
  border: none;
  padding: 5px 10px;
  cursor: pointer;
}

.follow-btn i {
  font-size: 20px;
  color: #54a673;
}

/* 탭 스타일 */
.tabs {
  display: flex;
  gap: 10px;
  justify-content: center;
  margin: 20px 0;
}

button {
  padding: 10px;
  border-radius: 5px;
  cursor: pointer;
}

button.active {
  background-color: #54a673;
  color: white;
}

.tab-icon {
    font-size: 1.2rem; /* 이모지 크기 조정 */
    padding: 10px;   /* 테두리와 이모지 사이 여백 */
    display: inline-block; /* 이모지가 한 줄에 표시되게 */
    text-align: center;
    line-height: 1; /* 이모지 세로 정렬 */
    transition: all 0.3s ease; /* 호버 효과 부드럽게 */
  }

  /* 추가 스타일 (선택적) */
  .tab-label {
    cursor: pointer;
  }

/* 일기 스타일 */
.log-card {
  padding: 20px;
  margin: 15px 0;
  border-radius: 8px;
  background-color: #ffffff;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}


.meal-images {
  display: flex;
  gap: 20px;
  justify-content: center;
}

.meal-tabs {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
  justify-content: center;
  margin-top: -65px;
}

.meal-toggle {
  display: none; /* radio input을 숨깁니다. */
}

.meal-label {
  padding: 10px;
  cursor: pointer;
  background-color: #f0f0f0;
  border-radius: 15px;
}

.meal-toggle:checked + .meal-label {
  background-color: #54a673;
  color: white;
}

.meal-img-container {
  width: 100%; /* 부모 컨테이너가 전체 너비를 차지하도록 */
  display: flex;
  justify-content: center; /* 이미지를 가로 가운데로 배치 */
  align-items: center; /* 세로로도 가운데 배치 */
  max-width: 800px; /* 최대 너비 제한 */
  margin: 0 auto; /* 가로 가운데 정렬 */
}

.meal-img {
  width: 100%; /* 부모 너비를 100%로 채우기 */
  height: auto;
  max-width: 100%; /* 최대 너비를 부모에 맞춤 */
  max-height: 100%;
  object-fit: cover; /* 이미지가 부모 컨테이너를 꽉 채우되 비율 유지 */
  border-radius: 20px;
}

/* 수정 버튼 */
.edit-btn {
  background-color: #54a673;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 25px;
  cursor: pointer;
  align-self: flex-end; /* 오른쪽 정렬 */
  margin-top: 15px; /* 버튼과 내용 사이에 여백 추가 */
}

.edit-btn:hover {
  background-color: #4a9d62; /* 호버 시 색상 변경 */
}


/* 유저 카드 스타일 */
.user-card {
  display: flex;
  justify-content: space-between; /* 유저 ID와 버튼을 양 끝에 배치 */
  padding: 10px;
  border: 1px solid #ddd;
  margin: 5px 0;
  cursor: pointer;
  border-radius: 15px; /* 더 둥근 테두리 */
  background-color: #f9f9f9;
}

.user-card p {
  margin: 0;
  margin-top: 7px;
  margin-left: 13px;
  flex-grow: 1; /* 유저 ID가 공간을 차지하도록 설정 */
}

.user-card button {
  background: transparent;
  border: none;
  padding: 5px;
  cursor: pointer;
}

.diet-date {
  margin-top: -15px;
  font-size: 1rem;
}

.diet-content {
  font-family: 'Medium';
  font-size: 1.5rem;
  background-color: #ffffff;
  border-radius: 20px;
  height: 150px;
  box-shadow: rgba(0, 0, 0, 0.5);
  padding: 20px;
}


</style>
