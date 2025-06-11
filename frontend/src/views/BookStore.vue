<template>
  <div id="bookstore" class="modern-bookstore">
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
              id="search-input" 
              type="search" 
              class="modern-search-input" 
              placeholder="搜索图书、作者..." 
              @keyup.enter="search"
            />
            <button @click="search" class="modern-search-btn">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                <path d="m21 21-6-6m2-5a7 7 0 1 1-14 0 7 7 0 0 1 14 0Z" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              </svg>
            </button>
          </div>
        </div>
        
        <div class="nav-menu">
          <router-link to="/home" class="nav-link">首页</router-link>
          <router-link to="/bookstore" class="nav-link active">书籍良品</router-link>
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
            <router-link to="/orders" class="action-link">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
                <path d="M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2" stroke="currentColor" stroke-width="2"/>
                <rect x="8" y="2" width="8" height="4" rx="1" ry="1" stroke="currentColor" stroke-width="2"/>
              </svg>
              订单
            </router-link>
            <router-link to="/cart" class="action-link cart-link">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
                <path d="m2 2 3.6 7.4a2 2 0 0 0 1.8 1.1h9.2a2 2 0 0 0 1.9-1.4L21 4H6" stroke="currentColor" stroke-width="2"/>
                <circle cx="9" cy="21" r="1" stroke="currentColor" stroke-width="2"/>
                <circle cx="20" cy="21" r="1" stroke="currentColor" stroke-width="2"/>
              </svg>
              购物车
              <span v-if="cartCount > 0" class="cart-badge">{{ cartCount }}</span>
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

    <!-- 主要内容区域 -->
    <main class="main-content">
      <!-- 侧边栏分类菜单 -->
      <aside class="category-sidebar">
        <div class="sidebar-header">
          <h3>图书分类</h3>
        </div>
        <ul class="category-list">
          <li class="category-item" :class="{ active: !currentCategoryId }" @click="selectCategory(null)">
            <span class="category-icon">📚</span>
            <span class="category-name">全部分类</span>
          </li>
          <li 
            v-for="cat in categories" 
            :key="cat.id" 
            class="category-item" 
            :class="{ active: currentCategoryId === cat.id }"
            @click="selectCategory(cat.id)"
          >
            <span class="category-icon">{{ getCategoryIcon(cat.name) }}</span>
            <span class="category-name">{{ cat.name }}</span>
          </li>
        </ul>
      </aside>

      <!-- 图书内容区域 -->
      <section class="content-area">
        <!-- 页面标题和过滤器 -->
        <div class="content-header">
          <div class="page-title">
            <h2>{{ currentCategoryName }}</h2>
            <p class="subtitle">发现好书，分享知识</p>
          </div>
          <div class="content-filters">
            <div class="view-mode">
              <button class="mode-btn active">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                  <rect x="3" y="3" width="7" height="7" stroke="currentColor" stroke-width="2"/>
                  <rect x="14" y="3" width="7" height="7" stroke="currentColor" stroke-width="2"/>
                  <rect x="3" y="14" width="7" height="7" stroke="currentColor" stroke-width="2"/>
                  <rect x="14" y="14" width="7" height="7" stroke="currentColor" stroke-width="2"/>
                </svg>
              </button>
            </div>
          </div>
        </div>
        
        <!-- 空状态提示 -->
        <div v-if="books.length === 0" class="empty-state">
          <div class="empty-icon">📚</div>
          <h3>暂无图书数据</h3>
          <p>当前没有找到相关图书，请尝试其他分类或搜索关键词</p>
        </div>
        
        <!-- 图书网格 -->
        <div v-if="books.length > 0" class="books-grid">
          <div v-for="book in books" :key="book.id" class="book-card">
            <div class="book-cover">
              <router-link :to="`/books/${book.id}`" class="cover-link">
                <img 
                  :src="getBookImageSrc(book)" 
                  @error="handleImageError" 
                  :alt="book.name" 
                  class="cover-image"
                />
                <div class="cover-overlay">
                  <span class="view-detail">查看详情</span>
                </div>
              </router-link>
            </div>
            
            <div class="book-info">
              <h4 class="book-title">{{ book.name }}</h4>
              <p class="book-author">{{ book.author }}</p>
              <p class="book-desc">{{ truncateDescription(book.description) }}</p>
              
              <div class="book-meta">
                <div class="book-condition">
                  <span class="condition-badge">{{ book.degree }}成新</span>
                  <span class="stock-info">库存 {{ book.stock || 0 }}</span>
                </div>
                <div class="book-price">
                  <span class="current-price">¥{{ book.price }}</span>
                  <span v-if="book.originalPrice > book.price" class="original-price">¥{{ book.originalPrice }}</span>
                </div>
              </div>
              
              <div class="book-actions">
                <button 
                  class="btn-cart" 
                  :disabled="!book.stock || book.stock <= 0"
                  @click="addToCart(book)"
                >
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                    <path d="m2 2 3.6 7.4a2 2 0 0 0 1.8 1.1h9.2a2 2 0 0 0 1.9-1.4L21 4H6" stroke="currentColor" stroke-width="2"/>
                    <circle cx="9" cy="21" r="1" stroke="currentColor" stroke-width="2"/>
                    <circle cx="20" cy="21" r="1" stroke="currentColor" stroke-width="2"/>
                  </svg>
                  加入购物车
                </button>
                <button 
                  class="btn-buy" 
                  :disabled="!book.stock || book.stock <= 0"
                  @click="buyNow(book)"
                >
                  立即购买
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- 分页控制 -->
        <div v-if="books.length > 0" class="pagination">
          <button @click="changePage(1)" :disabled="pageNum === 1" class="page-btn">首页</button>
          <button @click="changePage(pageNum - 1)" :disabled="pageNum === 1" class="page-btn">上一页</button>
          <span class="page-info">第 {{ pageNum }} 页 / 共 {{ totalPages }} 页</span>
          <button @click="changePage(pageNum + 1)" :disabled="pageNum === totalPages" class="page-btn">下一页</button>
          <button @click="changePage(totalPages)" :disabled="pageNum === totalPages" class="page-btn">尾页</button>
        </div>
      </section>
    </main>

    <!-- 现代化页脚 -->
    <footer class="modern-footer">
      <div class="footer-content">
        <div class="footer-section">
          <h4>二手书院</h4>
          <p>让知识传递，让书香延续</p>
        </div>
        <div class="footer-section">
          <h4>帮助与支持</h4>
          <a href="#">使用条款</a>
          <a href="#">隐私政策</a>
          <a href="#">联系我们</a>
        </div>
        <div class="footer-section">
          <h4>关于我们</h4>
          <a href="#">意见反馈</a>
          <a href="#">服务介绍</a>
          <a href="#">加入我们</a>
        </div>
      </div>
      <div class="footer-bottom">
        <p>&copy; 2024 二手书院. 保留所有权利.</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import axios from 'axios'
import { useRoute, useRouter } from 'vue-router'

// 导入图片资源
import logo2 from '/img/logo2.jpg'

const route = useRoute()
const router = useRouter()
const user = ref(null)
const categories = ref([])
const books = ref([])
const keyword = ref('')
const pageNum = ref(1)
const pageSize = ref(10)
const totalPages = ref(1)
const currentCategoryId = ref(null)
const cartCount = ref(0)

const currentCategoryName = computed(() => {
  const cat = categories.value.find(c => c.id === currentCategoryId.value)
  return cat ? cat.name : '全部'
})

async function fetchCategories() {
  try {
    const res = await axios.get('/api/category/list')
    if (res.data.code === 200) {
      categories.value = res.data.data
    }
  } catch (error) {
    console.error('分类接口调用失败:', error)
  }
}

async function fetchBooks() {
  try {
    const params = {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      categoryId: currentCategoryId.value,
      keyword: keyword.value
    }
    const res = await axios.get('/api/book/list', { params })
    
    if (res.data.code === 200) {
      const page = res.data.data
      books.value = page.records || []
      totalPages.value = Math.ceil((page.total || 0) / pageSize.value)
    } else {
      books.value = []
    }
  } catch (error) {
    books.value = []
  }
}

// 获取购物车数量
async function fetchCartCount() {
  try {
    const res = await axios.get('/api/cart/count')
    if (res.data.code === 200 || res.data.resultCode === 200) {
      cartCount.value = res.data.data
    }
  } catch (error) {
    console.error('获取购物车数量失败:', error)
  }
}

// 加入购物车
async function addToCart(book) {
  try {
    console.log('=== BookStore 加入购物车调试 ===')
    console.log('书籍对象:', book)
    console.log('书籍ID:', book.id)
    console.log('书籍名称:', book.name)
    
    const res = await axios.post('/api/cart/add', null, {
      params: {
        bookId: book.id,
        quantity: 1
      }
    })
    console.log('服务器响应:', res.data)
    
    if (res.data.code === 200 || res.data.resultCode === 200) {
      alert('成功加入购物车！')
      fetchCartCount() // 刷新购物车数量
    } else {
      alert(res.data.message || '加入购物车失败')
    }
  } catch (error) {
    console.error('加入购物车失败:', error)
    alert('加入购物车失败，请先登录')
  }
}

// 立即购买
async function buyNow(book) {
  try {
    // 先加入购物车
    await addToCart(book)
    // 然后跳转到购物车页面
    router.push('/cart')
  } catch (error) {
    alert('购买失败，请重试')
  }
}

// 分类选择
async function selectCategory(categoryId) {
  currentCategoryId.value = categoryId
  pageNum.value = 1
  await fetchBooks()
}

// 搜索功能
async function search() {
  pageNum.value = 1
  await fetchBooks()
}

async function changePage(newPage) {
  if (newPage < 1 || newPage > totalPages.value) return
  pageNum.value = newPage
  await fetchBooks()
}

// 登出处理
async function logout() {
  try {
    const result = confirm('确定要退出登录吗？')
    if (!result) return
    
    // 清除本地存储的登录信息
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    
    // 跳转到登录页面
    router.push('/login')
  } catch (error) {
    console.error('退出登录失败:', error)
  }
}

function getBookImageSrc(book) {
  return `/img/book-list/article/${book.bookImage?.id || 'default'}.jpg`
}

function handleImageError(event) {
  event.target.src = '/img/book-list/article/default.jpg'
}

// 辅助函数
function getCategoryIcon(categoryName) {
  const iconMap = {
    '小说': '📖',
    '文学': '✍️', 
    '历史': '📜',
    '地理': '🌍',
    '艺术': '🎨',
    '政治': '🏛️',
    '法律': '⚖️',
    '军事': '🛡️',
    '哲学': '🤔',
    '心理': '🧠',
    '宗教': '⛪',
    '经济': '💰',
    '社科': '👥',
    '综合': '📚',
    '童书': '🧸',
    '生活': '🏠',
    '体育': '⚽',
    '工程技术': '⚙️',
    '互联网': '💻',
    '教材': '📝',
    '教辅': '📋',
    '考试': '✏️',
    '自然科学': '🔬',
    '医药卫生': '🏥'
  }
  
  for (const [key, icon] of Object.entries(iconMap)) {
    if (categoryName.includes(key)) {
      return icon
    }
  }
  return '📚'
}

function truncateDescription(description, maxLength = 60) {
  if (!description) return '暂无描述'
  if (description.length <= maxLength) return description
  return description.substring(0, maxLength) + '...'
}

onMounted(async () => {
  try {
    // 获取用户信息
    try {
      const userRes = await axios.get('/api/user/info')
      if (userRes.data.code === 200) {
        user.value = userRes.data.data
      }
    } catch (error) {
      console.error('用户信息获取失败:', error)
    }
    
    // 获取分类数据
    await fetchCategories()
    
    // 从URL查询参数中获取分类ID和关键词
    const categoryId = route.query.categoryId
    const searchKeyword = route.query.keyword
    
    if (categoryId) {
      currentCategoryId.value = parseInt(categoryId)
    }
    
    if (searchKeyword) {
      keyword.value = searchKeyword
    }
    
    // 获取图书数据和购物车数量
    await Promise.all([fetchBooks(), fetchCartCount()])
  } catch (error) {
    console.error('页面初始化失败:', error)
  }
})
</script>

<style scoped>
.modern-bookstore {
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

.modern-search-input {
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

.modern-search-input:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.modern-search-btn {
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

.modern-search-btn:hover {
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
  position: relative;
}

.nav-link:hover,
.nav-link.active {
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

.cart-link {
  position: relative;
}

.cart-badge {
  position: absolute;
  top: -8px;
  right: -8px;
  background: linear-gradient(135deg, #ef4444, #dc2626);
  color: white;
  font-size: 0.75rem;
  font-weight: 900;
  min-width: 20px;
  height: 20px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  animation: pulse 2s infinite;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.8);
  border: 2px solid white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}

@keyframes pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(239, 68, 68, 0.7);
  }
  70% {
    box-shadow: 0 0 0 10px rgba(239, 68, 68, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(239, 68, 68, 0);
  }
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

/* 主要内容区域 */
.main-content {
  display: flex;
  max-width: 1400px;
  margin: 0 auto;
  padding: 2rem;
  gap: 2rem;
  min-height: calc(100vh - 200px);
}

/* 侧边栏 */
.category-sidebar {
  width: 280px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 16px;
  padding: 1.5rem;
  height: fit-content;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.sidebar-header h3 {
  margin: 0 0 1.5rem 0;
  color: #2d3748;
  font-weight: 700;
  font-size: 1.125rem;
}

.category-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.category-item {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.75rem 1rem;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-bottom: 0.5rem;
  color: #2d3748;
}

.category-item:hover {
  background: rgba(102, 126, 234, 0.1);
  transform: translateX(4px);
  color: #1a202c;
}

.category-item.active {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  box-shadow: 0 8px 16px rgba(102, 126, 234, 0.3);
}

.category-icon {
  font-size: 1.25rem;
}

.category-name {
  font-weight: 500;
  font-size: 0.875rem;
  color: inherit;
}

/* 内容区域 */
.content-area {
  flex: 1;
}

.content-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 2rem;
}

.page-title h2 {
  margin: 0 0 0.5rem 0;
  color: white;
  font-size: 2rem;
  font-weight: 700;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.subtitle {
  margin: 0;
  color: rgba(255, 255, 255, 0.8);
  font-size: 1.125rem;
}

.content-filters {
  display: flex;
  gap: 1rem;
}

.mode-btn {
  background: rgba(255, 255, 255, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: white;
  padding: 0.75rem;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.mode-btn.active,
.mode-btn:hover {
  background: rgba(255, 255, 255, 0.3);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

/* 空状态 */
.empty-state {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 3rem;
  text-align: center;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
}

.empty-state h3 {
  margin: 0 0 1rem 0;
  color: #2d3748;
  font-size: 1.5rem;
  font-weight: 600;
}

.empty-state p {
  margin: 0;
  color: #64748b;
  font-size: 1.125rem;
}

/* 图书网格 */
.books-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
  margin-bottom: 3rem;
}

.book-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.book-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 30px 60px rgba(0, 0, 0, 0.15);
}

.book-cover {
  position: relative;
  height: 240px;
  overflow: hidden;
}

.cover-link {
  display: block;
  width: 100%;
  height: 100%;
  position: relative;
}

.cover-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.book-card:hover .cover-image {
  transform: scale(1.05);
}

.cover-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.8), rgba(118, 75, 162, 0.8));
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.book-card:hover .cover-overlay {
  opacity: 1;
}

.view-detail {
  color: white;
  font-weight: 600;
  font-size: 1rem;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.book-info {
  padding: 1.5rem;
}

.book-title {
  margin: 0 0 0.5rem 0;
  font-size: 1.125rem;
  font-weight: 700;
  color: #1a202c;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.book-author {
  margin: 0 0 0.75rem 0;
  color: #667eea;
  font-weight: 500;
  font-size: 0.95rem;
}

.book-desc {
  margin: 0 0 1rem 0;
  color: #64748b;
  font-size: 0.875rem;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.book-meta {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 1.5rem;
}

.book-condition {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.condition-badge {
  background: linear-gradient(135deg, #10b981, #059669);
  color: white;
  padding: 0.25rem 0.75rem;
  border-radius: 50px;
  font-size: 0.75rem;
  font-weight: 600;
  width: fit-content;
}

.stock-info {
  font-size: 0.75rem;
  color: #64748b;
}

.book-price {
  text-align: right;
}

.current-price {
  font-size: 1.25rem;
  font-weight: 700;
  color: #ef4444;
}

.original-price {
  font-size: 0.875rem;
  color: #94a3b8;
  text-decoration: line-through;
  margin-left: 0.5rem;
}

.book-actions {
  display: flex;
  gap: 0.75rem;
}

.btn-cart,
.btn-buy {
  flex: 1;
  padding: 0.75rem 1rem;
  border: none;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 0.875rem;
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

.btn-cart:hover:not(:disabled) {
  background: #667eea;
  color: white;
  transform: translateY(-2px);
}

.btn-buy {
  background: linear-gradient(135deg, #ef4444, #dc2626);
  color: white;
}

.btn-buy:hover:not(:disabled) {
  background: linear-gradient(135deg, #dc2626, #b91c1c);
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(239, 68, 68, 0.3);
}

.btn-cart:disabled,
.btn-buy:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  transform: none;
}

/* 分页 */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
  padding: 2rem 0;
}

.page-btn {
  padding: 0.75rem 1.5rem;
  border: 2px solid rgba(255, 255, 255, 0.3);
  background: rgba(255, 255, 255, 0.1);
  color: white;
  border-radius: 12px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s ease;
}

.page-btn:hover:not(:disabled) {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  color: white;
  font-weight: 500;
  padding: 0 1rem;
}

/* 现代化页脚 */
.modern-footer {
  background: rgba(15, 23, 42, 0.95);
  backdrop-filter: blur(20px);
  color: white;
  margin-top: 4rem;
}

.footer-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 3rem 2rem 2rem;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
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

.footer-section a {
  display: block;
  color: #94a3b8;
  text-decoration: none;
  margin-bottom: 0.5rem;
  transition: color 0.3s ease;
}

.footer-section a:hover {
  color: #667eea;
}

.footer-bottom {
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  padding: 1.5rem 2rem;
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
@media (max-width: 1200px) {
  .main-content {
    padding: 1.5rem;
  }
  
  .category-sidebar {
    width: 240px;
  }
  
  .books-grid {
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  }
}

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
  
  .main-content {
    flex-direction: column;
    padding: 1rem;
  }
  
  .category-sidebar {
    width: 100%;
    order: 1;
  }
  
  .books-grid {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 1rem;
  }
  
  .modern-search-input {
    width: 100%;
    max-width: 300px;
  }
}
</style> 