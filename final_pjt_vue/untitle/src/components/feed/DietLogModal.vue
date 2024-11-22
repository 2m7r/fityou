<template>
  <div class="modal-overlay" @click="closeModal">
    <div class="modal-content" @click.stop>
      <h2>식단일기 작성</h2>
      
      <p>오늘의 식단을 작성해주세요.</p>

      <div class="form-group">
        <label for="meal">식사 내용</label>
        <input v-model="dietContent" type="text" id="meal" placeholder="식사 내용을 입력하세요" />
      </div>

      <div class="modal-actions">
        <button class="btn btn-secondary" @click="closeModal">취소</button>
        <button class="btn btn-primary" @click="submitDietLog">제출</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import apiClient from '@/components/api/apiClient';

const props = defineProps({
  userId: {
    type: String,
    required: true,
  },
});

const emit = defineEmits(['close']);

const dietContent = ref('');
const isDietExist = ref(false);

// 모달 닫기
const closeModal = () => {
  emit('close');
};

// 식단일기 제출
const submitDietLog = async () => {
  try {
    if (isDietExist.value) {
      await apiClient.put(`/api-diet/feed/${props.userId}`, { content: dietContent.value });
      alert("식단일기가 수정되었습니다.");
    } else {
      await apiClient.post(`/api-diet/feed/${props.userId}`, { content: dietContent.value });
      alert("식단일기가 등록되었습니다.");
    }
    closeModal();
  } catch (error) {
    console.error("식단일기 제출 실패", error);
  }
};

// 컴포넌트 마운트 시 데이터 확인
onMounted(async () => {
  try {
    const response = await apiClient.get(`/api-diet/feed/${props.userId}`);
    if (response.data) {
      isDietExist.value = true;
      dietContent.value = response.data.content;
    }
  } catch (error) {
    console.error("식단일기 불러오기 실패", error);
  }
});
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
  padding: 20px;
  border-radius: 8px;
  width: 90%;
  max-width: 500px;
}

.form-group {
  margin-bottom: 10px;
}

.modal-actions {
  display: flex;
  justify-content: space-between;
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
</style>
