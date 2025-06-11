<template>
  <div class="register-wrapper">
    <div class="register-background">
      <div class="floating-shapes">
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
        <div class="shape shape-3"></div>
      </div>
    </div>

    <div class="register-container">
      <div class="register-card">
        <div class="register-header">
          <div class="logo-container">
            <div class="logo">🚀</div>
          </div>
          <h1 class="register-title">创建您的账户</h1>
          <p class="register-subtitle">加入校园二手书交易平台</p>
        </div>

        <form @submit.prevent="handleRegister" class="register-form">
          <div class="input-group">
            <input v-model="form.studentid" type="text" class="form-input" placeholder="请输入学号" required />
          </div>
          <div class="input-group">
            <input v-model="form.name" type="text" class="form-input" placeholder="请输入姓名" required />
          </div>
          <div class="input-group">
            <input v-model="form.password" type="password" class="form-input" placeholder="请输入密码" required />
          </div>
          <div class="input-group">
            <input v-model="form.confirmPassword" type="password" class="form-input" placeholder="请再次输入密码" required />
          </div>
           <div class="input-group">
            <input v-model="form.sex" type="text" class="form-input" placeholder="请输入性别" />
          </div>
          <div class="input-group">
            <input v-model="form.tel" type="text" class="form-input" placeholder="请输入电话" />
          </div>
          <div class="input-group">
            <input v-model="form.address" type="text" class="form-input" placeholder="请输入地址" />
          </div>
          <div class="input-group">
            <input v-model="form.major" type="text" class="form-input" placeholder="请输入专业" />
          </div>

          <button type="submit" class="register-button" :disabled="isLoading">
            <span v-if="!isLoading">立即注册</span>
            <span v-else class="loading">
              <div class="spinner"></div>
              注册中...
            </span>
          </button>

          <div v-if="errorInfo" class="error-message">
            <i class="error-icon">⚠️</i>
            {{ errorInfo }}
          </div>
        </form>

        <div class="register-footer">
          <p>已经有账户了？ <router-link to="/login" class="login-link">直接登录</router-link></p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const form = ref({
  studentid: '',
  name: '',
  password: '',
  confirmPassword: '',
  sex: '',
  tel: '',
  address: '',
  major: '',
});
const errorInfo = ref('');
const isLoading = ref(false);
const router = useRouter();

const handleRegister = async () => {
  if (isLoading.value) return;

  errorInfo.value = '';
  if (form.value.password !== form.value.confirmPassword) {
    errorInfo.value = '两次输入的密码不一致！';
    return;
  }
  
  const requiredFields = ['studentid', 'name', 'password'];
  for (const field of requiredFields) {
    if (!form.value[field]) {
      errorInfo.value = '学号、姓名和密码为必填项！';
      return;
    }
  }

  isLoading.value = true;

  try {
    const registrationData = { ...form.value };
    delete registrationData.confirmPassword;

    const res = await axios.post('/api/user/register', registrationData);

    if (res.data.code === 200) {
      alert('✅ 注册成功！现在您可以去登录了。');
      router.push('/login');
    } else {
      errorInfo.value = res.data.message || '注册失败，请稍后再试';
    }
  } catch (e) {
    console.error('注册错误:', e);
    errorInfo.value = e.response?.data?.message || '注册出错，请检查网络连接或联系管理员';
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
.register-wrapper {
  min-height: 100vh;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen', 'Ubuntu', 'Cantarell', sans-serif;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.register-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
  overflow: hidden;
}

.floating-shapes .shape {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 8s ease-in-out infinite;
}

.shape-1 { width: 80px; height: 80px; top: 15%; left: 10%; animation-delay: 0s; }
.shape-2 { width: 120px; height: 120px; top: 70%; right: 15%; animation-delay: 2s; }
.shape-3 { width: 60px; height: 60px; bottom: 10%; left: 25%; animation-delay: 4s; }

@keyframes float {
  0%, 100% { transform: translateY(0px) rotate(0deg); }
  50% { transform: translateY(-25px) rotate(180deg); }
}

.register-container {
  width: 100%;
  max-width: 480px;
  padding: 20px;
}

.register-card {
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 20px 50px rgba(0, 0, 0, 0.15);
  border: 1px solid rgba(255, 255, 255, 0.2);
  animation: fadeIn 0.7s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: scale(0.95); }
  to { opacity: 1; transform: scale(1); }
}

.register-header {
  text-align: center;
  margin-bottom: 30px;
}

.logo-container {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.logo {
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 2rem;
  color: white;
  box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
}

.register-title {
  font-size: 1.75rem;
  font-weight: 700;
  color: #2d3748;
  margin: 0 0 10px;
}

.register-subtitle {
  font-size: 1rem;
  color: #64748b;
  margin: 0;
}

.register-form {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.input-group {
  position: relative;
}

.form-input {
  width: 100%;
  padding: 14px 16px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  font-size: 1rem;
  color: #2d3748;
  background-color: #f7fafc;
  transition: all 0.3s ease;
  box-sizing: border-box;
}

.form-input:focus {
  outline: none;
  border-color: #667eea;
  background-color: white;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.2);
}

.register-button {
  width: 100%;
  padding: 14px;
  border: none;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin-top: 10px;
}

.register-button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.register-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.loading .spinner {
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.5);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.error-message {
  margin-top: 10px;
  padding: 12px;
  background-color: rgba(239, 68, 68, 0.1);
  color: #c53030;
  border: 1px solid rgba(239, 68, 68, 0.2);
  border-radius: 8px;
  text-align: center;
  font-size: 0.9rem;
  display: flex;
  align-items: center;
  justify-content: center;
}

.error-message .error-icon {
  margin-right: 8px;
  font-style: normal;
}

.register-footer {
  text-align: center;
  margin-top: 20px;
  font-size: 0.9rem;
  color: #64748b;
}

.login-link {
  color: #667eea;
  font-weight: 600;
  text-decoration: none;
  transition: color 0.3s ease;
}

.login-link:hover {
  color: #5a67d8;
}
</style> 