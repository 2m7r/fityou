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
        <div
          v-for="challenge in challenges"
          :key="challenge.challenge_id"
          class="card-container"
        >
          <div class="card" @click="viewChallengeDetail(challenge)">
            <img :src="challenge.image || 'https://via.placeholder.com/150'" class="card-img-top" alt="Challenge Image" />
            <div class="card-body">
              <h5 class="card-title">{{ challenge.title }} 챌린지이름</h5>
              <p class="card-text">{{ challenge.description }} 챌린지설명~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~</p>

              <div class="button-container">
                <!-- 참여자 수와 불모양 아이콘을 flexbox로 정렬 -->
                <span class="participant-count">
                  {{ challenge.participant_count }} 명 참여
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
      scrollContainer.value.scrollLeft -= 700;
  }
};

const scrollRight = () => {
  if (scrollContainer.value) {
      scrollContainer.value.scrollLeft += 700;
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

.scroll-button.left {
  left: 50%;
  transform: translateX(-50%) translateY(0);
}

.scroll-button.right {
  left: 50%;
  transform: translateX(-50%) translateY(0);
}

.scroll-button {
  left: 50%;
  transform: translateX(-50%) translateY(-50%);
}

.participant-count {
  font-size: 0.9rem;
  color: #555;
  margin-right: 5px; /* 오른쪽으로 밀어서 텍스트와 아이콘 간의 간격을 추가 */
}

.participant-icon {
  font-size: 1.2rem; /* 불 아이콘 크기 */
  color: #ff5733; /* 불 아이콘 색상 */
}

.button-container {
  display: flex;
  justify-content: flex-end; /* 오른쪽 끝으로 정렬 */
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
  transition: background-color 0.3s ease, box-shadow 0.3s ease;
}

.btn-close:hover {
  background-color: rgba(0, 0, 0, 0.3);
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.2);
}
</style>
