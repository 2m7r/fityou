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
        <ChallengeList />
      </div>
    </div>
  </div>
</template>

<script setup>
import UserAside from '@/components/user/UserAside.vue';
import ChallengesRecommended from '@/components/challenge/ChallengesRecommended.vue';
import ChallengesMy from '@/components/challenge/ChallengesMy.vue';
import ChallengeList from '@/components/challenge/ChallengeList.vue';
</script>

<style scoped>
.row {
  width: calc(94vw);
}


/* 전체 화면에서 UserAside 고정 위치 설정 */
.user-aside {
  position: fixed;
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
  display: flex;
  flex-direction: column;
  width: calc(100% - 350px);
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
