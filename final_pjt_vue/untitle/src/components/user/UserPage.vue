<template>
    <div class="user-page">
      <h2>내 정보 수정</h2>
      <form @submit.prevent="updateUserInfo">
        <!-- 이름 입력 -->
        <div class="input-group">
          <label for="name">이름</label>
          <input type="text" id="name" v-model="name" required placeholder="이름을 입력하세요" />
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
          </select>
        </div>
  
        <!-- 프로필 사진 입력 -->
        <div class="input-group">
          <label for="profileImage">프로필 사진</label>
          <input type="file" id="profileImage" @change="handleProfileImage" />
        </div>
  
        <!-- 트레이너일 경우 체육관 이름 입력 -->
        <div v-if="role === 'TRAINER'" class="input-group">
          <label for="gymName">체육관 이름</label>
          <input type="text" id="gymName" v-model="gymName" placeholder="체육관 이름을 입력하세요" />
        </div>
  
        <!-- 공개/비공개 설정 -->
        <div class="input-group">
          <label for="privacy">정보 공개 여부</label>
          <select id="privacy" v-model="privacy" required>
            <option value="PUBLIC">공개</option>
            <option value="PRIVATE">비공개</option>
          </select>
        </div>
  
        <!-- 수정 버튼 -->
        <button type="submit">정보 수정</button>
      </form>
    </div>
  </template>
  
  <script>
  import { useUserStore } from '@/stores/userStore'; // Pinia store 가져오기
  import apiClient from '@/components/api/apiClient';
  
  export default {
    data() {
      return {
        name: '',
        email: '',
        phoneNum: '',
        gender: 'M',  // 기본값은 남성
        profileImage: null,
        role: 'USER',  // 기본값은 USER
        gymName: '',
        privacy: 'PUBLIC',  // 기본값은 공개
      };
    },
    async created() {
      const userStore = useUserStore();
  
      // 사용자 정보를 Pinia store에서 가져오기
      if (userStore.isAuthenticated) {
        const userData = userStore.user;
        
        this.name = userData.name;
        this.email = userData.email;
        this.phoneNum = userData.phoneNum;
        this.gender = userData.gender;
        this.role = userData.role;
        this.privacy = userData.privacy;
        if (this.role === 'TRAINER') {
          this.gymName = userData.gymName;
        }
      } else {
        alert('로그인이 필요합니다.');
        this.$router.push('/login'); // 로그인 페이지로 리디렉션
      }
    },
    methods: {
      async updateUserInfo() {
        const userStore = useUserStore();
        const updatedData = {
          name: this.name,
          email: this.email,
          phoneNum: this.phoneNum,
          gender: this.gender,
          privacy: this.privacy,
          profileImage: this.profileImage,
          gymName: this.gymName,
        };
  
        try {
          await userStore.updateUserInfo(updatedData); // Pinia store에서 사용자 정보 업데이트
        } catch (error) {
          console.error('정보 수정 실패', error);
          alert('정보 수정에 실패했습니다.');
        }
      },
  
      handleProfileImage(event) {
        const file = event.target.files[0];
        if (file) {
          this.profileImage = file;
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
    },
  };
  </script>
  
  <style scoped>
  /* 기존 스타일 유지 */
  </style>
  