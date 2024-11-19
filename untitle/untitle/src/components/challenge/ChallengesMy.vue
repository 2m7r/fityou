<template>
  <div class="container py-4">
    <h2>내가 참여한 챌린지</h2>
  
    <!-- 내가 참여한 챌린지 배경 색상 추가 -->
    <div class="challenges-scroll-container">
      <div v-if="challenges.length === 0" class="col-12">
        <p>참여한 챌린지가 없습니다.</p>
      </div>
  
      <!-- 스크롤 버튼 왼쪽, 오른쪽 -->
      <div class="scroll-button left" @click="scrollLeft">
        <i class="fas fa-chevron-left"></i>
      </div>
  
      <!-- 카드들이 가로로 스크롤되는 영역 -->
      <div class="row flex-nowrap" ref="scrollContainer">
        <div
          v-for="challenge in challenges"
          :key="challenge.challenge_id"
          class="col-md-4 mb-4 card-container"
        >
          <!-- 부트스트랩 카드 -->
          <div class="card" @click="viewChallengeDetail(challenge)">
            <img :src="challenge.image || 'https://via.placeholder.com/150'" class="card-img-top" alt="Challenge Image" />
            <div class="card-body">
              <h5 class="card-title">챌린지 제목</h5>
              <p class="card-text">챌린지 설명</p>
              <!-- 참여하기 버튼 추가 -->
              <button
                class="btn btn-success"
                @click.stop="joinChallenge(challenge.challenge_id)"
              >
                참여하기
              </button>
            </div>
          </div>
        </div>
      </div>
  
      <!-- 스크롤 버튼 오른쪽 -->
      <div class="scroll-button right" @click="scrollRight">
        <i class="fas fa-chevron-right"></i>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

// 챌린지 데이터
const challenges = ref([]);

// 모달 상태 관리
const showModal = ref(false);
const modalChallenge = ref({});

// 사용자가 참여한 챌린지 데이터를 가져오는 함수
const fetchChallenges = async () => {
  try {
    const response = await axios.get('/api/user/challenges');
    challenges.value = response.data;
  } catch (error) {
    console.error('챌린지 데이터를 가져오는 데 실패했습니다.', error);
  }
};

// 챌린지 클릭 시 상세보기
const viewChallengeDetail = (challenge) => {
  modalChallenge.value = challenge;
  showModal.value = true;
};

// 챌린지 참여하기
const joinChallenge = async (challengeId) => {
  try {
    const response = await axios.post('/api/challenges/join', { challengeId });
    alert('챌린지에 참여하였습니다!');
    fetchChallenges(); // 데이터를 갱신
  } catch (error) {
    console.error('챌린지 참여에 실패했습니다.', error);
  }
};

// 모달 닫기
const closeModal = () => {
  showModal.value = false;
};

// 왼쪽으로 스크롤
const scrollLeft = () => {
  const container = document.querySelector('.challenges-scroll-container');
  container.scrollLeft -= 360; // 360px 만큼 왼쪽으로 이동
};

// 오른쪽으로 스크롤
const scrollRight = () => {
  const container = document.querySelector('.challenges-scroll-container');
  container.scrollLeft += 360; // 360px 만큼 오른쪽으로 이동
};

onMounted(() => {
  fetchChallenges(); // 컴포넌트 마운트 시 참여한 챌린지를 불러옵니다.
});
</script>

<style scoped>
/* 내가 참여한 챌린지 배경색 */
.challenges-scroll-container {
  background-color: #f7f7f7; /* 연한 회색 배경 */
  overflow-x: hidden; /* 기본 스크롤바 숨기기 */
  white-space: nowrap; /* 카드들이 한 줄로 배치되도록 */
  padding-bottom: 20px; /* 아래 여백 */
  padding-top: 20px; /* 상단 여백 */
  border-radius: 10px; /* 전체 컨테이너 모서리 둥글게 */
  position: relative;
  width: 100%;
  padding-left: 20px; /* 왼쪽 여백 */
  padding-right: 20px; /* 오른쪽 여백 */
}

.row {
  display: flex; /* flexbox로 한 줄에 배치 */
  gap: 20px; /* 카드 간 간격 */
  padding-left: 0;
}

.card-container {
  display: inline-block; /* 인라인 블록으로 카드 표시 */
  width: 360px; /* 카드 크기 고정 */
  margin-right: 20px; /* 카드 간 간격 */
  flex-shrink: 0; /* 카드가 줄어들지 않도록 설정 */
}

.card {
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.1); /* 카드 그림자 */
  border-radius: 10px; /* 카드 모서리 둥글게 */
  transition: transform 0.2s ease-in-out;
  height: 400px; /* 카드 높이 고정 */
}

.card-img-top {
  height: 250px; /* 이미지 높이 고정 */
  object-fit: cover; /* 이미지 크기 비율 맞추기 */
}

.card-body {
  padding: 15px;
  height: 100%; /* 본문이 카드 크기를 넘지 않도록 */
  overflow: hidden; /* 본문 내용이 길어져도 넘치지 않도록 */
}

.card-title {
  font-size: 1.2rem;
  font-weight: bold;
}

.card-text {
  font-size: 1rem;
  color: #555;
  overflow: hidden;
  text-overflow: ellipsis; /* 설명이 길어지면 '...' 처리 */
  height: 80px; /* 설명 영역 크기 고정 */
  line-height: 1.5;
}

.card:hover {
  transform: scale(1.05); /* 카드 호버 시 살짝 커지도록 */
}

/* 참여하기 버튼 스타일 */
.btn-success {
  background-color: #54a673;
  border-color: #54a673;
  color: white;
  padding: 10px 20px;
  width: 100%;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s;
}

.btn-success:hover {
  background-color: #4c9b64;
  border-color: #4c9b64;
}

/* 스크롤 버튼 스타일 */
.scroll-button {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: rgba(0, 0, 0, 0.2); /* 투명도 있는 회색 */
  color: white;
  font-size: 24px;
  border: none;
  border-radius: 50%;
  padding: 15px;
  cursor: pointer;
  transition: background-color 0.3s;
  z-index: 10;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 40px;
  height: 40px;
}

.scroll-button:hover {
  background-color: rgba(0, 0, 0, 0.5); /* 호버 시 다크한 색상 */
}

.scroll-button.left {
  left: 10px;
}

.scroll-button.right {
  right: 10px;
}
</style>
