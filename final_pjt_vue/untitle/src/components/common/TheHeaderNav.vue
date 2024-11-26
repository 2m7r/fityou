<template>
  <nav class="custom-navbar">
    <!-- 로고 추가 -->
    <div class="logo-container">
      <img src="@/assets/logo.png" alt="Logo" class="logo" />
    </div>

    <!-- 탭들을 감싸는 컨테이너 -->
    <div class="tabs-wrapper">
      <RouterLink 
        :to="{ name: 'home' }" 
        class="tab-link" 
        :class="{ active: isActive('home') }"
        @click="setActive('home')">HOME</RouterLink>
      <RouterLink 
        :to="{ name: 'feed' }" 
        class="tab-link" 
        :class="{ active: isActive('feed') }"
        @click="setActive('feed')">FEED</RouterLink>
      <RouterLink 
        :to="{ name: 'challenge' }" 
        class="tab-link" 
        :class="{ active: isActive('challenge') }"
        @click="setActive('challenge')">CHALLENGE</RouterLink>
      <!-- <RouterLink 
        :to="{ name: 'management' }" 
        class="tab-link" 
        :class="{ active: isActive('management') }"
        @click="setActive('management')">MEMBERS</RouterLink> -->
      <RouterLink 
        :to="{ name: 'my-page' }" 
        class="tab-link" 
        :class="{ active: isActive('my-page') }"
        @click="setActive('my-page')">MY PAGE</RouterLink>
    </div>
  </nav>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const activeTab = ref(route.name) // 초기 활성화된 탭을 현재 경로로 설정


// 경로가 변경될 때마다 activeTab을 업데이트
watch(() => route.name, (newRoute) => {
  activeTab.value = newRoute
})


const isActive = (tab) => {
  return activeTab.value === tab
}

const setActive = (tab) => {
  activeTab.value = tab
}
</script>

<style scoped>
/* 네비게이션 바 전체 스타일 */
.custom-navbar {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 10px 2%;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;  /* 오버레이보다 높은 z-index */
  width: 100%;
  background-color: transparent;
  box-shadow: none;
  gap: 20px;
}

/* 그라데이션 배경 */
.custom-navbar::before {
  content: '';
  position: absolute;
  left: 0;
  width: 100%;
  height: 250px; /* 그라데이션의 높이를 조정 */
  background: linear-gradient(to bottom, rgba(255, 255, 255, 1), rgba(255, 255, 255, 1),rgba(255, 255, 255, 0));
  z-index: -1; /* 네비게이션 바 뒤에 배경을 설정 */
  pointer-events: none; /* 그라데이션 영역에서 마우스 이벤트를 비활성화 */
}


/* 로고 컨테이너 */
.logo-container {
  position: absolute; /* 로고를 절대 위치로 설정 */
  top: 12px; /* 상단 여백 */
  left: 3%; /* 왼쪽 여백 */
  display: flex;
  align-items: center;
  justify-content: flex-start;
}

.logo {
  width: 350px; /* 로고 크기 */
  height: auto;
}

/* 탭들을 감싸는 컨테이너 */
.tabs-wrapper {
  display: flex;
  justify-content: center; /* 탭들을 중앙에 정렬 */
  align-items: center;
  background-color: white;
  border-radius: 30px;
  border: 1px solid #ccc;
  overflow: hidden;
  position: relative;
  box-shadow: 0 5px 8px rgba(0, 0, 0, 0.05);
  margin-top: 30px;
}

/* 탭 항목 스타일 */
.tab-link {
  text-transform: uppercase;
  color: black;
  font-size: 16px;
  padding: 10px 20px;
  text-decoration: none;
  margin: 0;
  background-color: transparent;
  border: 1px solid transparent;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 140px;
  height: 50px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  box-sizing: border-box;
  line-height: 20px;
}

.tab-link:not(:last-child) {
  border-right: 1px solid #ccc;
}

.tab-link:first-child {
  border-top-left-radius: 30px;
  border-bottom-left-radius: 30px;
}

.tab-link:last-child {
  border-top-right-radius: 30px;
  border-bottom-right-radius: 30px;
}

.tab-link.active {
  background-color: #54a673;
  color: white;
  border-color: #54a673;
  font-weight: bold;
  box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
}

.tab-link:hover {
  background-color: #54a673;
  color: white;
  border-color: #54a673;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
}
</style>
