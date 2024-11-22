<template>
  <div class="modal-overlay" @click="closeModal">
    <div class="modal-content" @click.stop>
      <h2 class="modal-title">식단일기</h2>
      
      <p class="modal-description">오늘의 식단을 작성해주세요.</p>

      <!-- 식사 내용 입력 -->
      <div class="form-group">
        <textarea v-model="dietContent" id="meal" placeholder="식사 내용을 입력하세요" class="meal-input"></textarea>
      </div>

      <!-- 사진 첨부 -->
      <div class="form-group">
        <div class="image-upload-container">
          <!-- 아침 사진 첨부 -->
          <div class="image-item">
            <label for="breakfastImage" class="image-label">
              <button v-if="!breakfastImage" class="image-upload-btn" @click="triggerFileInput('breakfast')">
                <i class="bi bi-plus"></i>
              </button>
              <input v-if="!breakfastImage" type="file" id="breakfastImage" class="image-input" @change="handleImageChange('breakfast', $event)" />
              <div v-if="breakfastImagePreview" class="image-preview-container">
                <img :src="breakfastImagePreview" alt="아침 식사 사진 미리보기" class="image-preview" />
                <button class="remove-image-btn" @click="removeImage('breakfast')">
                  <i class="bi bi-x-circle"></i> <!-- X 버튼을 부트스트랩 아이콘으로 변경 -->
                </button>
              </div>
            </label>
          </div>

          <!-- 점심 사진 첨부 -->
          <div class="image-item">
            <label for="lunchImage" class="image-label">
              <button v-if="!lunchImage" class="image-upload-btn" @click="triggerFileInput('lunch')">
                <i class="bi bi-plus"></i>
              </button>
              <input v-if="!lunchImage" type="file" id="lunchImage" class="image-input" @change="handleImageChange('lunch', $event)" />
              <div v-if="lunchImagePreview" class="image-preview-container">
                <img :src="lunchImagePreview" alt="점심 식사 사진 미리보기" class="image-preview" />
                <button class="remove-image-btn" @click="removeImage('lunch')">
                  <i class="bi bi-x-circle"></i> <!-- X 버튼을 부트스트랩 아이콘으로 변경 -->
                </button>
              </div>
            </label>
          </div>

          <!-- 저녁 사진 첨부 -->
          <div class="image-item">
            <label for="dinnerImage" class="image-label">
              <button v-if="!dinnerImage" class="image-upload-btn" @click="triggerFileInput('dinner')">
                <i class="bi bi-plus"></i>
              </button>
              <input v-if="!dinnerImage" type="file" id="dinnerImage" class="image-input" @change="handleImageChange('dinner', $event)" />
              <div v-if="dinnerImagePreview" class="image-preview-container">
                <img :src="dinnerImagePreview" alt="저녁 식사 사진 미리보기" class="image-preview" />
                <button class="remove-image-btn" @click="removeImage('dinner')">
                  <i class="bi bi-x-circle"></i> <!-- X 버튼을 부트스트랩 아이콘으로 변경 -->
                </button>
              </div>
            </label>
          </div>
        </div>
      </div>

      <!-- 제출 버튼 -->
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
    type: Number,
    required: true,
  },
});

const emit = defineEmits(['close']);

const dietContent = ref('');
const isDietExist = ref(false);

// 이미지 미리보기용 변수
const breakfastImagePreview = ref(null);
const lunchImagePreview = ref(null);
const dinnerImagePreview = ref(null);

// 이미지 파일 저장용 변수
const breakfastImage = ref(null);
const lunchImage = ref(null);
const dinnerImage = ref(null);

// 모달 닫기
const closeModal = () => {
  emit('close');  // 부모로부터 받은 close 이벤트 실행
};

// 이미지 업로드 처리
const handleImageChange = (mealType, event) => {
  const file = event.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = () => {
      if (mealType === 'breakfast') {
        breakfastImagePreview.value = reader.result;
        breakfastImage.value = file;
      } else if (mealType === 'lunch') {
        lunchImagePreview.value = reader.result;
        lunchImage.value = file;
      } else if (mealType === 'dinner') {
        dinnerImagePreview.value = reader.result;
        dinnerImage.value = file;
      }
    };
    reader.readAsDataURL(file);
  }
};

// 이미지 제거
const removeImage = (mealType) => {
  if (mealType === 'breakfast') {
    breakfastImagePreview.value = null;
    breakfastImage.value = null;
  } else if (mealType === 'lunch') {
    lunchImagePreview.value = null;
    lunchImage.value = null;
  } else if (mealType === 'dinner') {
    dinnerImagePreview.value = null;
    dinnerImage.value = null;
  }
};

// 식단일기 제출
const submitDietLog = async () => {
  try {
    const formData = new FormData();
    formData.append('content', dietContent.value);
    formData.append('userId', props.userId);

    if (breakfastImage.value) {
      formData.append('breakfastImage', breakfastImage.value);
    }
    if (lunchImage.value) {
      formData.append('lunchImage', lunchImage.value);
    }
    if (dinnerImage.value) {
      formData.append('dinnerImage', dinnerImage.value);
    }

    if (isDietExist.value) {
      await apiClient.put(`/api-diet/feed/${props.userId}`, formData);
      alert("식단일기가 수정되었습니다.");
    } else {
      await apiClient.post(`/api-diet/feed/${props.userId}`, formData);
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
      breakfastImagePreview.value = response.data.breakfastImagePath;
      lunchImagePreview.value = response.data.lunchImagePath;
      dinnerImagePreview.value = response.data.dinnerImagePath;
    }
  } catch (error) {
    console.error("식단일기 불러오기 실패", error);
  }
});

// 사진 선택을 위한 파일 입력 트리거 함수
const triggerFileInput = (mealType) => {
  const inputElement = document.getElementById(`${mealType}Image`);
  if (inputElement) {
    inputElement.click();
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
  height: auto; /* 세로 길이가 유동적으로 변하도록 설정 */
  max-height: 6000px; /* 최대 높이 제한 */
  overflow: hidden; /* 내부 스크롤을 막고 컨텐츠에 맞춰 높이 조정 */
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

.meal-input {
  width: 100%;
  height: 100px;
  padding: 10px;
  border-radius: 10px;
  border: 1px solid #ddd;
  resize: none;
  box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1);
}

.image-upload-container {
  display: flex;
  gap: 20px;
}

.image-item {
  flex: 1;
}

.image-label {
  display: block;
  position: relative;
  width: 100%;
}

.image-upload-btn {
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #54a673;
  color: white;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  border: none;
  cursor: pointer;
  font-size: 24px;
}

.image-input {
  display: none;
}

.image-preview-container {
  position: relative;
  margin-top: 10px;
}

.image-preview {
  width: 100%;
  height: auto;
  max-height: 200px;
  object-fit: cover;
  border-radius: 10px;
}

.remove-image-btn {
  position: absolute;
  top: 5px;
  right: 5px;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  border-radius: 50%;
  cursor: pointer;
  padding: 5px;
  font-size: 18px;
  width: 30px;
  height: 30px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.remove-image-btn:hover {
  background-color: #f44336;
}
</style>
