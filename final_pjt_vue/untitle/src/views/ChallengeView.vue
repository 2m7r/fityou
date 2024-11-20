<template>
  <div class="container-fluid py-4">
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
.user-aside {
  position: fixed;
  top: 0;
  left: 0;
  width: 350px; /* 고정 너비 */
  height: 100vh; /* 전체 화면 높이 */
  background-color: #f8f9fa;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  z-index: 100;
}

/* 오른쪽 영역: 화면 크기에 맞춰 확장 */
.right {
  margin-left: 350px; /* UserAside 너비만큼 여백 */
  padding: 20px;
  box-sizing: border-box;
  overflow-x: hidden; /* 가로 스크롤 방지 */
  display: flex;
  flex-direction: column;
  height: 100vh;
  overflow-y: auto; /* 세로 스크롤 필요시 */
  width: 100%;
  max-width: 84%; /* 부모를 벗어나지 않도록 */
}


/* 작은 화면에서 레이아웃 조정 */
@media (max-width: 992px) {
  .right {
    margin-left: 0;
    width: 100%; /* 화면 크기 전체를 사용 */
  }

  .user-aside {
    position: relative;
    width: 100%;
    margin-left: 0;
    top: 0;
  }
}

</style>
