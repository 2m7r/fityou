<template>
  <div>
    <!-- 잔디!!!! -->
    <div class="usergrass">
        <UserGrass/>
    </div>
    <!-- 모달을 호출하는 버튼 -->
    <button @click="showModal = true">내 정보 수정</button>

    <!-- 모달 창 -->
    <div v-if="showModal" class="modal-overlay">
      <div class="modal-content">
        <!-- UserPage 컴포넌트 불러오기 -->
        <UserPage />
        
        <!-- 모달 닫기 버튼 -->
        <button @click="showModal = false">닫기</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import UserPage from '@/components/user/UserPage.vue'; // UserPage 모달 컴포넌트 임포트
import UserGrass from '@/components/user/userGrass.vue';

// 모달의 표시 여부를 제어하는 변수
const showModal = ref(false);
</script>

<style scoped>
/* 모달 오버레이 스타일 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 모달 콘텐츠 스타일 */
.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 10px;
  width: 80%;
  max-width: 600px;
}

/* 버튼 스타일 */
button {
  padding: 10px 20px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 10px;
}

button:hover {
  background-color: #388e7f;
}

.usergrass {
    margin-top: 150px;
}
</style>
