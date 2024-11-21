<template>
  <!-- 로그인 페이지가 아닌 경우에만 네비게이션 바를 렌더링 -->
  <TheHeaderNav v-if="!isLoginPage && !isSignupPage &&!isFindIdPage && !isFindPwPage" />
  <RouterView class="main-content" />
</template>

<script setup>
import { useRoute } from 'vue-router'
import { computed } from 'vue'
import TheHeaderNav from './components/common/TheHeaderNav.vue'

// 현재 라우트 정보를 가져옴
const route = useRoute()

// 로그인 페이지 여부를 계산된 속성으로 처리
const isLoginPage = computed(() => route.name === 'login')
const isSignupPage = computed(() => route.name === 'signup')
const isFindIdPage = computed(() => route.name === 'find-id')
const isFindPwPage = computed(() => route.name === 'find-pw')

</script>

<style scoped>
.main-content {
  margin: 3% 5%; /* 네비게이션 바 높이에 맞게 여백 추가 */
  margin-top: 80px;
}
</style>