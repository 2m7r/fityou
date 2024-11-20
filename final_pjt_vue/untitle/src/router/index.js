import { createRouter, createWebHistory } from 'vue-router'

import UserPage from '../components/user/UserPage.vue'

import HomeView from '../views/HomeView.vue'
import FeedView from '../views/FeedView.vue'
import LoginView from '../views/LoginView.vue'
import SignupView from '../views/SignupView.vue'
import ChallengeView from '../views/ChallengeView.vue'
import ManagementView from '../views/ManagementView.vue'



const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/feed',
      name: 'feed',
      component: FeedView,
    },
    {
      path: '/user',
      name: 'userpage',
      component: UserPage,
      children: [
        {
          path: 'signup',
          name: 'signup',
          component: SignupView,
        },
        {
          path: 'login',
          name: 'login',
          component: LoginView,
        },
        {
          path: 'my-page',
          name: 'my-page',
          component: UserPage,
        },
      ]
    },
    {
      path: '/challenge',
      name: 'challenge',
      component: ChallengeView,
    },
    {
      path: '/management',
      name: 'management',
      component: ManagementView,
    },
  ],
})

export default router
