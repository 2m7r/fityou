<template>
  <div class="challenges-my-container">
    <!-- '내가 참여한 챌린지' 제목 -->
    <h2>내가 참여한 챌린지</h2>

    <!-- 챌린지 카드들이 들어있는 스크롤 영역 -->
    <div class="challenges-scroll-container">
      <div v-if="challenges.length === 0" class="col-12">
        <p>참여한 챌린지가 없습니다.</p>
      </div>

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
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

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

const fetchChallenges = async () => {
  try {
    const response = await axios.get('/api/user/challenges');
    challenges.value = response.data;
  } catch (error) {
    console.error('챌린지 데이터를 가져오는 데 실패했습니다.', error);
  }
};

onMounted(() => {
  fetchChallenges();
});
</script>

<style scoped>
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

/* 스크롤 버튼 */
.scroll-button {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: rgba(0, 0, 0, 0.2);
  color: white;
  font-size: 24px;
  border: none;
  border-radius: 50%;
  padding: 15px;
  cursor: pointer;
  z-index: 1;
  width: 40px;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.scroll-button:hover {
  background-color: rgba(0, 0, 0, 0.4);
}

.scroll-button.left {
  left: 10px;
}

.scroll-button.right {
  right: 10px;
}

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
</style>
