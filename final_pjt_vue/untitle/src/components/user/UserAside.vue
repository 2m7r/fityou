<template>
  <div class="user-aside">
    <!-- 사용자 프로필 사진 -->
    <div class="profile">
      <img src="@/assets/profile.jpg" alt="User Profile" class="profile-img" />
    </div>

    <!-- 사용자 이름 -->
    <h4 class="user-name">{{ userStore.userName }}님, FITYOU!</h4>

    <!-- 로그아웃 버튼 -->
    <button class="btn btn-link logout-btn" @click="logout">로그아웃</button>

    <!-- 식단일기 작성 버튼 -->
    <button
      class="btn btn-lg btn-diet my-2 d-flex align-items-center rounded-full"
      @click="openDietLogModal"
    >
      <i class="bi bi-pencil-square me-2"></i>식단일기 작성
    </button>

    <!-- 운동일기 작성 버튼 -->
    <button
      class="btn btn-lg btn-exercise d-flex align-items-center rounded-full"
      @click="openWorkoutLogModal"
    >
      <i class="bi bi-bicycle me-2"></i>운동일기 작성
    </button>

    <!-- 운동일기 작성과 캘린더 사이 여백 추가 -->
    <div class="my-3"></div>

    <!-- 캘린더 -->
    <UserCalender class="calender" />

    <!-- 식단일기 모달 --> <!-- userId를 userStore에서 가져오기 -->
    <DietLogModal
      v-if="isDietLogModalOpen"
      @close="closeDietLogModal"
      :userId="userStore.user?.userId"  
    />
  </div>
</template>

<script setup>
import { ref } from "vue";
import DietLogModal from "@/components/feed/DietLogModal.vue";
import UserCalender from "@/components/user/UserCalender.vue";
import { useUserStore } from "@/stores/userStore";
import { useRouter } from "vue-router";


// 'showOverlay'는 App.vue에서 props로 받습니다.
const props = defineProps({
  showOverlay: Function,
});

// 이벤트 정의: 'showOverlay' 이벤트를 App.vue로 전달
const emit = defineEmits();

// 사용자 상태
const userStore = useUserStore();
const router = useRouter();

// 모달 상태 관리
const isDietLogModalOpen = ref(false);

// 식단일기 작성 모달 열기
const openDietLogModal = () => {
  isDietLogModalOpen.value = true;
  if (typeof props.showOverlay === "function") {
    props.showOverlay(true); // App.vue에 오버레이 표시 요청
  }
};

// 식단일기 작성 모달 닫기
const closeDietLogModal = () => {
  isDietLogModalOpen.value = false;
  if (typeof props.showOverlay === "function") {
    props.showOverlay(false); // App.vue에 오버레이 숨기기 요청
  }
};

// 운동일기 작성 페이지로 이동
const goToExerciseLog = () => {
  router.push("/exercise/create");
};


// 로그아웃 처리
const logout = () => {
  sessionStorage.removeItem("access-token"); // 세션 스토리지에서 토큰 삭제
  userStore.clearUser(); // 사용자 정보 초기화
  router.push({ name: "login" }); // 로그인 페이지로 리디렉션
};

// sessionStorage에서 userId 가져오기
const user = JSON.parse(sessionStorage.getItem("user"));
const userId = user ? user.userId : null;  // userId가 없으면 null을 반환

</script>

<style scoped>
.user-aside {
  position: relative; /* 고정된 위치를 제거하고 흐름에 맞게 배치 */
  top: 20px; /* 위치를 상단으로 미세 조정 */
  left: 0;
  width: 350px; /* 기본 너비는 350px */
  height: calc(100vh - 180px); /* height를 조정하여 화면에 맞게 고정 */
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  overflow-y: auto;
  overflow-x: hidden;
  z-index: 100;
  margin-left: 3%; /* 왼쪽 여백을 더 추가 */
  margin-top: 120px;
}

/* 화면 크기가 992px 이하일 때, 사이드바의 너비를 100%로 변경 */
@media (max-width: 992px) {
  .user-aside {
    position: relative; /* 고정된 위치가 아니라 흐름에 따라 위치 */
    width: 100%; /* 너비를 100%로 설정 */
    margin-left: 0; /* 왼쪽 여백 없앰 */
    top: 0; /* 화면 상단에 고정 */
  }
}

/* 프로필 사진 확대 */
.profile-img {
  width: 300px;
  height: 300px;
  border-radius: 50%;
  object-fit: cover;
  border: 4px solid #54a673;
  margin: 20px 50px;
}

/* 사용자 이름 스타일 */
.user-name {
  font-size: 22px; /* 글씨 크기 약간 키움 */
  font-weight: bold;
  margin-bottom: 20px;
}

/* 버튼 스타일 - 로그아웃 버튼 */
.logout-btn {
  font-size: 16px;
  text-decoration: underline;
  color: #54a673;
  background: transparent;
  border: none;
  margin-top: -10px;
  margin-bottom: 5%;
}

.logout-btn:hover {
  color: #54a673;
}

.router-link {
  width: 100%;
  border-radius: 50px;
}

.router-link i {
  font-size: 18px;
}
s

/* 버튼 크기 고정 */
.my-2 {
  margin-top: 15px;
}

.btn-lg {
  font-size: 16px;
  padding: 12px 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%; /* 버튼 너비를 100%로 설정 */
  margin-top: 3%;
  margin-bottom: 10%;
  height: 50px; /* 버튼의 높이를 고정 */
}

.btn-diet,
.btn-exercise {
  background-color: #54a673;
  color: white;
}

.btn-diet:hover,
.btn-exercise:hover {
  background-color: #4a8e5c;
}

/* 완전 둥글게 설정 */
.rounded-full {
  border-radius: 50px !important;
}

/* 운동일기 작성과 캘린더 사이 여백 */
.my-3 {
  margin-top: 20px;
}
</style>
