<template>
  <div class="modern-profile-edit">
    <!-- 现代化导航栏 -->
    <header class="modern-header">
      <nav class="modern-navbar">
        <div class="nav-brand">
          <router-link to="/home" class="brand-link">
            <img class="brand-logo" :src="logo2" alt="书院" />
            <span class="brand-text">二手书院</span>
          </router-link>
        </div>
        
        <div class="nav-menu">
          <router-link to="/home" class="nav-link">首页</router-link>
          <router-link to="/bookstore" class="nav-link">书籍良品</router-link>
          <router-link to="/askBookStore" class="nav-link">求书区</router-link>
          <router-link to="/myBookshelf" class="nav-link">我的书架</router-link>
        </div>
        
        <div class="nav-user">
          <router-link to="/myBookshelf" class="back-btn">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
              <path d="M19 12H5M12 19l-7-7 7-7" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            返回书架
          </router-link>
        </div>
      </nav>
    </header>

    <!-- 主要内容区域 -->
    <main class="main-content">
      <div class="edit-container">
        <!-- 页面标题 -->
        <div class="page-header">
          <div class="header-icon">
            <svg width="48" height="48" viewBox="0 0 24 24" fill="none">
              <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" stroke="currentColor" stroke-width="2"/>
              <circle cx="12" cy="7" r="4" stroke="currentColor" stroke-width="2"/>
            </svg>
          </div>
          <div class="header-text">
            <h1>编辑个人资料</h1>
            <p>完善你的个人信息，让其他用户更好地认识你</p>
          </div>
        </div>

        <!-- 编辑表单 -->
        <form @submit.prevent="handleSubmit" class="edit-form">
          <div class="form-sections">
            <!-- 基本信息 -->
            <div class="form-section">
              <h3 class="section-title">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                  <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" stroke="currentColor" stroke-width="2"/>
                  <circle cx="12" cy="7" r="4" stroke="currentColor" stroke-width="2"/>
                </svg>
                基本信息
              </h3>
              <div class="form-grid">
                <div class="form-group">
                  <label class="form-label">
                    <span class="label-text">姓名 *</span>
                    <span class="label-icon">👤</span>
                  </label>
                  <input 
                    v-model="form.name" 
                    type="text" 
                    class="form-input"
                    placeholder="请输入您的姓名"
                    required
                  />
                </div>
                
                <div class="form-group">
                  <label class="form-label">
                    <span class="label-text">性别</span>
                    <span class="label-icon">⚥</span>
                  </label>
                  <select v-model="form.sex" class="form-select">
                    <option value="0">男</option>
                    <option value="1">女</option>
                  </select>
                </div>
              </div>
            </div>

            <!-- 联系信息 -->
            <div class="form-section">
              <h3 class="section-title">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                  <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z" stroke="currentColor" stroke-width="2"/>
                </svg>
                联系信息
              </h3>
              <div class="form-grid">
                <div class="form-group">
                  <label class="form-label">
                    <span class="label-text">联系电话</span>
                    <span class="label-icon">📞</span>
                  </label>
                  <input 
                    v-model="form.tel" 
                    type="tel" 
                    class="form-input"
                    placeholder="请输入联系电话"
                  />
                </div>
                
                <div class="form-group">
                  <label class="form-label">
                    <span class="label-text">地址</span>
                    <span class="label-icon">📍</span>
                  </label>
                  <input 
                    v-model="form.address" 
                    type="text" 
                    class="form-input"
                    placeholder="请输入地址信息"
                  />
                </div>
              </div>
            </div>

            <!-- 学业信息 -->
            <div class="form-section">
              <h3 class="section-title">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                  <path d="M22 10v6M2 10l10-5 10 5-10 5z" stroke="currentColor" stroke-width="2"/>
                  <path d="M6 12v5c3 3 9 3 12 0v-5" stroke="currentColor" stroke-width="2"/>
                </svg>
                学业信息
              </h3>
              <div class="form-group">
                <label class="form-label">
                  <span class="label-text">年级专业</span>
                  <span class="label-icon">🎓</span>
                </label>
                <input 
                  v-model="form.major" 
                  type="text" 
                  class="form-input"
                  placeholder="例如：计算机科学与技术2021级"
                />
              </div>
            </div>
          </div>

          <!-- 操作按钮 -->
          <div class="form-actions">
            <button type="button" @click="goBack" class="btn-cancel">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                <path d="M19 12H5M12 19l-7-7 7-7" stroke="currentColor" stroke-width="2"/>
              </svg>
              取消
            </button>
            <button type="submit" class="btn-submit" :disabled="isSubmitting">
              <svg v-if="!isSubmitting" width="16" height="16" viewBox="0 0 24 24" fill="none">
                <path d="M20 6L9 17l-5-5" stroke="currentColor" stroke-width="2"/>
              </svg>
              <svg v-else width="16" height="16" viewBox="0 0 24 24" fill="none">
                <circle cx="12" cy="12" r="3" stroke="currentColor" stroke-width="2"/>
              </svg>
              {{ isSubmitting ? '保存中...' : '保存修改' }}
            </button>
          </div>
        </form>
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
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

// 导入图片资源
import logo2 from '/img/logo2.jpg'

const router = useRouter()
const isSubmitting = ref(false)

const form = ref({
  name: '',
  sex: 0,
  tel: '',
  address: '',
  major: ''
})

async function fetchUserInfo() {
  try {
    const res = await axios.get('/api/user/info')
    if (res.data.code === 200) {
      const userData = res.data.data
      form.value = {
        name: userData.name || '',
        sex: userData.sex || 0,
        tel: userData.tel || '',
        address: userData.address || '',
        major: userData.major || ''
      }
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

function goBack() {
  router.back()
}

const handleSubmit = async () => {
  if (!form.value.name) {
    alert('请输入姓名！')
    return
  }

  isSubmitting.value = true
  
  try {
    const res = await axios.put('/api/user/update', form.value)
    if (res.data.code === 200) {
      alert('个人资料更新成功！')
      router.push('/myBookshelf')
    } else {
      alert(res.data.message || '更新失败，请重试')
    }
  } catch (error) {
    console.error('更新失败:', error)
    alert('更新失败，请重试')
  } finally {
    isSubmitting.value = false
  }
}

onMounted(fetchUserInfo)
</script>

<style scoped>
.modern-profile-edit {
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

.back-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  text-decoration: none;
  color: #64748b;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.back-btn:hover {
  color: #667eea;
  background: rgba(102, 126, 234, 0.1);
}

/* 主要内容区域 */
.main-content {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem;
}

.edit-container {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  overflow: hidden;
}

/* 页面标题 */
.page-header {
  display: flex;
  align-items: center;
  gap: 1.5rem;
  padding: 2rem 2rem 1rem 2rem;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
}

.header-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 80px;
  height: 80px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 16px;
  backdrop-filter: blur(10px);
}

.header-text h1 {
  font-size: 2rem;
  font-weight: 700;
  margin: 0 0 0.5rem 0;
}

.header-text p {
  font-size: 1.125rem;
  margin: 0;
  opacity: 0.9;
}

/* 表单样式 */
.edit-form {
  padding: 2rem;
}

.form-sections {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.form-section {
  background: #f8f9fa;
  border-radius: 16px;
  padding: 2rem;
  border: 1px solid #e2e8f0;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  font-size: 1.25rem;
  font-weight: 700;
  color: #1a202c;
  margin: 0 0 1.5rem 0;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-label {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-weight: 600;
  color: #2d3748;
  font-size: 0.95rem;
}

.label-text {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.label-icon {
  font-size: 1rem;
}

.form-input,
.form-select {
  padding: 0.75rem 1rem;
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  font-size: 0.95rem;
  background: white;
  color: #1a202c;
  transition: all 0.3s ease;
  outline: none;
}

.form-input:focus,
.form-select:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.form-input::placeholder {
  color: #94a3b8;
}

/* 操作按钮 */
.form-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  padding-top: 2rem;
  border-top: 1px solid #e2e8f0;
  margin-top: 2rem;
}

.btn-cancel,
.btn-submit {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 2rem;
  border: none;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 0.95rem;
}

.btn-cancel {
  background: #f1f5f9;
  color: #64748b;
  border: 2px solid #e2e8f0;
}

.btn-cancel:hover {
  background: #e2e8f0;
  color: #475569;
}

.btn-submit {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  min-width: 140px;
}

.btn-submit:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
}

.btn-submit:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
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
  
  .main-content {
    padding: 1rem;
  }
  
  .page-header {
    flex-direction: column;
    text-align: center;
    padding: 2rem 1rem 1rem 1rem;
  }
  
  .edit-form {
    padding: 1rem;
  }
  
  .form-grid {
    grid-template-columns: 1fr;
  }
  
  .form-actions {
    flex-direction: column;
  }
  
  .btn-cancel,
  .btn-submit {
    width: 100%;
    justify-content: center;
  }
}
</style> 