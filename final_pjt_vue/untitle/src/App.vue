<template>
  <!-- 로그인 페이지가 아닌 경우에만 네비게이션 바를 렌더링 -->
<<<<<<< HEAD
  <TheHeaderNav v-if="!isLoginPage && !isSignupPage && !ispreferredExercise && !isFindIdPage && !isFindPwPage" />
  
  
  <!-- 전역 오버레이 -->
  <div v-if="isOverlayVisible" class="overlay" @click="closeOverlay"></div>
 
 
  <RouterView :showOverlay="showOverlay" class="main-content" />
=======
  <TheHeaderNav v-if="!isLoginPage && !isSignupPage &&!isFindIdPage && !ispreferredExercise && !isFindPwPage" />
  <RouterView class="main-content" />
>>>>>>> master
</template>

<script setup>
import { useRoute } from 'vue-router'
import { ref,computed } from 'vue'
import TheHeaderNav from './components/common/TheHeaderNav.vue'

// 현재 라우트 정보를 가져옴
const route = useRoute()

// 로그인 페이지 여부를 계산된 속성으로 처리
const isLoginPage = computed(() => route.name === 'login')
const isSignupPage = computed(() => route.name === 'signup')
const ispreferredExercise = computed(() => route.name === 'preferredExercise')
const isFindIdPage = computed(() => route.name === 'find-id')
const isFindPwPage = computed(() => route.name === 'find-pw')

<<<<<<< HEAD
// 오버레이 상태 관리
const isOverlayVisible = ref(false)

// 오버레이 닫기
const closeOverlay = () => {
  isOverlayVisible.value = false
}

// 오버레이 상태를 변경하는 함수 (UserAside.vue에서 호출됨)
const showOverlay = (isVisible) => {
  isOverlayVisible.value = isVisible
}
=======
>>>>>>> master

</script>

<style scoped>
.main-content {
  margin: 3% 5%; /* 네비게이션 바 높이에 맞게 여백 추가 */
  margin-top: 80px;
}


/* 오버레이 스타일 */
/* 전역 오버레이 스타일 */
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 1050; /* 오버레이의 z-index 값을 높여서 모든 요소 위에 표시되도록 설정 */
}

/* 네비게이션 바의 z-index를 낮춰서 오버레이 아래로 설정 */
.custom-navbar {
  z-index: 100; /* 네비게이션 바의 z-index 값을 100으로 설정하여 오버레이보다 뒤로 */
}

.logo-container {
  z-index: 101; /* 로고의 z-index 값을 101로 설정하여 네비게이션 바보다는 위로 오지만, 오버레이보다 아래로 */
}


</style>