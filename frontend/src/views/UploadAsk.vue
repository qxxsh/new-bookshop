<template>
  <div class="modern-upload-ask">
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
      <div class="upload-container">
        <!-- 页面标题 -->
        <div class="page-header">
          <div class="header-icon">
            <svg width="48" height="48" viewBox="0 0 24 24" fill="none">
              <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z" stroke="currentColor" stroke-width="2"/>
              <path d="M9 9h6M9 13h3" stroke="currentColor" stroke-width="2"/>
            </svg>
          </div>
          <div class="header-text">
            <h1>发布求书信息</h1>
            <p>寻找你需要的图书，让同学们帮你找到它</p>
          </div>
        </div>

        <!-- 求书表单 -->
        <form @submit.prevent="handleSubmit" class="upload-form">
          <div class="form-sections">
            <!-- 参考图片上传区域 -->
            <div class="form-section">
              <h3 class="section-title">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                  <rect x="3" y="3" width="18" height="18" rx="2" ry="2" stroke="currentColor" stroke-width="2"/>
                  <circle cx="8.5" cy="8.5" r="1.5" stroke="currentColor" stroke-width="2"/>
                  <polyline points="21,15 16,10 5,21" stroke="currentColor" stroke-width="2"/>
                </svg>
                参考图片
                <span class="optional-badge">可选</span>
              </h3>
              <div class="image-upload-area" @click="triggerFileInput" @dragover.prevent @drop.prevent="handleDrop">
                <div v-if="!imagePreview" class="upload-placeholder">
                  <svg width="48" height="48" viewBox="0 0 24 24" fill="none">
                    <rect x="3" y="3" width="18" height="18" rx="2" ry="2" stroke="currentColor" stroke-width="2"/>
                    <circle cx="8.5" cy="8.5" r="1.5" stroke="currentColor" stroke-width="2"/>
                    <polyline points="21,15 16,10 5,21" stroke="currentColor" stroke-width="2"/>
                  </svg>
                  <p class="upload-text">上传书籍封面或相关图片</p>
                  <p class="upload-hint">帮助其他同学更容易识别你需要的书籍</p>
                </div>
                <div v-else class="image-preview">
                  <img :src="imagePreview" alt="预览图" />
                  <button type="button" @click.stop="removeImage" class="remove-image">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                      <line x1="18" y1="6" x2="6" y2="18" stroke="currentColor" stroke-width="2"/>
                      <line x1="6" y1="6" x2="18" y2="18" stroke="currentColor" stroke-width="2"/>
                    </svg>
                  </button>
                </div>
                <input 
                  ref="fileInput" 
                  type="file" 
                  accept="image/*" 
                  @change="handleFileSelect"
                  style="display: none"
                />
              </div>
            </div>

            <!-- 求书基本信息 -->
            <div class="form-section">
              <h3 class="section-title">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                  <path d="M4 19.5v-15A2.5 2.5 0 0 1 6.5 2H20v20H6.5a2.5 2.5 0 0 1 0-5H20" stroke="currentColor" stroke-width="2"/>
                </svg>
                求书信息
              </h3>
              <div class="form-grid">
                <div class="form-group">
                  <label class="form-label">
                    <span class="label-text">书名 *</span>
                    <span class="label-icon">📚</span>
                  </label>
                  <input 
                    v-model="form.name" 
                    type="text" 
                    class="form-input"
                    placeholder="请输入您要寻找的书名"
                    required
                  />
                </div>
                
                <div class="form-group">
                  <label class="form-label">
                    <span class="label-text">作者 *</span>
                    <span class="label-icon">✍️</span>
                  </label>
                  <input 
                    v-model="form.author" 
                    type="text" 
                    class="form-input"
                    placeholder="请输入作者姓名"
                    required
                  />
                </div>
                
                <div class="form-group">
                  <label class="form-label">
                    <span class="label-text">出版社</span>
                    <span class="label-icon">🏢</span>
                  </label>
                  <input 
                    v-model="form.press" 
                    type="text" 
                    class="form-input"
                    placeholder="请输入出版社（如果知道的话）"
                  />
                </div>

                <div class="form-group">
                  <label class="form-label">
                    <span class="label-text">ISBN</span>
                    <span class="label-icon">📖</span>
                  </label>
                  <input 
                    v-model="form.isbn" 
                    type="text" 
                    class="form-input"
                    placeholder="如果知道ISBN号，有助于精确匹配"
                  />
                </div>
              </div>
            </div>

            <!-- 联系方式和期望条件 -->
            <div class="form-section">
              <h3 class="section-title">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                  <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z" stroke="currentColor" stroke-width="2"/>
                </svg>
                联系方式与期望
              </h3>
              <div class="form-grid">
                <div class="form-group">
                  <label class="form-label">
                    <span class="label-text">联系方式</span>
                    <span class="label-icon">📱</span>
                  </label>
                  <input 
                    v-model="form.contact" 
                    type="text" 
                    class="form-input"
                    placeholder="QQ、微信或手机号"
                  />
                </div>
                
                <div class="form-group">
                  <label class="form-label">
                    <span class="label-text">期望价格</span>
                    <span class="label-icon">💰</span>
                  </label>
                  <div class="input-with-unit">
                    <span class="input-prefix">¥</span>
                    <input 
                      v-model="form.expectedPrice" 
                      type="number" 
                      step="0.01"
                      class="form-input"
                      placeholder="期望购买价格（可选）"
                    />
                  </div>
                </div>
                
                <div class="form-group">
                  <label class="form-label">
                    <span class="label-text">期望品相</span>
                    <span class="label-icon">⭐</span>
                  </label>
                  <select v-model="form.expectedCondition" class="form-select">
                    <option value="">不限品相</option>
                    <option value="9">九成新以上</option>
                    <option value="8">八成新以上</option>
                    <option value="7">七成新以上</option>
                    <option value="6">六成新以上</option>
                    <option value="5">五成新以上</option>
                  </select>
                </div>

                <div class="form-group">
                  <label class="form-label">
                    <span class="label-text">急需程度</span>
                    <span class="label-icon">🚀</span>
                  </label>
                  <select v-model="form.urgency" class="form-select">
                    <option value="normal">一般需求</option>
                    <option value="urgent">比较急需</option>
                    <option value="very-urgent">非常急需</option>
                  </select>
                </div>
              </div>
            </div>

            <!-- 详细说明 -->
            <div class="form-section">
              <h3 class="section-title">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                  <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" stroke="currentColor" stroke-width="2"/>
                  <polyline points="14,2 14,8 20,8" stroke="currentColor" stroke-width="2"/>
                  <line x1="16" y1="13" x2="8" y2="13" stroke="currentColor" stroke-width="2"/>
                  <line x1="16" y1="17" x2="8" y2="17" stroke="currentColor" stroke-width="2"/>
                </svg>
                详细说明
              </h3>
              <div class="form-group">
                <textarea 
                  v-model="form.description" 
                  class="form-textarea"
                  placeholder="请详细描述您要寻找的书籍信息，包括用途、版本要求、交易地点偏好等..."
                  rows="4"
                ></textarea>
                <div class="char-count">{{ form.description.length }}/500</div>
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
              {{ isSubmitting ? '发布中...' : '发布求书信息' }}
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
const imagePreview = ref('')
const fileInput = ref(null)
const user = ref(null)

const form = ref({ 
  name: '', 
  author: '',
  press: '',
  isbn: '',
  contact: '',
  expectedPrice: '',
  expectedCondition: '',
  urgency: 'normal',
  description: ''
})

function triggerFileInput() {
  fileInput.value?.click()
}

function handleFileSelect(event) {
  const file = event.target.files[0]
  if (file) {
    const reader = new FileReader()
    reader.onload = (e) => {
      imagePreview.value = e.target.result
    }
    reader.readAsDataURL(file)
  }
}

function handleDrop(event) {
  const files = event.dataTransfer.files
  if (files.length > 0) {
    const file = files[0]
    if (file.type.startsWith('image/')) {
      fileInput.value.files = files
      handleFileSelect({ target: { files } })
    }
  }
}

function removeImage() {
  imagePreview.value = ''
  fileInput.value.value = ''
}

function goBack() {
  router.back()
}

const handleSubmit = async () => {
  if (!form.value.name || !form.value.author) {
    alert('请填写书名和作者信息！')
    return
  }

  if (!user.value) {
    alert('请先登录！')
    return
  }

  isSubmitting.value = true
  
  try {
    // 创建求书记录
    const bookData = {
      name: form.value.name,
      author: form.value.author,
      press: form.value.press || '',
      description: form.value.description || '',
      bookType: 0, // 0表示求书
      uid: user.value.id // 设置用户ID
    }
    
    const res = await axios.post('/api/book', bookData)
    
    if (res.data.code !== 200) {
      throw new Error(res.data.message || '创建求书记录失败')
    }
    
    const book = res.data.data
    
    // 如果有图片，先上传图片再关联
    if (fileInput.value?.files[0]) {
      // 先创建图片关联记录
      const imageData = { bid: book.id, type: 0 }
      const imageRes = await axios.post('/api/bookImage', imageData)
      
      if (imageRes.data.code === 200) {
        const bookImage = imageRes.data.data
        
        // 上传图片到指定位置
        const formData = new FormData()
        formData.append('file', fileInput.value.files[0])
        formData.append('imageId', bookImage.id)
        await axios.post('/api/file/upload-book-image', formData, { 
          headers: { 'Content-Type': 'multipart/form-data' } 
        })
      }
    }
    
    alert('求书信息发布成功！')
    router.push('/askBookStore')
  } catch (error) {
    console.error('发布失败:', error)
    alert('发布失败，请重试: ' + (error.message || error))
  } finally {
    isSubmitting.value = false
  }
}

// 获取用户信息
async function fetchUser() {
  try {
    const res = await axios.get('/api/user/info')
    if (res.data.code === 200) {
      user.value = res.data.data
      console.log('求书页面 - 用户信息:', user.value)
    } else {
      console.error('获取用户信息失败:', res.data.message)
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

onMounted(() => {
  fetchUser()
  
  // 检查是否从AI推荐跳转过来
  const route = router.currentRoute.value
  if (route.query.fromRecommendation === 'true') {
    // 预填书名和作者
    if (route.query.prefillTitle) {
      form.value.name = route.query.prefillTitle
    }
    if (route.query.prefillAuthor) {
      form.value.author = route.query.prefillAuthor
    }
    
    // 显示提示信息
    setTimeout(() => {
      const bookInfo = route.query.prefillAuthor 
        ? `《${route.query.prefillTitle}》 - ${route.query.prefillAuthor}`
        : `《${route.query.prefillTitle}》`
      alert(`已为您预填写求书信息：${bookInfo}\n请补充其他必要信息后发布`)
    }, 500)
  }
})
</script>

<style scoped>
.modern-upload-ask {
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
  max-width: 1000px;
  margin: 0 auto;
  padding: 2rem;
}

.upload-container {
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
  background: linear-gradient(135deg, #f59e0b, #d97706);
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
.upload-form {
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

.optional-badge {
  background: #f59e0b;
  color: white;
  font-size: 0.75rem;
  padding: 0.25rem 0.5rem;
  border-radius: 50px;
  font-weight: 500;
  margin-left: auto;
}

/* 图片上传区域 */
.image-upload-area {
  border: 2px dashed #cbd5e0;
  border-radius: 12px;
  padding: 2rem;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  background: white;
}

.image-upload-area:hover {
  border-color: #f59e0b;
  background: rgba(245, 158, 11, 0.02);
}

.upload-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
  color: #64748b;
}

.upload-text {
  font-weight: 600;
  color: #2d3748;
  margin: 0;
}

.upload-hint {
  font-size: 0.875rem;
  color: #64748b;
  margin: 0;
}

.image-preview {
  position: relative;
  display: inline-block;
}

.image-preview img {
  max-width: 200px;
  max-height: 300px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.remove-image {
  position: absolute;
  top: -8px;
  right: -8px;
  background: #ef4444;
  color: white;
  border: none;
  border-radius: 50%;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.remove-image:hover {
  background: #dc2626;
  transform: scale(1.1);
}

/* 表单网格 */
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
.form-select,
.form-textarea {
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
.form-select:focus,
.form-textarea:focus {
  border-color: #f59e0b;
  box-shadow: 0 0 0 3px rgba(245, 158, 11, 0.1);
}

.form-input::placeholder,
.form-textarea::placeholder {
  color: #94a3b8;
}

.input-with-unit {
  position: relative;
  display: flex;
  align-items: center;
}

.input-prefix {
  position: absolute;
  left: 1rem;
  color: #64748b;
  font-weight: 600;
  z-index: 1;
}

.input-with-unit .form-input {
  padding-left: 2rem;
}

.form-textarea {
  resize: vertical;
  min-height: 100px;
  font-family: inherit;
}

.char-count {
  text-align: right;
  font-size: 0.875rem;
  color: #64748b;
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
  background: linear-gradient(135deg, #f59e0b, #d97706);
  color: white;
  min-width: 160px;
}

.btn-submit:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(245, 158, 11, 0.3);
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
  
  .upload-form {
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