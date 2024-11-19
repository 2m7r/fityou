// // src/stores/userStore.js
// import { defineStore } from 'pinia';
// import api from '@/utils/api';

// export const useUserStore = defineStore('user', {
//   state: () => ({
//     username: '',
//     profileImage: '',
//     isAuthenticated: false,
//   }),
//   getters: {
//     getUsername: (state) => state.username,
//     getProfileImage: (state) => state.profileImage,
//   },
//   actions: {
//     async fetchUserData() {
//       try {
//         const response = await api.get('user/profile');  // 사용자 프로필 정보 API 호출
//         this.username = response.data.username;
//         this.profileImage = response.data.profileImage;
//         this.isAuthenticated = true;
//       } catch (error) {
//         console.error('사용자 데이터를 가져오는 데 실패했습니다.', error);
//         this.isAuthenticated = false;
//       }
//     },
//   },
// });


// // stores/user.js (Pinia store 예시)
// import { defineStore } from 'pinia'

// export const useUserStore = defineStore('user', {
//   state: () => ({
//     user: null, // 유저 정보
//     token: null, // JWT 토큰
//   }),
//   getters: {
//     isAuthenticated: (state) => !!state.token, // 토큰이 존재하면 인증됨
//   },
//   actions: {
//     login(userData, token) {
//       this.user = userData
//       this.token = token
//       localStorage.setItem('token', token) // JWT 토큰 저장
//     },
//     logout() {
//       this.user = null
//       this.token = null
//       localStorage.removeItem('token') // 토큰 제거
//     },
//   },
// })
