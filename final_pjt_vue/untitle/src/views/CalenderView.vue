<template>
    <div class="calendar-view">
      <div class="calendar-header">
        <h4>2024년 10월 {{ date }}일</h4>
      </div>
      <div class="logs">
        <h5>식단일기</h5>
        <p v-if="dietLog">식단일기 내용: {{ dietLog }}</p>
        <h5>운동일기</h5>
        <p v-if="exerciseLog">운동일기 내용: {{ exerciseLog }}</p>
        <h5>챌린지</h5>
        <p v-if="challenge">챌린지 내용: {{ challenge }}</p>
      </div>
    </div>
  </template>
  
  <script setup>
  import { useRoute } from 'vue-router'
  
  const route = useRoute()
  const date = route.params.date // 선택한 날짜
  
  const dietLog = '아침: 오트밀, 점심: 샐러드'  // 예시 데이터
  const exerciseLog = '팔굽혀펴기 20회, 스쿼트 30회'  // 예시 데이터
  const challenge = '수분 챌린지 참여 완료'  // 예시 데이터
  </script>
  
  <style scoped>
  .calendar-view {
    padding: 20px;
  }
  
  .calendar-header {
    font-size: 20px;
  }
  
  .logs {
    margin-top: 20px;
  }
  
  h5 {
    font-size: 16px;
  }
  </style>
  