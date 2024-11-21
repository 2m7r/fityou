<template>
  <div class="signup-form">
    <h2>회원가입</h2>
    <form @submit.prevent="signup">
      <!-- 아이디 입력 -->
      <div class="input-group">
        <label for="username">아이디</label>
        <input type="text" id="username" v-model="username" required placeholder="아이디를 입력하세요" />
      </div>

      <!-- 비밀번호 입력 -->
      <div class="input-group">
        <label for="password">비밀번호</label>
        <input type="password" id="password" v-model="password" required placeholder="비밀번호를 입력하세요" />
      </div>

      <!-- 비밀번호 확인 -->
      <div class="input-group">
        <label for="confirmPassword">비밀번호 확인</label>
        <input type="password" id="confirmPassword" v-model="confirmPassword" required placeholder="비밀번호를 다시 입력하세요" />
      </div>

      <!-- 이름 입력 -->
      <div class="input-group">
        <label for="name">이름</label>
        <input type="text" id="name" v-model="name" required placeholder="이름을 입력하세요" />
      </div>

      <!-- 생년월일 입력 -->
      <div class="input-group">
        <label for="birthDate">생년월일</label>
        <input type="date" id="birthDate" v-model="birthDate" required />
      </div>


      <!-- 이메일 입력 -->
      <div class="input-group">
        <label for="email">이메일</label>
        <input type="email" id="email" v-model="email" required placeholder="이메일을 입력하세요" />
      </div>

      <!-- 역할 선택 -->
      <div class="input-group">
        <label for="role">회원 유형</label>
        <select id="role" v-model="role" required>
          <option value="USER">일반유저</option>
          <option value="TRAINER">트레이너</option>
        </select>
      </div>

      <!-- 트레이너일 경우 체육관 이름 입력 -->
      <div v-if="role === 'TRAINER'" class="input-group">
        <label for="gymName">체육관 이름</label>
        <input type="text" id="gymName" v-model="gymName" placeholder="체육관 이름을 입력하세요" />
      </div>

      <!-- 회원가입 버튼 -->
      <button type="submit">회원가입</button>
    </form>
    <p>이미 계정이 있으신가요? <span @click="goToLogin" class="link">로그인</span></p>
  </div>
</template>

<script>
import apiClient from '../api/apiClient';

export default {
  data() {
    return {
      username: '',
      password: '',
      confirmPassword: '',
      name: '',
      birthDate: '',
      gender: 'M', 
      email: '',
      role: 'USER', 
      gymName: '', 
    };
  },
  methods: {
    async signup() {
      if (this.password !== this.confirmPassword) {
        alert('비밀번호가 일치하지 않습니다.');
        return;
      }

      const formData = new FormData();
      formData.append('username', this.username);
      formData.append('password', this.password);
      formData.append('name', this.name);
      formData.append('birthDate', this.birthDate);
      formData.append('gender', this.gender);
      formData.append('email', this.email);
      formData.append('role', this.role);
      if (this.role === 'TRAINER') {
        formData.append('gymName', this.gymName);
      }

      try {
        const response = await apiClient.post('/api-user/signup', formData, {
          headers: {
            'Content-Type': 'application/json',
          },
        });
        alert('회원가입 성공');
        this.$router.push({ name: 'login' });
      } catch (error) {
        console.error('회원가입 실패', error);
        alert('회원가입 실패');
      }
    },

    handleFileChange(event) {
      this.profileImage = event.target.files[0];
    },

    goToLogin() {
      this.$router.push({ name: 'login' });
    },
  },
};
</script>

<style scoped>
.signup-form {
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

.input-group input, .input-group select {
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
