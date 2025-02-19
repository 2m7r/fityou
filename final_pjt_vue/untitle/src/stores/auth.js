// src/store/auth.js
import { createStore } from 'vue';

export const store = createStore({
  state: {
    isAuthenticated: false, // 인증 상태
    user: null,             // 로그인한 사용자 정보
  },
  mutations: {
    setAuth(state, token) {
      state.isAuthenticated = true;
      sessionStorage.setItem('access-token', token);
    },
    logout(state) {
      state.isAuthenticated = false;
      state.user = null;
      sessionStorage.removeItem('access-token'); // JWT 삭제
    },
  },
  actions: {
    login({ commit }, token) {
      commit('setAuth', token);
    },
    logout({ commit }) {
      commit('logout');
    },
  },
  getters: {
    isAuthenticated(state) {
      return state.isAuthenticated;
    },
    user(state) {
      return state.user;
    },
  },
});
