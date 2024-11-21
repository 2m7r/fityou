<template>
  <div class="user-aside">
    <!-- 사용자 프로필 사진 -->
    <div class="profile">
      <img src="@/assets/profile.jpg" alt="User Profile" class="profile-img" />
    </div>

    <!-- 사용자 이름 -->
    <h4 class="user-name">홍길동</h4>

    <!-- 로그아웃 버튼 -->
    <button class="btn btn-link logout-btn" @click="logout">로그아웃</button>

    <!-- 식단일기 작성 버튼 -->
    <button
      class="btn btn-lg btn-diet my-2 d-flex align-items-center rounded-full"
      @click="goToDietLog"
    >
      <i class="bi bi-pencil-square me-2"></i>식단일기 작성
    </button>

    <!-- 운동일기 작성 버튼 -->
    <button
      class="btn btn-lg btn-exercise d-flex align-items-center rounded-full"
      @click="goToExerciseLog"
    >
      <i class="bi bi-bicycle me-2"></i>운동일기 작성
    </button>

    <!-- 운동일기 작성과 캘린더 사이 여백 추가 -->
    <div class="my-3"></div>

    <!-- 캘린더 -->
    <UserCalender class="calender"/>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import UserCalender from "@/components/user/UserCalender.vue";

const router = useRouter();

const logout = () => {
  // 로그아웃 처리
  localStorage.removeItem("isLoggedIn"); // localStorage에서 로그인 상태 삭제

  // 2. 세션 또는 인증 토큰이 있다면 제거 (필요시)
  localStorage.removeItem("token"); // 예: JWT 토큰 제거

  // 3. 로그아웃 후 로그인 페이지로 리디렉션
  router.push("/login");
};





const goToDietLog = () => {
  //만약 값이 없으면 등록
  router.push("/diet/create");
  //값이 있으면 수정
  // router.push(`/diet/${    }`)
};

// const goToDietLog = (userId) => {
//   // 예시로, 유저가 오늘 날짜에 등록한 식단 값이 없으면 등록하는 로직
//   const dietExist = checkIfDietExistsToday(userId); // 이 함수는 유저가 오늘 등록한 식단이 있는지 확인하는 함수라고 가정

//   if (!dietExist) {
//     router.push('/diet/create'); // 값이 없으면 등록 페이지로 이동
//   } else {
//     router.push(`/diet/${userId}`); // 값이 있으면 수정 페이지로 이동
//   }
// }

const goToExerciseLog = () => {
  router.push("/workout/create");
};
</script>

<style scoped>
.user-aside {
  position: relative; /* 고정된 위치를 제거하고 흐름에 맞게 배치 */
  top: 20px; /* 위치를 상단으로 미세 조정 */
  left: 0;
  width: 350px; /* 기본 너비는 350px */
  height: calc(100vh - 100px); /* height를 조정하여 화면에 맞게 고정 */
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
  margin-bottom: 500px;
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
/* .user-aside {
  position: fixed;
  top: 150px;
  left: 0;
  width: 350px; // 기본 너비는 350px
  height: calc(100vh - 200px);
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  overflow-y: auto;
  z-index: 100;
  max-height: 100vh;
  overflow: hidden;
  margin-left: 3%; //왼쪽 여백을 더 추가 
} */

/* 화면 크기가 992px 이하일 때, 사이드바의 너비를 100%로 변경
@media (max-width: 992px) {
  .user-aside {
    position: relative; //고정된 위치가 아니라 흐름에 따라 위치 *
    width: 100%; // 너비를 100%로 설정 *
    margin-left: 0; // 왼쪽 여백 없앰 *
    top: 0; // 화면 상단에 고정 *
  }
} */

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
