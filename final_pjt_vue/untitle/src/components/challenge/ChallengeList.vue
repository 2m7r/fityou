<template>
  <div class="recommended-challenges-container">
    <h2>모든 챌린지</h2>

    <!-- 모든 챌린지 카드들이 들어있는 스크롤 영역 -->
    <div class="challenges-scroll-container" ref="scrollContainer">
      <div v-if="allChallenges.length === 0" class="col-12">
        <p>챌린지가 없습니다.</p>
      </div>

      <!-- 카드들 -->
      <div class="card-wrapper">
        <div
          v-for="challenge in allChallenges"
          :key="challenge.challengeId"
          class="card-container"
        >
          <div class="card" @click="viewChallengeDetail(challenge)">
            <img :src="challenge.image || 'https://via.placeholder.com/150'" class="card-img-top" alt="Challenge Image" />
            <div class="card-body">
              <h5 class="card-title">{{ challenge.name }}</h5>
              <p class="card-text">{{ challenge.description }}</p>

              <div class="button-container">
                <!-- 참여하기 버튼을 눌렀을 때 -->
                <button
                  v-if="!isUserParticipated(challenge)"
                  class="btn btn-success rounded-full"
                  @click.stop="joinChallenge(challenge)"
                  :disabled="isChallengeClosed(challenge)"
                >
                참여하기
                </button>
                <!-- 이미 참여한 경우 -->
                <button
                  v-else
                  class="btn btn-secondary rounded-full"
                  disabled
                >
                참여한 챌린지
                </button>
              </div>
            </div>

            <!-- 참여자 수 텍스트 및 불모양 아이콘 -->
            <div class="participant-info">
              <span class="participant-count">
                {{ challenge.participantCount }} 명 참여
              </span>
              <i class="bi bi-fire participant-icon"></i>
            </div>

            <!-- 마감된 챌린지 표시 -->
            <div v-if="isChallengeClosed(challenge)" class="closed-badge">
              <span>끝난 챌린지</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 스크롤 버튼 -->
    <div class="scroll-buttons-container">
      <button class="scroll-button left" @click="scrollLeft">
        <i class="bi bi-arrow-left"></i>
        <!-- 왼쪽 화살표 아이콘 -->
      </button>
      <button class="scroll-button right" @click="scrollRight">
        <i class="bi bi-arrow-right"></i>
        <!-- 오른쪽 화살표 아이콘 -->
      </button>
    </div>

     <!-- 챌린지 상세 모달 -->
     <div v-if="isModalOpen" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <span class="close-btn" @click="closeModal">&times;</span>
        <h2 class="modal-title">{{ modalChallenge.name }}</h2>
        <p>{{ modalChallenge.description }}</p>
        <p><strong>등록유저:</strong> {{ modalChallenge.creatorUsername }}</p>
        <p><strong>시작일:</strong> {{ modalChallenge.durationStart }}</p>
        <p><strong>마감일:</strong> {{ modalChallenge.durationEnd }}</p>
        <p><strong>참여자 수:</strong> {{ modalChallenge.participantCount }} 명</p>
        
        <button class="btn btn-success btn-modal rounded-full"
          @click="joinChallenge(modalChallenge)"
          :disabled="isChallengeClosed(modalChallenge) || isUserParticipated(modalChallenge)">참여하기</button>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import apiClient from '../api/apiClient';
import eventBus from "@/eventBus";

// 이미 참여한 챌린지인지 확인하는 함수
const isUserParticipated = (challenge) => {
  return userParticipatedChallenges.value.some(challengeItem => challengeItem.challengeId === challenge.challengeId);
};

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

const allChallenges = ref([]); // 추천 챌린지 목록
const userParticipatedChallenges = ref([]); // 내가 참여한 챌린지 목록
const scrollContainer = ref(null);

// 사용자 참여한 챌린지 목록 가져오는 함수
const fetchUserParticipatedChallenges = async () => {
  try {
    const user = JSON.parse(sessionStorage.getItem('user'));
    const userId = user.userId;  // 로그인된 사용자 ID
    const response = await apiClient.get(`/api-challenge/challenges`, { params: { userId } });
    userParticipatedChallenges.value = response.data; // 사용자가 참여한 챌린지 목록 저장
  } catch (error) {
    console.error("참여한 챌린지 목록을 가져오는 데 실패했습니다.", error);
  }
};

// 모달 상태 관리
const isModalOpen = ref(false);
const modalChallenge = ref({}); // 초기값을 빈 객체로 설정


// 모든 챌린지 데이터를 가져오는 함수
const fetchAllChallenges = async () => {
  try {
    const response = await apiClient.get("/api-challenge"); // 서버에서 데이터를 가져옴
    allChallenges.value = response.data; // 모든 챌린지 목록을 받아옴
  } catch (error) {
    console.error("모든 챌린지 데이터를 가져오는 데 실패했습니다.", error);
  }
};

// 챌린지 참여하기 버튼 클릭 시
const joinChallenge = async (challenge) => {
  const user = JSON.parse(sessionStorage.getItem('user'));
  const userId = user.userId  // userId가 없으면 null을 반환
  const challenge_id = challenge.challengeId;
  
  console.log('userId:', userId); // userId 확인
  console.log('challenge_id:', challenge_id); // challenge_id 확인

  try {
    // 서버에 참여 요청
    const response = await apiClient.post(`/api-challenge/join/${challenge_id}`, null, {
      params: { userId }
    });

    // 네트워크 응답 상태 확인
    console.log('API 응답:', response);

    // 네트워크 응답 상태 확인
    if (response.status === 200) {
      // 참여가 성공하면
      // 1. allChallenges에서 해당 챌린지 제거
      const index = allChallenges.value.findIndex(c => c.challengeId === challenge.challengeId);
      if (index !== -1) {
        allChallenges.value.splice(index, 1); // 해당 항목 삭제
      }

      isModalOpen.value = false;
      // 챌린지 목록을 새로고침하도록 EventBus에 알림
      eventBus.triggerRefresh();
    }
  } catch (error) {
    if (error.response) {
      alert('이미 참여하신 것 아닌가요?')
      console.error('서버 에러 메시지:', error.response.data); // 서버에서 반환한 오류 메시지 확인
    } else {
      console.error('네트워크 에러 또는 기타 오류:', error);
    }
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

// 챌린지 마감 여부 확인 함수
const isChallengeClosed = (challenge) => {
  const currentDate = new Date();
  const endDate = new Date(challenge.durationEnd);
  return currentDate > endDate; // 마감일이 지나면 true 반환
};

onMounted(() => {
  fetchAllChallenges(); // 모든 챌린지 목록 가져오기
  fetchUserParticipatedChallenges(); // 사용자가 참여한 챌린지 목록 가져오기
});
</script>
  


<style scoped>
/* 제목 스타일 */
.modal-title {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
  text-align: center;
  border-bottom: 2px solid #ddd;
  padding-bottom: 10px;
}
  .closed-badge {
    position: absolute;
    top: 10px;
    left: 10px;
    background-color: rgba(255, 0, 0, 0.7);
    color: white;
    padding: 5px 10px;
    border-radius: 5px;
    font-size: 1rem;
  }
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

  h2 {
    font-family: 'Bold';
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
    margin-top: -10px;
    margin-bottom: 0px;
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
  font-size : 1.2rem
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

.rounded-full {
  border-radius: 50px;
  background-color: 54a673;
}
</style>
  