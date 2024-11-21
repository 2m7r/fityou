<template>
    <div class="login-page">
      <h1>로그인</h1>
      <form @submit.prevent="handleLogin">
        <div class="input-group">
          <label for="id">아이디</label>
          <input type="text" v-model="id" id="id" required />
        </div>
        <div class="input-group">
          <label for="password">비밀번호</label>
          <input
            type="password"
            v-model="password"
            id="password"
            required
            @keyup.enter="handleLogin"
          />
        </div>
        <button type="submit">로그인</button>
      </form>
    </div>
  </template>
  
  <script>
  import { ref } from 'vue'
  import { useRouter } from 'vue-router'
  import apiClient from '@/components/api/apiClient' // axios 클라이언트를 임포트
  
  export default {
    name: 'UserLogin',
    setup() {
      const id = ref('')
      const password = ref('')
      const router = useRouter()
  
      // 로그인 처리 함수
      const handleLogin = async () => {
        try {
          // 로그인 요청 (아이디와 비밀번호를 서버에 전달)
          const response = await apiClient.post('/user/login', {
            username: id.value,
            password: password.value
          })
  
          // 서버에서 반환한 JWT 토큰을 localStorage에 저장
          const token = response.data.token // 서버에서 JWT 반환
          localStorage.setItem('authToken', token) // 토큰을 로컬 스토리지에 저장
  
          // 로그인 성공 후, 대시보드 페이지로 리디렉션
          router.push({ name: 'dashboard' })
  
          console.log('로그인 성공')
        } catch (error) {
          console.error('로그인 실패:', error)
          alert('로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.')
        }
      }
  
      return { id, password, handleLogin }
    }
  }
  </script>
  
  <style scoped>
  .login-page {
    max-width: 400px;
    margin: 0 auto;
    margin-top: 5%;
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 8px;
    box-sizing: border-box;
  }
  
  h1 {
    text-align: center;
  }
  
  form {
    display: flex;
    flex-direction: column;
  }
  
  form div {
    margin-bottom: 1em;
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
  }
  </style>
  