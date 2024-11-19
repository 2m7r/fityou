<template>
  <div class="container py-4">
    <div class="row">
      <!-- 왼쪽: UserAside (0.5/12) -->
      <div class="col-md-3">
        <UserAside />
      </div>

      <!-- 오른쪽: UserFeed (8.5/12) -->
      <div class="col-md-9 right">
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
.row {
  margin-left: 0; /* 오른쪽 영역을 꽉 채우도록 여백을 없앰 */
}

/* 오른쪽 영역을 가득 채우도록 수정 */
.right {
  margin-top: 20px;
}
</style>
