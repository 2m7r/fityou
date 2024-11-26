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
          @focus="showRecommendedUsers = true"
        />
        <button @click="searchUser">
          <i class="bi bi-search"></i>
          <!-- 검색 아이콘 -->
        </button>
        <button @click="resetSearch">
          <i class="bi bi-arrow-clockwise"></i>
          <!-- 초기화 아이콘 -->
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
        <!-- 팔로우 버튼 표시 (내 자신을 제외) -->
        <div v-if="user.userId !== userId">
          <!-- 내 자신을 제외하는 조건 -->
          <button
            v-if="!isFollowing(user.userId)"
            @click="followUser(user)"
            class="follow-btn"
          >
            <i class="bi bi-plus"></i>
            <!-- 팔로우 버튼을 + 아이콘으로 -->
          </button>
          <button
            v-if="isFollowing(user.userId)"
            @click="unfollowUser(user)"
            class="follow-btn"
          >
            <i class="bi bi-check"></i>
            <!-- 팔로우 취소 버튼 -->
          </button>
        </div>
      </div>
    </div>

    <!-- 선호운동이 겹치는 추천 유저들 -->
    <div
      v-if="showRecommendedUsers && recommendedUsers.length > 0"
      class="recommended-users"
    >
      <hr />
      <h3>추천 유저</h3>
      <div
        v-for="user in recommendedUsers"
        :key="user.userId"
        class="user-card"
        @click="selectUser(user)"
        @focus="showRecommendedUsers = false"
      >
        <p>{{ user.username }}</p>
        <button
          v-if="!isFollowing(user.userId)"
          @click="followUser(user)"
          class="follow-btn"
        >
          <i class="bi bi-plus"></i>
          <!-- 팔로우 버튼을 + 아이콘으로 -->
        </button>
        <button
          v-if="isFollowing(user.userId)"
          @click="unfollowUser(user)"
          class="follow-btn"
        >
          <i class="bi bi-check"></i>
          <!-- 팔로우 취소 버튼 -->
        </button>
      </div>
    </div>

    <!-- 탭 버튼 -->
    <div class="tabs">
      <input
        type="radio"
        id="diet-tab"
        v-model="currentTab"
        value="diet"
        class="tab-toggle"
      />
      <label for="diet-tab" class="tab-label">
        <i class="bi tab-icon">🥗</i>
      </label>

      <input
        type="radio"
        id="workout-tab"
        v-model="currentTab"
        value="workout"
        class="tab-toggle"
      />
      <label for="workout-tab" class="tab-label">
        <i class="bi tab-icon">🏃</i>
      </label>
    </div>

    <!-- 식단일기 탭 -->
    <div v-if="currentTab === 'diet'" class="diet-tab">
      <!-- 나의 최근 식단일기 하나만 출력 -->
      <div class="my-diet" v-if="myDietLogs.length > 0">
        <div class="log-card">
          <h3>{{ myDietLogs[0].name }}님의 최근 식단일기</h3>
          <p class="diet-date">{{ myDietLogs[0].recordDate }}</p>

          <!-- 식사 이미지 탭 -->
          <div class="meal-tabs">
            <input
              type="radio"
              id="breakfast-tab"
              v-model="mealTab"
              value="breakfast"
              class="meal-toggle"
            />
            <label for="breakfast-tab" class="meal-label">아침</label>

            <input
              type="radio"
              id="lunch-tab"
              v-model="mealTab"
              value="lunch"
              class="meal-toggle"
            />
            <label for="lunch-tab" class="meal-label">점심</label>

            <input
              type="radio"
              id="dinner-tab"
              v-model="mealTab"
              value="dinner"
              class="meal-toggle"
            />
            <label for="dinner-tab" class="meal-label">저녁</label>
          </div>

          <!-- 아침 이미지 -->
          <div
            v-if="mealTab === 'breakfast' && myDietLogs[0].breakfastImagePath"
          >
            <img
              :src="'http://localhost:8080/' + myDietLogs[0].breakfastImagePath"
              alt="Breakfast Image"
              class="meal-img"
            />
          </div>

          <div v-if="mealTab === 'lunch' && myDietLogs[0].lunchImagePath">
            <img
              :src="'http://localhost:8080/' + myDietLogs[0].lunchImagePath"
              alt="Lunch Image"
              class="meal-img"
            />
          </div>

          <div v-if="mealTab === 'dinner' && myDietLogs[0].dinnerImagePath">
            <img
              :src="'http://localhost:8080/' + myDietLogs[0].dinnerImagePath"
              alt="Dinner Image"
              class="meal-img"
            />
          </div>

          <p class="diet-content">{{ myDietLogs[0].content }}</p>

          <!-- 댓글 아이콘 -->
          <button
            @click="toggleComments(myDietLogs[0].dietId, 'diet')"
            class="comment-btn"
          >
            💬
            <!-- 댓글 아이콘 -->
          </button>

          <!-- 댓글창 토글 -->
          <div
            v-for="(comment, index) in comments[
              `${myDietLogs[0].dietId}_diet`
            ]?.slice(0, 3)"
            :key="comment.commentId"
          >
            <strong>{{ comment.name }}</strong> {{ comment.content }}
            <span>{{ getRelativeTime(comment.createdAt) }}</span>
          </div>

          <!-- 전체 댓글 보여주기 (상위 3개 외) -->
          <div
            v-if="showComments[`${myDietLogs[0].dietId}_diet`]"
            class="all-comments"
          >
            <div
              v-for="(comment, index) in comments[
                `${myDietLogs[0].dietId}_diet`
              ]?.slice(3)"
              :key="comment.commentId"
            >
              <p>
                <strong>{{ comment.name }}</strong> {{ comment.content }}
              </p>
              <span>{{ getRelativeTime(comment.createdAt) }}</span>
            </div>
          </div>

          <!-- 댓글 입력창 -->
          <div class="comment-box">
            <input
              v-model="newComment[myDietLogs[0].dietId]"
              placeholder="댓글을 입력하세요"
            />
            <button @click="addComment(myDietLogs[0].dietId, 'diet')">
              댓글 추가
            </button>
          </div>
        </div>
      </div>
      <div v-else>
        <hr />
        <h3>최근 식단일기가 없습니다.... 다이어트 안하셨나요..?</h3>
      </div>

      <!-- 다른 식단일기 리스트 -->
      <div v-for="log in dietLogs" :key="log.dietId" class="log-card">
        <h3>{{ log.name }}</h3>
        <p>
          <strong>{{ log.recordDate }}</strong>
        </p>

        <!-- 식사 이미지 탭 -->
        <div class="meal-tabs">
          <input
            type="radio"
            id="breakfast-tab-{{ log.dietId }}"
            v-model="mealTab"
            value="breakfast"
            class="meal-toggle"
          />
          <label for="breakfast-tab-{{ log.dietId }}" class="meal-label"
            >아침</label
          >

          <input
            type="radio"
            id="lunch-tab-{{ log.dietId }}"
            v-model="mealTab"
            value="lunch"
            class="meal-toggle"
          />
          <label for="lunch-tab-{{ log.dietId }}" class="meal-label"
            >점심</label
          >

          <input
            type="radio"
            id="dinner-tab-{{ log.dietId }}"
            v-model="mealTab"
            value="dinner"
            class="meal-toggle"
          />
          <label for="dinner-tab-{{ log.dietId }}" class="meal-label"
            >저녁</label
          >
        </div>

        <!-- 이미지 표시 -->
        <div v-if="mealTab === 'breakfast' && log.breakfastImagePath">
          <img
            :src="'http://localhost:8080/' + log.breakfastImagePath"
            alt="Breakfast Image"
            class="meal-img"
          />
        </div>

        <div v-if="mealTab === 'lunch' && log.lunchImagePath">
          <img
            :src="'http://localhost:8080/' + log.lunchImagePath"
            alt="Lunch Image"
            class="meal-img"
          />
        </div>

        <div v-if="mealTab === 'dinner' && log.dinnerImagePath">
          <img
            :src="'http://localhost:8080/' + log.dinnerImagePath"
            alt="Dinner Image"
            class="meal-img"
          />
        </div>

        <p class="diet-content">{{ log.content }}</p>

        <!-- 댓글 아이콘 -->
        <button @click="toggleComments(log.dietId, 'diet')" class="comment-btn">
          💬
          <!-- 댓글 아이콘 -->
        </button>

        <!-- 댓글창 토글 -->
        <div
          v-for="(comment, index) in comments[`${log.dietId}_diet`]?.slice(
            0,
            3
          )"
          :key="comment.commentId"
        >
          <strong>{{ comment.name }}</strong> {{ comment.content }}
          <span>{{ getRelativeTime(comment.createdAt) }}</span>
        </div>

        <!-- 전체 댓글 보여주기 (상위 3개 외) -->
        <div v-if="showComments[`${log.dietId}_diet`]" class="all-comments">
          <div
            v-for="(comment, index) in comments[`${log.dietId}_diet`]?.slice(3)"
            :key="comment.commentId"
          >
            <p>
              <strong>{{ comment.name }}</strong> {{ comment.content }}
            </p>
            <span>{{ getRelativeTime(comment.createdAt) }}</span>
          </div>
        </div>

        <!-- 댓글 입력창 -->
        <div class="comment-box">
          <input
            v-model="newComment[log.dietId]"
            placeholder="댓글을 입력하세요"
          />
          <button @click="addComment(log.dietId, 'diet')">댓글 추가</button>
        </div>
      </div>
    </div>

    <!-- 운동일기 탭 -->
    <div v-if="currentTab === 'workout'" class="workout-tab">
      <div class="my-workout">
        <!-- 나의 최근 운동일기 하나만 출력 -->
        <div v-if="myWorkoutLogs.length > 0" class="log-card">
          <h3>{{ myWorkoutLogs[0].name }}의 최근 운동일기</h3>

          <!-- 날짜 -->
          <p class="diet-date">{{ myWorkoutLogs[0].recordDate }}</p>

          <p class="workout-content">{{ myWorkoutLogs[0].description }}</p>

          <div
            v-for="exercise in myWorkoutLogs[0].exercises"
            :key="exercise.id"
            class="exc-content"
          >
            <strong>{{ exercise.exerciseName }}</strong>
            {{ exercise.weight }} kg {{ exercise.reps }} 회
            {{ exercise.sets }} 세트
          </div>
        </div>

        <div v-else>
          <hr />
          <h3>최근 운동일기가 없습니다.... 운동... 안하셨나요..?</h3>
        </div>

        <!-- 댓글 아이콘 -->
        <button
          @click="toggleComments(myWorkoutLogs[0].workoutId, 'workout')"
          class="comment-btn"
        >
          💬
          <!-- 댓글 아이콘 -->
        </button>

        <!-- 댓글창 토글 -->
        <div
          v-for="(comment, index) in comments[
            `${myWorkoutLogs[0].workoutId}_workout`
          ]?.slice(0, 3)"
          :key="comment.commentId"
        >
          <strong>{{ comment.name }}</strong> {{ comment.content }}
          <span>{{ getRelativeTime(comment.createdAt) }}</span>
        </div>

        <!-- 전체 댓글 보여주기 (상위 3개 외) -->
        <div
          v-if="showComments[`${myWorkoutLogs[0].workoutId}_workout`]"
          class="all-comments"
        >
          <div
            v-for="(comment, index) in comments[
              `${myWorkoutLogs[0].workoutId}_workout`
            ]?.slice(3)"
            :key="comment.commentId"
          >
            <p>
              <strong>{{ comment.name }}</strong> {{ comment.content }}
            </p>
            <span>{{ getRelativeTime(comment.createdAt) }}</span>
          </div>
        </div>

        <!-- 댓글 입력창 -->
        <div class="comment-box">
          <input
            v-model="newComment[myWorkoutLogs[0].workoutId]"
            placeholder="댓글을 입력하세요"
          />
          <button @click="addComment(myWorkoutLogs[0].workoutId, 'workout')">
            댓글 추가
          </button>
        </div>
      </div>

      <hr />
      <!-- 팔로우한 유저들의 운동일기 -->
      <div class="my-workout">
        <div v-for="log in workoutLogs" :key="log.workout_id" class="log-card">
          <!-- 이름 -->
          <h3>{{ log.name }}</h3>

          <!-- 날짜 -->
          <p class="diet-date">
            {{ log.recordDate }}
          </p>

          <!-- 내용 -->
          <p class="workout-content">{{ log.description }}</p>

          <div
            v-for="exercise in log.exercises"
            :key="exercise.id"
            class="exc-content"
          >
            <strong>{{ exercise.exerciseName }}</strong>
            {{ exercise.weight }} kg {{ exercise.reps }} 회
            {{ exercise.sets }} 세트
          </div>
        </div>

        <!-- 댓글 아이콘 -->
        <button
          @click="toggleComments(log.workoutId, 'workout')"
          class="comment-btn"
        >
          💬
          <!-- 댓글 아이콘 -->
        </button>

        <!-- 댓글창 토글 -->
        <div
          v-for="(comment, index) in comments[
            `${log.workoutId}_workout`
          ]?.slice(0, 3)"
          :key="comment.commentId"
        >
          <strong>{{ comment.name }}</strong> {{ comment.content }}
          <span>{{ getRelativeTime(comment.createdAt) }}</span>
        </div>

        <!-- 전체 댓글 보여주기 (상위 3개 외) -->
        <div
          v-if="showComments[`${log.workoutId}_workout`]"
          class="all-comments"
        >
          <div
            v-for="(comment, index) in comments[
              `${log.workoutId}_workout`
            ]?.slice(3)"
            :key="comment.commentId"
          >
            <p>
              <strong>{{ comment.name }}</strong> {{ comment.content }}
            </p>
            <span>{{ getRelativeTime(comment.createdAt) }}</span>
          </div>
        </div>

        <!-- 댓글 입력창 -->
        <div class="comment-box">
          <input
            v-model="newComment[log.workoutId]"
            placeholder="댓글을 입력하세요"
          />
          <button @click="addComment(log.workoutId, 'workout')">
            댓글 추가
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, watch, computed } from "vue";
import apiClient from "@/components/api/apiClient";

// sessionStorage에서 'user' 키로 객체를 가져오기
const user = ref(JSON.parse(sessionStorage.getItem("user")));

// 현재 선택된 탭
const currentTab = ref("diet");

// 유저 데이터 처리
const userId = ref(user.value ? user.value.userId : null);
const name = ref(user.value ? user.value.name : null);

const searchUserId = ref(""); // 검색한 userId
const searchedUsers = ref([]); // 검색된 유저 목록
const recommendedUsers = ref([]); // 선호운동이 겹치는 추천 유저 목록
const showRecommendedUsers = ref(false); // 추천 유저 목록 표시 여부

const dietLogs = ref([]); // 팔로우한 유저들의 식단일기 배열
const myDietLogs = ref([]); // 내 식단일기 배열
const workoutLogs = ref([]); // 팔로우한 유저들의 운동일기 배열
const myWorkoutLogs = ref([]); // 내 운동일기 배열

// 각 일기 항목에 대한 댓글 관리
const comments = ref({}); // {logId_logType: [댓글들]}
const showComments = ref({}); // 각 logId에 대한 댓글창 표시 여부

const selectedDietLog = ref(null); // 선택한 식단일기
const isDietLogModalOpen = ref(false); // 모달 열림 여부

const newComment = ref({}); // 새로 추가할 댓글 내용

// 메서드 작성 (computed나 method로)
const getRelativeTime = (createdAt) => {
  const now = new Date();
  const createdAtDate = new Date(createdAt); // 문자열을 Date 객체로 변환

  const diffInSeconds = Math.floor((now - createdAtDate) / 1000); // 초 단위 차이
  const diffInMinutes = Math.floor(diffInSeconds / 60); // 분 단위 차이
  const diffInHours = Math.floor(diffInMinutes / 60); // 시간 단위 차이
  const diffInDays = Math.floor(diffInHours / 24); // 일 단위 차이

  if (diffInMinutes < 1) {
    return "방금 전"; // 1분 이내
  } else if (diffInMinutes < 60) {
    return `${diffInMinutes}분 전`; // 1시간 미만
  } else if (diffInHours < 24) {
    return `${diffInHours}시간 전`; // 24시간 이내
  } else {
    return `${diffInDays}일 전`; // 1일 이상
  }
};

// 댓글 추가 함수
const addComment = async (logId, logType) => {
  if (!newComment.value[logId]) return; // 입력값이 없으면 리턴

  try {
    const response = await apiClient.post("/api-comment/create", {
      targetId: logId,
      userId: userId.value,
      name: name.value,
      content: newComment.value[logId],
      targetType: logType,
      createdAt: new Date().toISOString(), // 현재 시간을 ISO 형식으로 추가
    });

    // 댓글 추가 후 댓글 목록 업데이트
    fetchComments(logId, logType);
    newComment.value[logId] = ""; // 댓글 입력창 초기화
    toggleComments(logId, logType);
  } catch (error) {
    console.error("댓글 추가 실패", error);
  }
};

// 댓글 불러오기 함수
const fetchComments = async (logId, logType) => {
  try {
    const response = await apiClient.get("/api-comment/comment", {
      params: { targetId: logId, targetType: logType },
    });
    comments.value[`${logId}_${logType}`] = response.data;
  } catch (error) {
    console.error("댓글 불러오기 실패", error);
  }
};

// 댓글 토글 함수
const toggleComments = (logId, logType) => {
  const key = `${logId}_${logType}`;
  if (showComments.value[key] !== undefined) {
    showComments.value[key] = !showComments.value[key];
  } else {
    // 댓글이 없으면 댓글을 불러오고 상태 설정
    fetchComments(logId, logType).then(() => {
      showComments.value[key] = true;
    });
  }
};

// 팔로우 목록 상태
const followingUsers = ref([]); // 팔로우한 유저들의 목록

// 팔로우 상태 확인 함수 (computed로 변경)
const isFollowing = computed(() => {
  return (otherUserId) => {
    return followingUsers.value.includes(otherUserId); // 팔로우 목록에 userId가 포함되어 있으면 true
  };
});

// 팔로우 목록 불러오기
const fetchFollowingUsers = async () => {
  try {
    const response = await apiClient.get(
      `/api-follow/following/${userId.value}`
    ); // 팔로우 목록 API 호출
    followingUsers.value = response.data; // 팔로우한 유저들의 userId만 배열로 저장
  } catch (error) {
    console.error("팔로우 목록 불러오기 실패", error);
  }
};

// 팔로우하기
const followUser = async (user) => {
  try {
    // 팔로우 API 호출
    await apiClient.post("/api-follow/follow", {
      followerId: userId.value,
      followingId: user.userId,
    });

    // 팔로우 후 팔로우 목록 갱신
    await fetchFollowingUsers(); // 팔로우 목록을 다시 불러오기

    resetSearch();
    window.location.reload();
  } catch (error) {
    console.error("팔로우 실패", error);
  }
};

// 팔로우 취소하기
const unfollowUser = async (user) => {
  try {
    await apiClient.delete("/api-follow/unfollow", {
      params: {
        followerId: userId.value,
        followingId: user.userId,
      },
    });

    // 팔로우 취소 후 팔로우 목록 갱신
    await fetchFollowingUsers(); // 팔로우 목록을 다시 불러오기

    resetSearch();
    window.location.reload();
  } catch (error) {
    console.error("팔로우 취소 실패", error);
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
      const response = await apiClient.get(
        `/api-user/search/${searchUserId.value}`
      );
      searchedUsers.value = response.data;
    } catch (error) {
      console.error("유저 검색 실패", error);
    }
  }
};

// 선호운동이 겹치는 유저 추천 함수
const recommendUsers = async () => {
  try {
    const response = await apiClient.get("/api-user/recommend", {
      params: {
        userId: userId.value,
      },
    });
    recommendedUsers.value = response.data;
  } catch (error) {
    console.error("추천 유저 불러오기 실패", error);
  }
};

// 선택한 유저의 일기 정보 불러오기
const selectUser = async (user) => {
  userId.value = user.userId;
  searchUserId.value = user.userId;
  fetchLogs(currentTab.value);
  searchedUsers.value = [];
};

// 로그 데이터 불러오기
const fetchLogs = async (tab) => {
  try {
    if (!userId.value) {
      console.error("User ID가 존재하지 않습니다.");
      return;
    }

    if (tab === "diet") {
      const response = await apiClient.get(`/api-diet/follow/${userId.value}`);
      dietLogs.value = response.data;
      const myresponse = await apiClient.get(`/api-diet/feed/${userId.value}`);
      myDietLogs.value = myresponse.data;
    } else if (tab === "workout") {
      const response = await apiClient.get(
        `/api-workout/follow/${userId.value}`
      );
      workoutLogs.value = response.data;
      const myresponse = await apiClient.get(
        `/api-workout/feed/${userId.value}`
      );
      myWorkoutLogs.value = myresponse.data;
    }
  } catch (error) {
    console.error("로그 불러오기 실패", error);
  }
};

watch(
  () => userId.value,
  (newUserId) => {
    if (newUserId) {
      fetchLogs(currentTab.value);
    }
  }
);

watch(
  () => showRecommendedUsers.value,
  (newValue) => {
    if (newValue) {
      recommendUsers();
    }
  }
);

watch(
  () => currentTab.value,
  (newTab) => {
    fetchLogs(newTab);
  }
);

// 검색 초기화 함수
const resetSearch = () => {
  searchUserId.value = "";
  searchedUsers.value = [];
  userId.value = user.value.userId;
  showRecommendedUsers.value = false;
  fetchLogs(currentTab.value);
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

const mealTab = ref("breakfast"); // 식사 이미지 탭

onMounted(() => {
  if (userId.value) {
    fetchLogs(currentTab.value);
    fetchFollowingUsers();
  }
});
</script>

<style scoped>
.comment-btn {
  background: none;
  border: none;
  font-size: 16px;
  cursor: pointer;
}

.comment-box {
  margin-top: 10px;
}

.comment-box input {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 5px;
  width: 80%;
}

.comment-input {
  margin-top: 10px;
}

.comment-input input {
  width: 80%;
  padding: 5px;
}

.comment-input button {
  padding: 5px 10px;
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
  max-width: 1300px;
  margin: 0 auto;
}

.workout-tab {
  padding: 15px 40px;
  background-color: #ffffff;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  border-radius: 15px; /* 모서리 둥글게 */
  max-width: 1300px;
  margin: 0 auto;
}

.diet-tab .log-card {
  margin-bottom: 30px; /* 카드 간격 추가 */
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 15px;
}

.my-workout .log-card {
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
  padding: 10px; /* 테두리와 이모지 사이 여백 */
  display: inline-block; /* 이모지가 한 줄에 표시되게 */
  text-align: center;
  line-height: 1; /* 이모지 세로 정렬 */
  transition: all 0.3s ease; /* 호버 효과 부드럽게 */
  font-style: normal;
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
  height: 1000px;
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
  font-family: "Medium";
  font-size: 1.5rem;
  background-color: #ffffff;
  border-radius: 20px;
  height: 150px;
  box-shadow: rgba(0, 0, 0, 0.5);
  padding: 20px;
}

.workout-content {
  font-family: "Medium";
  font-size: 1.5rem;
  background-color: #ffffff;
  border-radius: 20px;
  height: 150px;
  box-shadow: rgba(0, 0, 0, 0.5);
  padding: 20px;
}

.exc-content {
  margin: 0 5px;
  font-size: 1.3rem;
  border: 1px solid #ccc;
  padding: 20px;
  border-radius: 20px;
}
</style>
