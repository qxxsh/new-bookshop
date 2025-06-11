<template>
  <div class="modern-bookshelf">
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
          <router-link to="/bookstore" class="nav-link">书籍良品</router-link>
          <router-link to="/askBookStore" class="nav-link">求书区</router-link>
          <router-link to="/myBookshelf" class="nav-link active">我的书架</router-link>
        </div>
        
        <div class="nav-user">
          <div class="user-info">
            <span class="user-avatar">{{ user?.name?.charAt(0) || 'U' }}</span>
            <span class="user-name">{{ user?.name || '用户' }}</span>
          </div>
          <button @click="logout" class="logout-btn">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
              <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" stroke="currentColor" stroke-width="2"/>
              <polyline points="16,17 21,12 16,7" stroke="currentColor" stroke-width="2"/>
              <line x1="21" y1="12" x2="9" y2="12" stroke="currentColor" stroke-width="2"/>
            </svg>
          </button>
        </div>
      </nav>
    </header>

    <!-- 主要内容区域 -->
    <main class="main-content">
      <!-- 个人信息卡片 -->
      <section class="profile-section">
        <div class="profile-card" v-if="user">
          <div class="profile-header">
            <div class="avatar-section">
              <div class="large-avatar">{{ user.name?.charAt(0) || 'U' }}</div>
              <img :src="user.sex === 0 ? boy : girl" class="gender-icon" alt="性别图标"/>
            </div>
            <div class="profile-info">
              <h2 class="profile-name">{{ user.name }}</h2>
              <p class="profile-gender">{{ user.sex === 0 ? '男' : '女' }}</p>
              <button @click="editProfile" class="edit-profile-btn">
                <img :src="setting" class="setting-icon" alt="设置"/>
                编辑个人主页
              </button>
            </div>
          </div>
          
          <div class="contact-info">
            <div class="contact-item">
              <div class="contact-icon">📞</div>
              <div class="contact-detail">
                <span class="contact-label">联系电话</span>
                <span class="contact-value">{{ user.tel || '未设置' }}</span>
              </div>
            </div>
            <div class="contact-item">
              <div class="contact-icon">📍</div>
              <div class="contact-detail">
                <span class="contact-label">住址</span>
                <span class="contact-value">{{ user.address || '未设置' }}</span>
              </div>
            </div>
            <div class="contact-item">
              <div class="contact-icon">🎓</div>
              <div class="contact-detail">
                <span class="contact-label">年级专业</span>
                <span class="contact-value">{{ user.major || '未设置' }}</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 书籍管理区域 -->
      <section class="books-section">
        <!-- 选项卡导航 -->
        <div class="tab-navigation">
          <button 
            :class="['tab-btn', { active: activeTab === 'sell' }]" 
            @click="activeTab = 'sell'"
          >
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path d="M2 3h6l2 4h9l-3 7H6l-2-4M6 21h12" stroke="currentColor" stroke-width="2"/>
            </svg>
            我的书摊
            <span class="tab-count">{{ sellBooks.length }}</span>
          </button>
          <button 
            :class="['tab-btn', { active: activeTab === 'ask' }]" 
            @click="activeTab = 'ask'"
          >
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z" stroke="currentColor" stroke-width="2"/>
              <path d="M9 9h6M9 13h3" stroke="currentColor" stroke-width="2"/>
            </svg>
            我的求书
            <span class="tab-count">{{ askBooks.length }}</span>
          </button>
          
          <div class="tab-actions">
            <button @click="goUploadSell" class="action-btn primary" v-if="activeTab === 'sell'">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                <path d="M12 5v14M5 12h14" stroke="currentColor" stroke-width="2"/>
              </svg>
              上传书籍
            </button>
            <button @click="goUploadAsk" class="action-btn primary" v-if="activeTab === 'ask'">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                <path d="M12 5v14M5 12h14" stroke="currentColor" stroke-width="2"/>
              </svg>
              发布求书
            </button>
          </div>
        </div>

        <!-- 书籍列表 -->
        <div class="books-content">
          <!-- 我的书摊 -->
          <div v-if="activeTab === 'sell'" class="books-grid">
            <div v-if="sellBooks.length === 0" class="empty-state">
              <div class="empty-icon">📚</div>
              <h3>还没有上传任何书籍</h3>
              <p>快来上传你的闲置图书，让知识传递下去吧！</p>
              <button @click="goUploadSell" class="empty-action-btn">
                立即上传
              </button>
            </div>
            
            <div v-for="book in sellBooks" :key="book.id" class="book-card">
              <div class="book-cover">
                <img 
                  :src="`/img/book-list/article/${book.bookImage?.id || 'default'}.jpg`" 
                  @error="$event.target.src='/img/book-list/article/default.jpg'"
                  :alt="book.name" 
                  class="cover-image"
                />
                <div class="book-actions">
                  <button @click="goEditBook(book.id)" class="action-icon edit" title="编辑">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                      <path d="M12 20h9M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z" stroke="currentColor" stroke-width="2"/>
                    </svg>
                  </button>
                  <button @click="deleteItem(book.id, 1)" class="action-icon delete" title="删除">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                      <path d="M3 6h18M8 6V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2M19 6l-1 14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2L5 6" stroke="currentColor" stroke-width="2"/>
                    </svg>
                  </button>
                </div>
              </div>
              
              <div class="book-info">
                <h4 class="book-title">{{ book.name }}</h4>
                <div class="book-meta">
                  <div class="price-info">
                    <span class="current-price">¥{{ book.price }}</span>
                    <span class="original-price">原价 ¥{{ book.originalPrice }}</span>
                  </div>
                  <span class="condition-badge">{{ book.degree }}成新</span>
                </div>
                <p class="book-description">{{ book.description || '暂无描述' }}</p>
              </div>
            </div>
          </div>

          <!-- 我的求书 -->
          <div v-if="activeTab === 'ask'" class="books-grid">
            <div v-if="askBooks.length === 0" class="empty-state">
              <div class="empty-icon">🔍</div>
              <h3>还没有发布任何求书信息</h3>
              <p>快来发布你想要的图书信息，让其他同学帮你找到它！</p>
              <button @click="goUploadAsk" class="empty-action-btn">
                立即发布
              </button>
            </div>
            
            <div v-for="book in askBooks" :key="book.id" class="book-card ask-card">
              <div class="book-cover">
                <img 
                  :src="`/img/book-list/article/${book.bookImage?.id || 'default'}.jpg`" 
                  @error="$event.target.src='/img/book-list/article/default.jpg'"
                  :alt="book.name" 
                  class="cover-image"
                />
                <div class="book-actions">
                  <button @click="goEditBook(book.id)" class="action-icon edit" title="编辑">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                      <path d="M12 20h9M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z" stroke="currentColor" stroke-width="2"/>
                    </svg>
                  </button>
                  <button @click="deleteItem(book.id, 0)" class="action-icon delete" title="删除">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                      <path d="M3 6h18M8 6V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2M19 6l-1 14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2L5 6" stroke="currentColor" stroke-width="2"/>
                    </svg>
                  </button>
                </div>
                <div class="ask-badge">求书</div>
              </div>
              
              <div class="book-info">
                <h4 class="book-title">{{ book.name }}</h4>
                <div class="book-meta">
                  <span class="author">作者：{{ book.author || '未知' }}</span>
                  <span class="press">出版社：{{ book.press || '未知' }}</span>
                </div>
                <p class="book-description">{{ book.description || '暂无描述' }}</p>
              </div>
            </div>
          </div>
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
import boy from '/img/boy.png'
import girl from '/img/girl.png'
import setting from '/img/setting.png'

const router = useRouter()
const user = ref(null)
const sellBooks = ref([])
const askBooks = ref([])
const activeTab = ref('sell')
const keyword = ref('')

async function fetchUser() {
  try {
    const res = await axios.get('/api/user/info')
    if (res.data.code === 200) {
      user.value = res.data.data
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

async function fetchSellBooks() {
  try {
    const res = await axios.get('/api/book/list', { 
      params: { pageNum: 1, pageSize: 20, bookType: 1, userId: user.value?.id } 
    })
    if (res.data.code === 200) {
      sellBooks.value = res.data.data.records || []
    }
  } catch (error) {
    console.error('获取书摊数据失败:', error)
    sellBooks.value = []
  }
}

async function fetchAskBooks() {
  try {
    console.log('=== 获取我的求书信息调试 ===')
    console.log('当前用户ID:', user.value?.id)
    console.log('请求参数:', { pageNum: 1, pageSize: 20, bookType: 0, userId: user.value?.id })
    
    const res = await axios.get('/api/book/list', { 
      params: { pageNum: 1, pageSize: 20, bookType: 0, userId: user.value?.id } 
    })
    
    console.log('我的求书API响应:', res.data)
    
    if (res.data.code === 200) {
      askBooks.value = res.data.data.records || []
      console.log('我的求书数据:', askBooks.value)
      console.log('我的求书数量:', askBooks.value.length)
    } else {
      console.error('获取我的求书数据失败:', res.data.message)
      askBooks.value = []
    }
  } catch (error) {
    console.error('获取求书数据失败:', error)
    askBooks.value = []
  }
}

function goUploadSell() {
  router.push('/uploadSell')
}

function goUploadAsk() {
  router.push('/uploadAsk')
}

function goEditBook(id) {
  router.push(`/editBook/${id}`)
}

async function deleteItem(id, type) {
  try {
    const result = confirm('确定要删除这本书吗？')
    if (!result) return
    
    await axios.delete(`/api/book/${id}`)
    alert('删除成功！')
    
    if (type === 1) {
      await fetchSellBooks()
    } else {
      await fetchAskBooks()
    }
  } catch (error) {
    console.error('删除失败:', error)
    alert('删除失败，请重试')
  }
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

function editProfile() {
  // 编辑个人资料功能
  router.push('/profile/edit')
}

onMounted(async () => {
  await fetchUser()
  if (user.value) {
    await Promise.all([fetchSellBooks(), fetchAskBooks()])
  }
})
</script>

<style scoped>
.modern-bookshelf {
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
  max-width: 1400px;
  margin: 0 auto;
  padding: 2rem;
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

/* 个人信息卡片 */
.profile-section {
  margin-bottom: 2rem;
}

.profile-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 2rem;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.profile-header {
  display: flex;
  align-items: center;
  gap: 2rem;
  margin-bottom: 2rem;
}

.avatar-section {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.large-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 2rem;
  box-shadow: 0 10px 30px rgba(102, 126, 234, 0.3);
}

.gender-icon {
  position: absolute;
  bottom: -5px;
  right: -5px;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background: white;
  padding: 4px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.profile-info {
  flex: 1;
}

.profile-name {
  font-size: 2rem;
  font-weight: 700;
  color: #1a202c;
  margin: 0 0 0.5rem 0;
}

.profile-gender {
  color: #64748b;
  font-size: 1.125rem;
  margin: 0 0 1rem 0;
}

.edit-profile-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 50px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.edit-profile-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
}

.setting-icon {
  width: 16px;
  height: 16px;
}

.contact-info {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  background: rgba(102, 126, 234, 0.05);
  border-radius: 12px;
  border: 1px solid rgba(102, 126, 234, 0.1);
}

.contact-icon {
  font-size: 1.5rem;
}

.contact-detail {
  display: flex;
  flex-direction: column;
}

.contact-label {
  font-size: 0.875rem;
  color: #64748b;
  margin-bottom: 0.25rem;
}

.contact-value {
  font-weight: 600;
  color: #1a202c;
}

/* 书籍管理区域 */
.books-section {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 2rem;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.tab-navigation {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 2rem;
  border-bottom: 2px solid #f1f5f9;
  padding-bottom: 1rem;
}

.tab-btn {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  background: none;
  border: none;
  padding: 1rem 1.5rem;
  border-radius: 12px;
  font-weight: 600;
  color: #64748b;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
}

.tab-btn.active {
  color: #667eea;
  background: rgba(102, 126, 234, 0.1);
}

.tab-btn:hover {
  color: #667eea;
  background: rgba(102, 126, 234, 0.05);
}

.tab-count {
  background: #667eea;
  color: white;
  font-size: 0.75rem;
  padding: 0.25rem 0.5rem;
  border-radius: 50px;
  min-width: 20px;
  text-align: center;
}

.tab-actions {
  display: flex;
  gap: 1rem;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.action-btn.primary {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
}

.action-btn.primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
}

/* 书籍网格 */
.books-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1.5rem;
}

.empty-state {
  grid-column: 1 / -1;
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
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  border: none;
  padding: 1rem 2rem;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.empty-action-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
}

.book-card {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  border: 1px solid #f1f5f9;
}

.book-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
}

.book-cover {
  position: relative;
  height: 200px;
  overflow: hidden;
  background: #f8f9fa;
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

.book-actions {
  position: absolute;
  top: 0.75rem;
  right: 0.75rem;
  display: flex;
  gap: 0.5rem;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.book-card:hover .book-actions {
  opacity: 1;
}

.action-icon {
  width: 36px;
  height: 36px;
  border: none;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.action-icon.edit {
  background: rgba(34, 197, 94, 0.9);
  color: white;
}

.action-icon.edit:hover {
  background: #22c55e;
  transform: scale(1.1);
}

.action-icon.delete {
  background: rgba(239, 68, 68, 0.9);
  color: white;
}

.action-icon.delete:hover {
  background: #ef4444;
  transform: scale(1.1);
}

.ask-badge {
  position: absolute;
  top: 0.75rem;
  left: 0.75rem;
  background: linear-gradient(135deg, #f59e0b, #d97706);
  color: white;
  padding: 0.25rem 0.75rem;
  border-radius: 50px;
  font-size: 0.75rem;
  font-weight: 600;
}

.book-info {
  padding: 1.5rem;
}

.book-title {
  font-size: 1.125rem;
  font-weight: 700;
  color: #1a202c;
  margin: 0 0 1rem 0;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.book-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.price-info {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
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
}

.condition-badge {
  background: linear-gradient(135deg, #10b981, #059669);
  color: white;
  padding: 0.25rem 0.75rem;
  border-radius: 50px;
  font-size: 0.75rem;
  font-weight: 600;
}

.book-description {
  color: #64748b;
  font-size: 0.875rem;
  line-height: 1.5;
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.ask-card .book-meta {
  flex-direction: column;
  align-items: flex-start;
  gap: 0.5rem;
}

.author,
.press {
  color: #64748b;
  font-size: 0.875rem;
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
    padding: 1rem;
  }
  
  .profile-header {
    flex-direction: column;
    text-align: center;
  }
  
  .tab-navigation {
    flex-direction: column;
    gap: 1rem;
    align-items: stretch;
  }
  
  .books-grid {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 1rem;
  }
  
  .modern-search-input {
    width: 100%;
    max-width: 250px;
  }
  
  .contact-info {
    grid-template-columns: 1fr;
  }
}
</style> 