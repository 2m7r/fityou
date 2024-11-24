import { defineStore } from 'pinia';
import api from '@/components/api/apiClient';

export const useUserStore = defineStore('user', {
  state: () => ({
    user: JSON.parse(sessionStorage.getItem('user')) || null, // sessionStorage에서 사용자 정보 로드
    token: sessionStorage.getItem('access-token') || null, // sessionStorage에서 토큰 로드
  }),
  getters: {
    userName: (state) => state.user?.name || 'Guest',
    isAuthenticated: (state) => !!state.token,
    userEmail: (state) => state.user?.email || '',
    userPhone: (state) => state.user?.phoneNum || '',
    userGender: (state) => state.user?.gender || 'M',
   // 프로필 이미지 URL 반환
   userProfileImage: (state) => state.user?.profileImage || '@/assets/profile.jpg', // 기본 이미지 추가
  },
  actions: {
    setUser(userData) {
      this.user = userData;
      sessionStorage.setItem('user', JSON.stringify(userData)); // sessionStorage에 사용자 정보 저장
    },
    setToken(token) {
      this.token = token;
      sessionStorage.setItem('access-token', token); // sessionStorage에 토큰 저장
    },
    clearUser() {
      this.user = null;
      this.token = null;
      sessionStorage.removeItem('access-token');
      sessionStorage.removeItem('user');
    },
  },
});