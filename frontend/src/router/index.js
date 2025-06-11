import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import BookStore from '../views/BookStore.vue'
import Home from '../views/Home.vue'
import AskBookStore from '../views/AskBookStore.vue'
import MyBookshelf from '../views/MyBookshelf.vue'
import BookDetail from '../views/BookDetail.vue'
import UploadAsk from '../views/UploadAsk.vue'
import UploadSell from '../views/UploadSell.vue'
import EditBook from '../views/EditBook.vue'
import Cart from '../views/Cart.vue'
import OrderConfirm from '../views/OrderConfirm.vue'
import OrderList from '../views/OrderList.vue'
import ProfileEdit from '../views/ProfileEdit.vue'
import Register from '../views/Register.vue'

const routes = [
  { path: '/login', component: Login },
  { path: '/register', component: Register },
  { path: '/home', component: Home },
  { path: '/bookstore', component: BookStore },
  { path: '/askBookStore', component: AskBookStore },
  { path: '/myBookshelf', component: MyBookshelf },
  { path: '/books/:id', component: BookDetail },
  { path: '/uploadAsk', component: UploadAsk },
  { path: '/uploadSell', component: UploadSell },
  { path: '/editBook/:id', component: EditBook },
  { path: '/cart', component: Cart },
  { path: '/order/confirm', component: OrderConfirm },
  { path: '/orders', component: OrderList },
  { path: '/orders/:id', component: OrderList },
  { path: '/profile/edit', component: ProfileEdit },
  { path: '/', redirect: '/login' }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router 