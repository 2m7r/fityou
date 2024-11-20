<template>
  <div>
    <TheHeaderNav />
    <RouterView class="main-content" />
  </div>
</template>

<script setup>
import TheHeaderNav from './components/common/TheHeaderNav.vue'
</script>

<style scoped>
.main-content {
  margin: 3% 5%; /* 네비게이션 바 높이에 맞게 여백 추가 */
  margin-top: 80px;
}
</style>
