마이페이지 뷰
<template>
  <div>
    <div class="marginplz"></div>
    <div class="mp-profile">
      <div class="profile-container">

        <!-- 왼쪽: 사용자 프로필 사진 -->
        <div class="profile">
          <img :src="userProfileImage" class="profile-img" />
        </div>

        <!-- 중간: 사용자 이름과 설명 -->
        <div class="user-info">
          <h3 class="user-name">{{ userStore.userName || '이름 없음' }}</h3>
          <p class="user-description">{{ userStore.userDescription || '설명 없음' }}</p>
        </div>

        <!-- 문구 추가 -->
        <div class="extra-st">
          <p>FITYOU!</p>
        </div>

        <!-- 오른쪽: 내 정보 수정 버튼 -->
        <div class="edit-button-container">
          <button
            class="btn btn-lg btn-edit my-2 d-flex align-items-center rounded-full"
            @click="openEditModal"
          >
            <i class="bi bi-pencil-square"></i> 편집
          </button>
        </div>
      </div>
    </div>

    <div class="next-container">
      <!-- 잔디!!!! -->
      <div class="usergrass">
        <UserGrass />
      </div>
      <RouterLink 
        :to="{ name: 'challenge' }" >
        <div class="challenge-box">
          <img src="@/assets/challenge.png" alt="챌린지하세요" class="challenge-img"/>
        </div>
      </RouterLink>
    </div>

    <!-- UserPage 모달 열기 -->
    <UserPageCopy v-if="isEditModalOpen" @close="closeEditModal" />
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useUserStore } from "@/stores/userStore";
import UserPageCopy from "@/components/user/UserPagecopy.vue";
import UserGrass from "@/components/user/userGrass.vue";
import defaultprofileImage from '@/assets/profile.jpg'

const isEditModalOpen = ref(false); // 모달 상태

// 사용자 상태
const userStore = useUserStore();

// 기본 이미지 경로 지정
const defaultImage = defaultprofileImage;

// userProfileImage 계산 로직
const userProfileImage = userStore.userProfileImage && userStore.userProfileImage.trim()
  ? 'http://localhost:8080/' + userStore.userProfileImage.replace(/\\/g, '/') 
  : defaultImage;

// 모달 열기
const openEditModal = () => {
  isEditModalOpen.value = true;
};

// 모달 닫기
const closeEditModal = () => {
  isEditModalOpen.value = false;
};
</script>

<style scoped>

/* 프로필 사진 크기 */
.profile-img {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
  border: 4px solid #54a673;
}

/* 프로필 영역의 Flexbox */
.mp-profile {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

/* 3개의 영역을 가로로 배치 (왼쪽, 중간, 오른쪽) */
.profile-container {
  display: flex;
  flex-wrap: wrap; /* 화면 크기에 맞춰서 줄 바꿈 가능 */
  justify-content: space-between;
  align-items: center;
  width: 100%;
  height: 250px;
  max-width: 1500px;
  padding: 20px; /* 상하 여백 20px */
  padding-left: 60px; /* 좌측 여백 추가 */
  padding-right: 60px; /* 우측 여백 추가 */
  background-color: #f8f9fa;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  gap: 20px; /* 아이템 간격 설정 */
}

/* 왼쪽: 프로필 사진 */
.profile {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  min-width: 150px; /* 최소 너비 설정 */
}

/* 중간: 사용자 이름 및 설명 */
.user-info {
  flex: 2;
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-width: 230px; /* 최소 너비 설정 */
  padding-left: 20px;
}

.user-name {
  font-size: 1.5rem;
  font-weight: bold;
}

.user-description {
  font-size: 1rem;
  color: #555;
  margin-top: 5px;
}

/* 문구 스타일 */
.extra-st {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
  font-size: 4rem;
  color: #6c757d;
  font-style: italic;
  margin-top: 10px;
  font-weight: bold;
  font-family: Arial, Helvetica, sans-serif;
  letter-spacing: 15px;
}

/* 오른쪽: 수정 버튼 */
.edit-button-container {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  min-width: 150px;
}

/* 아이콘과 텍스트 간격 조정 */
.bi {
  margin-right: 8px;
}


/* 추가된 문구 스타일 */
.extra-st {
  flex: 1; /* 비율 맞추기 */
  padding: 0 20px; /* 좌우 여백 */
  text-align: center;
  font-size: 4rem;
  color: #8ea59b;
  font-style: italic;
  margin-top: 10px;
  margin-right: 100px;
  font-weight: bold;
  font-family: 'Bold';
  letter-spacing: 15px;
}




.my-2 {
  margin-top: 15px;
}

.btn-lg {
  font-size: 16px;
  padding: 12px 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  margin-top: 3%;
  margin-bottom: 10%;
  height: 50px;
}

.btn-edit {
  background-color: #54a673;
  color: white;
  max-width: 150px;
}

.btn-edit:hover {
  background-color: #4a8e5c;
}

.rounded-full {
  border-radius: 50px !important;
}

.my-3 {
  margin-top: 20px;
}

.marginplz {
  margin-top: 180px;
}


.usergrass {
  width: 500px;
  margin: 10px 0;
}


.next-container {
  margin-top: 50px;
  display: flex;
  justify-content: center;
  gap: 20px;
}

.challenge-img {
  width: 900px;
  height: 500px;
  margin-left: 20px;
}

</style>
