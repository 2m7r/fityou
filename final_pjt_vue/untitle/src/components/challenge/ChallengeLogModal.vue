<template>
    <div class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <h2 class="modal-title">챌린지 등록</h2>
        <p class="modal-description">새로운 챌린지를 등록하세요.</p>
  
        <!-- 챌린지 이름 입력 -->
        <div class="form-group">
          <label for="challengeName">챌린지 이름</label>
          <input
            type="text"
            v-model="challenge.name"
            id="challengeName"
            placeholder="챌린지 이름을 입력하세요"
            class="input-field"
            required
          />
        </div>
  
        <!-- 챌린지 설명 입력 -->
        <div class="form-group">
          <label for="description">설명</label>
          <textarea
            v-model="challenge.description"
            id="description"
            placeholder="챌린지 설명을 입력하세요"
            class="input-field"
            required
          ></textarea>
        </div>
  
        <!-- 시작 날짜 선택 -->
        <div class="form-group">
          <label for="durationStart">시작 날짜</label>
          <input
            type="date"
            v-model="challenge.durationStart"
            id="durationStart"
            class="input-field"
            required
          />
        </div>
  
        <!-- 종료 날짜 선택 -->
        <div class="form-group">
          <label for="durationEnd">종료 날짜</label>
          <input
            type="date"
            v-model="challenge.durationEnd"
            id="durationEnd"
            class="input-field"
            required
          />
        </div>
  
        <!-- 운동 종류 입력 -->
        <div class="form-group">
          <label for="exerciseType">운동 종류</label>
          <input
            type="text"
            v-model="challenge.exerciseType"
            id="exerciseType"
            placeholder="운동 종류를 입력하세요"
            class="input-field"
            required
          />
        </div>
  
        <!-- 제출 버튼 -->
        <div class="modal-actions">
          <button class="btn btn-secondary" @click="closeModal">취소</button>
          <button class="btn btn-primary" @click="submitChallenge">{{ isEditMode ? '수정' : '등록' }}</button>
        </div>
      </div>
    </div>
</template>
  
<script setup>
  import { ref, onMounted } from "vue";
  import apiClient from "@/components/api/apiClient";
  
  const props = defineProps({
    userId: {
      type: Number,
      required: true,
    },
    challengeId: {
      type: Number,
      default: null,
    },
  });

    // sessionStorage에서 'user' 키로 객체를 가져오기
    const user = ref(JSON.parse(sessionStorage.getItem('user')));

    // userId가 존재하는지 확인
    const name = ref(user.value ? user.value.name : null);
  
  const emit = defineEmits(["close"]);
  
  const challenge = ref({
    challengeId: null,
    name: "",
    creatorUserId: props.userId,
    creatorUsername: name, // 생성자 이름
    description: "",
    durationStart: "",
    durationEnd: "",
    exerciseType: "", // 운동 종류 입력 필드
  });
  
  const isEditMode = ref(false);
  
  // 모달 닫기
  const closeModal = () => {
    emit("close");
  };
  
  // 챌린지 등록/수정 제출
  const submitChallenge = async () => {
    try {
        await apiClient.post("/api-challenge/create", challenge.value);
        alert("챌린지가 등록되었습니다.");

      closeModal();
      window.location.reload();
    } catch (error) {
      console.error("챌린지 등록/수정 실패", error);
    }
  };

</script>
  
<style scoped>
  .modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.7);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1050;
  }
  
  .modal-content {
    background: white;
    padding: 40px;
    border-radius: 8px;
    width: 90%;
    max-width: 850px;
    height: auto;
    max-height: 6000px;
    overflow: hidden;
  }
  
  .modal-title {
    text-align: center;
    font-weight: bold;
    margin-bottom: 5px;
  }
  
  .modal-description {
    text-align: center;
    margin-bottom: 25px;
  }
  
  .form-group {
    margin-bottom: 20px;
  }
  
  .input-field {
    width: 100%;
    padding: 10px;
    border-radius: 10px;
    border: 1px solid #ddd;
    box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1);
  }
  
  .modal-actions {
    display: flex;
    justify-content: space-between;
    gap: 20px;
    align-items: center;
    margin-top: 20px;
  }
  
  .btn {
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  
  .btn-secondary {
    background-color: #ccc;
  }
  
  .btn-primary {
    background-color: #54a673;
    color: white;
  }
  
  .btn-primary:hover {
    background-color: #4e9d63;
  }
</style>
  