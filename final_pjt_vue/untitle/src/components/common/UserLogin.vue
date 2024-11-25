<template>
    <div class="login-form">
      <h2>로그인</h2>
      <form @submit.prevent="login">
        <div>
          <label for="username">아이디</label>
          <input type="text" id="username" v-model="username" required />
        </div>
        <div>
          <label for="password">비밀번호</label>
          <input type="password" id="password" v-model="password" required />
        </div>
        <button type="submit">로그인</button>
      </form>
      <p>아직 계정이 없으신가요? <span @click="goToSignup" class="link">회원가입</span></p>
    </div>
  </template>
  
  <script>
  import apiClient from '../api/apiClient';
  
  export default {
    data() {
      return {
        username: '',
        password: '',
      };
    },
    methods: {
      async login() {
        try {
          const response = await apiClient.post('/api-user/login', {
            username: this.username,
            password: this.password,
          });
          const token = response.data.token;
          sessionStorage.setItem('access-token', token); // 로그인 성공 시 토큰 저장
          
          this.$router.push({ name: 'home' }); // 로그인 후 대시보드로 이동
        } catch (error) {
          console.error('로그인 실패', error);
          alert('로그인 실패');
        }
      },
      goToSignup() {
        this.$router.push({ name: 'signup' }); // 회원가입 페이지로 이동
      },
    },
  };
  </script>
  
  <style scoped>
  .login-form {
    max-width: 400px;
    margin: 0 auto;
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 8px;
    background-color: #f9f9f9;
  }
  form {
    display: flex;
    flex-direction: column;
  }
  input {
    margin-bottom: 10px;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 4px;
  }
  button {
    padding: 10px;
    background-color: #54a673;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  button:hover {
    background-color: #4a8e5c;
  }
  .link {
    color: #54a673;
    cursor: pointer;
  }
  .link:hover {
    text-decoration: underline;
  }
  </style>
  