<template>
<<<<<<< HEAD
  <div class="container py-4">
    <h2>내가 참여한 챌린지</h2>
  
    <!-- 내가 참여한 챌린지 배경 색상 추가 -->
=======
  <div class="challenges-my-container">
    <!-- '내가 참여한 챌린지' 제목 -->
    <h2>내가 참여한 챌린지</h2>

    <!-- 챌린지 카드들이 들어있는 스크롤 영역 -->
>>>>>>> vue
    <div class="challenges-scroll-container">
      <div v-if="challenges.length === 0" class="col-12">
        <p>참여한 챌린지가 없습니다.</p>
      </div>
<<<<<<< HEAD
  
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
=======

      <!-- 카드들 -->
      <div class="card-wrapper" ref="scrollContainer">
        <div
          v-for="challenge in challenges"
          :key="challenge.challenge_id"
          class="card-container"
        >
          <div class="card" @click="viewChallengeDetail(challenge)">
            <img :src="challenge.image || 'https://via.placeholder.com/150'" class="card-img-top" alt="Challenge Image" />
            <div class="card-body">
              <h5 class="card-title">{{ challenge.title }}</h5>
              <p class="card-text">{{ challenge.description }}</p>

              <div class="button-container">
                <button class="btn btn-success" @click.stop="joinChallenge(challenge.challenge_id)">
                  참여하기
                </button>
              </div>
            </div>

            <!-- X 버튼 -->
            <button class="btn btn-danger btn-close" @click.stop="leaveChallenge(challenge.challenge_id)">
              <i class="fas fa-times"></i>
            </button>
          </div>
        </div>
      </div>

      <!-- 스크롤 버튼 -->
      <button class="scroll-button left" @click="scrollLeft">
        <span>&lt;</span>
      </button>
      <button class="scroll-button right" @click="scrollRight">
        <span>&gt;</span>
      </button>
>>>>>>> vue
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

<<<<<<< HEAD
// 챌린지 데이터
const challenges = ref([]);

// 모달 상태 관리
const showModal = ref(false);
const modalChallenge = ref({});

// 사용자가 참여한 챌린지 데이터를 가져오는 함수
=======
const challenges = ref([]);
const scrollContainer = ref(null);

const getCardWidth = () => {
  const firstCard = scrollContainer.value?.querySelector('.card-container');
  return firstCard ? firstCard.offsetWidth + 20 : 0;
};

const scrollLeft = () => {
  if (scrollContainer.value) {
    const cardWidth = getCardWidth();
    if (cardWidth > 0) {
      scrollContainer.value.scrollLeft -= cardWidth;
    }
  }
};

const scrollRight = () => {
  if (scrollContainer.value) {
    const cardWidth = getCardWidth();
    if (cardWidth > 0) {
      scrollContainer.value.scrollLeft += cardWidth;
    }
  }
};

>>>>>>> vue
const fetchChallenges = async () => {
  try {
    const response = await axios.get('/api/user/challenges');
    challenges.value = response.data;
  } catch (error) {
    console.error('챌린지 데이터를 가져오는 데 실패했습니다.', error);
  }
};

<<<<<<< HEAD
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
=======
onMounted(() => {
  fetchChallenges();
>>>>>>> vue
});
</script>

<style scoped>
<<<<<<< HEAD
/* 내가 참여한 챌린지 배경색 */
.challenges-scroll-container {
  background-color: #f7f7f7;
  overflow-x: hidden;
  white-space: nowrap;
  padding-bottom: 20px;
  padding-top: 20px;
  border-radius: 10px;
  width: 100%;
  padding-left: 20px;
  padding-right: 20px;
  display: flex;
  justify-content: flex-start; /* 카드들을 왼쪽 정렬 */
}

.row {
  display: flex; /* flexbox로 한 줄에 배치 */
  gap: 20px; /* 카드 간 간격 */
  padding-left: 0;
}

.card-container {
  display: inline-block;
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
=======
/* ChallengesMy 컨테이너 */
.challenges-my-container {
  padding: 20px;
  display: flex;
  flex-direction: column; /* 세로로 배치 */
  width: 100%;
  box-sizing: border-box;
}

/* 챌린지 제목 */
h2 {
  font-size: 1.8rem;
  font-weight: bold;
  margin-bottom: 20px; /* 제목과 카드들 간격 추가 */
}

/* 스크롤 영역 */
.challenges-scroll-container {
  background-color: #f7f7f7;
  overflow-x: auto;
  padding-bottom: 20px;
  padding-top: 20px;
  border-radius: 10px;
  position: relative;
  width: 100%;
}

/* 카드 래퍼 */
.card-wrapper {
  display: flex;
  gap: 10px;
  flex-wrap: nowrap;
  overflow-x: auto;
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
}

.card-img-top {
  height: 250px;
  object-fit: cover;
>>>>>>> vue
}

.card-body {
  padding: 15px;
<<<<<<< HEAD
  height: 100%; /* 본문이 카드 크기를 넘지 않도록 */
  overflow: hidden; /* 본문 내용이 길어져도 넘치지 않도록 */
=======
  height: 100%;
  overflow: hidden;
>>>>>>> vue
}

.card-title {
  font-size: 1.2rem;
  font-weight: bold;
}

.card-text {
  font-size: 1rem;
  color: #555;
  overflow: hidden;
<<<<<<< HEAD
  text-overflow: ellipsis; /* 설명이 길어지면 '...' 처리 */
  height: 80px; /* 설명 영역 크기 고정 */
=======
  text-overflow: ellipsis;
  height: 3.0em;
>>>>>>> vue
  line-height: 1.5;
}

.card:hover {
<<<<<<< HEAD
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
=======
  transform: scale(1.05);
}

/* 스크롤 버튼 */
>>>>>>> vue
.scroll-button {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
<<<<<<< HEAD
  background-color: rgba(0, 0, 0, 0.2); /* 투명도 있는 회색 */
=======
  background-color: rgba(0, 0, 0, 0.2);
>>>>>>> vue
  color: white;
  font-size: 24px;
  border: none;
  border-radius: 50%;
  padding: 15px;
  cursor: pointer;
<<<<<<< HEAD
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
=======
  z-index: 1;
  width: 40px;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.scroll-button:hover {
  background-color: rgba(0, 0, 0, 0.4);
>>>>>>> vue
}

.scroll-button.left {
  left: 10px;
}

.scroll-button.right {
  right: 10px;
}
<<<<<<< HEAD
=======

/* X 버튼 */
.btn-close {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: red;
  border-color: red;
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
>>>>>>> vue
</style>
