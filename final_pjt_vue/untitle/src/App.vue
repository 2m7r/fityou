<template>
  <div class="all">

    <!-- 로그인, 회원가입, 선호 운동, 아이디 찾기, 비밀번호 찾기 페이지 제외 -->
    <TheHeaderNav v-if="!excludedPages.includes(route.name)" />
    
    <!-- 전역 오버레이 -->
    <div v-if="isOverlayVisible" class="overlay" @click="closeOverlay"></div>
    
    <!-- RouterView에 showOverlay prop 전달 -->
    <RouterView :showOverlay="showOverlayHandler" class="main-content" />
  </div>
  </template>

<script setup>
import { useRoute } from 'vue-router'
import { ref, onMounted, watch } from 'vue'
import TheHeaderNav from './components/common/TheHeaderNav.vue'

onMounted(() => {
  document.title = 'FITYOU';  
})

// 현재 라우트 정보를 가져옴
const route = useRoute()

// 제외할 페이지 목록
const excludedPages = ['login', 'signup', 'preferredExercise', 'find-id', 'find-pw'];

// 오버레이 상태 관리
const isOverlayVisible = ref(false)

// 오버레이 상태를 변경하는 함수 (UserAside.vue에서 호출됨)
const showOverlayHandler = (isVisible) => {
  isOverlayVisible.value = isVisible
}

// 오버레이 닫기
const closeOverlay = () => {
  isOverlayVisible.value = false
}

// 라우트가 변경될 때 오버레이 상태를 초기화
watch(route, () => {
  if (excludedPages.includes(route.name)) {
    isOverlayVisible.value = false
  }
})
</script>

<style scoped>

*{
  font-family: 'Medium',sans-serif;
}

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
