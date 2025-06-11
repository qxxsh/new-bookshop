<template>
  <div class="modern-ask-store">
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
              placeholder="搜索求书信息..." 
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
          <router-link to="/askBookStore" class="nav-link active">求书区</router-link>
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

    <!-- 英雄区域 -->
    <section class="hero-section">
      <div class="hero-content">
        <div class="hero-text">
          <h1 class="hero-title">求书广场</h1>
          <p class="hero-subtitle">找不到心仪的书？发布求书信息，让书找到你</p>
          <router-link to="/uploadAsk" class="hero-action-btn">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path d="M12 5v14M5 12h14" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            发布求书
          </router-link>
        </div>
        <div class="hero-stats">
          <div class="stat-card">
            <div class="stat-number">{{ books.length }}</div>
            <div class="stat-label">求书信息</div>
          </div>
        </div>
      </div>
    </section>

    <!-- 主要内容区域 -->
    <main class="main-content">
      <div class="content-container">
        <!-- 页面标题和操作 -->
        <div class="content-header">
          <div class="header-left">
            <h2 class="section-title">所有求书信息</h2>
            <p class="section-subtitle">帮助他人找到心仪图书</p>
          </div>
          <div class="header-actions">
            <router-link to="/uploadAsk" class="add-btn">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                <path d="M12 5v14M5 12h14" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              </svg>
              发布求书
            </router-link>
          </div>
        </div>

        <!-- 空状态 -->
        <div v-if="books.length === 0" class="empty-state">
          <div class="empty-icon">📚</div>
          <h3>暂无求书信息</h3>
          <p>还没有人发布求书信息，成为第一个吧！</p>
          <router-link to="/uploadAsk" class="empty-action-btn">
            发布求书信息
          </router-link>
        </div>

        <!-- 求书信息列表 -->
        <div v-else class="ask-list">
          <div v-for="book in books" :key="book.id" class="ask-item">
            <div class="ask-image">
              <img 
                :src="`/img/book-list/article/${book.bookImage?.id || 'default'}.jpg`"
                @error="$event.target.src='/img/book-list/article/default.jpg'"
                :alt="book.name"
                class="book-cover"
              />
              <div class="ask-badge">求书</div>
            </div>

            <div class="ask-content">
              <div class="ask-header">
                <h3 class="book-title">{{ book.name }}</h3>
                <div class="book-meta">
                  <span class="book-author">{{ book.author }}</span>
                  <span class="book-price">期望价格: ¥{{ book.price }}</span>
                </div>
              </div>

              <div class="ask-description">
                <p>{{ book.description || '求购这本书籍' }}</p>
              </div>

              <div class="ask-footer">
                <div class="publisher-info">
                  <span class="publisher-name">{{ book.user?.name || '匿名用户' }}</span>
                  <span class="publish-time">{{ formatDate(book.uploadTime) }}</span>
                </div>
                <div class="ask-actions">
                  <button class="contact-btn">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                      <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z" stroke="currentColor" stroke-width="2"/>
                    </svg>
                    联系TA
                  </button>
                  <router-link :to="`/books/${book.id}`" class="detail-btn">
                    查看详情
                  </router-link>
                </div>
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
      </div>
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
      </div>
      <div class="footer-bottom">
        <p>&copy; 2024 二手书院. 保留所有权利.</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

// 导入图片资源
import logo2 from '/img/logo2.jpg'

const router = useRouter()
const user = ref(null)
const books = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const totalPages = ref(1)
const keyword = ref('')

async function fetchBooks() {
  try {
    console.log('正在获取求书数据...')
    const params = {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      keyword: keyword.value,
      bookType: 0
    }
    console.log('请求参数:', params)
    const res = await axios.get('/api/book/list', { params })
    console.log('求书数据响应:', res.data)
    
    if (res.data.code === 200) {
      const page = res.data.data
      books.value = page.records || []
      totalPages.value = Math.ceil((page.total || 0) / pageSize.value)
      console.log('求书数据获取成功:', books.value)
      console.log('总页数:', totalPages.value)
    } else {
      console.error('求书数据获取失败:', res.data.message)
      books.value = []
    }
  } catch (error) {
    console.error('求书接口调用失败:', error)
    books.value = []
  }
}

function changePage(num) {
  if (num < 1 || num > totalPages.value) return
  pageNum.value = num
  fetchBooks()
}

async function logout() {
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

function search() {
  pageNum.value = 1
  fetchBooks()
}

function formatDate(dateString) {
  if (!dateString) return '未知时间'
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN')
}

onMounted(async () => {
  try {
    console.log('AskBookStore页面加载中...')
    
    // 检查是否从AI推荐跳转过来
    const route = router.currentRoute.value
    if (route.query.fromRecommendation === 'true') {
      console.log('从AI推荐跳转过来，搜索参数:', route.query)
      
      // 设置搜索关键词
      if (route.query.searchTitle) {
        keyword.value = route.query.searchTitle
        console.log('设置搜索关键词:', keyword.value)
      }
      
      // 显示提示信息
      setTimeout(() => {
        const bookInfo = route.query.searchAuthor 
          ? `《${route.query.searchTitle}》 - ${route.query.searchAuthor}`
          : `《${route.query.searchTitle}》`
        alert(`正在为您搜索 ${bookInfo} 的求书信息...`)
      }, 500)
    }
    
    // 获取用户信息
    try {
      const userRes = await axios.get('/api/user/info')
      console.log('用户信息响应:', userRes.data)
      if (userRes.data.code === 200) {
        user.value = userRes.data.data
        console.log('用户信息获取成功:', user.value)
      }
    } catch (error) {
      console.error('用户信息获取失败:', error)
    }
    
    await fetchBooks()
    console.log('AskBookStore页面初始化完成')
  } catch (error) {
    console.error('AskBookStore页面初始化失败:', error)
  }
})
</script>

<style scoped>
.modern-ask-store {
  min-height: 100vh;
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
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
  background: linear-gradient(135deg, #f093fb, #f5576c);
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
  border-color: #f5576c;
  box-shadow: 0 0 0 3px rgba(245, 87, 108, 0.1);
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
  color: #f5576c;
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

.nav-link:hover,
.nav-link.active {
  color: #f5576c;
  background: rgba(245, 87, 108, 0.1);
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
  background: linear-gradient(135deg, #f093fb, #f5576c);
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
  color: #f5576c;
  background: rgba(245, 87, 108, 0.1);
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

/* 英雄区域 */
.hero-section {
  padding: 3rem 2rem;
  text-align: center;
  color: white;
}

.hero-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 3rem;
}

.hero-text {
  flex: 1;
  text-align: left;
}

.hero-title {
  font-size: 3rem;
  font-weight: 800;
  margin-bottom: 1rem;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.hero-subtitle {
  font-size: 1.25rem;
  margin-bottom: 2rem;
  opacity: 0.9;
}

.hero-action-btn {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  text-decoration: none;
  padding: 1rem 2rem;
  border-radius: 12px;
  font-weight: 600;
  border: 2px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s ease;
}

.hero-action-btn:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
}

.hero-stats {
  display: flex;
  gap: 2rem;
}

.stat-card {
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 2rem;
  text-align: center;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.stat-number {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
}

.stat-label {
  font-size: 1rem;
  opacity: 0.8;
}

/* 主要内容区域 */
.main-content {
  padding: 2rem;
}

.content-container {
  max-width: 1200px;
  margin: 0 auto;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  overflow: hidden;
}

.content-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 2rem 2rem 1rem 2rem;
  background: linear-gradient(135deg, #f093fb, #f5576c);
  color: white;
}

.section-title {
  font-size: 2rem;
  font-weight: 700;
  margin: 0 0 0.5rem 0;
}

.section-subtitle {
  font-size: 1.125rem;
  margin: 0;
  opacity: 0.9;
}

.header-actions {
  display: flex;
  gap: 1rem;
}

.add-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  text-decoration: none;
  padding: 0.75rem 1.5rem;
  border-radius: 12px;
  font-weight: 600;
  border: 1px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s ease;
}

.add-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  color: #64748b;
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
}

.empty-state h3 {
  font-size: 1.5rem;
  color: #1a202c;
  margin-bottom: 1rem;
}

.empty-state p {
  font-size: 1.125rem;
  margin-bottom: 2rem;
}

.empty-action-btn {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  background: linear-gradient(135deg, #f093fb, #f5576c);
  color: white;
  text-decoration: none;
  padding: 1rem 2rem;
  border-radius: 12px;
  font-weight: 600;
  transition: all 0.3s ease;
}

.empty-action-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 20px rgba(245, 87, 108, 0.3);
}

/* 求书信息列表 */
.ask-list {
  padding: 2rem;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.ask-item {
  display: flex;
  gap: 1.5rem;
  background: white;
  border-radius: 16px;
  padding: 1.5rem;
  border: 1px solid #e2e8f0;
  transition: all 0.3s ease;
}

.ask-item:hover {
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.ask-image {
  position: relative;
  flex-shrink: 0;
}

.book-cover {
  width: 120px;
  height: 160px;
  object-fit: cover;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.ask-badge {
  position: absolute;
  top: -8px;
  right: -8px;
  background: linear-gradient(135deg, #f093fb, #f5576c);
  color: white;
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
}

.ask-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.ask-header {
  border-bottom: 1px solid #e2e8f0;
  padding-bottom: 1rem;
}

.book-title {
  font-size: 1.25rem;
  font-weight: 700;
  color: #1a202c;
  margin: 0 0 0.5rem 0;
}

.book-meta {
  display: flex;
  gap: 1rem;
  color: #64748b;
  font-size: 0.875rem;
}

.book-price {
  color: #ef4444;
  font-weight: 600;
}

.ask-description {
  flex: 1;
}

.ask-description p {
  color: #64748b;
  line-height: 1.6;
  margin: 0;
}

.ask-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.publisher-info {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.publisher-name {
  font-weight: 600;
  color: #2d3748;
}

.publish-time {
  font-size: 0.875rem;
  color: #64748b;
}

.ask-actions {
  display: flex;
  gap: 0.75rem;
}

.contact-btn,
.detail-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  font-size: 0.875rem;
  font-weight: 500;
  text-decoration: none;
  transition: all 0.3s ease;
}

.contact-btn {
  background: linear-gradient(135deg, #f093fb, #f5576c);
  color: white;
  border: none;
  cursor: pointer;
}

.contact-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(245, 87, 108, 0.3);
}

.detail-btn {
  background: rgba(245, 87, 108, 0.1);
  color: #f5576c;
  border: 1px solid #f5576c;
}

.detail-btn:hover {
  background: #f5576c;
  color: white;
}

/* 分页控制 */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
  padding: 2rem;
  border-top: 1px solid #e2e8f0;
}

.page-btn {
  padding: 0.75rem 1.5rem;
  border: 2px solid #f5576c;
  background: white;
  color: #f5576c;
  border-radius: 12px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s ease;
}

.page-btn:hover:not(:disabled) {
  background: #f5576c;
  color: white;
  transform: translateY(-2px);
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  color: #64748b;
  font-weight: 500;
  padding: 0 1rem;
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
  color: #f5576c;
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
  
  .hero-content {
    flex-direction: column;
    text-align: center;
  }
  
  .hero-title {
    font-size: 2rem;
  }
  
  .content-header {
    flex-direction: column;
    gap: 1rem;
    text-align: center;
  }
  
  .ask-item {
    flex-direction: column;
    text-align: center;
  }
  
  .ask-footer {
    flex-direction: column;
    gap: 1rem;
    text-align: center;
  }
  
  .search-input {
    width: 100%;
    max-width: 300px;
  }
}
</style> 