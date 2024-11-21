<template>
  <div class="find-password-form">
    <h2>비밀번호 찾기</h2>
    <form @submit.prevent="findPassword">
      <!-- 이메일 입력 -->
      <div class="input-group">
        <label for="email">이메일</label>
        <input
          type="email"
          id="email"
          v-model="email"
          @blur="getSecurityQuestion"
          required
          placeholder="이메일을 입력하세요"
        />
      </div>

      <!-- 아이디 입력 -->
      <div class="input-group">
        <label for="username">아이디</label>
        <input
          type="text"
          id="username"
          v-model="username"
          required
          placeholder="아이디를 입력하세요"
        />
      </div>

      <!-- 이름 입력 -->
      <div class="input-group">
        <label for="name">이름</label>
        <input
          type="text"
          id="name"
          v-model="name"
          required
          placeholder="이름을 입력하세요"
        />
      </div>

      <!-- 보안 질문 -->
      <div v-if="securityQuestion" class="input-group">
        <label>{{ securityQuestion }}</label>
        <input
          type="text"
          v-model="securityAnswer"
          required
          placeholder="보안 질문의 답을 입력하세요"
        />
      </div>

      <!-- 비밀번호 찾기 버튼 -->
      <button type="submit">비밀번호 찾기</button>
    </form>
    <p>비밀번호를 기억하셨나요? <span @click="goToLogin" class="link">로그인</span></p>
  </div>
</template>

<script>
import apiClient from '@/components/api/apiClient';

export default {
  data() {
    return {
      email: '', // 이메일 입력 값
      name: '', // 이름 입력 값
      username: '',
      securityQuestion: '', // 보안 질문
      securityAnswer: '', // 보안 질문 답변
    };
  },
  methods: {
    // 이메일 입력 후 보안 질문을 가져오는 함수
    async getSecurityQuestion() {
      if (!this.email) return; // 이메일이 비어있으면 요청하지 않음
      try {
        const response = await apiClient.get('/api-user/get-security-question', {
          params: { email: this.email },
        });
        this.securityQuestion = response.data.securityQuestion || ''; // 보안 질문 설정
      } catch (error) {
        console.error('보안 질문을 가져오는 데 실패했습니다.', error);
        alert('등록된 이메일이 아닙니다.');
        this.securityQuestion = ''; // 실패 시 초기화
      }
    },

    // 비밀번호 찾기 요청
    async findPassword() {
      if (!this.email || !this.name || !this.securityAnswer) {
        alert('모든 필드를 입력해주세요.');
        return;
      }

      try {
        const response = await apiClient.post('/api-user/find-password', {
          email: this.email,
          name: this.name,
          username: this.username,
          securityAnswer: this.securityAnswer,
        });

        // 서버에서 반환된 비밀번호를 알림으로 표시
        alert(`비밀번호는 다음과 같습니다: ${response.data}`);
        this.$router.push({ name: 'login' }); // 로그인 페이지로 리디렉션
      } catch (error) {
        console.error('비밀번호 찾기 실패', error);
        alert('입력 정보가 일치하지 않습니다.');
      }
    },

    // 로그인 페이지로 이동
    goToLogin() {
      this.$router.push({ name: 'login' });
    },
  },
};
</script>

<style scoped>
.find-password-form {
  width: 100%;
  max-width: 480px;
  margin: 0 auto;
  padding: 40px;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.1);
}
h2 {
  text-align: center;
  margin-bottom: 30px;
  font-size: 24px;
  color: #333;
}

.input-group {
  margin-bottom: 20px;
}

.input-group label {
  display: block;
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.input-group input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 14px;
}

button {
  width: 100%;
  padding: 12px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
  margin-top: 20px;
}

button:hover {
  background-color: #3c8f6a;
}

.link {
  color: #42b983;
  cursor: pointer;
}

.link:hover {
  text-decoration: underline;
}
</style>
