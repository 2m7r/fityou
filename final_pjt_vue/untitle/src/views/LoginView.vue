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

      <!-- 로그인 버튼 -->
      <button type="submit">로그인</button>

      <!-- 계정 찾기, 회원가입 버튼 -->
      <div class="footer-links">
        <div>
          <span class="link" @click="goToFindId">아이디</span> / 
          <span class="link" @click="goToFindPW">비밀번호 찾기</span>
        </div>
        <span class="link" @click="goToSignup">회원가입</span>
      </div>
    </form>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/userStore'; // Pinia 스토어 불러오기
import apiClient from '@/components/api/apiClient' // axios 클라이언트를 임포트

export default {
  name: 'UserLogin',
  setup() {
    const userStore = useUserStore(); // 스토어 사용
    const router = useRouter();
    const id = ref('');
    const password = ref('');

    // 로그인 처리 함수
    const handleLogin = async () => {
      try {
        // 로그인 요청 (아이디와 비밀번호를 서버에 전달)
        const response = await apiClient.post('/api-user/login', {
          username: id.value,
          password: password.value
        })

        const result = response.data; // 서버에서 반환된 데이터
        const token = result['access-token'];
        const loginUser = result['loginUser'];

        sessionStorage.setItem('access-token', token); // 세션 스토리지에 토큰 저장

        userStore.setUser(loginUser); // 사용자 정보 스토어에 저장


        // preferredExercises가 존재하는지 확인하고 처리
        if (loginUser && loginUser.preferredExercises) {
          router.push({ name: 'home' });
        } else {
          router.push({ name: 'preferredExercise' });
        }

        console.log('로그인 성공')
      } catch (error) {
        console.error('로그인 실패:', error)
        alert('로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.')
      }
    }

    // 아이디 찾기 페이지로 이동
    const goToFindId = () => {
      router.push({ name: 'find-id' })
    }

    // 비번 찾기 페이지로 이동
    const goToFindPW = () => {
      router.push({ name: 'find-pw' })
    }

    // 회원가입 페이지로 이동
    const goToSignup = () => {
      router.push({ name: 'signup' })
    }

    return { id, password, handleLogin, goToFindId, goToFindPW, goToSignup }
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

button:hover {
  background-color: #358e5b;
}

.footer-links {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  margin-top: 10px;
}

.link {
  color: #42b983;
  cursor: pointer;
}

.link:hover {
  text-decoration: underline;
}
</style>
