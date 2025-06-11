<template>
  <div class="modern-book-detail">
    <!-- 现代化导航栏 -->
    <header class="modern-header">
      <nav class="modern-navbar">
        <div class="nav-brand">
          <router-link to="/home" class="brand-link">
            <img class="brand-logo" :src="logo2" alt="书院" />
            <span class="brand-text">二手书院</span>
          </router-link>
        </div>
        
        <div class="nav-search">
          <div class="search-container">
            <input 
              v-model="keyword" 
              type="search" 
              class="search-input" 
              placeholder="搜索图书、作者..." 
              @keyup.enter="search"
            />
            <button @click="search" class="search-btn">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                <path d="m21 21-6-6m2-5a7 7 0 1 1-14 0 7 7 0 0 1 14 0Z" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              </svg>
            </button>
          </div>
        </div>
        
        <div class="nav-menu">
          <router-link to="/home" class="nav-link">首页</router-link>
          <router-link to="/bookstore" class="nav-link">书籍良品</router-link>
          <router-link to="/askBookStore" class="nav-link">求书区</router-link>
        </div>
        
        <div class="nav-user">
          <div class="user-info">
            <span class="user-avatar">{{ user?.name?.charAt(0) || 'U' }}</span>
            <span class="user-name">{{ user?.name || '用户' }}</span>
          </div>
          <div class="user-actions">
            <router-link to="/myBookshelf" class="action-link">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
                <path d="M4 19.5v-15A2.5 2.5 0 0 1 6.5 2H20v20H6.5a2.5 2.5 0 0 1 0-5H20" stroke="currentColor" stroke-width="2"/>
              </svg>
              书架
            </router-link>
            <router-link to="/cart" class="action-link">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
                <path d="m2 2 3.6 7.4a2 2 0 0 0 1.8 1.1h9.2a2 2 0 0 0 1.9-1.4L21 4H6" stroke="currentColor" stroke-width="2"/>
                <circle cx="9" cy="21" r="1" stroke="currentColor" stroke-width="2"/>
                <circle cx="20" cy="21" r="1" stroke="currentColor" stroke-width="2"/>
              </svg>
              购物车
            </router-link>
            <button @click="logout" class="logout-btn">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
                <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" stroke="currentColor" stroke-width="2"/>
                <polyline points="16,17 21,12 16,7" stroke="currentColor" stroke-width="2"/>
                <line x1="21" y1="12" x2="9" y2="12" stroke="currentColor" stroke-width="2"/>
              </svg>
            </button>
          </div>
        </div>
      </nav>
    </header>

    <!-- 面包屑导航 -->
    <div class="breadcrumb">
      <div class="breadcrumb-container">
        <router-link to="/home" class="breadcrumb-link">首页</router-link>
        <span class="breadcrumb-separator">></span>
        <router-link to="/bookstore" class="breadcrumb-link">书籍良品</router-link>
        <span class="breadcrumb-separator">></span>
        <span class="breadcrumb-current">{{ book.name }}</span>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <main class="main-content">
      <div class="detail-container">
        <!-- 书籍信息卡片 -->
        <div class="book-card">
          <!-- 书籍图片 -->
          <div class="book-image-section">
            <div class="main-image">
              <img :src="bookImageUrl" :alt="book.name" class="book-cover" />
            </div>
          </div>

          <!-- 书籍详情 -->
          <div class="book-info-section">
            <div class="book-header">
              <h1 class="book-title">{{ book.name }}</h1>
              <div class="book-rating">
                <div class="stars">
                  <span v-for="i in 5" :key="i" class="star">⭐</span>
                </div>
                <span class="rating-text">4.8分 (120评价)</span>
              </div>
            </div>

            <div class="book-meta">
              <div class="meta-item">
                <span class="meta-label">作者：</span>
                <span class="meta-value">{{ book.author }}</span>
              </div>
              <div class="meta-item">
                <span class="meta-label">出版社：</span>
                <span class="meta-value">{{ book.press }}</span>
              </div>
              <div class="meta-item">
                <span class="meta-label">出版时间：</span>
                <span class="meta-value">{{ book.publishDate }}</span>
              </div>
              <div class="meta-item">
                <span class="meta-label">版本：</span>
                <span class="meta-value">{{ book.version }}</span>
              </div>
            </div>

            <div class="price-section">
              <div class="price-main">
                <span class="current-price">¥{{ book.price }}</span>
                <span v-if="book.originalPrice > book.price" class="original-price">¥{{ book.originalPrice }}</span>
                <span class="discount" v-if="book.originalPrice > book.price">
                  省¥{{ (book.originalPrice - book.price).toFixed(2) }}
                </span>
              </div>
              <div class="condition-info">
                <span class="condition-badge">{{ book.degree }}成新</span>
                <span class="stock-info">库存：{{ book.stock || 0 }} 本</span>
              </div>
            </div>

            <div class="description-section">
              <h3 class="section-title">商品描述</h3>
              <p class="description-text">{{ book.description || '暂无描述' }}</p>
              <div class="upload-time">
                <span class="time-label">上架时间：</span>
                <span class="time-value">{{ formattedDate }}</span>
              </div>
            </div>

            <div class="action-section">
              <button class="btn-cart" @click="addToCart">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                  <path d="m2 2 3.6 7.4a2 2 0 0 0 1.8 1.1h9.2a2 2 0 0 0 1.9-1.4L21 4H6" stroke="currentColor" stroke-width="2"/>
                  <circle cx="9" cy="21" r="1" stroke="currentColor" stroke-width="2"/>
                  <circle cx="20" cy="21" r="1" stroke="currentColor" stroke-width="2"/>
                </svg>
                加入购物车
              </button>
              <button class="btn-buy" @click="buyNow">
                立即购买
              </button>
            </div>
          </div>
        </div>

        <!-- 卖家信息卡片 -->
        <div class="seller-card">
          <div class="seller-header">
            <h3 class="card-title">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" stroke="currentColor" stroke-width="2"/>
                <circle cx="12" cy="7" r="4" stroke="currentColor" stroke-width="2"/>
              </svg>
              卖家信息
            </h3>
          </div>
          
          <div class="seller-content">
            <div class="seller-avatar">
              <span class="avatar-text">{{ seller?.name?.charAt(0) || 'S' }}</span>
            </div>
            
            <div class="seller-details">
              <h4 class="seller-name">{{ seller?.name || '匿名卖家' }}</h4>
              <div class="seller-rating">
                <span class="rating-stars">⭐⭐⭐⭐⭐</span>
                <span class="rating-score">好评率 95.2%</span>
              </div>
              
              <div class="contact-info">
                <div class="contact-item">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                    <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z" stroke="currentColor" stroke-width="2"/>
                  </svg>
                  <span>{{ seller?.tel || '暂不公开' }}</span>
                </div>
                <div class="contact-item">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                    <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z" stroke="currentColor" stroke-width="2"/>
                    <circle cx="12" cy="10" r="3" stroke="currentColor" stroke-width="2"/>
                  </svg>
                  <span>{{ seller?.address || '校内交易' }}</span>
                </div>
              </div>
              
              <div class="seller-actions">
                <button class="contact-btn">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                    <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z" stroke="currentColor" stroke-width="2"/>
                  </svg>
                  联系卖家
                </button>
                <button class="view-store-btn">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                    <path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z" stroke="currentColor" stroke-width="2"/>
                    <path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z" stroke="currentColor" stroke-width="2"/>
                  </svg>
                  查看店铺
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>

    <!-- 现代化页脚 -->
    <footer class="modern-footer">
      <div class="footer-content">
        <div class="footer-section">
          <h4>二手书院</h4>
          <p>让知识传递，让书香延续</p>
        </div>
      </div>
      <div class="footer-bottom">
        <p>&copy; 2024 二手书院. 保留所有权利.</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'
import { useRoute, useRouter } from 'vue-router'

// 导入图片资源
import logo2 from '/img/logo2.jpg'

const route = useRoute()
const router = useRouter()
const book = ref({})
const seller = ref({})
const images = ref([])
const user = ref(null)
const keyword = ref('')

const bookImageUrl = computed(() => {
  if (images.value.length > 0) {
    return `/img/book-list/article/${images.value[0].id}.jpg`
  } else if (book.value.bookImage?.id) {
    return `/img/book-list/article/${book.value.bookImage.id}.jpg`
  } else {
    return '/img/book-list/article/default.jpg'
  }
})

const formattedDate = computed(() => {
  if (!book.value.uploadTime) return '未知时间'
  const date = new Date(book.value.uploadTime)
  return date.toLocaleDateString('zh-CN')
})

function search() {
  router.push({ path: '/bookstore', query: { keyword: keyword.value } })
}

function logout() {
  try {
    const result = confirm('确定要退出登录吗？')
    if (!result) return
    
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    router.push('/login')
  } catch (error) {
    console.error('退出登录失败:', error)
  }
}

// 加入购物车
async function addToCart() {
  try {
    console.log('=== 前端购物车调试 ===')
    console.log('当前书籍ID:', book.value.id)
    console.log('当前书籍名称:', book.value.name)
    console.log('当前路由参数:', route.params)
    
    const requestData = {
      bookId: book.value.id,
      quantity: 1
    }
    console.log('发送的请求参数:', requestData)
    
    const res = await axios.post('/api/cart/add', null, {
      params: requestData
    })
    
    console.log('服务器响应:', res.data)
    
    if (res.data.code === 200 || res.data.resultCode === 200) {
      alert('成功加入购物车！')
    } else {
      alert(res.data.message || '加入购物车失败')
    }
  } catch (error) {
    console.error('加入购物车失败:', error)
    alert('加入购物车失败，请先登录')
  }
}

// 立即购买
async function buyNow() {
  try {
    await addToCart()
    router.push('/cart')
  } catch (error) {
    alert('购买失败，请重试')
  }
}

async function fetchData() {
  try {
    const id = route.params.id
    const res = await axios.get(`/api/book/${id}`)
    if (res.data.code === 200) {
      book.value = res.data.data
      seller.value = book.value.user || {}
      
      // 获取图片
      try {
        const imgRes = await axios.get(`/api/bookImage/book/${id}`)
        if (imgRes.data && Array.isArray(imgRes.data)) {
          images.value = imgRes.data
        }
      } catch (error) {
        console.error('获取图片失败:', error)
      }
    }
  } catch (error) {
    console.error('获取书籍详情失败:', error)
  }
}

onMounted(async () => {
  try {
    // 获取用户信息
    const userRes = await axios.get('/api/user/info')
    if (userRes.data.code === 200) {
      user.value = userRes.data.data
    }
    
    await fetchData()
  } catch (error) {
    console.error('页面初始化失败:', error)
  }
})
</script>

<style scoped>
.modern-book-detail {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

/* 现代化导航栏 */
.modern-header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 1000;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.modern-navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem 2rem;
  max-width: 1400px;
  margin: 0 auto;
}

.nav-brand {
  display: flex;
  align-items: center;
}

.brand-link {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: #2d3748;
  font-weight: 700;
  font-size: 1.25rem;
}

.brand-logo {
  width: 130px;
  height: 130px;
  margin-right: 0.75rem;
  border-radius: 8px;
  object-fit: contain;
}

.brand-text {
  background: linear-gradient(135deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.search-container {
  position: relative;
  display: flex;
  align-items: center;
}

.search-input {
  width: 400px;
  padding: 0.75rem 1rem 0.75rem 2.5rem;
  border: 2px solid #e2e8f0;
  border-radius: 50px;
  font-size: 0.95rem;
  background: white;
  color: #1a202c;
  transition: all 0.3s ease;
  outline: none;
}

.search-input:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.search-btn {
  position: absolute;
  left: 0.75rem;
  background: none;
  border: none;
  color: #64748b;
  cursor: pointer;
  padding: 0.25rem;
  border-radius: 50%;
  transition: color 0.3s ease;
}

.search-btn:hover {
  color: #667eea;
}

.nav-menu {
  display: flex;
  gap: 2rem;
}

.nav-link {
  text-decoration: none;
  color: #64748b;
  font-weight: 500;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.nav-link:hover {
  color: #667eea;
  background: rgba(102, 126, 234, 0.1);
}

.nav-user {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 0.875rem;
}

.user-name {
  font-weight: 500;
  color: #2d3748;
}

.user-actions {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.action-link {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  text-decoration: none;
  color: #64748b;
  padding: 0.5rem 0.75rem;
  border-radius: 8px;
  font-size: 0.875rem;
  font-weight: 500;
  transition: all 0.3s ease;
}

.action-link:hover {
  color: #667eea;
  background: rgba(102, 126, 234, 0.1);
}

.logout-btn {
  background: none;
  border: none;
  color: #64748b;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 8px;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
}

.logout-btn:hover {
  color: #ef4444;
  background: rgba(239, 68, 68, 0.1);
}

/* 面包屑导航 */
.breadcrumb {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  padding: 1rem 0;
}

.breadcrumb-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.875rem;
}

.breadcrumb-link {
  color: #667eea;
  text-decoration: none;
  transition: color 0.3s ease;
}

.breadcrumb-link:hover {
  color: #5a67d8;
}

.breadcrumb-separator {
  color: #94a3b8;
}

.breadcrumb-current {
  color: #2d3748;
  font-weight: 500;
}

/* 主要内容区域 */
.main-content {
  padding: 2rem;
}

.detail-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

/* 书籍信息卡片 */
.book-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  overflow: hidden;
  display: flex;
  gap: 2rem;
  padding: 2rem;
}

.book-image-section {
  flex-shrink: 0;
  width: 400px;
}

.main-image {
  margin-bottom: 1rem;
}

.book-cover {
  width: 100%;
  height: 500px;
  object-fit: cover;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.book-info-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.book-header {
  border-bottom: 1px solid #e2e8f0;
  padding-bottom: 1rem;
}

.book-title {
  font-size: 2rem;
  font-weight: 700;
  color: #1a202c;
  margin: 0 0 1rem 0;
  line-height: 1.3;
}

.book-rating {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.stars {
  color: #fbbf24;
}

.rating-text {
  color: #64748b;
  font-size: 0.875rem;
}

.book-meta {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 0.75rem;
}

.meta-item {
  display: flex;
  gap: 0.5rem;
}

.meta-label {
  color: #64748b;
  font-weight: 500;
  min-width: 80px;
}

.meta-value {
  color: #1a202c;
  font-weight: 600;
}

.price-section {
  background: #f8f9fa;
  border-radius: 12px;
  padding: 1.5rem;
  border: 1px solid #e2e8f0;
}

.price-main {
  display: flex;
  align-items: baseline;
  gap: 1rem;
  margin-bottom: 1rem;
}

.current-price {
  font-size: 2.5rem;
  font-weight: 700;
  color: #ef4444;
}

.original-price {
  font-size: 1.25rem;
  color: #94a3b8;
  text-decoration: line-through;
}

.discount {
  background: #ef4444;
  color: white;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.875rem;
  font-weight: 600;
}

.condition-info {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.condition-badge {
  background: linear-gradient(135deg, #10b981, #059669);
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-weight: 600;
  font-size: 0.875rem;
}

.stock-info {
  color: #64748b;
  font-size: 0.875rem;
}

.description-section {
  background: #f8f9fa;
  border-radius: 12px;
  padding: 1.5rem;
  border: 1px solid #e2e8f0;
}

.section-title {
  font-size: 1.125rem;
  font-weight: 700;
  color: #1a202c;
  margin: 0 0 1rem 0;
}

.description-text {
  color: #64748b;
  line-height: 1.6;
  margin: 0 0 1rem 0;
}

.upload-time {
  display: flex;
  gap: 0.5rem;
  font-size: 0.875rem;
}

.time-label {
  color: #64748b;
}

.time-value {
  color: #1a202c;
  font-weight: 500;
}

.action-section {
  display: flex;
  gap: 1rem;
  margin-top: auto;
}

.btn-cart,
.btn-buy {
  flex: 1;
  padding: 1rem 2rem;
  border-radius: 12px;
  font-weight: 600;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

.btn-cart {
  background: rgba(102, 126, 234, 0.1);
  color: #667eea;
  border: 2px solid #667eea;
}

.btn-cart:hover {
  background: #667eea;
  color: white;
  transform: translateY(-2px);
}

.btn-buy {
  background: linear-gradient(135deg, #ef4444, #dc2626);
  color: white;
  border: none;
}

.btn-buy:hover {
  background: linear-gradient(135deg, #dc2626, #b91c1c);
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(239, 68, 68, 0.3);
}

/* 卖家信息卡片 */
.seller-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  overflow: hidden;
}

.seller-header {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  padding: 1.5rem 2rem 1rem 2rem;
}

.card-title {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 1.25rem;
  font-weight: 700;
  margin: 0;
}

.seller-content {
  padding: 2rem;
  display: flex;
  gap: 1.5rem;
  align-items: center;
}

.seller-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 2rem;
  font-weight: 700;
  flex-shrink: 0;
}

.seller-details {
  flex: 1;
}

.seller-name {
  font-size: 1.25rem;
  font-weight: 700;
  color: #1a202c;
  margin: 0 0 0.5rem 0;
}

.seller-rating {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.rating-stars {
  color: #fbbf24;
}

.rating-score {
  color: #64748b;
  font-size: 0.875rem;
}

.contact-info {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #64748b;
  font-size: 0.875rem;
}

.seller-actions {
  display: flex;
  gap: 1rem;
}

.contact-btn,
.view-store-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1.5rem;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
  font-size: 0.875rem;
}

.contact-btn {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
}

.contact-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.view-store-btn {
  background: rgba(102, 126, 234, 0.1);
  color: #667eea;
  border: 1px solid #667eea;
}

.view-store-btn:hover {
  background: #667eea;
  color: white;
}

/* 现代化页脚 */
.modern-footer {
  background: rgba(15, 23, 42, 0.95);
  backdrop-filter: blur(20px);
  color: white;
  margin-top: 2rem;
}

.footer-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 2rem;
  text-align: center;
}

.footer-section h4 {
  margin: 0 0 1rem 0;
  font-size: 1.125rem;
  font-weight: 700;
  color: #f1f5f9;
}

.footer-section p {
  margin: 0;
  color: #94a3b8;
  line-height: 1.6;
}

.footer-bottom {
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  padding: 1rem 2rem;
  text-align: center;
  max-width: 1400px;
  margin: 0 auto;
}

.footer-bottom p {
  margin: 0;
  color: #64748b;
  font-size: 0.875rem;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .modern-navbar {
    flex-direction: column;
    gap: 1rem;
    padding: 1rem;
  }
  
  .nav-menu {
    order: -1;
    width: 100%;
    justify-content: center;
  }
  
  .book-card {
    flex-direction: column;
    padding: 1rem;
  }
  
  .book-image-section {
    width: 100%;
  }
  
  .action-section {
    flex-direction: column;
  }
  
  .seller-content {
    flex-direction: column;
    text-align: center;
  }
  
  .seller-actions {
    justify-content: center;
  }
  
  .search-input {
    width: 100%;
    max-width: 300px;
  }
}
</style> 