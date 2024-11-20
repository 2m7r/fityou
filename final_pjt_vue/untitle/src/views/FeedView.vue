<template>
  <div class="container py-4">
    <h3>피드 탭</h3>
    <div class="row">
      <!-- 왼쪽: UserAside (3/12) -->
      <div class="col-md-3">
        <UserAside />
      </div>

      <!-- 오른쪽: UserFeed (9/12) -->
      <div class="col-md-9">
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
</style>
