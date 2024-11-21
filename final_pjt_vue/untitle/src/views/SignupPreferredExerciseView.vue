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
import apiClient from '@/components/api/apiClient'; // axios 클라이언트

export default {
  data() {
    return {
      categories: [
        '헬스', '필라테스', '요가', '방송댄스', '크로스핏',
        '수영', '러닝', '사이클', '스피닝', '복싱',
        '태권도', '유도', '스쿼시', '탁구', '배드민턴',
        '축구', '농구', '배구', '골프', '테니스',
        '보디빌딩', '발레', '에어로빅', '클라이밍', '기타'
      ],
      selectedCategories: [],
    };
  },
  methods: {
    // 운동 항목을 클릭할 때 선택 및 해제
    toggleCategory(category) {
      if (this.selectedCategories.includes(category)) {
        this.selectedCategories = this.selectedCategories.filter(c => c !== category);
      } else {
        this.selectedCategories.push(category);
      }
    },

    // 선호 운동 제출
    async submitPreferredExercises() {
      if (this.selectedCategories.length === 0) {
        alert('하나 이상의 운동을 선택해주세요.');
        return;
      }

      try {
        const response = await apiClient.post('/api-user/preferred-exercises', {
          exercises: this.selectedCategories
        });

        alert('선호 운동 선택 완료');
        this.$router.push({ name: 'home' });
      } catch (error) {
        console.error('선호 운동 선택 실패', error);
        alert('선호 운동 선택에 실패했습니다.');
      }
    }
  }
}
</script>

<style scoped>
.exercise-selection-container {
  text-align: center;
  padding: 40px 20px;
  display: flex;
  flex-direction: column;
  align-items: center; /* 세로 중앙 정렬 */
  margin-top: 10%;
}

.title {
  font-size: 30px;
  font-weight: bold;
  margin-bottom: 50px;
}

.exercise-categories {
  display: grid; /* 그리드 사용 */
  grid-template-columns: repeat(5, 150px); /* 한 줄에 5개의 박스를 고정 크기로 배치 */
  gap: 30px; /* 박스 간 간격 */
  justify-content: center; /* 그리드 항목을 가로로 중앙 정렬 */
  justify-items: center; /* 그리드 항목을 세로로 중앙 정렬 */
}

.exercise-box {
  width: 150px;  /* 각 박스의 너비 고정 */
  height: 110px; /* 박스의 세로 길이 고정 */
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
  border: 2px solid #bbbbbb; /* 선택 시 보더 컬러를 회색으로 */
  box-shadow: 0 4px 8px rgba(200, 200, 200, 0.3);
}

.exercise-box:hover {
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.category-name {
  font-size: 16px;
  font-weight: 500;
}

/* 제출 버튼 스타일 */
.submit-btn {
  margin-top: 50px;
  padding: 17px 30px; /* padding을 더 크게 설정하여 버튼 크기 확대 */
  font-size: 18px; /* 글씨 크기도 키움 */
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

