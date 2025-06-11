<template>
  <div class="modern-home">
    <!-- 现代化导航栏 -->
    <header class="home-header">
      <nav class="home-navbar">
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
          <router-link to="/home" class="nav-link active">首页</router-link>
          <router-link to="/bookstore" class="nav-link">书籍良品</router-link>
          <router-link to="/askBookStore" class="nav-link">求书区</router-link>
        </div>
        
        <div class="nav-user">
          <span class="user-avatar">{{ user?.name?.charAt(0) || 'U' }}</span>
          <span class="user-name">{{ user?.name || '用户' }}</span>
          <div class="user-actions">
            <router-link to="/myBookshelf" class="action-btn">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
                <path d="M4 19.5v-15A2.5 2.5 0 0 1 6.5 2H20v20H6.5a2.5 2.5 0 0 1 0-5H20" stroke="currentColor" stroke-width="2"/>
              </svg>
            </router-link>
            <router-link to="/cart" class="action-btn">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
                <path d="m2 2 3.6 7.4a2 2 0 0 0 1.8 1.1h9.2a2 2 0 0 0 1.9-1.4L21 4H6" stroke="currentColor" stroke-width="2"/>
                <circle cx="9" cy="21" r="1" stroke="currentColor" stroke-width="2"/>
                <circle cx="20" cy="21" r="1" stroke="currentColor" stroke-width="2"/>
              </svg>
            </router-link>
            <button @click="logout" class="action-btn logout-btn">
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
          <h1 class="hero-title">
            <span class="title-line">发现好书</span>
            <span class="title-line">分享知识</span>
          </h1>
          <p class="hero-subtitle">
            校园二手书交易平台，让知识传递，让书香延续
          </p>
          <div class="hero-stats">
            <div class="stat-item">
              <span class="stat-number">{{ categories.length }}</span>
              <span class="stat-label">图书分类</span>
            </div>
            <div class="stat-item">
              <span class="stat-number">{{ getTotalBooks() }}</span>
              <span class="stat-label">在售图书</span>
            </div>
            <div class="stat-item">
              <span class="stat-number">1000+</span>
              <span class="stat-label">活跃用户</span>
            </div>
          </div>
          <div class="hero-actions">
            <router-link to="/bookstore" class="hero-btn primary">
              开始购书
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                <path d="M5 12h14M12 5l7 7-7 7" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              </svg>
            </router-link>
            <router-link to="/askBookStore" class="hero-btn secondary">
              发布求书
            </router-link>
          </div>
        </div>
        
        <div class="hero-visual">
          <div class="floating-books">
            <div class="book-card book-1">📖</div>
            <div class="book-card book-2">📚</div>
            <div class="book-card book-3">📘</div>
            <div class="book-card book-4">📙</div>
            <div class="book-card book-5">📗</div>
          </div>
        </div>
      </div>
    </section>

    <!-- 特色功能 -->
    <section class="features-section">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">平台特色</h2>
          <p class="section-subtitle">为您提供最优质的二手书交易体验</p>
        </div>
        
        <div class="features-grid">
          <div class="feature-card">
            <div class="feature-icon">🛡️</div>
            <h3>安全可靠</h3>
            <p>校园实名认证，交易安全有保障</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">💰</div>
            <h3>价格优惠</h3>
            <p>比原价优惠30%-70%，让知识更实惠</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">📱</div>
            <h3>便捷交易</h3>
            <p>线上下单，线下交易，便捷高效</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">🎯</div>
            <h3>精准匹配</h3>
            <p>智能推荐，快速找到所需图书</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 分类导航 -->
    <section class="categories-section">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">图书分类</h2>
          <p class="section-subtitle">按分类浏览，快速找到心仪图书</p>
        </div>
        
        <div class="categories-grid">
          <div 
            v-for="cat in categories" 
            :key="cat.id" 
            class="category-card"
            @click="scrollToPart(cat.id)"
          >
            <div class="category-icon">{{ getCategoryIcon(cat.name) }}</div>
            <h3 class="category-name">{{ cat.name }}</h3>
            <p class="category-count">{{ getCategoryBookCount(cat.id) }} 本在售</p>
            <div class="category-arrow">→</div>
          </div>
        </div>
      </div>
    </section>

    <!-- 现代化页脚 -->
    <footer class="modern-footer">
      <div class="container">
        <div class="footer-content">
          <div class="footer-section">
            <h4>二手书院</h4>
            <p>让知识传递，让书香延续</p>
            <div class="social-links">
              <a href="#" class="social-link">📧</a>
              <a href="#" class="social-link">📱</a>
              <a href="#" class="social-link">🌐</a>
            </div>
          </div>
          <div class="footer-section">
            <h4>快速链接</h4>
            <router-link to="/bookstore">图书商城</router-link>
            <router-link to="/askBookStore">求书广场</router-link>
          </div>
          <div class="footer-section">
            <h4>帮助支持</h4>
            <a href="#">使用指南</a>
            <a href="#">交易规则</a>
            <a href="#">联系我们</a>
          </div>
        </div>
        <div class="footer-bottom">
          <p>&copy; 2024 二手书院. 保留所有权利.</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

// 导入图片资源
import logo2 from '/img/logo2.jpg'

const router = useRouter()
const user = ref(null)
const categories = ref([])
const keyword = ref('')

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

function scrollToPart(id) {
  router.push({ path: '/bookstore', query: { categoryId: id } })
}

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

function search() {
  router.push({ path: '/bookstore', query: { keyword: keyword.value } })
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

function getCategoryBookCount(categoryId) {
  return '多本'
}

function getTotalBooks() {
  return '1000+'
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
  } catch (error) {
    console.error('页面初始化失败:', error)
  }
})
</script>

<style scoped>
/* 现代化首页样式 */
.modern-home {
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  line-height: 1.6;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
}

.home-header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.home-navbar {
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

.hero-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  min-height: 100vh;
  display: flex;
  align-items: center;
  position: relative;
  overflow: hidden;
}

.hero-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
  width: 100%;
}

.hero-text {
  flex: 1;
  max-width: 600px;
}

.hero-title {
  font-size: 4rem;
  font-weight: 800;
  color: white;
  margin-bottom: 1.5rem;
  line-height: 1.1;
}

.title-line {
  display: block;
  background: linear-gradient(45deg, #ffffff, #f1f5f9);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-subtitle {
  font-size: 1.25rem;
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 2rem;
  line-height: 1.6;
}

.hero-stats {
  display: flex;
  gap: 3rem;
  margin-bottom: 3rem;
}

.stat-item {
  text-align: center;
}

.stat-number {
  display: block;
  font-size: 2.5rem;
  font-weight: 700;
  color: white;
  margin-bottom: 0.5rem;
}

.stat-label {
  color: rgba(255, 255, 255, 0.8);
  font-size: 0.875rem;
  font-weight: 500;
}

.hero-actions {
  display: flex;
  gap: 1rem;
}

.hero-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 1rem 2rem;
  border-radius: 50px;
  font-weight: 600;
  text-decoration: none;
  transition: all 0.3s ease;
  font-size: 1.1rem;
}

.hero-btn.primary {
  background: white;
  color: #667eea;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.hero-btn.primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.3);
}

.hero-btn.secondary {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 2px solid rgba(255, 255, 255, 0.3);
}

.hero-btn.secondary:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
}

.search-container {
  position: relative;
  display: flex;
  align-items: center;
}

.search-input {
  width: 300px;
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

.action-btn {
  display: flex;
  align-items: center;
  padding: 0.5rem;
  border: none;
  background: none;
  color: #64748b;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  text-decoration: none;
}

.action-btn:hover {
  color: #667eea;
  background: rgba(102, 126, 234, 0.1);
}

.logout-btn:hover {
  color: #ef4444;
  background: rgba(239, 68, 68, 0.1);
}

.floating-books {
  position: relative;
  width: 400px;
  height: 400px;
}

.book-card {
  position: absolute;
  width: 80px;
  height: 80px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 2rem;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  animation: float 6s ease-in-out infinite;
}

.book-1 { top: 20%; left: 10%; animation-delay: 0s; }
.book-2 { top: 10%; right: 20%; animation-delay: 1s; }
.book-3 { bottom: 30%; left: 20%; animation-delay: 2s; }
.book-4 { bottom: 10%; right: 10%; animation-delay: 3s; }
.book-5 { top: 50%; right: 50%; animation-delay: 4s; }

@keyframes float {
  0%, 100% { transform: translateY(0px) rotate(0deg); }
  33% { transform: translateY(-20px) rotate(5deg); }
  66% { transform: translateY(10px) rotate(-3deg); }
}

.features-section {
  padding: 6rem 0;
  background: #f8fafc;
}

.section-header {
  text-align: center;
  margin-bottom: 4rem;
}

.section-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #1a202c;
  margin-bottom: 1rem;
}

.section-subtitle {
  font-size: 1.125rem;
  color: #64748b;
  max-width: 600px;
  margin: 0 auto;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
}

.feature-card {
  background: white;
  padding: 2.5rem 2rem;
  border-radius: 20px;
  text-align: center;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 20px 50px rgba(0, 0, 0, 0.15);
}

.feature-icon {
  font-size: 3rem;
  margin-bottom: 1.5rem;
}

.feature-card h3 {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1a202c;
  margin-bottom: 1rem;
}

.feature-card p {
  color: #64748b;
  line-height: 1.6;
}

.categories-section {
  padding: 6rem 0;
  background: white;
}

.categories-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1.5rem;
}

.category-card {
  background: linear-gradient(135deg, #f8fafc, #ffffff);
  padding: 2rem;
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
  position: relative;
  overflow: hidden;
}

.category-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(102, 126, 234, 0.15);
  border-color: #667eea;
}

.category-icon {
  font-size: 2.5rem;
  margin-bottom: 1rem;
}

.category-name {
  font-size: 1.125rem;
  font-weight: 600;
  color: #1a202c;
  margin-bottom: 0.5rem;
}

.category-count {
  color: #64748b;
  font-size: 0.875rem;
  margin-bottom: 1rem;
}

.category-arrow {
  color: #667eea;
  font-size: 1.5rem;
  font-weight: 600;
  opacity: 0;
  transform: translateX(-10px);
  transition: all 0.3s ease;
}

.category-card:hover .category-arrow {
  opacity: 1;
  transform: translateX(0);
}

.modern-footer {
  background: linear-gradient(135deg, #1a202c, #2d3748);
  color: white;
  padding: 3rem 0 1.5rem;
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
  margin-bottom: 2rem;
}

.footer-section h4 {
  font-size: 1.125rem;
  font-weight: 700;
  margin-bottom: 1rem;
  color: #f1f5f9;
}

.footer-section p {
  color: #94a3b8;
  margin-bottom: 1rem;
  line-height: 1.6;
}

.social-links {
  display: flex;
  gap: 1rem;
  margin-top: 1rem;
}

.social-link {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  text-decoration: none;
  font-size: 1.25rem;
  transition: all 0.3s ease;
}

.social-link:hover {
  background: rgba(102, 126, 234, 0.3);
  transform: translateY(-2px);
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
  padding-top: 1.5rem;
  text-align: center;
}

.footer-bottom p {
  color: #64748b;
  margin: 0;
}

@media (max-width: 768px) {
  .home-navbar {
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
    gap: 3rem;
  }
  
  .hero-title {
    font-size: 2.5rem;
  }
  
  .hero-stats {
    justify-content: center;
  }
  
  .search-input {
    width: 100%;
    max-width: 250px;
  }
  
  .books-grid {
    grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  }
}
</style> 