<template>
  <div class="container py-4">
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
.user-aside {
  position: fixed;
  top: 20px;
  left: 0;
  width: 350px; /* UserAside 고정 너비 */
  height: calc(100vh - 20px); /* 화면 높이에서 여백만큼 빼기 */
  background-color: #f8f9fa;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
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
