<template>
  <div class="container py-4">
    <h3>피드 탭</h3>
    <div class="row">
      <!-- 왼쪽: UserAside (0.5/12) -->
      <div class="col-md-0.5">
        <UserAside />
      </div>

      <!-- 오른쪽: UserFeed (11.5/12) -->
      <div class="col-md-11.5 right">
        <UserFeed />
      </div>
    </div>
  </div>
</template>

<script setup>
  import UserAside from '@/components/user/UserAside.vue'
  import UserFeed from '@/components/user/UserFeed.vue'
</script>

<style scoped>
.row {
  margin-left: 350px; /* 사이드바 너비만큼 여백을 추가 */
}


@media (max-width: 992px) {
  .row {
    margin-left: 0; /* 작은 화면에서는 사이드바가 아래로 밀리므로 여백을 없앰 */
  }
}

.right {
  margin-top: -20px;
}
</style>
