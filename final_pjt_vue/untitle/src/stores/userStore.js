// src/stores/userStore.js
import { defineStore } from 'pinia';
import api from '@/components/api/apiClient';

export const useUserStore = defineStore('user', {
  state: () => ({
    user: JSON.parse(sessionStorage.getItem('user')) || null, // sessionStorage에서 사용자 정보 로드
    token: sessionStorage.getItem('access-token') || null, // sessionStorage에서 토큰 로드
  }),
  getters: {
    // 사용자 이름을 반환
    userName: (state) => state.user?.name || 'Guest',
    // 인증 여부 (토큰 존재 여부로 판단)
    isAuthenticated: (state) => !!state.token,
    // 이메일, 전화번호 등 필요한 다른 정보들을 추가할 수 있음
    userEmail: (state) => state.user?.email || '',
    userPhone: (state) => state.user?.phoneNum || '',
    userGender: (state) => state.user?.gender || 'M',
  },
  actions: {
    // 사용자 정보 설정
    setUser(userData) {
      this.user = userData; // 사용자 정보를 상태에 저장
      sessionStorage.setItem('user', JSON.stringify(userData)); // sessionStorage에 사용자 정보 저장
    },

    // 토큰 설정 (로그인 후 토큰을 설정)
    setToken(token) {
      this.token = token; // JWT 토큰을 상태에 저장
      sessionStorage.setItem('access-token', token); // sessionStorage에 토큰 저장
    },

    // 사용자 정보 초기화 (로그아웃 시)
    clearUser() {
      this.user = null; // 사용자 정보 초기화
      this.token = null; // JWT 토큰 초기화
      sessionStorage.removeItem('access-token'); // 세션 스토리지에서 토큰 삭제
      sessionStorage.removeItem('user'); // 세션 스토리지에서 사용자 정보 삭제
    },

    // 사용자 정보 업데이트
    async updateUserInfo(updatedData) {
      try {
        const formData = new FormData();
        formData.append('name', updatedData.name);
        formData.append('email', updatedData.email);
        formData.append('phoneNum', updatedData.phoneNum);
        formData.append('gender', updatedData.gender);
        formData.append('isPrivateAccount', updatedData.isPrivateAccount);

        if (updatedData.profileImage) {
          formData.append('profile', updatedData.profile);
        }

        if (updatedData.role === 'TRAINER') {
          formData.append('gymName', updatedData.gymName);
        }

        // 서버에 사용자 정보 업데이트 요청
        const response = await api.put('/api-user/update/${updatedData.id}', formData);

        // 요청이 성공하면, 사용자 정보를 업데이트
        this.setUser(response.data);
        alert('정보가 성공적으로 수정되었습니다.');
      } catch (error) {
        console.error('정보 수정 실패', error);
        alert('정보 수정에 실패했습니다.');
      }
    },
  },
});
