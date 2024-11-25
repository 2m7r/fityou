<template>
  <div class="challenges-my-container">
    <h2>내가 참여한 챌린지</h2>

    <!-- 챌린지 카드들이 들어있는 스크롤 영역 -->
    <div class="challenges-scroll-container" ref="scrollContainer">
      <div v-if="challenges.length === 0" class="col-12">
        <p>참여한 챌린지가 없습니다.</p>
      </div>

      <!-- 카드들 -->
      <div class="card-wrapper">
        <div v-for="challenge in challenges"
        :key="challenge.challengeId"
          class="card-container"
        >
          <div class="card" @click="viewChallengeDetail(challenge)">
            <img :src="challenge.image || 'https://via.placeholder.com/150'" class="card-img-top" alt="Challenge Image" />
            <div class="card-body">
              <h5 class="card-title">{{ challenge.name }}</h5>
              <p class="card-text">{{ challenge.description }}</p>

              <div class="button-container">
                <!-- 참여자 수와 불모양 아이콘을 flexbox로 정렬 -->
                <span class="participant-count">
                  {{ challenge.participantCount }} 명 참여
                </span>
                <i class="bi bi-fire participant-icon"></i> <!-- 불모양 아이콘 추가 -->
              </div>
            </div>

            <!-- X 버튼 -->
            <button class="btn btn-danger btn-close" @click.stop="leaveChallenge(challenge.challenge_id)">
              <i class="fas fa-times"></i>
            </button>
          </div>
        </div>
      </div>

    </div>
    <div class="scroll-buttons-container">
      <!-- 스크롤 버튼 -->
      <button class="scroll-button left" @click="scrollLeft">
        <i class="bi bi-arrow-left"></i> <!-- 왼쪽 화살표 아이콘 -->
      </button>
      <button class="scroll-button right" @click="scrollRight">
        <i class="bi bi-arrow-right"></i> <!-- 오른쪽 화살표 아이콘 -->
      </button>
    </div>

    <!-- 챌린지 상세 모달 -->
    <div v-if="isModalOpen" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <span class="close-btn" @click="closeModal">&times;</span>
        <h2>{{ modalChallenge.name }}</h2>
        <p>{{ modalChallenge.description }}</p>
        <p><strong>시작일:</strong> {{ modalChallenge.startDate }}</p>
        <p><strong>진행 상태:</strong> {{ modalChallenge.status }}</p>
        <p><strong>참여자 수:</strong> {{ modalChallenge.participantCount }} 명</p>
      </div>
    </div>



  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import apiClient from '../api/apiClient';

const challenges = ref([]);  // 내가 참여한 챌린지 목록
const scrollContainer = ref(null);

// 모달 상태 관리
const isModalOpen = ref(false);
const modalChallenge = ref({}); // 초기값을 빈 객체로 설정

// 스크롤 버튼 클릭 시 동작
const scrollLeft = () => {
  if (scrollContainer.value) {
    scrollContainer.value.scrollLeft -= 700;
  }
};

const scrollRight = () => {
  if (scrollContainer.value) {
    scrollContainer.value.scrollLeft += 700;
  }
};

// 내가 참여한 챌린지 데이터를 가져오는 함수
const fetchChallenges = async () => {
  const user = JSON.parse(sessionStorage.getItem('user'));
  const userId = user.userId;

  try {
    const response = await apiClient.get('/api-challenge/challenges', {
      params: { userId }
    });
    challenges.value = response.data;
  } catch (error) {
    console.error('챌린지 데이터를 가져오는 데 실패했습니다.', error);
  }
};

// 참여한 챌린지를 떠나는 함수 (X 버튼)
const leaveChallenge = async (challengeId) => {
  try {
    // 서버에 챌린지 탈퇴 요청
    await apiClient.post('/api-challenge/leave', { challenge_id: challengeId });

    // 성공적으로 나갔으면 리스트에서 해당 챌린지 삭제
    const index = challenges.value.findIndex(challenge => challenge.challenge_id === challengeId);
    if (index !== -1) {
      challenges.value.splice(index, 1);  // 해당 항목 삭제
    }
  } catch (error) {
    console.error('챌린지를 떠나지 못했습니다.', error);
  }
};


// 챌린지 상세보기
const viewChallengeDetail = (challenge) => {
  modalChallenge.value = challenge; // 모달에 선택된 챌린지 데이터 설정
  isModalOpen.value = true; // 모달 열기
};

// 모달 닫기
const closeModal = () => {
  isModalOpen.value = false; // 모달 닫기
};


onMounted(() => {
  fetchChallenges(); // 나의 챌린지 목록 가져오기
});
</script>

<style scoped>
.challenges-my-container {
  padding: 20px;
  position: relative;
  flex-direction: column;
  width: 100%;
  box-sizing: border-box;
}

h2 {
  font-size: 2rem;
  font-weight: bold;
  margin-bottom: 20px;
}

.challenges-scroll-container {
  background-color: #f7f7f7;
  padding-bottom: 20px;
  padding-top: 20px;
  border-radius: 10px;
  width: 100%;
  box-sizing: border-box;
  overflow-x: scroll;
  white-space: nowrap;
  position: relative;
  margin-bottom: 60px;
}

.challenges-scroll-container::-webkit-scrollbar {
  display: none;
}

.card-wrapper {
  padding-left: 1.2%;
  display: flex;
  gap: 10px;
  flex-wrap: nowrap;
  width: 100%;
}

.card-container {
  width: 360px;
  flex-shrink: 0;
}

.card {
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  height: 400px;
  position: relative;
  margin: 2%;
}

.card-img-top {
  height: 250px;
  object-fit: cover;
}

.card-body {
  padding: 15px;
  height: 100%;
  overflow: hidden;
}

.card-title {
  font-size: 1.2rem;
  font-weight: bold;
}

.card-text {
  font-size: 1rem;
  color: #555;
  overflow: hidden;
  text-overflow: ellipsis;
  height: 3.0em;
  line-height: 1.5;
}

.card:hover {
  transform: scale(1.05);
}

.scroll-buttons-container {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  justify-content: space-between;
  width: 120px;
}

.scroll-button {
  background-color: rgba(0, 0, 0, 0.2);
  color: white;
  font-size: 24px;
  border: none;
  border-radius: 50%;
  padding: 10px;
  cursor: pointer;
  width: 40px;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: background-color 0.3s ease;
  z-index: 2;
}

.participant-count {
  font-size: 0.9rem;
  color: #555;
  margin-right: 5px;
}

.participant-icon {
  font-size: 1.2rem;
  color: #ff5733;
}

.button-container {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.btn-close {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: transparent;
  border-color: transparent;
  color: white;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 16px;
  cursor: pointer;
}

.btn-close:hover {
  background-color: rgba(0, 0, 0, 0.3);
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.2);
}

/* 모달 스타일 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7); /* 어두운 배경 */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  opacity: 0;
  animation: fadeIn 0.3s forwards; /* 애니메이션 추가 */
  
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 10px;
  width: 50%;
  max-width: 600px;
  position: relative;
  transform: scale(0.8);
  animation: scaleUp 0.3s forwards;
}

.close-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 24px;
  cursor: pointer;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes scaleUp {
  from {
    transform: scale(0.8);
  }
  to {
    transform: scale(1);
  }
}

.btn-modal {
  max-width: 100px;
  margin-left: auto;
}


</style>
