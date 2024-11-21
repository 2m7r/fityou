// src/api/apiClient.js
import axios from 'axios';

// API 클라이언트 생성
const apiClient = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  },
});

// JWT를 요청 헤더에 자동으로 추가
apiClient.interceptors.request.use((config) => {
  const token = sessionStorage.getItem('access-token');
  if (token) {
    config.headers['Authorization'] = `Bearer ${token}`;
  }
  return config;
}, (error) => {
  return Promise.reject(error);
});

export default apiClient;
