<template>
  <div class="login-wrapper">
    <div class="login-background">
      <div class="floating-shapes">
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
        <div class="shape shape-3"></div>
      </div>
    </div>
    
    <div class="login-container">
      <div class="login-card">
        <div class="login-header">
          <div class="logo-container">
            <div class="logo">📚</div>
          </div>
          <h1 class="login-title">校园二手书交易平台</h1>
          <p class="login-subtitle">欢迎回来，请登录您的账户</p>
          
          <!-- 测试账户提示 -->
          <div class="test-accounts">
            <h4>📋 测试账户</h4>
            <div class="account-list">
              <div class="account-item" @click="fillAccount('2300822', '675844')">
                <span class="student-id">2300822</span>
                <span class="name">刘一帆</span>
              </div>
              <div class="account-item" @click="fillAccount('2300823', '549256')">
                <span class="student-id">2300823</span>
                <span class="name">于文轩</span>
              </div>
              <div class="account-item" @click="fillAccount('2300824', '123456')">
                <span class="student-id">2300824</span>
                <span class="name">邱子昂</span>
              </div>
            </div>
            <p class="tip">💡 点击任意账户快速填入</p>
          </div>
        </div>

        <form @submit.prevent="handleLogin" class="login-form">
          <div class="input-group">
            <input 
              v-model="studentid" 
              type="text" 
              class="form-input" 
              placeholder="👤 请输入学号"
              :class="{ 'error': errorInfo && !studentid }"
            />
          </div>

          <div class="input-group">
            <input 
              v-model="password" 
              type="password" 
              class="form-input" 
              placeholder="🔒 请输入密码"
              :class="{ 'error': errorInfo && !password }"
            />
          </div>

          <div class="form-options">
            <label class="remember-me">
              <input type="checkbox" />
              <span class="checkmark"></span>
              记住我
            </label>
            <router-link to="" class="forgot-password">忘记密码?</router-link>
          </div>

          <button type="submit" class="login-button" :disabled="isLoading">
            <span v-if="!isLoading">登录</span>
            <span v-else class="loading">
              <div class="spinner"></div>
              登录中...
            </span>
          </button>

          <div v-if="errorInfo" class="error-message">
            <i class="error-icon">⚠️</i>
            {{ errorInfo }}
          </div>
        </form>

        <div class="login-footer">
          <p>还没有账户？ <a href="#" @click.prevent="handleRegister" class="register-link">立即注册</a></p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const studentid = ref('')
const password = ref('')
const errorInfo = ref('')
const isLoading = ref(false)
const router = useRouter()

const handleLogin = async () => {
  if (isLoading.value) return
  
  errorInfo.value = ''
  if (!studentid.value || !password.value) {
    errorInfo.value = '学号或密码不能为空！'
    return
  }
  
  isLoading.value = true
  
  try {
    const res = await axios.post('/api/user/login', {
      studentid: studentid.value,
      password: password.value
    })
    
    console.log('登录响应:', res.data) // 添加调试信息
    
    if (res.data.code === 200) {
      // 显示成功提示
      alert('✅ 登录成功！正在跳转到首页...')
      
      // 存储用户信息到localStorage
      localStorage.setItem('userInfo', JSON.stringify(res.data.data))
      
      // 跳转到首页
      setTimeout(() => {
        router.push('/home')
      }, 500)
    } else {
      errorInfo.value = res.data.message || '登录失败，请检查账号密码'
    }
  } catch (e) {
    console.error('登录错误:', e)
    errorInfo.value = '登录出错，请检查网络连接'
  } finally {
    isLoading.value = false
  }
}

const handleRegister = () => {
  // 暂时显示提示信息，提醒用户注册功能
  alert('注册功能正在开发中！\n\n如需注册账户，请联系管理员或稍后再试。\n\n测试账号：可以使用任意学号和密码进行测试。')
}

const fillAccount = (studentId, pwd) => {
  studentid.value = studentId
  password.value = pwd
}
</script>

<style scoped>
.login-wrapper {
  min-height: 100vh;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen', 'Ubuntu', 'Cantarell', sans-serif;
}

.login-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  z-index: -1;
}

.floating-shapes {
  position: absolute;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.shape {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 6s ease-in-out infinite;
}

.shape-1 {
  width: 80px;
  height: 80px;
  top: 20%;
  left: 10%;
  animation-delay: 0s;
}

.shape-2 {
  width: 120px;
  height: 120px;
  top: 60%;
  right: 15%;
  animation-delay: 2s;
}

.shape-3 {
  width: 60px;
  height: 60px;
  bottom: 20%;
  left: 20%;
  animation-delay: 4s;
}

@keyframes float {
  0%, 100% { transform: translateY(0px) rotate(0deg); }
  50% { transform: translateY(-20px) rotate(180deg); }
}

.login-container {
  width: 100%;
  max-width: 420px;
  padding: 20px;
}

.login-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  animation: slideUp 0.6s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.logo-container {
  margin-bottom: 15px;
}

.logo {
  font-size: 48px;
  display: inline-block;
  animation: bounce 2s infinite;
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% { transform: translateY(0); }
  40% { transform: translateY(-10px); }
  60% { transform: translateY(-5px); }
}

.login-title {
  font-size: 28px;
  font-weight: 700;
  color: #2d3748;
  margin: 0 0 8px 0;
}

.login-subtitle {
  font-size: 16px;
  color: #718096;
  margin: 0 0 20px 0;
}

.login-form {
  margin-bottom: 20px;
}

.input-group {
  margin-bottom: 20px;
}

.form-input {
  width: 100%;
  padding: 15px 20px;
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  font-size: 16px;
  transition: all 0.3s ease;
  background: #fff;
  color: #2d3748;
  box-sizing: border-box;
}

.form-input::placeholder {
  color: #a0aec0;
}

.form-input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
  color: #2d3748;
}

.form-input.error {
  border-color: #e53e3e;
  box-shadow: 0 0 0 3px rgba(229, 62, 62, 0.1);
  color: #2d3748;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  font-size: 14px;
}

.remember-me {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #4a5568;
  font-size: 14px;
}

.remember-me input[type="checkbox"] {
  width: 18px;
  height: 18px;
  margin-right: 8px;
  cursor: pointer;
  appearance: none;
  border: 2px solid #e2e8f0;
  border-radius: 4px;
  background: #fff;
  position: relative;
  transition: all 0.3s ease;
}

.remember-me input[type="checkbox"]:checked {
  background: #667eea;
  border-color: #667eea;
}

.remember-me input[type="checkbox"]:checked::after {
  content: '✓';
  position: absolute;
  top: -2px;
  left: 2px;
  color: white;
  font-size: 14px;
  font-weight: bold;
}

.remember-me input[type="checkbox"]:focus {
  outline: none;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.checkmark {
  display: none;
}

.forgot-password {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s ease;
}

.forgot-password:hover {
  color: #5a67d8;
}

.login-button {
  width: 100%;
  padding: 15px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.login-button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
}

.login-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.loading {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.spinner {
  width: 20px;
  height: 20px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top-color: white;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.error-message {
  display: flex;
  align-items: center;
  gap: 8px;
  background: #fed7d7;
  color: #c53030;
  padding: 12px 15px;
  border-radius: 8px;
  font-size: 14px;
  margin-top: 15px;
  border-left: 4px solid #e53e3e;
}

.error-icon {
  font-size: 16px;
}

.login-footer {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #718096;
}

.register-link {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s ease;
}

.register-link:hover {
  color: #5a67d8;
}

/* 响应式设计 */
@media (max-width: 480px) {
  .login-container {
    padding: 15px;
  }
  
  .login-card {
    padding: 30px 25px;
  }
  
  .login-title {
    font-size: 24px;
  }
  
  .form-input {
    padding: 12px 12px 12px 45px;
  }
  
  .login-button {
    padding: 12px;
  }
}

.test-accounts {
  background: rgba(102, 126, 234, 0.05);
  border: 1px solid rgba(102, 126, 234, 0.2);
  border-radius: 12px;
  padding: 15px;
  margin-bottom: 20px;
}

.test-accounts h4 {
  margin: 0 0 10px 0;
  font-size: 14px;
  color: #4a5568;
  font-weight: 600;
}

.account-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.account-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  background: rgba(255, 255, 255, 0.8);
  border: 1px solid rgba(102, 126, 234, 0.2);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 13px;
}

.account-item:hover {
  background: rgba(102, 126, 234, 0.1);
  border-color: #667eea;
  transform: translateY(-1px);
}

.student-id {
  font-weight: 600;
  color: #667eea;
}

.name {
  color: #4a5568;
}

.tip {
  margin: 10px 0 0 0;
  font-size: 12px;
  color: #718096;
  text-align: center;
}
</style> 