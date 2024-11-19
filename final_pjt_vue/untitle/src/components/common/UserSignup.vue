<!-- <template>
    <div class="signup-form">
      <h2>회원가입</h2>
      <form @submit.prevent="signup">
        <div>
          <label for="username">아이디</label>
          <input type="text" id="username" v-model="username" required />
        </div>
        <div>
          <label for="password">비밀번호</label>
          <input type="password" id="password" v-model="password" required />
        </div>
        <div>
          <label for="confirmPassword">비밀번호 확인</label>
          <input type="password" id="confirmPassword" v-model="confirmPassword" required />
        </div>
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
      };
    },
    methods: {
      async signup() {
        if (this.password !== this.confirmPassword) {
          alert('비밀번호가 일치하지 않습니다.');
          return;
        }
  
        try {
          const response = await apiClient.post('/user/signup', {
            username: this.username,
            password: this.password,
          });
          alert('회원가입 성공');
          this.$router.push({ name: 'login' }); // 회원가입 성공 후 로그인 페이지로 이동
        } catch (error) {
          console.error('회원가입 실패', error);
          alert('회원가입 실패');
        }
      },
      goToLogin() {
        this.$router.push({ name: 'login' }); // 로그인 페이지로 이동
      },
    },
  };
  </script>
  
  <style scoped>
  .signup-form {
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
   -->