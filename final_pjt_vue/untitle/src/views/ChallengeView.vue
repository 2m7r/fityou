<<<<<<< HEAD
<template>
  <div class="container py-4">
=======
<template> 
  <div class="container-fluid py-4">
>>>>>>> e25b799800bba5e65348a45c0eb2a566ac760567
    <div class="row">
      <!-- 왼쪽: UserAside (fixed 위치) -->
      <div class="user-aside">
        <UserAside />
      </div>

      <!-- 오른쪽: ChallengesMy와 ChallengesRecommended -->
      <div class="right">
        <ChallengesMy />
        <ChallengesRecommended />
      </div>
    </div>
  </div>
</template>

<script setup>
import UserAside from '@/components/user/UserAside.vue';
import ChallengesRecommended from '@/components/challenge/ChallengesRecommended.vue';
import ChallengesMy from '@/components/challenge/ChallengesMy.vue';
</script>

<style scoped>
/* 전체 화면에서 UserAside 고정 위치 설정 */
<<<<<<< HEAD
=======
=======
/* 전체 레이아웃에 box-sizing: border-box 적용 */
* {
  box-sizing: border-box;
}

/* container-fluid에 max-width 적용 */
.container-fluid {
  max-width: 93%; /* 페이지가 화면을 벗어나지 않도록 */
  overflow-x: hidden; /* 수평 스크롤 방지 */
}

.row {
  display: flex;
  margin: 0;
  height: 100vh;
  overflow: hidden; /* row에서 스크롤 막기 */
  width: 100%;
}

.row > * {
  max-width: 100%; /* 자식 요소가 부모의 너비를 넘지 않도록 제한 */
}

/* 왼쪽 UserAside 고정 영역 */
>>>>>>> vue
>>>>>>> e25b799800bba5e65348a45c0eb2a566ac760567
.user-aside {
  position: fixed;
  top: 20px;
  left: 0;
<<<<<<< HEAD
  width: 350px; /* UserAside 고정 너비 */
  height: calc(100vh - 20px); /* 화면 높이에서 여백만큼 빼기 */
  background-color: #f8f9fa;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
=======
  width: 350px; /* 고정 너비 */
  height: 100vh; /* 전체 화면 높이 */
  /* background-color: #f8f9fa; 배경 색상 적용 */
  padding: 20px;
  /* box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); 그림자 적용 */
>>>>>>> e25b799800bba5e65348a45c0eb2a566ac760567
  z-index: 100;
}

/* 오른쪽 콘텐츠 영역: UserAside의 너비를 제외하고 나머지 화면 차지 */
.right {
  margin-left: 350px; /* UserAside 너비만큼 왼쪽 여백 */
  padding-top: 20px;
  padding-left: 20px;
  padding-right: 20px;
  overflow-x: auto;
  box-sizing: border-box;
  width: calc(100vw - 350px); /* 화면 너비에서 UserAside 영역 너비를 제외한 값 */
  display: flex;
  flex-direction: column;
}

/* 작은 화면에서 레이아웃 조정 */
@media (max-width: 992px) {
  .right {
    margin-left: 0;
    width: 100%; /* 화면 크기가 작아지면 100%로 설정 */
  }

  .user-aside {
    position: relative;
    width: 100%;
    margin-left: 0;
    top: 0;
  }
}
</style>
