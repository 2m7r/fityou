<template>
  <div class="modal-overlay" @click="closeModal">
    <div class="modal-content" @click.stop>
      <h2 class="modal-title">운동일기</h2>
      <p class="modal-description">오늘의 운동을 작성해주세요.</p>

      <!-- 날짜 선택 -->
      <div class="form-group">
        <input type="date" v-model="workoutDate" class="date-input" />
      </div>

      <!-- 운동일기 내용 입력 -->
      <div class="form-group">
        <textarea v-model="workoutContent" id="workout" placeholder="운동 내용을 입력하세요" class="workout-input"></textarea>
      </div>

      <!-- 운동 선택 및 루틴 입력 영역 (왼쪽/오른쪽 나누기) -->
      <div class="exercise-container">
        <!-- 왼쪽: 운동 카테고리 선택 -->
        <div class="exercise-category">
          <h3 class="category-title">운동 선택</h3>
          <div class="category-tabs">
            <button
              v-for="(category, index) in exerciseCategories"
              :key="index"
              :class="['category-tab', { active: selectedCategory === category }]"
              @click="selectCategory(category)"
              :style="{
                backgroundColor: selectedCategory === category ? categoryColors[category] : '#f0f0f0',
                color: selectedCategory === category ? 'white' : 'black'
              }"
            >
              {{ category }}
            </button>
          </div>

          <div v-if="selectedCategory" class="exercise-boxes">
            <div
              v-for="(name, index) in exercisesByCategory[selectedCategory]"
              :key="index"
              class="exercise-box"
              @click="addExerciseToRoutine(name)"
            >
              {{ name }}
            </div>
          </div>
        </div>

        <!-- 오른쪽: 운동 루틴 입력 -->
        <div class="exercise-routine">
          <h3 class="routine-title">운동 루틴</h3>
          <div v-for="(exercise, index) in selectedExercises" :key="index" class="exercise-entry">
            <div class="exercise-box">
              <div class="exercise-name">{{ exercise.name }}</div>

              <div class="input-group">
                <input v-model.number="exercise.weight" type="number" placeholder="무게" min="0" class="input-field weight-field" />
                <span>kg</span>
                <input v-model.number="exercise.reps" type="number" placeholder="횟수" min="0" class="input-field reps-field" />
                <span>회</span>
                <input v-model.number="exercise.sets" type="number" placeholder="세트수" min="0" class="input-field sets-field" />
                <span>세트</span>
              </div>
            </div>
          </div>
        </div>
      </div>


      <!-- 제출 버튼 -->
      <div class="modal-actions">
        <button class="btn btn-secondary" @click="closeModal">취소</button>
        <button class="btn btn-primary" @click="submitWorkoutLog" :disabled="!isFormValid">제출</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import apiClient from '@/components/api/apiClient';

const props = defineProps({
  userId: {
    type: Number,
    required: true,
  },
});

const emit = defineEmits(['close']);

// 상태 정의
const workoutContent = ref('');
const isWorkoutExist = ref(false);
const workoutDate = ref('');

// sessionStorage에서 'user' 키로 객체를 가져오기
const user = ref(JSON.parse(sessionStorage.getItem('user')));

// userId가 존재하는지 확인
const name = ref(user.value ? user.value.name : null);

// 운동 종류 및 루틴 관련 상태
const exerciseCategories = ref(['가슴', '등', '어깨', '하체', '팔', '복부', '전신']); // 카테고리
const selectedCategory = ref('');
const selectedExercises = ref([]); // 선택된 운동 루틴
const exercisesByCategory = ref({
  '가슴': ['벤치프레스', '덤벨프레스', '푸쉬업'],
  '등': ['랫풀다운', '풀업', '로우'],
  '어깨': ['밀리터리 프레스', '덤벨 숄더 프레스', '레터럴 레이즈'],
  '하체': ['스쿼트', '레그프레스', '런지'],
  '팔': ['바벨 컬', '덤벨 컬', '트라이셉스 푸쉬다운'],
  '복부': ['크런치', '플랭크', '레그레이즈'],
  '전신': ['버피', '점핑잭', '케틀벨 스윙'],
}); // 카테고리별 운동

// 카테고리별 색상 설정
const categoryColors = {
  '가슴': '#54a673', '등': '#8E44AD', '어깨': '#3498DB',
  '하체': '#F39C12', '팔': '#E74C3C', '복부': '#1ABC9C', '전신': '#fa8ec0'
};

// 운동일기 제출 여부 체크
const isFormValid = computed(() => {
  return workoutContent.value.trim() !== '' && selectedExercises.value.length > 0;
});

// 모달 닫기
const closeModal = () => {
  emit('close');
};

// 운동 루틴 데이터 받아오기
const fetchWorkoutData = async () => {
  try {
    const response = await apiClient.get(`/api-workout/feed/${props.userId}`);
    if (response.data) {
      isWorkoutExist.value = true;
      workoutContent.value = response.data.content;
      selectedExercises.value = response.data.exercises || [];
    }
  } catch (error) {
    console.error("운동일기 불러오기 실패", error);
  }
};

// 운동 카테고리 선택
const selectCategory = (category) => {
  selectedCategory.value = category;
};

// 운동을 루틴에 추가
const addExerciseToRoutine = (exercise) => {
  const exists = selectedExercises.value.some(item => item.name === exercise);
  if (!exists) {
    selectedExercises.value.push({
      name: exercise,
      category: selectedCategory.value,
      weight: '',
      reps: '',
      sets: ''
    });
  }
};

const logFormData = (formData) => {
  for (let [key, value] of formData.entries()) {
    console.log(key, value);
  }
};

const submitWorkoutLog = async () => {
  try {
    const workoutData = {
      description: workoutContent.value,
      userId: props.userId,
      name: name.value,
      recordDate: workoutDate.value,
      exerciseNames: selectedExercises.value.map(exercise => exercise.name),
      weights: selectedExercises.value.map(exercise => exercise.weight),
      reps: selectedExercises.value.map(exercise => exercise.reps),
      sets: selectedExercises.value.map(exercise => exercise.sets)
    };

    // 로그 데이터 출력
    console.log('로그 데이터:', workoutData);

    // API 호출 (JSON 방식으로 전송)
    const response = await apiClient.post(`/api-workout/create/${props.userId}`, workoutData);
    alert("운동일기가 등록되었습니다.");
    closeModal();
    window.location.reload();
    console.log("운동일기 제출 성공", response.data);
  } catch (error) {
    alert("해당 날짜의 운동일기가 이미 존재합니다");
    console.error("운동일기 제출 실패", error);
  }
};

onMounted(() => {
  fetchWorkoutData();
});
</script>

<style scoped>
/* 스타일 수정 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: white;
  width: 80%;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  max-width: 850px;
  overflow: hidden;
}

.modal-title {
  font-weight: bold;
  text-align: center;
  margin-bottom: 5px;
}

.modal-description {
  text-align: center;
  margin-top: 10px;
}

.form-group {
  margin-bottom: 20px;
}

.workout-input {
  width: 100%;
  height: 100px;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.exercise-container {
  display: flex;
  justify-content: space-between;
  max-height: 400px;  /* 최대 높이 설정 */
  overflow-y: auto;   /* 세로 스크롤 활성화 */
}

.exercise-category, .exercise-routine {
  width: 48%;
}

.category-title, .routine-title {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 10px;
}

.category-tabs {
  display: flex;
  flex-wrap: wrap;
}

.category-tab {
  padding: 8px 16px;
  margin: 5px;
  border-radius: 12px;
  cursor: pointer;
  border: 1px solid transparent;
  transition: all 0.3s ease;
}

.category-tab.active {
  font-weight: bold;
  color: white;
}

.exercise-box {
  background-color: #f9f9f9;
  padding: 12px;
  margin: 5px;
  border: 1px solid #ddd;
  border-radius: 12px;
  cursor: pointer;
  width: 90%;
  transition: all 0.3s ease;
}

.exercise-entry {
  margin-bottom: 20px;
}

.exercise-name {
  font-weight: bold;
}

.input-group {
  display: flex;
  gap: 10px;
}

.input-field {
  width: 60px;
  padding: 5px;
  text-align: center;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.input-field:focus {
  outline: none;
  border-color: #54a673;
}

.image-upload-container {
  display: flex;
  gap: 10px;
}

.image-preview-container {
  position: relative;
}

.image-preview {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 8px;
}

.remove-image-btn {
  position: absolute;
  top: -10px;
  right: -10px;
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  border: none;
  border-radius: 50%;
  padding: 5px;
  cursor: pointer;
}

.modal-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.btn {
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
}

.btn-secondary {
  background-color: #f0f0f0;
  border: 1px solid #ddd;
}

.btn-primary {
  background-color: #54a673;
  color: white;
}



.image-upload-btn {
  padding: 10px;
  background-color: #54a673;
  color: white;
  border: none;
  cursor: pointer;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.image-upload-btn i {
  font-size: 24px;
}
</style>
