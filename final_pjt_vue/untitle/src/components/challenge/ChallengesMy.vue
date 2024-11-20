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

      <!-- 스크롤 버튼 (스크롤 컨테이너 내에서 고정) -->
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
  padding-bottom: 20px;
  padding-top: 20px;
  border-radius: 10px;
  width: 100%;
  box-sizing: border-box;
  overflow-x: scroll; /* 수평 스크롤 허용 */
  white-space: nowrap;
  position: relative; /* 버튼을 스크롤 컨테이너 내부에서 위치시키기 위해 상대적인 위치 설정 */
}

/* 스크롤바 숨기기 */
.challenges-scroll-container::-webkit-scrollbar {
  display: none; /* 웹킷 기반 브라우저에서 스크롤바 숨기기 */
}

.card-wrapper {
  padding-left: 1.2%;
  display: flex;
  gap: 10px;
  flex-wrap: nowrap;
  width: 100%;
  transition: scroll-left 0.3s ease; /* 스크롤 애니메이션 추가 */
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
  height: 3.0em;
  line-height: 1.5;
}

.card:hover {
  transform: scale(1.05);
}

/* 스크롤 버튼 (스크롤 컨테이너 내에서 고정) */
.scroll-button {
  position: absolute;  /* 스크롤 컨테이너 내에서 절대 위치 설정 */
  top: 50%;  /* 세로 중앙 정렬 */
  transform: translateY(-50%); /* 정확히 중앙 배치 */
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
  z-index: 2; /* 버튼이 카드들 위에 표시되도록 */
}

/* 왼쪽 버튼 */
.scroll-button.left {
  left: 10px; /* 스크롤 컨테이너의 왼쪽에 고정 */
}

/* 오른쪽 버튼 */
.scroll-button.right {
  right: 10px; /* 스크롤 컨테이너의 오른쪽에 고정 */
}

.scroll-button:hover {
  background-color: rgba(0, 0, 0, 0.4);
}

/* X 버튼 */
.btn-close {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: transparent; /* 기본 배경을 투명하게 설정 */
  border-color: transparent; /* 테두리도 없애기 */
  color: white; /* 텍스트 색상은 흰색 유지 */
  width: 30px;
  height: 30px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s ease, box-shadow 0.3s ease; /* 배경과 그림자 효과 부드럽게 전환 */
}

/* 호버 상태 */
.btn-close:hover {
  background-color: rgba(0, 0, 0, 0.3); /* 호버 시 반투명한 검은색 배경 */
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.2); /* 호버 시 주변에 그림자 효과 */
}
</style>
