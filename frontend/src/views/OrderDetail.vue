<template>
  <div class="order-detail-container" v-if="order">
    <h2>订单详情</h2>
    
    <div class="order-summary-card">
        <div class="summary-header">
            <h3>订单状态: <span :class="getStatusClass(order.status)">{{ getStatusText(order.status) }}</span></h3>
            <p>订单号: {{ order.orderNo }}</p>
            <p>下单时间: {{ formatDate(order.createTime) }}</p>
        </div>

        <div class="summary-grid">
            <div class="grid-item">
                <h4>收货信息</h4>
                <p>{{ order.deliveryName }}</p>
                <p>{{ order.deliveryPhone }}</p>
                <p>{{ order.deliveryAddress }}</p>
            </div>
            <div class="grid-item">
                <h4>支付信息</h4>
                <p>支付方式: {{ order.paymentMethod || '未指定' }}</p>
                <p>支付时间: {{ order.paymentTime ? formatDate(order.paymentTime) : '未支付' }}</p>
                <p>总金额: <span class="price">¥{{ order.totalAmount.toFixed(2) }}</span></p>
            </div>
            <div class="grid-item">
                <h4>物流信息</h4>
                <p>发货时间: {{ order.deliveryTime ? formatDate(order.deliveryTime) : '待发货' }}</p>
                <p>完成时间: {{ order.finishTime ? formatDate(order.finishTime) : '未完成' }}</p>
                 <p>备注: {{ order.remark || '无' }}</p>
            </div>
        </div>
    </div>

    <div class="items-card">
        <h3>商品列表</h3>
        <div v-for="item in order.orderItems" :key="item.id" class="order-item">
            <img :src="`/api/bookImage/${item.bookId}/0`" :alt="item.bookName" class="item-image" @error="handleImageError"/>
            <div class="item-info">
                <h4>{{ item.bookName }}</h4>
                <p>作者: {{ item.bookAuthor }}</p>
                <p>出版社: {{ item.bookPress }}</p>
            </div>
            <div class="item-price-quantity">
                <p>¥{{ item.bookPrice.toFixed(2) }}</p>
                <p>x{{ item.quantity }}</p>
            </div>
        </div>
    </div>

    <div class="actions-card">
      <button @click="router.push('/orders')" class="btn btn-primary">返回订单列表</button>
    </div>
  </div>
  <div v-else-if="loading" class="loading">
    <p>正在加载订单详情...</p>
  </div>
  <div v-else class="error">
    <p>{{ errorMessage }}</p>
    <router-link to="/orders">返回订单列表</router-link>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const router = useRouter();

const order = ref(null);
const loading = ref(true);
const errorMessage = ref('');

const fetchOrderDetail = async () => {
  const orderId = route.params.id;
  if (!orderId) {
    errorMessage.value = '无效的订单ID';
    loading.value = false;
    return;
  }
  try {
    const res = await axios.get(`/api/order/${orderId}`);
    if (res.data.resultCode === 200) {
      order.value = res.data.data;
    } else {
      errorMessage.value = res.data.message || '获取订单详情失败';
    }
  } catch (err) {
    console.error('获取订单详情异常:', err);
    errorMessage.value = '网络错误，请稍后再试';
  } finally {
    loading.value = false;
  }
};

onMounted(fetchOrderDetail);

const formatDate = (dateString) => {
  if (!dateString) return '';
  return new Date(dateString).toLocaleString();
};

const getStatusText = (status) => {
    const statusMap = {
        1: '待支付',
        2: '待发货',
        3: '已发货',
        4: '已完成',
        5: '已取消',
    };
    return statusMap[status] || '未知状态';
};

const getStatusClass = (status) => {
    const classMap = {
        1: 'status-pending',
        2: 'status-paid',
        3: 'status-shipped',
        4: 'status-completed',
        5: 'status-cancelled',
    };
    return classMap[status] || '';
};

const handleImageError = (event) => {
  event.target.src = '/default-book.png';
};
</script>

<style scoped>
.order-detail-container {
  max-width: 1000px;
  margin: 20px auto;
  padding: 20px;
}
.loading, .error {
  text-align: center;
  padding: 50px;
  font-size: 18px;
}
h2 {
    text-align: center;
    margin-bottom: 25px;
}
.order-summary-card, .items-card, .actions-card {
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.08);
    margin-bottom: 20px;
    padding: 25px;
}
.summary-header {
    border-bottom: 1px solid #f0f0f0;
    padding-bottom: 15px;
    margin-bottom: 20px;
}
.summary-header h3 {
    margin: 0;
    font-size: 20px;
}
.summary-header p {
    font-size: 14px;
    color: #666;
    margin: 5px 0 0;
}
.summary-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 20px;
}
.grid-item h4 {
    font-size: 16px;
    margin-bottom: 10px;
    color: #333;
}
.grid-item p {
    margin: 4px 0;
    font-size: 14px;
    color: #555;
}
.price {
    font-weight: bold;
    color: #e53935;
}
.items-card h3 {
    margin-top: 0;
    margin-bottom: 20px;
}
.order-item {
    display: flex;
    align-items: center;
    padding: 15px 0;
    border-bottom: 1px solid #f0f0f0;
}
.order-item:last-child {
    border-bottom: none;
}
.item-image {
    width: 80px;
    height: 80px;
    border-radius: 4px;
    margin-right: 15px;
}
.item-info {
    flex-grow: 1;
}
.item-info h4 {
    margin: 0 0 5px;
}
.item-info p {
    margin: 0;
    font-size: 13px;
    color: #777;
}
.item-price-quantity {
    text-align: right;
}
.item-price-quantity p {
    margin: 5px 0;
    font-size: 14px;
}
.actions-card {
    text-align: center;
}
.btn {
  padding: 10px 20px;
  border-radius: 5px;
  border: none;
  cursor: pointer;
}
.btn-primary {
  background-color: #007bff;
  color: white;
}
.status-pending { color: #ff9800; }
.status-paid { color: #4caf50; }
.status-shipped { color: #2196f3; }
.status-completed { color: #9e9e9e; }
.status-cancelled { color: #f44336; }
</style> 