<template>
  <div class="user-page">
    <h2>내 정보 수정</h2>
    
    <!-- 버튼 클릭 시 모달 띄우기 -->
    <button @click="showModal = true">정보 수정</button>

    <!-- 모달 창 -->
    <div v-if="showModal" class="modal-overlay">
      <div class="modal">
        <h3>정보 수정</h3>
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
            <label for="profileImage">프로필 사진</label>
            <div class="profile-upload-container">
              <input
                type="file"
                id="profileImage"
                ref="profileImageInput"
                @change="handleProfileImage"
                style="display: none"
              />
              <div v-if="previewImage">
                <img :src="previewImage" alt="프로필 사진 미리보기" class="profile-img-preview" />
              </div>
              <div v-else>
                <img :src="userProfileImage.replace(/\\/g, '/') || defaultImage" alt="기본 프로필 이미지" class="profile-img-preview" />
              </div>
              <button type="button" class="upload-btn" @click="triggerFileInput">프로필 업로드</button>
            </div>
          </div>

          <!-- 수정 버튼 -->
          <button type="submit">정보 수정</button>
        </form>

        <!-- 모달 닫기 버튼 -->
        <button @click="showModal = false">닫기</button>
      </div>
    </div>
  </div>
</template>

<script>
import { useUserStore } from "@/stores/userStore"; // Pinia store 가져오기
import apiClient from "@/components/api/apiClient";
import { ref } from "vue";
import defaultprofileImage from '@/assets/profile.jpg'

export default {
  data() {
    return {
      name: "",
      email: "",
      phoneNum: "",
      gender: "M", // 기본값은 남성
      profileImage: null, // 프로필 사진 (파일)
      previewImage: null, // 프로필 이미지 미리보기 URL
      userProfileImage: null, // 사용자 프로필 이미지 URL (기본값은 빈 문자열)
      defaultImage: defaultprofileImage,
      role: "USER", // 기본값은 USER
      gymName: "",
      isPrivateAccount: false, // 기본값은 공개로 설정
      showModal: false, // 모달 표시 여부
    };
  },
  async created() {
    const userStore = useUserStore();
    const userData = userStore.user;
    
    if (userData.profileImage) {
      this.userProfileImage = 'http://localhost:8080/' + userData.profileImage.replace(/\\/g, '/');
    } else {
      this.userProfileImage = this.defaultImage;
    }
    
    this.name = userData.name;
    this.email = userData.email;
    this.phoneNum = userData.phoneNum;
    this.gender = userData.gender;
    this.role = userData.role;
    this.isPrivateAccount = userData.isPrivateAccount !== undefined ? userData.isPrivateAccount : false;
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
      formData.append("profileImage", this.profileImage);
      formData.append("gymName", this.gymName);
      try {
        const userId = userStore.user.userId;
        const response = await apiClient.put(`/api-user/update/${userId}`, formData, {
          headers: {
            "Content-Type": "multipart/form-data"
          }
        });

        const updatedUserData = response.data;
        userStore.setUser(updatedUserData);

        alert("정보가 성공적으로 수정되었습니다.");
        this.showModal = false; // 수정 후 모달 닫기
      } catch (error) {
        console.error("정보 수정 실패", error);
        alert("정보 수정에 실패했습니다.");
      }
    },

    handleProfileImage(event) {
      const file = event.target.files[0];
      if (file) {
        this.profileImage = file; 
        this.previewImage = URL.createObjectURL(file); 
      } else {
        this.previewImage = this.userProfileImage || this.defaultImage;
      }
    },

    triggerFileInput() {
      this.$refs.profileImageInput.click(); 
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
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal {
  background-color: white;
  padding: 20px;
  border-radius: 10px;
  width: 80%;
  max-width: 600px;
}

button {
  padding: 10px 20px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #388e7f;
}
</style>
