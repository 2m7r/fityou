<template>
  <div class="user-view">
    <!-- 검색창 추가 -->
    <div class="search-bar">
      <input 
        type="text" 
        v-model="searchUserId" 
        placeholder="유저 ID를 입력하세요"
        @keyup.enter="searchUser"
        @focus="showRecommendedUsers = true" 
      />
      <button @click="searchUser">검색</button>
      <!-- 초기화 버튼 추가 -->
      <button @click="resetSearch">초기화</button>
    </div>

    <!-- 검색된 유저 목록 -->
    <div v-if="searchedUsers.length > 0" class="user-list">
      <div 
        v-for="user in searchedUsers" 
        :key="user.userId" 
        class="user-card" 
        @click="selectUser(user)"
      >
        <p>{{ user.username }}</p>
        <!-- 팔로우 버튼 표시 (내 자신을 제외) -->
        <div v-if="user.userId !== userId"> <!-- 내 자신을 제외하는 조건 -->
          <button 
            v-if="!isFollowing(user.userId)"
            @click="followUser(user)"
          >
            팔로우
          </button>
          <button 
            v-if="isFollowing(user.userId)"
            @click="unfollowUser(user)"
          >
            팔로우 취소
          </button>
        </div>
      </div>
    </div>

    <!-- 선호운동이 겹치는 추천 유저들 -->
    <div v-if="showRecommendedUsers && recommendedUsers.length > 0" class="recommended-users">
      <hr>
      <h3>추천 유저</h3>
      <div 
        v-for="user in recommendedUsers" 
        :key="user.userId" 
        class="user-card"
        @click="selectUser(user)"
      >
        <p>{{ user.username }}</p>
        <button 
            v-if="!isFollowing(user.userId)"
            @click="followUser(user)"
          >
            팔로우
          </button>
          <button 
            v-if="isFollowing(user.userId)"
            @click="unfollowUser(user)"
          >
            팔로우 취소
          </button>
      </div>
    </div>

    <div class="tabs">
      <button 
        :class="{ active: currentTab === 'diet' }" 
        @click="selectTab('diet')"
      >
        식단일기
      </button>
      <button 
        :class="{ active: currentTab === 'workout' }" 
        @click="selectTab('workout')"
      >
        운동일기
      </button>
    </div>

    <!-- 식단일기 탭 -->
    <div v-if="currentTab === 'diet'" class="diet-tab">
      <!-- 나의 최근 식단일기 하나만 출력 -->
      <div v-if="myDietLogs.length > 0" class="log-card">
        <h3>{{ myDietLogs[0].name }}님의 최근 식단일기</h3>
    
        <!-- 날짜 -->
        <p><strong>{{ myDietLogs[0].recordDate }}</strong></p>
    
        <!-- 내용 -->
        <p>{{ myDietLogs[0].content }}</p>
    
        <!-- 이미지 (아침, 점심, 저녁) -->
        <div class="meal-images">
          <div v-if="myDietLogs[0].breakfastImagePath">
            <p>아침</p>
            <img :src="'http://localhost:8080/'+myDietLogs[0].breakfastImagePath" alt="Breakfast Image" />
          </div>
          <div v-if="myDietLogs[0].lunchImagePath">
            <p>점심</p>
            <img :src="'http://localhost:8080/'+myDietLogs[0].lunchImagePath" alt="Lunch Image" />
          </div>
          <div v-if="myDietLogs[0].dinnerImagePath">
            <p>저녁</p>
            <img :src="'http://localhost:8080/'+myDietLogs[0].dinnerImagePath" alt="Dinner Image" />
          </div>
        </div>
        <!-- 수정 버튼 추가 -->
        <button @click="openDietLogModal(myDietLogs[0])">수정</button>
      </div>
      <div v-else>
        <hr>
        <h3>최근 식단일기가 없습니다.... 다이어트 안하셨나요..?</h3>
      </div>
      <hr>
      <!-- 팔로우한 유저들의 식단일기 -->
      <div v-for="log in dietLogs" :key="log.diet_id" class="log-card">
        <!-- 이름 -->
        <h3>{{log.name}}</h3>
        
        <!-- 날짜 -->
        <p><strong>{{ log.recordDate }}</strong></p>
        
        <!-- 내용 -->
        <div>{{ log.content }}</div>
        
        <!-- 이미지 (아침, 점심, 저녁) -->
        <div class="meal-images">
          <div v-if="log.breakfastImagePath">
            <p>아침</p>
            <img :src="'http://localhost:8080/'+log.breakfastImagePath" alt="Breakfast Image" />
          </div>
          <div v-if="log.lunchImagePath">
            <p>점심</p>
            <img :src="'http://localhost:8080/'+log.lunchImagePath" alt="Lunch Image" />
          </div>
          <div v-if="log.dinnerImagePath">
            <p>저녁</p>
            <img :src="'http://localhost:8080/'+log.dinnerImagePath" alt="Dinner Image" />
          </div>
        </div>

        <!-- 식단일기 또는 운동일기에서 댓글 추가 및 표시 -->
        <div v-if="log.comments">
          <div v-for="comment in log.comments" :key="comment.id">
            <p>{{ comment.username }}: {{ comment.content }}</p>
          </div>
        </div>

          <!-- 댓글 입력창 -->
          <div class="comment-box">
            <input v-model="newComment" placeholder="댓글을 입력하세요" />
            <button @click="addComment(log)">댓글 추가</button>
          </div>
      </div>
    </div>

    <!-- 운동일기 탭 -->
    <div v-if="currentTab === 'workout'" class="workout-tab">
      <!-- 나의 최근 운동일기 하나만 출력 -->
      <div v-if="myWorkoutLogs.length > 0" class="log-card">
        <h3>{{ myWorkoutLogs[0].name }}의 최근 운동일기</h3>

        <!-- 날짜 -->
        <p><strong>{{ myWorkoutLogs[0].recordDate }}</strong></p>
    
        <p>{{ myWorkoutLogs[0].description }}</p>

        <div v-for="exercise in myWorkoutLogs[0].exercises" :key="exercise.id">
          <strong>{{ exercise.exerciseName }}</strong>
          {{ exercise.weight }} kg
          {{ exercise.reps }} 회
          {{ exercise.sets }} 세트
        </div>
      </div>
      <div v-else>
        <hr>
        <h3>최근 운동일기가 없습니다.... 운동 안하셨나요..?</h3>
      </div>
      <hr>
      <!-- 팔로우한 유저들의 운동일기 -->
      <div v-for="log in workoutLogs" :key="log.workout_id" class="log-card">
        <!-- 이름 -->
        <h3>{{ log.name }}</h3>

        <!-- 날짜 -->
        <p><strong>{{ log.recordDate }}</strong></p>

        <!-- 내용 -->
        <p>{{ log.description }}</p>
        
        <div v-for="exercise in log.exercises" :key="exercise.id">
          <strong>{{ exercise.exerciseName }}</strong>
          {{ exercise.weight }} kg
          {{ exercise.reps }} 회
          {{ exercise.sets }} 세트
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import apiClient from '@/components/api/apiClient';

// sessionStorage에서 'user' 키로 객체를 가져오기
const user = ref(JSON.parse(sessionStorage.getItem('user')));

// 현재 선택된 탭
const currentTab = ref('diet');

// 유저 데이터 처리
const userId = ref(user.value ? user.value.userId : null);
const username = ref(user.value ? user.value.username : null);

const searchUserId = ref('');  // 검색한 userId
const searchedUsers = ref([]);  // 검색된 유저 목록
const recommendedUsers = ref([]);  // 선호운동이 겹치는 추천 유저 목록
const showRecommendedUsers = ref(false);  // 추천 유저 목록 표시 여부

const dietLogs = ref([]);  // 팔로우한 유저들의 식단일기 배열
const myDietLogs = ref([]);  // 내 식단일기 배열
const workoutLogs = ref([]);  // 팔로우한 유저들의 운동일기 배열
const myWorkoutLogs = ref([]);  // 내 운동일기 배열

// 각 일기 항목에 대한 댓글 관리
const comments = ref({});  // {logId: [댓글들]}

const selectedDietLog = ref(null);  // 선택한 식단일기
const isDietLogModalOpen = ref(false);  // 모달 열림 여부

const newComment = ref('');  // 새로 추가할 댓글 내용

const addComment = async (log) => {
  if (newComment.value.trim() === '') {
    return;
  }

  try {
    const response = await apiClient.post('/api-comment/create', {
      targetId: log.id,  // 해당 일기의 ID
      userId: userId.value,  // 댓글 작성자의 ID
      content: newComment.value,  // 댓글 내용
      targetType: currentTab.value
    });

    // 댓글 추가 후 댓글 목록 업데이트
    fetchComments(log.id);
    newComment.value = '';  // 댓글 입력창 초기화
  } catch (error) {
    console.error('댓글 추가 실패', error);
  }
};

// 댓글을 불러오는 함수
const fetchComments = async (logId) => {
  console.log(currentTab.value)
  try {
    const response = await apiClient.get('/api-comment/comment', {
      params:{
        targetId: logId,
        targetType: currentTab.value
      }
    });
    comments.value[logId] = response.data;  // 해당 일기의 댓글 목록
  } catch (error) {
    console.error('댓글 불러오기 실패', error);
  }
};

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

      // 추천 유저 목록 업데이트
      recommendUsers();
    } catch (error) {
      console.error('유저 검색 실패', error);
    }
  }
};

// 선호운동이 겹치는 유저 추천 함수
const recommendUsers = async () => {
  console.log('추천 호출됨')
  try {
    // 전체 유저 목록에서 선호 운동이 겹치는 유저 추천 요청
    const response = await apiClient.get('/api-user/recommend', {
      params: {
        userId: userId.value,  // 현재 유저 ID를 전송
      }
    });
    recommendedUsers.value = response.data;  // 추천된 유저들
  } catch (error) {
    console.error('추천 유저 불러오기 실패', error);
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

watch(() => userId.value, (newUserId) => {
  if (newUserId) {
    fetchLogs(currentTab.value); // userId가 변경되면 데이터를 다시 로드
  }
});

watch(() => showRecommendedUsers.value, (newValue) => {
  if (newValue) {
    recommendUsers(); // showRecommendedUsers가 true로 변경되면 추천 유저 목록을 업데이트
  }
});


// 검색 초기화 함수
const resetSearch = () => {
  searchUserId.value = '';  // 검색어 초기화
  searchedUsers.value = [];  // 검색된 유저 목록 초기화
  userId.value = user.value.userId;  // 내 ID로 돌아가기
  showRecommendedUsers.value = false;
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

onMounted(() => {
  // 컴포넌트가 마운트될 때 기본 탭의 로그를 불러옵니다.
  if (userId.value) {
    fetchLogs(currentTab.value);  
  }
});
</script>

<style scoped>
.comment-box {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.comment-box input {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 5px;
  width: 80%;
}

.comment-box button {
  padding: 8px;
  border-radius: 5px;
  background-color: #54a673;
  color: white;
  cursor: pointer;
}

.comment-box button:hover {
  background-color: #457f4e;
}

.comment-box input:focus {
  outline: none;
  border-color: #54a673;
}

.tabs {
  display: flex;
  gap: 10px;
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
  border: 1px solid #ddd;
  padding: 15px;
  margin: 10px 0;
  border-radius: 8px;
}

.meal-images {
  display: flex;
  gap: 15px;
}

img {
  max-width: 200px;
  max-height: 200px;
  object-fit: cover;
}

/* 검색창 스타일 */
.search-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.search-bar input {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  width: 200px;
}

.search-bar button {
  padding: 10px;
  border-radius: 5px;
  cursor: pointer;
  background-color: #54a673;
  color: white;
}

/* 검색된 유저 목록 스타일 */
.user-list {
  margin-top: 20px;
}

.user-card {
  padding: 10px;
  border: 1px solid #ddd;
  margin: 5px 0;
  cursor: pointer;
  border-radius: 5px;
  background-color: #f9f9f9;
}

.user-card:hover {
  background-color: #f0f0f0;
}
</style>
