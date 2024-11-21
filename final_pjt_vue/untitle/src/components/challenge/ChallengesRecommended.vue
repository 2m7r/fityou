<template>
  <div class="recommended-challenges-container">
    <h5>추천 챌린지</h5>

    <!-- 추천 챌린지 카드들이 들어있는 스크롤 영역 -->
    <div class="challenges-scroll-container" ref="scrollContainer">
      <div v-if="recommendedChallenges.length === 0" class="col-12">
        <p>추천할 챌린지가 없습니다.</p>
      </div>

      <!-- 카드들 -->
      <div class="card-wrapper">
        <div
          v-for="challenge in recommendedChallenges"
          :key="challenge.challenge_id"
          class="card-container"
        >
          <div class="card" @click="viewChallengeDetail(challenge)">
            <img
              :src="challenge.image || 'https://via.placeholder.com/150'"
              class="card-img-top"
              alt="Challenge Image"
            />
            <div class="card-body">
              <h5 class="card-title">{{ challenge.name }}</h5>
              <p class="card-text">{{ challenge.description }}</p>

              <div class="button-container">
                <!-- 참여하기 버튼 -->
                <button
                  class="btn btn-success"
                  @click.stop="joinChallenge(challenge)"
                >
                  참여하기
                </button>
              </div>
            </div>

            <!-- 참여자 수 텍스트 및 불모양 아이콘 -->
            <div class="participant-info">
              <span class="participant-count">
                {{ challenge.participant_count }} 명 참여
              </span>
              <i class="bi bi-fire participant-icon"></i>
            </div>

            <!-- X 버튼 -->
            <button
              class="btn btn-danger btn-close"
              @click.stop="leaveChallenge(challenge.challenge_id)"
            >
              <i class="fas fa-times"></i>
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="scroll-buttons-container">
      <!-- 스크롤 버튼 -->
      <button class="scroll-button left" @click="scrollLeft">
        <i class="bi bi-arrow-left"></i>
        <!-- 왼쪽 화살표 아이콘 -->
      </button>
      <button class="scroll-button right" @click="scrollRight">
        <i class="bi bi-arrow-right"></i>
        <!-- 오른쪽 화살표 아이콘 -->
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";

// 스크롤 버튼 클릭 시 동작
const scrollLeft = () => {
  if (scrollContainer.value) {
    scrollContainer.value.scrollLeft -= 700; // 왼쪽으로 700px 스크롤
  }
};

const scrollRight = () => {
  if (scrollContainer.value) {
    scrollContainer.value.scrollLeft += 700; // 오른쪽으로 700px 스크롤
  }
};

const recommendedChallenges = ref([]); // 추천 챌린지 목록
const challenges = ref([]); // 내가 참여한 챌린지 목록

const scrollContainer = ref(null);

// 추천 챌린지 데이터를 가져오는 함수
const fetchRecommendedChallenges = async () => {
  try {
    const response = await axios.get("/api/recommended/challenges"); // 서버에서 데이터를 가져옴
    recommendedChallenges.value = response.data; // 추천 챌린지 목록을 받아옴
  } catch (error) {
    console.error("추천 챌린지 데이터를 가져오는 데 실패했습니다.", error);
  }
};

// 내가 참여한 챌린지 데이터를 가져오는 함수
const fetchMyChallenges = async () => {
  try {
    const response = await axios.get('/api/user/challenges'); // 내가 참여한 챌린지 목록을 가져옴
    challenges.value = response.data;  // 내가 참여한 챌린지 목록을 저장
  } catch (error) {
    console.error('내 챌린지 데이터를 가져오는 데 실패했습니다.', error);
  }
};

// 추천 챌린지 참여하기 버튼 클릭 시
const joinChallenge = async (challenge) => {
  try {
    // 서버에 참여 요청
    await axios.post('/api/join/challenge', { challenge_id: challenge.challenge_id });

    // 추천 챌린지 목록에서 해당 챌린지 제거
    recommendedChallenges.value = recommendedChallenges.value.filter(c => c.challenge_id !== challenge.challenge_id);

    // 내가 참여한 챌린지 목록에 해당 챌린지 추가
    challenges.value.push(challenge);

    // 참여자 수 업데이트
    challenge.participant_count += 1;
  } catch (error) {
    console.error('챌린지 참여에 실패했습니다.', error);
  }
};

onMounted(() => {
  fetchRecommendedChallenges();  // 추천 챌린지 목록 가져오기
  fetchMyChallenges();  // 내가 참여한 챌린지 목록 가져오기
});
</script>

<style scoped>
.recommended-challenges-container {
  padding: 20px;
  position: relative;
  flex-direction: column;
  width: 100%;
  box-sizing: border-box;
}

/* 추천 챌린지 제목 */
h5 {
  font-size: 1.8rem;
  font-weight: bold;
  margin-bottom: 20px;
}

/* 추천 챌린지 스크롤 영역 */
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

/* 스크롤바 숨기기 */
.challenges-scroll-container::-webkit-scrollbar {
  display: none;
}

/* 카드 래퍼 */
.card-wrapper {
  padding-left: 1.2%;
  display: flex;
  gap: 10px;
  flex-wrap: nowrap;
  width: 100%;
}

/* 카드 크기 */
.card-container {
  width: 360px;
  flex-shrink: 0;
}

/* 카드 스타일 */
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
  height: 3em;
  line-height: 1.5;
}

.card:hover {
  transform: scale(1.05);
}

/* 참여자 수 및 불모양 아이콘 */
.participant-info {
  position: absolute;
  bottom: 12px;
  right: 15px; /* 오른쪽에 고정 */
  display: flex;
  align-items: center;
}

.button-container {
  gap: 10px;
}

.participant-count {
  font-size: 0.9rem;
  color: #555;
  margin-left: 10px; /* 불모양 아이콘과 간격을 주기 위해 왼쪽 여백 추가 */
}

.participant-icon {
  font-size: 1.2rem;
  color: #ff6347;
}

/* 스크롤 버튼들 감싸는 div */
.scroll-buttons-container {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  justify-content: space-between;
  width: 120px;
}

/* 스크롤 버튼 */
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

/* X 버튼 */
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
</style>
