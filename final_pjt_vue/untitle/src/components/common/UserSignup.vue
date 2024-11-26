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
        <!-- 실시간 비밀번호 확인 메시지 -->
        <div v-if="password && confirmPassword" class="password-message">
          <span :class="isPasswordMatch ? 'match' : 'mismatch'">
            {{ isPasswordMatch ? '비밀번호가 일치합니다' : '비밀번호가 일치하지 않습니다' }}
          </span>
        </div>
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

      <!-- 전화번호 입력 -->
      <div class="input-group">
        <label for="phoneNum">전화번호</label>
        <input
          type="text"
          id="phoneNum"
          v-model="phoneNum"
          required
          placeholder="전화번호를 입력하세요"
          maxlength="13"
          @input="formatPhoneNumber"
        />
      </div>

      <!-- 성별 선택 -->
      <div class="input-group">
        <label for="gender">성별</label>
        <select id="gender" v-model="gender" required>
          <option value="M">남성</option>
          <option value="F">여성</option>
          <option value="O">기타</option>
        </select>
      </div>

      <!-- 보안 질문 선택 -->
      <div class="input-group">
        <label for="securityQuestion">보안 질문</label>
        <select id="securityQuestion" v-model="securityQuestion" required>
          <option value="" disabled>보안 질문을 선택하세요</option>
          <option value="어렸을 때 좋아했던 음식은 무엇인가요?">어렸을 때 좋아했던 음식은 무엇인가요?</option>
          <option value="첫 번째 애완동물의 이름은 무엇인가요?">첫 번째 애완동물의 이름은 무엇인가요?</option>
          <option value="가장 기억에 남는 여행지는 어디인가요?">가장 기억에 남는 여행지는 어디인가요?</option>
          <option value="당신의 첫 번째 학교 이름은 무엇인가요?">당신의 첫 번째 학교 이름은 무엇인가요?</option>
          <option value="부모님의 결혼 기념일은 언제인가요?">부모님의 결혼 기념일은 언제인가요?</option>
          <option value="당신의 첫 번째 친구의 이름은 무엇인가요?">당신의 첫 번째 친구의 이름은 무엇인가요?</option>
          <option value="가장 좋아하는 영화 제목은 무엇인가요?">가장 좋아하는 영화 제목은 무엇인가요?</option>
          <option value="첫 번째 차의 모델은 무엇인가요?">첫 번째 차의 모델은 무엇인가요?</option>
          <option value="어린 시절 살았던 도시 이름은 무엇인가요?">어린 시절 살았던 도시 이름은 무엇인가요?</option>
          <option value="가장 좋아하는 스포츠 팀은 무엇인가요?">가장 좋아하는 스포츠 팀은 무엇인가요?</option>
        </select>
      </div>

      <!-- 보안 질문 답변 입력 -->
      <div class="input-group">
        <label for="securityAnswer">보안 답변</label>
        <input
          type="text"
          id="securityAnswer"
          v-model="securityAnswer"
          required
          placeholder="보안 질문에 대한 답을 입력하세요"
        />
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
      phoneNum: '',
      securityQuestion: '',
      securityAnswer: '',
    };
  },
  computed: {
    // 비밀번호 확인이 일치하는지 여부를 판단하는 computed property
    isPasswordMatch() {
      return this.password === this.confirmPassword;
    }
  },
  methods: {
    async signup() {
      // 비밀번호가 일치하지 않으면 회원가입을 진행하지 않음
      if (this.password !== this.confirmPassword) {
        alert('비밀번호가 일치하지 않습니다.');
        return;
      }

      // 회원가입 폼 데이터 구성
      const formData = new FormData();
      formData.append('username', this.username);
      formData.append('password', this.password);
      formData.append('name', this.name);
      formData.append('birthDate', this.birthDate);
      formData.append('gender', this.gender);
      formData.append('email', this.email);
      formData.append('phoneNum', this.phoneNum);
      formData.append('securityQuestion', this.securityQuestion);
      formData.append('securityAnswer', this.securityAnswer);

      try {
        // 서버에 회원가입 요청
        const response = await apiClient.post('/api-user/signup', formData, {
          headers: {
            'Content-Type': 'application/json',
          },
        });
        alert('회원가입 성공');
        // 회원가입 후 선호 운동 선택 페이지로 리디렉션
        this.$router.replace({ name: 'login' });
      } catch (error) {
        console.error('회원가입 실패', error);
        alert('회원가입 실패');
      }
    },

    formatPhoneNumber() {
      let formattedPhoneNumber = this.phoneNum.replace(/\D/g, '');
      if (formattedPhoneNumber.length <= 3) {
        this.phoneNum = formattedPhoneNumber;
      } else if (formattedPhoneNumber.length <= 6) {
        this.phoneNum = formattedPhoneNumber.replace(/(\d{3})(\d{0,4})/, '$1-$2');
      } else {
        this.phoneNum = formattedPhoneNumber.replace(/(\d{3})(\d{4})(\d{0,4})/, '$1-$2-$3');
      }
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

/* 비밀번호 확인 메시지 스타일 */
.password-message {
  margin-top: 10px;
  font-size: 14px;
}

.match {
  color: green;
}

.mismatch {
  color: red;
}
</style>
