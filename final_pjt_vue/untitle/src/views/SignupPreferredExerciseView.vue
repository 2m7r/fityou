<template>
  <div class="exercise-selection-container">
    <h2 class="title">선호하는 운동을 선택하세요</h2>
    <div class="exercise-categories">
      <div
        v-for="(category, index) in categories"
        :key="index"
        :class="['exercise-box', { 'selected': selectedCategories.includes(category) }]"
        @click="toggleCategory(category)"
      >
        <p class="category-name">{{ category }}</p>
      </div>
    </div>

    <button 
      :disabled="selectedCategories.length === 0" 
      class="submit-btn" 
      :class="{ 'active': selectedCategories.length > 0 }"
      @click="submitPreferredExercises"
    >
      제출
    </button>
  </div>
</template>

<script>
import { ref } from 'vue'; // ref를 사용하여 reactive data 정의
import apiClient from '@/components/api/apiClient'; // axios 클라이언트
import { useUserStore } from '@/stores/userStore'; // Pinia store import
import { useRouter } from 'vue-router'

export default {
  setup() {
    const router = useRouter();
    
    // userStore를 setup에서 사용
    const userStore = useUserStore();

    // 운동 카테고리와 선택된 카테고리 상태
    const categories = ref([
      '헬스', '필라테스', '요가', '방송댄스', '크로스핏',
      '수영', '러닝', '사이클', '스피닝', '복싱',
      '태권도', '유도', '스쿼시', '탁구', '배드민턴',
      '축구', '농구', '배구', '골프', '테니스',
      '보디빌딩', '발레', '에어로빅', '클라이밍', '기타'
    ]);

    const selectedCategories = ref([]); // 선택된 카테고리

    // 운동 항목을 클릭할 때 선택 및 해제
    const toggleCategory = (category) => {
      if (selectedCategories.value.includes(category)) {
        selectedCategories.value = selectedCategories.value.filter(c => c !== category);
      } else {
        selectedCategories.value.push(category);
      }
    };

    // 선호 운동 제출
    const submitPreferredExercises = async () => {
      if (selectedCategories.value.length === 0) {
        alert('하나 이상의 운동을 선택해주세요.');
        return;
      }

      try {
        const response = await apiClient.post('/api-user/preferred-exercises', {
          loginUser: userStore.user,
          exercises: selectedCategories.value
        });

        alert('선호 운동 선택 완료');
        router.push({ name: 'home' });
      } catch (error) {
        console.error('선호 운동 선택 실패', error);
        alert('선호 운동 선택에 실패했습니다.');
      }
    };

    return {
      categories,
      selectedCategories,
      toggleCategory,
      submitPreferredExercises
    };
  }
};
</script>

<style scoped>
/* 스타일은 그대로 유지 */
.exercise-selection-container {
  text-align: center;
  padding: 40px 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 10%;
}

.title {
  font-size: 30px;
  font-weight: bold;
  margin-bottom: 50px;
}

.exercise-categories {
  display: grid;
  grid-template-columns: repeat(5, 150px);
  gap: 30px;
  justify-content: center;
  justify-items: center;
}

.exercise-box {
  width: 150px;
  height: 110px;
  background-color: #f8f9fa;
  border-radius: 12px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  transition: border-color 0.3s, box-shadow 0.3s;
  padding-top: 10px;
}

.exercise-box.selected {
  border: 2px solid #bbbbbb;
  box-shadow: 0 4px 8px rgba(200, 200, 200, 0.3);
}

.exercise-box:hover {
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.category-name {
  font-size: 16px;
  font-weight: 500;
}

.submit-btn {
  margin-top: 50px;
  padding: 17px 30px;
  font-size: 18px;
  border: none;
  border-radius: 50px;
  background-color: #ccc;
  color: white;
  cursor: not-allowed;
  opacity: 0.7;
  transition: background-color 0.3s;
}

.submit-btn.active {
  background-color: #54a673;
  cursor: pointer;
  opacity: 1;
}

.submit-btn:disabled {
  background-color: #e0e0e0;
}
</style>
