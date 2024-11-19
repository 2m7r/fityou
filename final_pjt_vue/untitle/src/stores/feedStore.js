import { defineStore } from 'pinia';
import axios from 'axios';

export const useFeedStore = defineStore('feedStore', {
  state: () => ({
    feeds: [], // 피드 목록
    calendarEvents: [], // 캘린더 이벤트 목록
  }),
  actions: {
    // 피드 데이터 가져오기
    async fetchFeeds() {
      try {
        const response = await axios.get('/api/feeds');
        this.feeds = response.data;
      } catch (error) {
        console.error('피드 데이터를 가져오는 데 실패했습니다.', error);
      }
    },
    
    // 캘린더 이벤트 데이터 가져오기
    async fetchCalendarEvents() {
      try {
        const response = await axios.get('/api/calendar-events');
        this.calendarEvents = response.data;
      } catch (error) {
        console.error('캘린더 이벤트 데이터를 가져오는 데 실패했습니다.', error);
      }
    },
  },
});
