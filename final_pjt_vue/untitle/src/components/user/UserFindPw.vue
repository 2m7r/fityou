<template>
    <div class="find-password-form">
      <h2>비밀번호 찾기</h2>
      <form @submit.prevent="findPassword">
        <!-- 이메일 입력 -->
        <div class="input-group">
          <label for="email">이메일</label>
          <input type="email" id="email" v-model="email" required placeholder="이메일을 입력하세요" />
        </div>
  
        <!-- 비밀번호 찾기 버튼 -->
        <button type="submit">비밀번호 찾기</button>
      </form>
      <p>비밀번호를 기억하셨나요? <span @click="goToLogin" class="link">로그인</span></p>
    </div>
  </template>
  
  <script>
  import apiClient from '../api/apiClient';
  
  export default {
    data() {
      return {
        email: '', // 이메일 입력 값
      };
    },
    methods: {
      async findPassword() {
        try {
          const response = await apiClient.post('/api-user/find-password', { email: this.email });
          alert('비밀번호 재설정 링크가 이메일로 전송되었습니다.');
          this.$router.push({ name: 'login' });
        } catch (error) {
          console.error('비밀번호 찾기 실패', error);
          alert('등록된 이메일이 없습니다.');
        }
      },
  
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
  