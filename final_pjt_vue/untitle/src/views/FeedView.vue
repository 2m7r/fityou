<template>
  <div class="container py-4">
    <div class="row">
      <!-- 왼쪽: UserAside (0.5/12) -->
      <div class="col-md-3">
        <UserAside />
      </div>

      <!-- 오른쪽: UserFeed (8.5/12) -->
      <div class="col-md-9 right">
        <UserFeed/>
      </div>
    </div>
  </div>
</template>

<script setup>
import UserAside from '@/components/user/UserAside.vue';
import UserFeed from '@/components/user/UserFeed.vue';
</script>

<style scoped>
/* 오른쪽 영역을 꽉 채우도록 수정 */
.right {
  display: flex;
  flex-direction: column;
  margin-top: 20px;  /* 위쪽 여백 */
  margin-left: 380px; /* UserAside 너비만큼 왼쪽 여백을 추가 */
  width: calc(100%); /* 오른쪽 영역의 크기 설정 */
}

/* UserAside의 고정 위치 스타일 */
.user-aside {
  position: fixed; /* 화면에서 고정 위치 */
  top: 20px;
  left: 0;
  width: 380px; /* UserAside의 너비 */
  height: calc(100vh - 20px); /* 화면 전체 높이에서 상단 여백을 빼고 높이 설정 */
  background-color: #f8f9fa;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  z-index: 100;
}
</style>
