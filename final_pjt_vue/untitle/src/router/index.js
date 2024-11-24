import { createRouter, createWebHistory } from 'vue-router'

import UserPage from '../components/user/UserPage.vue'
import UserFindId from '../components/user/UserFindId.vue'
import UserFindPw from '@/components/user/UserFindPw.vue'
import UserSignup from '../components/common/UserSignup.vue'

import HomeView from '../views/HomeView.vue'
import FeedView from '../views/FeedView.vue'
import LoginView from '../views/LoginView.vue'
import ChallengeView from '../views/ChallengeView.vue'
import ManagementView from '../views/ManagementView.vue'
import SignupPreferredExerciseView from '@/views/SignupPreferredExerciseView.vue'

import DietModal from '@/components/feed/DietLogModal.vue'
import MypageView from '@/views/MypageView.vue'



const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
      meta: { requiresAuth: true }, // 인증 필요
    },
    {
      path: '/feed',
      name: 'feed',
      component: FeedView,
      meta: { requiresAuth: true }, // 인증 필요
      children: [
        {
          path: '${id}',
          name: 'my-feed',
          component: FeedView,
          meta: { requiresAuth: false }, // 인증 노필요
        },
      ]
    },
    {
      path: '/user',
      children: [
        {
          path: 'signup',
          name: 'signup',
          component: UserSignup,
          meta: { requiresAuth: false }, // 인증 노필요
        },
        {
          path: 'login',
          name: 'login',
          component: LoginView,
          meta: { requiresAuth: false }, // 인증 노필요
        },
        {
          path: 'my-page',
          name: 'my-page',
          component: MypageView,
          meta: { requiresAuth: true }, // 인증 필요
        },
        {
          path: 'preferredExercise',
          name: 'preferredExercise',
          component: SignupPreferredExerciseView,
          meta: { requiresAuth: true }, // 인증 필요
        },
        {  
          path: 'find-id',
          name: 'find-id',
          component: UserFindId,
          meta: { requiresAuth: false }, // 인증 노필요
    
        },
        {
          path: 'find-pw',
          name: 'find-pw',
          component: UserFindPw,
          meta: { requiresAuth: false }, // 인증 노필요
    
        },
      ]
    },
    {
      path: '/challenge',
      name: 'challenge',
      component: ChallengeView,
      meta: { requiresAuth: true }, // 인증 필요
      
    },
    {
      path: '/management',
      name: 'management',
      component: ManagementView,
      meta: { requiresAuth: true }, // 인증 필요
    },
    {
      path: '/diet/create/:date',
      name: 'diet-create',
      component: DietModal,
      meta: { requiresAuth: true }, // 인증 필요
    },
    
  ],
})

// 라우터 가드: 인증이 필요한 페이지에 대해 토큰이 없으면 로그인 페이지로 리디렉션
router.beforeEach((to, from, next) => {
  const token = sessionStorage.getItem('access-token');
  if (to.meta.requiresAuth && !token) {
    next({ name: 'login' });
  } else {
    next();
  }
});




export default router
