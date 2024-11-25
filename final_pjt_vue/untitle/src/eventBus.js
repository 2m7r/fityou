// src/eventBus.js
import { reactive } from 'vue';

const eventBus = reactive({
  refreshChallenges: false,
  triggerRefresh() {
    this.refreshChallenges = !this.refreshChallenges;
  }
});

export default eventBus;