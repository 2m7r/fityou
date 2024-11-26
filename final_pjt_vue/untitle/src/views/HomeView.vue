<template>
  <div class="home-container">
    <div class="row justify-content-center">
      <div class="col-12 text-center">
        <h2>F I T   Y O U  ——————   W I T H   Y O U</h2>
      </div>
    </div>
  </div>
</template>

<script setup></script>

<style scoped>
/* 페이지 컨테이너 스타일 */
.home-container {
  display: flex;
  flex-direction: column;
  justify-content: center; /* 수직 중앙 정렬 */
  align-items: center; /* 수평 중앙 정렬 */
  height: 60vh; 
  background-color: #ffffff;
  padding: 0 20px; /* 좌우 여백 */
}

/* 타이틀 스타일 */
h2 {
  font-size: 30px;
  color: #333;
}
</style>


<!-- <template>
  <div v-if="isLoggedIn">
    <div class="home-container">
      <div class="row justify-content-center">
        <div class="col-12 text-center">
          <h2>내 몸의 변화를, 손끝에서 관리하다</h2>
        </div>
      </div>
    </div>
  </div>
  <div v-else>
    <p>로그인이 필요합니다.</p>
    <router-link to="/login">로그인</router-link>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const isLoggedIn = ref(false)  // 로그인 상태 (예시로 설정)

// 로그인 상태 체크 로직
if(localStorage.getItem('isLoggedIn') === 'true') {
  isLoggedIn.value = true
}
</script>

<style scoped>
.home-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 60vh;
  background-color: #ffffff;
  padding: 0 20px;
}

h2 {
  font-size: 30px;
  color: #333;
  font-weight: bold;
}
</style> -->
