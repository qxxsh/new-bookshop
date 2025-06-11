<template>
  <div class="order-list-container">
    <h2>我的订单</h2>
    
    <!-- 订单状态筛选 -->
    <div class="order-filter">
      <button 
        v-for="status in statusOptions" 
        :key="status.value"
        @click="filterByStatus(status.value)"
        :class="['filter-btn', { active: currentStatus === status.value }]"
      >
        {{ status.label }}
      </button>
    </div>
    
    <!-- 订单列表 -->
    <div v-if="orders.length === 0" class="empty-orders">
      <p>暂无订单</p>
      <router-link to="/bookstore" class="btn btn-primary">去购书</router-link>
    </div>
    
    <div v-else class="orders">
      <div v-for="order in orders" :key="order.id" class="order-card">
        <!-- 订单头部 -->
        <div class="order-header">
          <div class="order-info">
            <span class="order-no">订单号：{{ order.orderNo }}</span>
            <span class="order-time">{{ formatDate(order.createTime) }}</span>
          </div>
          <div class="order-status" :class="getStatusClass(order.status)">
            {{ order.statusText }}
          </div>
        </div>
        
        <!-- 订单商品 -->
        <div class="order-items">
          <div v-for="item in order.orderItems" :key="item.id" class="order-item">
            <div class="item-image">
              <img :src="`/api/bookImage/${item.bookId}/0`" :alt="item.bookName" @error="handleImageError">
            </div>
            <div class="item-info">
              <h4>{{ item.bookName }}</h4>
              <p>作者：{{ item.bookAuthor }}</p>
              <p>数量：{{ item.quantity }}</p>
            </div>
            <div class="item-price">
              <span>¥{{ item.bookPrice }}</span>
            </div>
            <div class="item-actions" v-if="order.status === 4">
              <!-- 已完成的订单显示评价按钮 -->
              <button 
                v-if="!item.reviewed" 
                @click="showReviewModal(item)" 
                class="btn btn-review btn-sm"
              >
                评价
              </button>
              <span v-else class="reviewed-text">已评价</span>
            </div>
          </div>
        </div>
        
        <!-- 订单底部 -->
        <div class="order-footer">
          <div class="order-total">
            <span>订单总额：</span>
            <span class="total-amount">¥{{ order.totalAmount }}</span>
          </div>
          
          <div class="order-actions">
            <!-- 根据订单状态显示不同操作按钮 -->
            <template v-if="order.status === 1">
              <!-- 待支付 -->
              <button @click="payOrder(order)" class="btn btn-primary">立即支付</button>
              <button @click="cancelOrder(order)" class="btn btn-danger">取消订单</button>
            </template>
            
            <template v-else-if="order.status === 2">
              <!-- 已支付 -->
              <span class="status-text">等待发货</span>
            </template>
            
            <template v-else-if="order.status === 3">
              <!-- 已发货 -->
              <button @click="confirmOrder(order)" class="btn btn-primary">确认收货</button>
            </template>
            
            <template v-else-if="order.status === 4">
              <!-- 已完成 -->
              <button @click="viewOrder(order)" class="btn btn-secondary">查看详情</button>
            </template>
            
            <template v-else-if="order.status === 5">
              <!-- 已取消 -->
              <button @click="viewOrder(order)" class="btn btn-secondary">查看详情</button>
            </template>
            
            <button @click="viewOrder(order)" class="btn btn-outline">订单详情</button>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 分页 -->
    <div v-if="totalPages > 1" class="pagination">
      <button @click="changePage(pageNum - 1)" :disabled="pageNum <= 1" class="page-btn">上一页</button>
      <span class="page-info">{{ pageNum }} / {{ totalPages }}</span>
      <button @click="changePage(pageNum + 1)" :disabled="pageNum >= totalPages" class="page-btn">下一页</button>
    </div>
    
    <!-- 支付弹窗 -->
    <div v-if="showPayModal" class="modal-overlay" @click="closePayModal">
      <div class="modal-content" @click.stop>
        <h3>选择支付方式</h3>
        <div class="payment-methods">
          <label>
            <input type="radio" v-model="selectedPaymentMethod" value="alipay">
            <span>支付宝</span>
          </label>
          <label>
            <input type="radio" v-model="selectedPaymentMethod" value="wechat">
            <span>微信支付</span>
          </label>
          <label>
            <input type="radio" v-model="selectedPaymentMethod" value="campus_card">
            <span>校园卡</span>
          </label>
        </div>
        <div class="modal-actions">
          <button @click="closePayModal" class="btn btn-secondary">取消</button>
          <button @click="confirmPayment" class="btn btn-primary">确认支付</button>
        </div>
      </div>
    </div>
    
    <!-- 评价弹窗 -->
    <div v-if="showReviewModalFlag" class="modal-overlay" @click="closeReviewModal">
      <div class="modal-content review-modal" @click.stop>
        <h3>商品评价</h3>
        <div class="review-form">
          <div class="book-info">
            <h4>{{ currentReviewItem?.bookName }}</h4>
            <p>作者：{{ currentReviewItem?.bookAuthor }}</p>
          </div>
          
          <div class="rating-section">
            <label>评分：</label>
            <div class="star-rating">
              <span 
                v-for="star in 5" 
                :key="star" 
                @click="setRating(star)"
                :class="['star', { active: star <= reviewForm.rating }]"
              >
                ★
              </span>
            </div>
          </div>
          
          <div class="content-section">
            <label>评价内容：</label>
            <textarea 
              v-model="reviewForm.content" 
              placeholder="请分享您的使用体验..."
              rows="4"
            ></textarea>
          </div>
          
          <div class="anonymous-section">
            <label>
              <input type="checkbox" v-model="reviewForm.isAnonymous">
              匿名评价
            </label>
          </div>
        </div>
        
        <div class="modal-actions">
          <button @click="closeReviewModal" class="btn btn-secondary">取消</button>
          <button @click="submitReview" :disabled="reviewForm.rating === 0" class="btn btn-primary">
            提交评价
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

const orders = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const totalPages = ref(1)
const currentStatus = ref(null)

const showPayModal = ref(false)
const selectedPaymentMethod = ref('alipay')
const currentPayingOrder = ref(null)

const showReviewModalFlag = ref(false)
const currentReviewItem = ref(null)
const reviewForm = ref({
  rating: 0,
  content: '',
  isAnonymous: false
})

// 订单状态选项
const statusOptions = [
  { value: null, label: '全部' },
  { value: 1, label: '待支付' },
  { value: 2, label: '待发货' },
  { value: 3, label: '已发货' },
  { value: 4, label: '已完成' },
  { value: 5, label: '已取消' }
]

// 页面加载
onMounted(() => {
  loadOrders()
})

// 加载订单列表
const loadOrders = async () => {
  try {
    const params = {
      pageNum: pageNum.value,
      pageSize: pageSize.value
    }
    
    if (currentStatus.value !== null) {
      params.status = currentStatus.value
    }
    
    const res = await axios.get('/api/order/list', { params })
    if (res.data.resultCode === 200) {
      const page = res.data.data
      orders.value = page.records
      totalPages.value = Math.ceil(page.total / pageSize.value)
      
      // 检查已完成订单的评价状态
      await checkReviewStatus()
    } else {
      alert(res.data.message)
    }
  } catch (error) {
    console.error('加载订单失败:', error)
    alert('加载订单失败')
  }
}

// 检查评价状态
const checkReviewStatus = async () => {
  for (const order of orders.value) {
    if (order.status === 4) { // 已完成的订单
      for (const item of order.orderItems) {
        try {
          const res = await axios.get(`/api/review/check/${item.id}`)
          if (res.data.resultCode === 200) {
            item.reviewed = !res.data.data // 如果可以评价说明还没评价
          }
        } catch (error) {
          console.error('检查评价状态失败:', error)
        }
      }
    }
  }
}

// 筛选订单
const filterByStatus = (status) => {
  currentStatus.value = status
  pageNum.value = 1 // 重置到第一页
  loadOrders()
}

// 改变页码
const changePage = (newPage) => {
  if (newPage > 0 && newPage <= totalPages.value) {
    pageNum.value = newPage
    loadOrders()
  }
}

// 支付订单 - 打开弹窗
const payOrder = (order) => {
  currentPayingOrder.value = order
  showPayModal.value = true
}

// 确认支付
const confirmPayment = async () => {
  if (!currentPayingOrder.value) return
  
  try {
    const res = await axios.put(`/api/order/${currentPayingOrder.value.id}/pay`, null, {
      params: { paymentMethod: selectedPaymentMethod.value }
    })
    
    if (res.data.resultCode === 200) {
      alert('支付成功！')
      closePayModal()
      loadOrders() // 刷新列表
    } else {
      alert(res.data.message || '支付失败')
    }
  } catch (error) {
    console.error('支付失败:', error)
    alert('支付操作失败')
  }
}

// 取消订单
const cancelOrder = async (order) => {
  if (!confirm(`确定要取消订单 ${order.orderNo} 吗？`)) return

  try {
    const res = await axios.put(`/api/order/${order.id}/cancel`)
    if (res.data.resultCode === 200) {
      alert('订单已取消')
      loadOrders() // 刷新列表
    } else {
      alert(res.data.message || '取消失败')
    }
  } catch (error) {
    console.error('取消订单失败:', error)
    alert('操作失败')
  }
}

// 确认收货
const confirmOrder = async (order) => {
  if (!confirm(`请确认您已收到订单 ${order.orderNo} 的全部商品。`)) return

  try {
    const res = await axios.put(`/api/order/${order.id}/confirm`)
    if (res.data.resultCode === 200) {
      alert('确认收货成功！')
      loadOrders() // 刷新列表
    } else {
      alert(res.data.message || '确认收货失败')
    }
  } catch (error) {
    console.error('确认收货失败:', error)
    alert('操作失败')
  }
}

// 查看订单详情
const viewOrder = (order) => {
  // 假设我们有一个订单详情页，路由为 /order/:id
  router.push(`/order/${order.id}`)
}

// 显示评价弹窗
const showReviewModal = (item) => {
  currentReviewItem.value = item
  reviewForm.value = { rating: 0, content: '', isAnonymous: false }
  showReviewModalFlag.value = true
}

// 设置评分
const setRating = (star) => {
  reviewForm.value.rating = star
}

// 提交评价
const submitReview = async () => {
  if (reviewForm.value.rating === 0) {
    alert('请选择评分')
    return
  }
  
  try {
    const reviewData = {
      orderItemId: currentReviewItem.value.id,
      bookId: currentReviewItem.value.bookId,
      rating: reviewForm.value.rating,
      content: reviewForm.value.content || '用户未填写评价。',
      isAnonymous: reviewForm.value.isAnonymous
    }
    const res = await axios.post('/api/reviews', reviewData)
    
    if (res.data.resultCode === 200) {
      alert('评价成功！')
      closeReviewModal()
      // 标记为已评价，避免重复评价
      currentReviewItem.value.reviewed = true
    } else {
      alert(res.data.message || '评价提交失败')
    }
  } catch (error) {
    console.error('评价失败:', error)
    alert('评价操作失败')
  }
}

// 关闭支付弹窗
const closePayModal = () => {
  showPayModal.value = false
  currentPayingOrder.value = null
}

const closeReviewModal = () => {
  showReviewModalFlag.value = false
  currentReviewItem.value = null
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleString()
}

// 获取状态样式类
const getStatusClass = (status) => {
  const statusClasses = {
    1: 'status-pending',
    2: 'status-paid',
    3: 'status-shipped',
    4: 'status-completed',
    5: 'status-cancelled'
  }
  return statusClasses[status] || ''
}

// 图片加载失败处理
const handleImageError = (event) => {
  event.target.src = '/default-book.png'
}
</script>

<style scoped>
.order-list-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Helvetica Neue', Arial, sans-serif;
}

h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}

.order-filter {
  margin-bottom: 20px;
  text-align: center;
  border: 1px solid #ddd;
  padding: 5px 10px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
  background-color: white;
}

.filter-btn {
  padding: 8px 16px;
  margin: 0 5px;
  border: 1px solid #ddd;
  background: white;
  cursor: pointer;
  border-radius: 4px;
}

.filter-btn.active,
.filter-btn:hover {
  background-color: #007bff;
  color: white;
  border-color: #007bff;
}

/* 空状态 */
.empty-orders {
  text-align: center;
  padding: 50px;
}

/* 订单卡片 */
.order-card {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  margin-bottom: 20px;
  background: white;
  overflow: hidden;
  box-shadow: 0 2px 5px rgba(0,0,0,0.05);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background-color: #f8f9fa;
  border-bottom: 1px solid #e0e0e0;
  border-radius: 8px 8px 0 0;
}

.order-info {
  display: flex;
  flex-direction: column;
  gap: 5px;
  font-size: 14px;
  color: #666;
}

.order-no {
  margin-right: 20px;
}

.order-time {
  color: #666;
  font-size: 14px;
}

.order-status {
  padding: 5px 10px;
  border-radius: 5px;
  font-size: 14px;
  font-weight: bold;
}

.status-pending { color: #ff9800; background-color: #fff5e6; }
.status-paid { color: #4caf50; background-color: #e8f5e9; }
.status-shipped { color: #2196f3; background-color: #e3f2fd; }
.status-completed { color: #9e9e9e; background-color: #f5f5f5; }
.status-cancelled { color: #f44336; background-color: #ffebee; }

/* 订单商品 */
.order-items {
  padding: 10px 0;
}

.order-item {
  display: flex;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid #f0f0f0;
}

.order-item:last-child {
  border-bottom: none;
}

.item-image {
  width: 80px;
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
  flex-grow: 1;
}

.item-info h4 {
  margin: 0 0 5px 0;
  font-size: 16px;
}

.item-info p {
  margin: 0;
  font-size: 14px;
  color: #666;
}

.item-price {
  font-size: 16px;
  font-weight: bold;
  min-width: 100px;
  text-align: right;
}

.item-actions {
  min-width: 100px;
  text-align: right;
}

.btn-review {
  background-color: #28a745;
  color: white;
}

.btn-review:hover {
  background-color: #218838;
}

.reviewed-text {
  color: #999;
  font-size: 14px;
}

/* 订单底部 */
.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background-color: #f8f9fa;
  border-top: 1px solid #e0e0e0;
}

.order-total {
  margin-bottom: 15px;
}

.order-total .total-amount {
  font-size: 18px;
  font-weight: bold;
  color: #e53935;
}

.order-actions {
  display: flex;
  gap: 10px;
}

/* 按钮样式 */
.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
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

.btn-danger {
  background-color: #dc3545;
  color: white;
}

.btn-danger:hover {
  background-color: #c82333;
}

.btn-secondary {
  background-color: #6c757d;
  color: white;
}

.btn-secondary:hover {
  background-color: #545b62;
}

.btn-outline {
  background-color: white;
  color: #007bff;
  border: 1px solid #007bff;
}

.btn-outline:hover {
  background-color: #007bff;
  color: white;
}

.btn-sm {
  padding: 4px 8px;
  font-size: 12px;
}

/* 分页 */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
}

.page-btn {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 8px 15px;
  border-radius: 4px;
  cursor: pointer;
}

.page-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.page-info {
  margin: 0 15px;
  font-size: 16px;
}

/* 弹窗样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 5px 15px rgba(0,0,0,0.3);
  width: 90%;
  max-width: 400px;
}

.modal-content h3 {
  margin-top: 0;
  text-align: center;
}

/* 支付方式 */
.payment-methods {
  margin-bottom: 20px;
}

.payment-methods label {
  display: block;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  margin-bottom: 10px;
  cursor: pointer;
}

.payment-methods label:hover {
  background-color: #f5f5f5;
}

.payment-methods input[type="radio"] {
  margin-right: 10px;
}

/* 评价弹窗 */
.review-modal {
  max-width: 500px;
}

.review-form {
  margin-bottom: 20px;
}

.review-form .book-info h4 {
  margin: 0 0 5px 0;
  color: #333;
}

.review-form .book-info p {
  margin: 0 0 15px 0;
  color: #666;
}

.rating-section,
.content-section,
.anonymous-section {
  margin-bottom: 15px;
}

.rating-section label,
.content-section label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.star-rating {
  display: inline-block;
}

.star-rating .star {
  cursor: pointer;
  font-size: 24px;
  color: #ccc;
}

.star-rating .star.active {
  color: #ffc107;
}

.content-section textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.anonymous-section label {
  display: flex;
  align-items: center;
  gap: 5px;
  cursor: pointer;
}

.modal-actions {
  margin-top: 20px;
  text-align: right;
}

.modal-actions button {
  margin-left: 10px;
}

.status-text {
  color: #555;
  font-weight: bold;
}
</style> 