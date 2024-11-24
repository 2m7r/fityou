<template>
  <div class="user-page">
    <h2>내 정보 수정</h2>
    <form @submit.prevent="updateUserInfo">
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

      <!-- 이메일 입력 -->
      <div class="input-group">
        <label for="email">이메일</label>
        <input
          type="email"
          id="email"
          v-model="email"
          required
          placeholder="이메일을 입력하세요"
        />
      </div>

      <!-- 전화번호 입력 -->
      <div class="input-group">
        <label for="phoneNum">전화번호</label>
        <input
          type="text"
          id="phoneNum"
          v-model="phoneNum"
          required
          maxlength="13"
          @input="formatPhoneNumber"
          placeholder="전화번호를 입력하세요"
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

      <!-- 프로필 사진 업로드 -->
      <div class="input-group profile-upload">
        <label for="profile">프로필 사진</label>
        <div class="profile-upload-container">
          <!-- 파일 선택 input 숨기기 -->
          <input
            type="file"
            id="profile"
            ref="profileInput"
            @change="handleProfileImage"
            style="display: none"
          />
          <!-- 프로필 사진 미리보기 -->
          <div v-if="previewImage">
            <img :src="previewImage" alt="프로필 사진 미리보기" class="profile-img-preview" />
          </div>
          <div v-else>
            <!-- 이미 프로필 사진이 있을 경우 해당 이미지 표시 -->
            <img :src="userProfileImage || defaultImage" alt="기본 프로필 이미지" class="profile-img-preview" />
          </div>
          <!-- 사진 업로드 버튼 -->
          <button type="button" class="upload-btn" @click="triggerFileInput">프로필 업로드</button>
        </div>
      </div>

      <!-- 트레이너일 경우 체육관 이름 입력 -->
      <div v-if="role === 'TRAINER'" class="input-group">
        <label for="gymName">체육관 이름</label>
        <input
          type="text"
          id="gymName"
          v-model="gymName"
          placeholder="체육관 이름을 입력하세요"
        />
      </div>

      <!-- 공개/비공개 설정 -->
      <div class="input-group privacy-group">
        <label for="isPrivateAccount" class="privacy-label">
          비공개 계정 전환
          <div class="switch-container">
            <input
              type="checkbox"
              id="isPrivateAccount"
              v-model="isPrivateAccount"
            />
            <span class="slider"></span>
          </div>
        </label>
      </div>

      <!-- 수정 버튼 -->
      <button type="submit">정보 수정</button>
    </form>
  </div>
</template>

<script>
import { useUserStore } from "@/stores/userStore"; // Pinia store 가져오기
import apiClient from "@/components/api/apiClient";

export default {
  data() {
    return {
      name: "",
      email: "",
      phoneNum: "",
      gender: "M", // 기본값은 남성
      profile: null,
      previewImage: null, // 프로필 이미지 미리보기 URL
      userProfileImage: '', // 사용자 프로필 이미지 URL (기본값은 빈 문자열)
      defaultImage: '@/assets/profile.jpg', // 기본 프로필 이미지 경로
      role: "USER", // 기본값은 USER
      gymName: "",
      isPrivateAccount: false, // 기본값은 공개로 설정
    };
  },
  async created() {
    const userStore = useUserStore();

    const userData = userStore.user;

    this.name = userData.name;
    this.email = userData.email;
    this.phoneNum = userData.phoneNum;
    this.gender = userData.gender;
    this.role = userData.role;
    this.isPrivateAccount = userData.isPrivateAccount !== undefined ? userData.isPrivateAccount : false;
    this.userProfileImage = userData.profileImage || ''; // 사용자 프로필 이미지 URL
    if (this.role === "TRAINER") {
      this.gymName = userData.gymName;
    }
  },
  methods: {
    async updateUserInfo() {
      const userStore = useUserStore();

      const formData = new FormData();
      formData.append("name", this.name);
      formData.append("email", this.email);
      formData.append("phoneNum", this.phoneNum);
      formData.append("gender", this.gender);
      formData.append("isPrivateAccount", this.isPrivateAccount);
      formData.append("profileImage", this.profile);
      formData.append("gymName", this.gymName);
      try {
        const userId = userStore.user.userId;
        const response = await apiClient.put(`/api-user/update/${userId}`, formData, {
          headers: {
            "Content-Type": "multipart/form-data"
          }
        });

        const updatedUserData = response.data;
        console.log('------업데이트 데이터-----')
        console.log(response.data)
        console.log('------------------------')

        userStore.setUser(updatedUserData);

        alert("정보가 성공적으로 수정되었습니다.");
      } catch (error) {
        console.error("정보 수정 실패", error);
        alert("정보 수정에 실패했습니다.");
      }
    },

    handleProfileImage(event) {
      const file = event.target.files[0];
      if (file) {
        this.profile = file;
        this.previewImage = URL.createObjectURL(file); // 파일을 미리보기 URL로 설정
      } else {
        this.profile = null;
        this.previewImage = null; // 파일이 선택되지 않으면 미리보기도 초기화
      }
    },

    triggerFileInput() {
      this.$refs.profileInput.click(); // 업로드 버튼 클릭 시 input 파일 열리도록
    },

    formatPhoneNumber() {
      let formattedPhoneNumber = this.phoneNum.replace(/\D/g, "");
      if (formattedPhoneNumber.length <= 3) {
        this.phoneNum = formattedPhoneNumber;
      } else if (formattedPhoneNumber.length <= 6) {
        this.phoneNum = formattedPhoneNumber.replace(/(\d{3})(\d{0,4})/, "$1-$2");
      } else {
        this.phoneNum = formattedPhoneNumber.replace(
          /(\d{3})(\d{4})(\d{0,4})/,
          "$1-$2-$3"
        );
      }
    },
  },
};
</script>

<style scoped>
.profile-upload-container {
  display: flex;
  align-items: center;
  gap: 10px; /* 이미지와 버튼 사이의 간격 */
}

.profile-img-preview {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
}

.upload-btn {
  padding: 10px 20px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
}

.upload-btn:hover {
  background-color: #388e7f;
}

.user-page {
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

.input-group input,
.input-group select {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 14px;
}

.privacy-label {
  display: flex;
  justify-content: space-between; /* 라벨과 스위치를 양 끝으로 배치 */
  align-items: center;
  font-size: 14px;
  color: #666;
  cursor: pointer; /* 전체 라벨에 클릭 효과 추가 */
}

/* 스위치 스타일 */
.switch-container {
  position: relative;
  display: inline-block;
  width: 50px;
  height: 25px;
}

.switch-container input {
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  transition: 0.4s;
  border-radius: 25px;
}

.slider:before {
  position: absolute;
  content: "";
  height: 17px;
  width: 17px;
  border-radius: 50%;
  left: 4px;
  bottom: 4px;
  background-color: white;
  transition: 0.4s;
}

input:checked + .slider {
  background-color: #42b983;
}

input:checked + .slider:before {
  transform: translateX(25px);
}

button[type="submit"] {
  width: 100%;
  padding: 14px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  margin-top: 20px;
}

button[type="submit"]:hover {
  background-color: #388e7f;
}
</style>
