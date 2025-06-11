<template>
  <div class="order-confirm-container">
    <h2>确认订单</h2>
    
    <div v-if="orderItems.length === 0" class="empty-order">
      <p>没有选择任何商品</p>
      <router-link to="/cart" class="btn btn-primary">返回购物车</router-link>
    </div>
    
    <div v-else class="order-form">
      <!-- 收货地址 -->
      <div class="section address-section">
        <h3>收货信息</h3>
        <div class="form-group">
          <label>收货人姓名：</label>
          <input v-model="orderForm.deliveryName" type="text" placeholder="请输入收货人姓名" required>
        </div>
        <div class="form-group">
          <label>联系电话：</label>
          <input v-model="orderForm.deliveryPhone" type="tel" placeholder="请输入手机号" required>
        </div>
        <div class="form-group">
          <label>收货地址：</label>
          <textarea v-model="orderForm.deliveryAddress" placeholder="请输入详细地址" required></textarea>
        </div>
      </div>
      
      <!-- 商品列表 -->
      <div class="section items-section">
        <h3>商品信息</h3>
        <div class="order-items">
          <div v-for="item in orderItems" :key="item.id" class="order-item">
            <div class="item-image">
              <img :src="`/api/bookImage/${item.bookId}/0`" :alt="item.book?.name" @error="handleImageError">
            </div>
            <div class="item-info">
              <h4>{{ item.book?.name }}</h4>
              <p>作者：{{ item.book?.author }}</p>
              <p>出版社：{{ item.book?.press }}</p>
            </div>
            <div class="item-price">
              <span>¥{{ item.book?.price }}</span>
            </div>
            <div class="item-quantity">
              <span>x{{ item.quantity }}</span>
            </div>
            <div class="item-subtotal">
              <span>¥{{ (item.book?.price * item.quantity).toFixed(2) }}</span>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 订单备注 -->
      <div class="section remark-section">
        <h3>订单备注</h3>
        <div class="form-group">
          <textarea v-model="orderForm.remark" placeholder="选填，可以备注特殊需求"></textarea>
        </div>
      </div>
      
      <!-- 订单总结 -->
      <div class="section summary-section">
        <div class="order-summary">
          <div class="summary-row">
            <span>商品总价：</span>
            <span>¥{{ totalAmount.toFixed(2) }}</span>
          </div>
          <div class="summary-row">
            <span>运费：</span>
            <span>¥0.00</span>
          </div>
          <div class="summary-row total">
            <span>应付总额：</span>
            <span class="total-price">¥{{ totalAmount.toFixed(2) }}</span>
          </div>
        </div>
      </div>
      
      <!-- 提交按钮 -->
      <div class="submit-section">
        <button @click="goBack" class="btn btn-secondary">返回购物车</button>
        <button @click="submitOrder" :disabled="!isFormValid || submitting" class="btn btn-primary">
          {{ submitting ? '提交中...' : '提交订单' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const route = useRoute()

const orderItems = ref([])
const submitting = ref(false)

const orderForm = ref({
  deliveryName: '',
  deliveryPhone: '',
  deliveryAddress: '',
  remark: '',
  cartIds: []
})

// 计算属性
const totalAmount = computed(() => {
  return orderItems.value.reduce((total, item) => {
    return total + (item.book?.price || 0) * item.quantity
  }, 0)
})

const isFormValid = computed(() => {
  return orderForm.value.deliveryName.trim() !== '' &&
         orderForm.value.deliveryPhone.trim() !== '' &&
         orderForm.value.deliveryAddress.trim() !== ''
})

// 页面加载
onMounted(() => {
  loadOrderData()
})

// 加载订单数据（从路由参数或localStorage获取）
const loadOrderData = async () => {
  try {
    // 从路由查询参数获取购物车ID列表
    const cartIds = route.query.cartIds
    if (!cartIds) {
      alert('没有选择商品')
      router.push('/cart')
      return
    }
    
    orderForm.value.cartIds = Array.isArray(cartIds) ? cartIds.map(id => parseInt(id)) : [parseInt(cartIds)]
    
    // 获取购物车数据
    const res = await axios.get('/api/cart/list')
    if (res.data.resultCode === 200) {
      // 过滤出选中的商品
      orderItems.value = res.data.data.filter(item => 
        orderForm.value.cartIds.includes(item.id)
      )
    } else {
      alert(res.data.message)
    }
  } catch (error) {
    console.error('加载订单数据失败:', error)
    alert('加载订单数据失败')
  }
}

// 提交订单
const submitOrder = async () => {
  if (!isFormValid.value) {
    alert('请填写完整的收货信息')
    return
  }
  
  submitting.value = true
  
  try {
    const orderData = {
      cartIds: orderForm.value.cartIds,
      deliveryName: orderForm.value.deliveryName,
      deliveryPhone: orderForm.value.deliveryPhone,
      deliveryAddress: orderForm.value.deliveryAddress,
      remark: orderForm.value.remark
    }
    
    const res = await axios.post('/api/order/create', orderData)
    if (res.data.resultCode === 200) {
      alert('订单创建成功！')
      // 跳转到订单详情或订单列表
      router.push(`/orders/${res.data.data.id}`)
    } else {
      alert(res.data.message)
    }
  } catch (error) {
    console.error('创建订单失败:', error)
    alert('创建订单失败，请重试')
  } finally {
    submitting.value = false
  }
}

// 返回购物车
const goBack = () => {
  router.push('/cart')
}

// 图片加载失败处理
const handleImageError = (event) => {
  event.target.src = '/default-book.png'
}
</script>

<style scoped>
.order-confirm-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.empty-order {
  text-align: center;
  padding: 50px;
}

.order-form {
  background: white;
  border-radius: 8px;
  overflow: hidden;
}

.section {
  padding: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.section:last-child {
  border-bottom: none;
}

.section h3 {
  margin: 0 0 15px 0;
  color: #333;
  font-size: 18px;
}

/* 表单样式 */
.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: inline-block;
  width: 100px;
  font-weight: bold;
  color: #555;
}

.form-group input,
.form-group textarea {
  width: calc(100% - 120px);
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.form-group textarea {
  min-height: 80px;
  resize: vertical;
}

/* 商品列表样式 */
.order-items {
  background: #fafafa;
  border-radius: 4px;
  padding: 15px;
}

.order-item {
  display: flex;
  align-items: center;
  padding: 15px;
  background: white;
  border-radius: 4px;
  margin-bottom: 10px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
}

.order-item:last-child {
  margin-bottom: 0;
}

.item-image {
  width: 60px;
  height: 80px;
  margin-right: 15px;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 4px;
}

.item-info {
  flex: 1;
  margin-right: 15px;
}

.item-info h4 {
  margin: 0 0 5px 0;
  font-size: 16px;
  color: #333;
}

.item-info p {
  margin: 2px 0;
  color: #666;
  font-size: 14px;
}

.item-price,
.item-quantity,
.item-subtotal {
  width: 80px;
  text-align: center;
  font-weight: bold;
}

.item-price {
  color: #ff4444;
}

.item-subtotal {
  color: #ff4444;
  font-size: 16px;
}

/* 订单总结样式 */
.order-summary {
  background: #f9f9f9;
  padding: 20px;
  border-radius: 4px;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  font-size: 14px;
}

.summary-row.total {
  border-top: 1px solid #ddd;
  padding-top: 10px;
  margin-top: 10px;
  font-size: 18px;
  font-weight: bold;
}

.total-price {
  color: #ff4444;
  font-size: 20px;
}

/* 提交按钮样式 */
.submit-section {
  padding: 30px 20px;
  text-align: right;
  background: #f9f9f9;
}

.btn {
  padding: 12px 24px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  margin-left: 10px;
  text-decoration: none;
  display: inline-block;
}

.btn-primary {
  background-color: #007bff;
  color: white;
}

.btn-primary:hover:not(:disabled) {
  background-color: #0056b3;
}

.btn-primary:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.btn-secondary {
  background-color: #6c757d;
  color: white;
}

.btn-secondary:hover {
  background-color: #545b62;
}
</style> 