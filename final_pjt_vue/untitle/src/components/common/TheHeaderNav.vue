<template>
    <nav class="custom-navbar">
      <div class="tabs-wrapper"> <!-- 탭들을 감싸는 컨테이너 -->
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
        <RouterLink 
          :to="{ name: 'management' }" 
          class="tab-link" 
          :class="{ active: isActive('management') }"
          @click="setActive('management')">MEMBERS</RouterLink>
        <RouterLink 
          :to="{ name: 'my-page' }" 
          class="tab-link" 
          :class="{ active: isActive('my-page') }"
          @click="setActive('my-page')">MY PAGE</RouterLink>
      </div>
    </nav>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  
  const activeTab = ref('home')
  
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
  padding: 10px 0;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1050;
  width: 100%;
  background-color: transparent;
  box-shadow: none; /* 네비게이션 바 자체 그림자 제거 */
}

/* 탭들을 감싸는 컨테이너 */
.tabs-wrapper {
  display: flex; /* 가로로 배치 */
  justify-content: center;
  align-items: center;
  background-color: white; /* 탭 배경색 */
  border-radius: 30px; /* 탭들을 둥글게 처리 */
  border: 1px solid #ccc; /* 전체 테두리 두께 1px */
  overflow: hidden; /* 자식이 벗어나지 않도록 처리 */
  position: relative;
  box-shadow: 0 5px 8px rgba(0, 0, 0, 0.05);
  margin-top: 20px;
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
  border: 1px solid transparent; /* 기본 상태에서 테두리 없음 */
  display: flex; /* flexbox로 텍스트 중앙 정렬 */
  justify-content: center; /* 수평 중앙 정렬 */
  align-items: center; /* 수직 중앙 정렬 */
  width: 140px; /* 넉넉한 너비 고정 */
  height: 50px; /* 높이 고정 */
  cursor: pointer;
  transition: all 0.3s ease; /* 부드러운 효과 */
  position: relative;
  box-sizing: border-box; /* 테두리, 패딩을 포함한 크기 고정 */
  line-height: 20px; /* 텍스트의 수직 정렬을 위한 라인 높이 설정 */
}

/* 각 탭 사이사이에 구분선 */
.tab-link:not(:last-child) {
  border-right: 1px solid #ccc; /* 탭 간 구분선 두께 1px */
}

/* 첫 번째 탭과 마지막 탭만 모서리 둥글게 처리 */
.tab-link:first-child {
  border-top-left-radius: 30px; /* 첫 번째 탭 왼쪽 모서리 둥글게 */
  border-bottom-left-radius: 30px;
}

.tab-link:last-child {
  border-top-right-radius: 30px; /* 마지막 탭 오른쪽 모서리 둥글게 */
  border-bottom-right-radius: 30px;
}

/* 활성화된 탭 강조 (안쪽 그림자) */
.tab-link.active {
  background-color: #54a673; /* 선택된 탭 배경색 */
  color: white; /* 선택된 탭 글자색 */
  border-color: #54a673; /* 선택된 탭 테두리 색 */
  font-weight: bold; 
  box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2); /* 선택된 탭 안쪽 그림자 */
}

/* 호버 효과 (바깥쪽 그림자) */
.tab-link:hover {
  background-color: #54a673; /* 호버 시 배경색 */
  color: white; /* 호버 시 글자색 */
  border-color: #54a673; /* 호버 시 테두리 색 */
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2); /* 호버 시 바깥쪽 그림자 */
}


  </style>
  