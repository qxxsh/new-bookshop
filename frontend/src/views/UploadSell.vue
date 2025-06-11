<template>
  <div class="modern-upload-sell">
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
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" stroke="currentColor" stroke-width="2"/>
              <polyline points="14,2 14,8 20,8" stroke="currentColor" stroke-width="2"/>
              <line x1="16" y1="13" x2="8" y2="13" stroke="currentColor" stroke-width="2"/>
              <line x1="16" y1="17" x2="8" y2="17" stroke="currentColor" stroke-width="2"/>
              <polyline points="10,9 9,9 8,9" stroke="currentColor" stroke-width="2"/>
            </svg>
          </div>
          <div class="header-text">
            <h1>上传二手书</h1>
            <p>分享你的闲置图书，让知识传递下去</p>
          </div>
        </div>

        <!-- 上传表单 -->
        <form @submit.prevent="handleSubmit" class="upload-form">
          <div class="form-sections">
            <!-- 图片上传区域 -->
            <div class="form-section">
              <h3 class="section-title">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                  <rect x="3" y="3" width="18" height="18" rx="2" ry="2" stroke="currentColor" stroke-width="2"/>
                  <circle cx="8.5" cy="8.5" r="1.5" stroke="currentColor" stroke-width="2"/>
                  <polyline points="21,15 16,10 5,21" stroke="currentColor" stroke-width="2"/>
                </svg>
                书籍图片
              </h3>
              <div class="image-upload-area" @click="triggerFileInput" @dragover.prevent @drop.prevent="handleDrop">
                <div v-if="!imagePreview" class="upload-placeholder">
                  <svg width="48" height="48" viewBox="0 0 24 24" fill="none">
                    <rect x="3" y="3" width="18" height="18" rx="2" ry="2" stroke="currentColor" stroke-width="2"/>
                    <circle cx="8.5" cy="8.5" r="1.5" stroke="currentColor" stroke-width="2"/>
                    <polyline points="21,15 16,10 5,21" stroke="currentColor" stroke-width="2"/>
                  </svg>
                  <p class="upload-text">点击或拖拽上传图片</p>
                  <p class="upload-hint">支持 JPG、PNG 格式，建议尺寸 400x600</p>
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

            <!-- 基本信息 -->
            <div class="form-section">
              <h3 class="section-title">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                  <path d="M4 19.5v-15A2.5 2.5 0 0 1 6.5 2H20v20H6.5a2.5 2.5 0 0 1 0-5H20" stroke="currentColor" stroke-width="2"/>
                </svg>
                基本信息
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
                    placeholder="请输入书名"
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
                    <span class="label-text">分类 *</span>
                    <span class="label-icon">📂</span>
                  </label>
                  <select v-model="form.cid" class="form-select" required>
                    <option value="">请选择分类</option>
                    <option v-for="cat in categories" :key="cat.id" :value="cat.id">
                      {{ cat.name }}
                    </option>
                  </select>
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
                    placeholder="请输入出版社"
                  />
                </div>
                
                <div class="form-group">
                  <label class="form-label">
                    <span class="label-text">出版时间</span>
                    <span class="label-icon">📅</span>
                  </label>
                  <input 
                    v-model="form.publishDate" 
                    type="month" 
                    class="form-input"
                  />
                </div>
                
                <div class="form-group">
                  <label class="form-label">
                    <span class="label-text">版本</span>
                    <span class="label-icon">🔢</span>
                  </label>
                  <input 
                    v-model="form.version" 
                    type="text" 
                    class="form-input"
                    placeholder="例如：第1版"
                  />
                </div>
              </div>
            </div>

            <!-- 价格信息 -->
            <div class="form-section">
              <h3 class="section-title">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                  <line x1="12" y1="1" x2="12" y2="23" stroke="currentColor" stroke-width="2"/>
                  <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6" stroke="currentColor" stroke-width="2"/>
                </svg>
                价格与品相
              </h3>
              <div class="form-grid">
                <div class="form-group">
                  <label class="form-label">
                    <span class="label-text">原价 *</span>
                    <span class="label-icon">💰</span>
                  </label>
                  <div class="input-with-unit">
                    <span class="input-prefix">¥</span>
                    <input 
                      v-model="form.originalPrice" 
                      type="number" 
                      step="0.01"
                      class="form-input"
                      placeholder="0.00"
                      required
                    />
                  </div>
                </div>
                
                <div class="form-group">
                  <label class="form-label">
                    <span class="label-text">售价 *</span>
                    <span class="label-icon">🏷️</span>
                  </label>
                  <div class="input-with-unit">
                    <span class="input-prefix">¥</span>
                    <input 
                      v-model="form.price" 
                      type="number" 
                      step="0.01"
                      class="form-input"
                      placeholder="0.00"
                      required
                    />
                  </div>
                </div>
                
                <div class="form-group">
                  <label class="form-label">
                    <span class="label-text">品相 *</span>
                    <span class="label-icon">⭐</span>
                  </label>
                  <select v-model="form.degree" class="form-select" required>
                    <option value="">请选择品相</option>
                    <option value="9.9">全新（近10成新）</option>
                    <option value="9.0">九成新</option>
                    <option value="8.0">八成新</option>
                    <option value="7.0">七成新</option>
                    <option value="6.0">六成新</option>
                    <option value="5.0">五成新及以下</option>
                  </select>
                </div>
              </div>
            </div>

            <!-- 详细描述 -->
            <div class="form-section">
              <h3 class="section-title">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                  <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" stroke="currentColor" stroke-width="2"/>
                  <polyline points="14,2 14,8 20,8" stroke="currentColor" stroke-width="2"/>
                  <line x1="16" y1="13" x2="8" y2="13" stroke="currentColor" stroke-width="2"/>
                  <line x1="16" y1="17" x2="8" y2="17" stroke="currentColor" stroke-width="2"/>
                </svg>
                详细描述
              </h3>
              <div class="form-group">
                <textarea 
                  v-model="form.description" 
                  class="form-textarea"
                  placeholder="请详细描述书籍的具体情况，包括使用痕迹、保存状况等..."
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
              {{ isSubmitting ? '上传中...' : '发布书籍' }}
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
const fileInput = ref(null)
const categories = ref([])
const imagePreview = ref('')
const isSubmitting = ref(false)
const user = ref(null)

const form = ref({
  name: '', 
  author: '', 
  cid: null,
  originalPrice: '', 
  price: '', 
  press: '',
  publishDate: '', 
  version: '', 
  degree: '', 
  description: '', 
  bookType: 1
})

async function fetchCategories() {
  try {
    const res = await axios.get('/api/category/list')
    if (res.data.code === 200) {
      categories.value = res.data.data || []
    }
  } catch (error) {
    console.error('获取分类失败:', error)
  }
}

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
  if (!fileInput.value?.files[0]) {
    alert('请上传书籍图片！')
    return
  }
  
  if (!form.value.name || !form.value.author || !form.value.cid || !form.value.originalPrice || !form.value.price || !form.value.degree) {
    alert('请填写完整的必填信息！')
    return
  }

  if (parseFloat(form.value.price) > parseFloat(form.value.originalPrice)) {
    alert('售价不能高于原价！')
    return
  }

  if (!user.value) {
    alert('请先登录！')
    return
  }

  isSubmitting.value = true
  
  try {
    console.log('=== 上传书籍调试信息 ===')
    console.log('当前用户:', user.value)
    console.log('表单数据:', form.value)
    
    // 1. 先创建图书记录
    const bookData = {
      ...form.value,
      bookType: 1, // 1表示出售
      uid: user.value.id // 使用当前登录用户ID
    }
    console.log('创建图书数据:', bookData)
    
    const bookRes = await axios.post('/api/book', bookData)
    console.log('图书创建响应:', bookRes.data)
    
    if (bookRes.data.code !== 200) {
      throw new Error(bookRes.data.message || '创建图书失败')
    }
    
    const book = bookRes.data.data
    console.log('图书创建成功，ID:', book.id)
    
    // 2. 创建图片关联记录
    const imageData = { 
      bid: book.id, 
      type: 0 // 0表示书籍图片（统一使用0）
    }
    console.log('创建图片关联:', imageData)
    
    const imageRes = await axios.post('/api/bookImage', imageData)
    console.log('图片关联响应:', imageRes.data)
    
    if (imageRes.data.code !== 200) {
      throw new Error(imageRes.data.message || '创建图片关联失败')
    }
    
    const bookImage = imageRes.data.data
    console.log('图片关联创建成功，ID:', bookImage.id)
    
    // 3. 上传图片到指定位置
    const formData = new FormData()
    formData.append('file', fileInput.value.files[0])
    formData.append('imageId', bookImage.id)
    console.log('开始上传图片，imageId:', bookImage.id)
    
    const uploadRes = await axios.post('/api/file/upload-book-image', formData, { 
      headers: { 'Content-Type': 'multipart/form-data' } 
    })
    console.log('图片上传响应:', uploadRes.data)
    
    if (uploadRes.data.code !== 200) {
      throw new Error(uploadRes.data.message || '图片上传失败')
    }
    
    alert('书籍发布成功！')
    router.push('/myBookshelf')
  } catch (error) {
    console.error('上传失败详情:', error)
    const errorMessage = error.response?.data?.message || error.message || '上传失败，请重试'
    alert(errorMessage)
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
      console.log('上传书籍页面 - 用户信息:', user.value)
    } else {
      console.error('获取用户信息失败:', res.data.message)
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

onMounted(async () => {
  await fetchUser()
  await fetchCategories()
})
</script>

<style scoped>
.modern-upload-sell {
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
  border-color: #667eea;
  background: rgba(102, 126, 234, 0.02);
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
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
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