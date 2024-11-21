<template>
  <div class="find-id">
    <h2>아이디 찾기</h2>
    <form @submit.prevent="findId">
      <div>
        <label for="email">이메일</label>
        <input type="email" v-model="email" required @blur="getSecurityQuestion" />
      </div>
      <div>
        <label for="name">이름</label>
        <input type="text" v-model="name" required />
      </div>
      <div v-if="securityQuestion">
        <label>{{ securityQuestion }}</label>
        <input type="text" v-model="securityAnswer" required />
      </div>
      <button type="submit">아이디 찾기</button>
    </form>
  </div>
</template>

<script>
import { useRouter } from 'vue-router';  // vue-router import
import apiClient from '@/components/api/apiClient';

export default {
  data() {
    return {
      email: '',
      name: '',
      securityQuestion: '',  // 보안 질문
      securityAnswer: '',    // 보안 답변
    };
  },
  methods: {
    // 이메일 입력 시 보안 질문을 가져오는 함수
    async getSecurityQuestion() {
      if (!this.email) return;  // 이메일이 없다면 요청하지 않음
      try {
        const response = await apiClient.get('/api-user/get-security-question', {
          params: { email: this.email }
        });
        if (response.data.securityQuestion) {
          this.securityQuestion = response.data.securityQuestion;  // 보안 질문을 설정
        } else {
          this.securityQuestion = '';  // 보안 질문이 없으면 빈 문자열
        }
      } catch (error) {
        console.error("보안 질문을 가져오는 데 실패했습니다.", error);
      }
    },

    // 아이디 찾기 요청
    async findId() {
      if (!this.email || !this.name || !this.securityAnswer) {
        alert("모든 필드를 입력해주세요.");
        return;
      }

      try {
        // 아이디 찾기 요청: 이메일, 이름, 보안 답변을 서버로 전송
        const response = await apiClient.post('/api-user/find-id', {
          email: this.email,
          name: this.name,
          securityAnswer: this.securityAnswer,
        });

        // 서버에서 반환된 메시지 알림
        alert(response.data);  // 메시지 반환 받기

        // 아이디 찾기 성공 후 로그인 페이지로 리디렉션
        this.$router.push({ name: 'login' });  // 로그인 화면으로 이동
      } catch (error) {
        // 에러 발생 시 메시지 표시
        alert(error.response?.data || "아이디 찾기 실패");
      }
    }
  }
};
</script>

<style scoped>
.find-id {
  max-width: 400px;
  margin: 0 auto;
  margin-top: 5%;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-sizing: border-box;
}

h2 {
  text-align: center;
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
}

form {
  display: flex;
  flex-direction: column;
}

.input-group {
  margin-bottom: 1em;
}

.input-group label {
  display: block;
  font-size: 14px;
  color: #555;
  margin-bottom: 5px;
}

.input-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 16px;
  color: #333;
  box-sizing: border-box;
}

.input-group input:focus {
  border-color: #42b983;
  outline: none;
}

button {
  background-color: #42b983;
  color: white;
  border: none;
  padding: 10px;
  cursor: pointer;
  border-radius: 5px;
  margin-top: 10px;
}

button:hover {
  background-color: #358e5b;
}

.footer-links {
  display: flex;
  justify-content: center;
  font-size: 14px;
  margin-top: 15px;
}

.footer-links span {
  margin: 0 5px;
}

.link {
  color: #42b983;
  cursor: pointer;
}

.link:hover {
  text-decoration: underline;
}
</style>
