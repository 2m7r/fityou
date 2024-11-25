<template>
  <div class="user-page">
    <div class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <h2 class="modal-title">내 정보 수정</h2>
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

          <!-- 유저 설명 -->
          <div class="input-group">
            <label for="userDescription">소개글</label>
            <input 
              type="text"
              id="userDescription"
              v-model="userDescription"
              placeholder="소개글을 작성해보세요!"
            />
          </div>

          <!-- 아이디 못바꿈 -->
          <div class="input-group">
            <label for="username">아이디</label>
            <input v-model="username"  readonly/>
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
              <!-- 파일 선택 input 숨기기 -->
              <input
                type="file"
                id="profileImage"
                ref="profileImageInput"
                @change="handleProfileImage"
                style="display: none"
              />
              <!-- 프로필 사진 미리보기 -->
              <div v-if="previewImage">
                <img
                  :src="previewImage"
                  alt="프로필 사진 미리보기"
                  class="profile-img-preview"
                />
              </div>
              <div v-else>
                <!-- 이미 프로필 사진이 있을 경우 해당 이미지 표시 -->
                <img
                  :src="userProfileImage.replace(/\\/g, '/') || defaultImage"
                  alt="기본 프로필 이미지"
                  class="profile-img-preview"
                />
              </div>
              <!-- 사진 업로드 버튼 -->
              <button
                type="button"
                class="upload-btn rounded-full"
                @click="triggerFileInput"
              >
                프로필 업로드
              </button>
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

          <!-- 제출 버튼 -->
          <div class="modal-actions">
            <button class="btn btn-secondary rounded-full" @click="closeModal">취소</button>
            <button class="btn btn-danger rounded-full" @click="joinOut">탈퇴</button>
            <button type="submit" class="btn btn-primary rounded-full" @click="submitEdit">
              수정
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { useUserStore } from "@/stores/userStore"; // Pinia store 가져오기
import apiClient from "@/components/api/apiClient";
import { ref } from "vue";
import defaultprofileImage from "@/assets/profile.jpg";

export default {
  data() {
    return {
      name: "",
      username: "",
      email: "",
      phoneNum: "",
      gender: "M", // 기본값은 남성
      profileImage: null, // 프로필 사진 (파일)
      previewImage: null, // 프로필 이미지 미리보기 URL
      userProfileImage: null, // 사용자 프로필 이미지 URL (기본값은 빈 문자열)
      defaultImage: defaultprofileImage,
      userDescription: "",
      role: "USER", // 기본값은 USER
      gymName: "",
      isPrivateAccount: false, // 기본값은 공개로 설정
    };
  },
  async created() {
    const userStore = useUserStore();
    const userData = userStore.user;

    // profileImage가 null인지 확인 후 처리
    if (userData.profileImage) {
      this.userProfileImage =
        "http://localhost:8080/" + userData.profileImage.replace(/\\/g, "/");
    } else {
      this.userProfileImage = this.defaultImage;
    }

    this.name = userData.name;
    this.username = userData.username;
    this.email = userData.email;
    this.phoneNum = userData.phoneNum;
    this.gender = userData.gender;
    this.role = userData.role;
    this.isPrivateAccount = userData.isPrivateAccount !== undefined ? userData.isPrivateAccount : false;
    if (this.role === "TRAINER") {
      this.gymName = userData.gymName;
    }
    this.userDescription = userData.userDescription;
  },
  methods: {
    async joinOut() {
      const userStore = useUserStore();
      const userId = userStore.user.userId;

      // 탈퇴 확인 팝업
      const isConfirmed = window.confirm(
        "정말로 탈퇴하시겠습니까? 다시 되돌릴 수 없습니다."
      );

      if (!isConfirmed) {
        return; // 취소 시 아무 작업도 하지 않음
      }

      try {
        // API 요청으로 탈퇴 처리
        await apiClient.delete(`/api-user/delete/${userId}`);
      
        // 사용자 데이터를 초기화하고 홈으로 이동
        userStore.clearUser();
        alert("탈퇴가 성공적으로 완료되었습니다.");
        clearUser();
        this.$router.push("/"); // 홈 화면으로 이동
      } catch (error) {
        console.error("탈퇴 실패", error);
        alert("탈퇴 중 문제가 발생했습니다. 다시 시도해주세요.");
      }
    },

    closeModal() {
      this.$emit("close"); // 부모 컴포넌트에 'close' 이벤트 전달
    },

    async updateUserInfo() {
      const userStore = useUserStore();

      const formData = new FormData();
      formData.append("username", this.username);
      formData.append("name", this.name);
      formData.append("email", this.email);
      formData.append("phoneNum", this.phoneNum);
      formData.append("gender", this.gender);
      formData.append("isPrivateAccount", this.isPrivateAccount);

      // 사용자가 이미지를 업로드하지 않았을 경우 기존 이미지 유지
      if (this.profileImage) {
        formData.append("profileImage", this.profileImage);
      } else if (this.userProfileImage) {
        formData.append("profileImage", this.userProfileImage.split("/").pop());
      }

      formData.append("gymName", this.gymName);
      formData.append("userDescription", this.userDescription);
      try {
        const userId = userStore.user.userId;
        const response = await apiClient.put(
          `/api-user/update/${userId}`,
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }
        );

        const updatedUserData = response.data;
        console.log("------업데이트 데이터-----");
        console.log(response.data);

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
        this.profileImage = file; // profileImage로 수정
        this.previewImage = URL.createObjectURL(file); // 파일을 미리보기 URL로 설정
      } else {
        this.previewImage = this.userProfileImage || this.defaultImage;
      }
    },

    triggerFileInput() {
      this.$refs.profileImageInput.click(); // 업로드 버튼 클릭 시 input 파일 열리도록
    },

    formatPhoneNumber() {
      let formattedPhoneNumber = this.phoneNum.replace(/\D/g, "");
      if (formattedPhoneNumber.length <= 3) {
        this.phoneNum = formattedPhoneNumber;
      } else if (formattedPhoneNumber.length <= 6) {
        this.phoneNum = formattedPhoneNumber.replace(
          /(\d{3})(\d{0,4})/,
          "$1-$2"
        );
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
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1050;
}

.modal-content {
  background: white;
  padding: 40px;
  border-radius: 8px;
  width: 90%;
  max-width: 850px;
  height: auto;
  max-height: 6000px;
  overflow: hidden;
  max-width: 600px;

}

.modal-title {
  text-align: center;
  font-weight: bold;
  margin-bottom: 5px;
}

.modal-actions {
  display: flex;
  justify-content: space-between;
  gap: 20px;
  align-items: center;
  margin-top: 20px;
}

.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.btn-secondary {
  background-color: #ccc;
}

.btn-primary {
  background-color: #54a673;
  color: white;
}

.btn-primary:hover {
  background-color: #388e7f;
}




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
  background-color: #54a673;
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
  margin-top: 150px;
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
}

.switch-container {
  display: inline-block;
  width: 34px;
  height: 20px;
  position: relative;
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
  border-radius: 20px;
}

.slider:before {
  position: absolute;
  content: "";
  height: 14px;
  width: 14px;
  left: 3px;
  bottom: 3px;
  background-color: white;
  transition: 0.4s;
}

input:checked + .slider {
  background-color: #42b983;
}

input:checked + .slider:before {
  transform: translateX(14px);
}

button {
  width: 100%;
  padding: 15px;
  background-color: #54a673;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  margin-top: 20px;
  max-width: 100px;
}

button:hover {
  background-color: #388e7f;
}

.rounded-full {
  border-radius: 50px !important;
}

.btn-danger {
  background-color: #e74c3c; /* 빨간색 */
  color: white;
}

.btn-danger:hover {
  background-color: #c0392b; /* 어두운 빨간색 */
}

</style>
