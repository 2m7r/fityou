<template>
  <div class="diet-modal-overlay" v-if="showModal">
    <div class="diet-modal-content">
      <h3>{{ isEditing ? "식단 수정" : "식단 작성" }}</h3>
      <div class="input-field">
        <label for="breakfastImage">아침 사진</label>
        <input type="file" @change="onFileChange('breakfast', $event)" />
      </div>
      <div class="input-field">
        <label for="lunchImage">점심 사진</label>
        <input type="file" @change="onFileChange('lunch', $event)" />
      </div>
      <div class="input-field">
        <label for="dinnerImage">저녁 사진</label>
        <input type="file" @change="onFileChange('dinner', $event)" />
      </div>
      <div class="input-field">
        <label for="content">식단 내용</label>
        <textarea v-model="dietContent" placeholder="오늘의 식단 내용을 작성하세요..."></textarea>
      </div>
      <div class="modal-actions">
        <button @click="submitDiet">{{ isEditing ? "수정 완료" : "등록 완료" }}</button>
        <button @click="closeModal">취소</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import axios from 'axios';
import { useUserStore } from '@/stores/userStore';
import { useRouter } from 'vue-router';

const showModal = ref(false);
const dietContent = ref('');
const breakfastImage = ref(null);
const lunchImage = ref(null);
const dinnerImage = ref(null);
const isEditing = ref(false);
const dietId = ref(null); // 기존 식단일기 ID (수정용)

const userStore = useUserStore();
const router = useRouter();

const openModal = (date) => {
  showModal.value = true;
  checkDietExist(date);
};

const closeModal = () => {
  showModal.value = false;
  resetForm();
};

const resetForm = () => {
  dietContent.value = '';
  breakfastImage.value = null;
  lunchImage.value = null;
  dinnerImage.value = null;
  isEditing.value = false;
  dietId.value = null;
};

const checkDietExist = async (date) => {
  try {
    const response = await axios.get(`/api-diet/feed/${userStore.userId}?date=${date}`);
    if (response.data.length > 0) {
      const diet = response.data[0];
      dietContent.value = diet.content;
      breakfastImage.value = diet.breakfastImagePath;
      lunchImage.value = diet.lunchImagePath;
      dinnerImage.value = diet.dinnerImagePath;
      dietId.value = diet.dietId;
      isEditing.value = true; // 수정 모드
    }
  } catch (error) {
    console.error('Error checking diet:', error);
  }
};

const onFileChange = (meal, event) => {
  const file = event.target.files[0];
  if (meal === 'breakfast') breakfastImage.value = file;
  else if (meal === 'lunch') lunchImage.value = file;
  else if (meal === 'dinner') dinnerImage.value = file;
};

const submitDiet = async () => {
  const formData = new FormData();
  formData.append('content', dietContent.value);
  formData.append('breakfastImage', breakfastImage.value);
  formData.append('lunchImage', lunchImage.value);
  formData.append('dinnerImage', dinnerImage.value);

  if (isEditing.value) {
    try {
      const response = await axios.put(`/api-diet/${dietId.value}`, formData);
      alert('식단일기 수정 성공');
      closeModal();
    } catch (error) {
      console.error('Error updating diet:', error);
      alert('식단일기 수정 실패');
    }
  } else {
    try {
      const response = await axios.post('/api-diet/create', formData);
      alert('식단일기 등록 성공');
      closeModal();
    } catch (error) {
      console.error('Error creating diet:', error);
      alert('식단일기 등록 실패');
    }
  }
};
</script>

<style scoped>
.diet-modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.diet-modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 10px;
  max-width: 500px;
  width: 100%;
}

.input-field {
  margin-bottom: 15px;
}

.input-field label {
  display: block;
  margin-bottom: 5px;
}

.input-field input,
.input-field textarea {
  width: 100%;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ccc;
}

.modal-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.modal-actions button {
  padding: 10px 20px;
  background-color: #54a673;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
</style>
