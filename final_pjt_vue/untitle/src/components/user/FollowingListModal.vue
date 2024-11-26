<template>
    <div class="modal-overlay" @click.self="close">
      <div class="modal-content">
        <span class="close-btn" @click="close">×</span>
        <h2>팔로잉 목록</h2>
        <ul>
          <li v-for="follow in following" :key="follow.userId">{{ follow.name }}</li>
        </ul>
      </div>
    </div>
  </template>
  
  <script setup>
  defineProps({
    following: Array
  });
  
  const emit = defineEmits(["close"]);
  
  const close = () => {
    emit("close");
  };
  </script>
  
  <style scoped>
  .modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .modal-content {
    background: white;
    padding: 20px;
    border-radius: 5px;
    width: 300px;
  }
  .close-btn {
    position: absolute;
    top: 10px;
    right: 10px;
    font-size: 20px;
    cursor: pointer;
  }
  </style>
  