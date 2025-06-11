<template>
  <div class="modern-cart">
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
          <router-link to="/bookstore" class="back-btn">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
              <path d="M19 12H5M12 19l-7-7 7-7" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            继续购物
          </router-link>
        </div>
      </nav>
    </header>

    <!-- 主要内容区域 -->
    <main class="main-content">
      <div class="cart-container">
        <!-- 页面标题 -->
        <div class="page-header">
          <div class="header-icon">
            <svg width="48" height="48" viewBox="0 0 24 24" fill="none">
              <path d="m2 2 3.6 7.4a2 2 0 0 0 1.8 1.1h9.2a2 2 0 0 0 1.9-1.4L21 4H6" stroke="currentColor" stroke-width="2"/>
              <circle cx="9" cy="21" r="1" stroke="currentColor" stroke-width="2"/>
              <circle cx="20" cy="21" r="1" stroke="currentColor" stroke-width="2"/>
            </svg>
          </div>
          <div class="header-text">
            <h1>我的购物车</h1>
            <p>{{ cartList.length > 0 ? `共 ${cartList.length} 件商品` : '购物车是空的' }}</p>
          </div>
        </div>

        <!-- 空购物车状态 -->
        <div v-if="cartList.length === 0" class="empty-cart">
          <div class="empty-icon">🛒</div>
          <h3>购物车是空的</h3>
          <p>还没有添加任何图书，去挑选一些好书吧！</p>
          <router-link to="/bookstore" class="empty-action-btn">
            开始购书
          </router-link>
        </div>

        <!-- 购物车内容 -->
        <div v-else class="cart-content">
          <!-- 购物车操作栏 -->
          <div class="cart-toolbar">
            <label class="select-all-container">
              <input type="checkbox" v-model="selectAll" @change="toggleSelectAll" class="checkbox-input">
              <span class="checkbox-custom"></span>
              <span class="checkbox-label">全选</span>
            </label>
            
            <div class="toolbar-info">
              <span class="selected-count">已选择 {{ selectedItems.length }} 件商品</span>
            </div>
            
            <button @click="clearCart" class="clear-btn" v-if="cartList.length > 0">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                <path d="M3 6h18M8 6V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2M19 6l-1 14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2L5 6" stroke="currentColor" stroke-width="2"/>
              </svg>
              清空购物车
            </button>
          </div>

          <!-- 购物车商品列表 -->
          <div class="cart-items">
            <div v-for="item in cartList" :key="item.id" class="cart-item">
              <label class="item-select">
                <input type="checkbox" v-model="item.selected" @change="updateSelectStatus" class="checkbox-input">
                <span class="checkbox-custom"></span>
              </label>

              <div class="item-image">
                <img 
                  :src="getBookImageSrc(item)" 
                  :alt="item.book?.name" 
                  @error="handleImageError"
                  class="book-cover"
                />
              </div>

              <div class="item-details">
                <h4 class="book-title">{{ item.book?.name }}</h4>
                <div class="book-meta">
                  <span class="book-author">{{ item.book?.author }}</span>
                  <span class="book-press">{{ item.book?.press }}</span>
                </div>
                <div class="book-condition">
                  <span class="condition-badge">{{ item.book?.degree }}成新</span>
                </div>
              </div>

              <div class="item-price">
                <span class="current-price">¥{{ item.book?.price }}</span>
              </div>

              <div class="item-quantity">
                <div class="quantity-controls">
                  <button 
                    @click="decreaseQuantity(item)" 
                    :disabled="item.quantity <= 1"
                    class="quantity-btn decrease"
                  >
                    <svg width="12" height="12" viewBox="0 0 24 24" fill="none">
                      <line x1="5" y1="12" x2="19" y2="12" stroke="currentColor" stroke-width="2"/>
                    </svg>
                  </button>
                  <input 
                    type="number" 
                    v-model.number="item.quantity" 
                    @change="updateQuantity(item)" 
                    min="1"
                    class="quantity-input"
                  >
                  <button 
                    @click="increaseQuantity(item)"
                    class="quantity-btn increase"
                  >
                    <svg width="12" height="12" viewBox="0 0 24 24" fill="none">
                      <line x1="12" y1="5" x2="12" y2="19" stroke="currentColor" stroke-width="2"/>
                      <line x1="5" y1="12" x2="19" y2="12" stroke="currentColor" stroke-width="2"/>
                    </svg>
                  </button>
                </div>
              </div>

              <div class="item-subtotal">
                <span class="subtotal-price">¥{{ (item.book?.price * item.quantity).toFixed(2) }}</span>
              </div>

              <div class="item-actions">
                <button @click="removeItem(item)" class="remove-btn" title="删除">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                    <path d="M3 6h18M8 6V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2M19 6l-1 14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2L5 6" stroke="currentColor" stroke-width="2"/>
                  </svg>
                </button>
              </div>
            </div>
          </div>

          <!-- AI推荐区域 -->
          <div v-if="cartList.length > 0" class="ai-recommendation-section">
            <div class="recommendation-header">
              <div class="recommendation-title">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
                  <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z" stroke="currentColor" stroke-width="2" fill="none"/>
                </svg>
                <span>AI为您推荐</span>
              </div>
              <button 
                @click="getAIRecommendations" 
                :disabled="isRecommending"
                class="recommendation-btn"
              >
                <svg v-if="!isRecommending" width="16" height="16" viewBox="0 0 24 24" fill="none">
                  <circle cx="12" cy="12" r="3" stroke="currentColor" stroke-width="2"/>
                  <path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1 0 2.83 2 2 0 0 1-2.83 0l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-2 2 2 2 0 0 1-2-2v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83 0 2 2 0 0 1 0-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1-2-2 2 2 0 0 1 2-2h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 0-2.83 2 2 0 0 1 2.83 0l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1 1.51V6a2 2 0 0 1 2-2 2 2 0 0 1 2 2v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 0 2 2 0 0 1 0 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82z" stroke="currentColor" stroke-width="2"/>
                </svg>
                <svg v-else width="16" height="16" viewBox="0 0 24 24" fill="none">
                  <circle cx="12" cy="12" r="3" stroke="currentColor" stroke-width="2"/>
                </svg>
                {{ isRecommending ? '正在分析...' : '获取推荐' }}
              </button>
            </div>
            
            <!-- 推荐结果 -->
            <div v-if="recommendations.length > 0" class="recommendations-list">
              <div class="recommendation-reason" v-if="recommendationReason">
                <p>{{ recommendationReason }}</p>
                <p class="recommendation-tip">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none">
                    <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2"/>
                    <path d="M12 16v-4M12 8h.01" stroke="currentColor" stroke-width="2"/>
                  </svg>
                  暂无现货的书籍可以点击"发布求书"按钮发布求书信息
                </p>
              </div>
              <div class="recommended-books">
                <div 
                  v-for="book in recommendations" 
                  :key="book.title" 
                  class="recommended-book"
                  :class="{ 'in-stock': book.existsInDatabase }"
                >
                  <div class="book-info">
                    <div class="book-name">{{ book.title }}</div>
                    <div class="book-author" v-if="book.author">{{ book.author }}</div>
                    <div class="book-reason" v-if="book.reason">{{ book.reason }}</div>
                    <div v-if="book.existsInDatabase" class="book-price">¥{{ book.price }}</div>
                  </div>
                  <div class="book-actions">
                    <button 
                      v-if="book.existsInDatabase" 
                      @click="addRecommendedBookToCart(book)"
                      class="add-to-cart-btn"
                    >
                      加入购物车
                    </button>
                    <div v-else class="out-of-stock-section">
                      <span class="out-of-stock">暂无现货</span>
                      <button 
                        @click="goToAskBook(book)"
                        class="ask-book-btn"
                        title="发布求书信息寻找这本书"
                      >
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                          <path d="M12 5v14M5 12h14" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                        </svg>
                        发布求书
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- 推荐错误信息 -->
            <div v-if="recommendationError" class="recommendation-error">
              <p>{{ recommendationError }}</p>
              <button @click="getAIRecommendations" class="retry-btn">重试</button>
            </div>
          </div>

          <!-- 结算区域 -->
          <div class="checkout-section">
            <div class="checkout-summary">
              <div class="summary-row">
                <span class="summary-label">商品总价：</span>
                <span class="summary-value">¥{{ totalPrice.toFixed(2) }}</span>
              </div>
              <div class="summary-row">
                <span class="summary-label">运费：</span>
                <span class="summary-value free">包邮</span>
              </div>
              <div class="summary-row total">
                <span class="summary-label">应付总额：</span>
                <span class="summary-value total-price">¥{{ totalPrice.toFixed(2) }}</span>
              </div>
            </div>
            
            <button 
              @click="checkout" 
              :disabled="selectedItems.length === 0 || isProcessing"
              class="checkout-btn"
            >
              <svg v-if="!isProcessing" width="20" height="20" viewBox="0 0 24 24" fill="none">
                <path d="M20 6L9 17l-5-5" stroke="currentColor" stroke-width="2"/>
              </svg>
              <svg v-else width="20" height="20" viewBox="0 0 24 24" fill="none">
                <circle cx="12" cy="12" r="3" stroke="currentColor" stroke-width="2"/>
              </svg>
              {{ isProcessing ? '处理中...' : `结算 (${selectedItems.length})` }}
            </button>
          </div>
        </div>
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
        <p>&copy; 2025 二手书院. 保留所有权利.</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

// 导入图片资源
import logo2 from '/img/logo2.jpg'

const router = useRouter()
const cartList = ref([])
const selectAll = ref(false)
const isProcessing = ref(false)

// AI推荐相关状态
const isRecommending = ref(false)
const recommendations = ref([])
const recommendationReason = ref('')
const recommendationError = ref('')

// 计算属性
const selectedItems = computed(() => {
  return cartList.value.filter(item => item.selected)
})

const totalPrice = computed(() => {
  return selectedItems.value.reduce((total, item) => {
    return total + (item.book?.price || 0) * item.quantity
  }, 0)
})

// 页面加载时获取购物车数据
onMounted(() => {
  loadCartData()
})

// 加载购物车数据
const loadCartData = async () => {
  try {
    const res = await axios.get('/api/cart/list')
    
    if (res.data.code === 200 || res.data.resultCode === 200) {
      const cartData = res.data.data || []
      
      cartList.value = cartData.map(item => {
        return {
          ...item,
          selected: false
        }
      })
      
    } else {
      console.error('加载购物车失败:', res.data.message)
    }
  } catch (error) {
    console.error('加载购物车失败:', error)
  }
}

// 全选/取消全选
const toggleSelectAll = () => {
  cartList.value.forEach(item => {
    item.selected = selectAll.value
  })
}

// 更新选择状态
const updateSelectStatus = () => {
  const selectedCount = cartList.value.filter(item => item.selected).length
  selectAll.value = selectedCount === cartList.value.length && cartList.value.length > 0
}

// 增加数量
const increaseQuantity = (item) => {
  item.quantity++
  updateQuantity(item)
}

// 减少数量
const decreaseQuantity = (item) => {
  if (item.quantity > 1) {
    item.quantity--
    updateQuantity(item)
  }
}

// 更新数量
const updateQuantity = async (item) => {
  try {
    const res = await axios.put('/api/cart/update', null, {
      params: {
        cartId: item.id,
        quantity: item.quantity
      }
    })
    if (res.data.code !== 200 && res.data.resultCode !== 200) {
      console.error('更新数量失败:', res.data.message)
      loadCartData() // 重新加载数据
    }
  } catch (error) {
    console.error('更新数量失败:', error)
    loadCartData()
  }
}

// 删除商品
const removeItem = async (item) => {
  if (confirm('确定要删除这件商品吗？')) {
    try {
      const res = await axios.delete(`/api/cart/remove/${item.id}`)
      if (res.data.code === 200 || res.data.resultCode === 200) {
        loadCartData()
      } else {
        alert(res.data.message || '删除失败')
      }
    } catch (error) {
      console.error('删除失败:', error)
      alert('删除失败')
    }
  }
}

// 清空购物车
const clearCart = async () => {
  if (confirm('确定要清空购物车吗？')) {
    try {
      const res = await axios.delete('/api/cart/clear')
      if (res.data.code === 200 || res.data.resultCode === 200) {
        cartList.value = []
      } else {
        alert(res.data.message || '清空失败')
      }
    } catch (error) {
      console.error('清空失败:', error)
      alert('清空失败')
    }
  }
}

// 获取AI推荐
const getAIRecommendations = async () => {
  if (cartList.value.length === 0) {
    recommendationError.value = '购物车为空，无法获取推荐'
    return
  }

  isRecommending.value = true
  recommendationError.value = ''
  recommendations.value = []
  recommendationReason.value = ''

  try {
    const res = await axios.get('/api/recommendation/cart-based')

    if (res.data.code === 200 || res.data.resultCode === 200) {
      const data = res.data.data
      if (data && data.recommendedBooks && data.recommendedBooks.length > 0) {
        recommendations.value = data.recommendedBooks
        recommendationReason.value = data.recommendationReason || 'AI为您推荐以下书籍：'
      } else {
        recommendationError.value = '暂无推荐结果，请稍后再试'
      }
    } else {
      recommendationError.value = res.data.message || 'AI推荐服务暂时不可用'
    }
  } catch (error) {
    console.error('获取AI推荐失败:', error)
    if (error.response && error.response.status === 401) {
      recommendationError.value = '请先登录后再使用推荐功能'
    } else {
      recommendationError.value = 'AI推荐服务暂时不可用，请稍后再试'
    }
  } finally {
    isRecommending.value = false
  }
}

// 将推荐的书籍加入购物车
const addRecommendedBookToCart = async (book) => {
  // 优先使用bookId，如果没有则使用id
  const actualBookId = book.bookId || book.id
  
  if (!book.existsInDatabase || !actualBookId) {
    alert('该书籍暂无现货')
    return
  }

  try {
    const res = await axios.post('/api/cart/add', null, {
      params: {
        bookId: actualBookId,
        quantity: 1
      }
    })

    if (res.data.code === 200 || res.data.resultCode === 200) {
      alert(`《${book.title}》已加入购物车`)
      // 重新加载购物车数据
      await loadCartData()
    } else {
      alert(res.data.message || '加入购物车失败')
    }
  } catch (error) {
    console.error('加入购物车失败:', error)
    alert('加入购物车失败: ' + (error.response?.data?.message || error.message))
  }
}

// 获取图书图片
function getBookImageSrc(item) {
  return `/img/book-list/article/${item.book?.bookImage?.id || 'default'}.jpg`
}

// 图片加载失败处理
const handleImageError = (event) => {
  event.target.src = '/img/book-list/article/default.jpg'
}

// 去结算
const checkout = async () => {
  if (selectedItems.value.length === 0) {
    alert('请选择要购买的商品')
    return
  }
  
  isProcessing.value = true
  
  try {
    // 这里可以实现订单确认逻辑
    // 暂时先简单显示提示
    alert(`已选择 ${selectedItems.value.length} 件商品，总价 ¥${totalPrice.value.toFixed(2)}`)
    
    // 获取选中商品的购物车ID
    const cartIds = selectedItems.value.map(item => item.id)
    
    // 可以跳转到订单确认页面
    // router.push({
    //   path: '/order/confirm',
    //   query: { cartIds: cartIds.join(',') }
    // })
  } catch (error) {
    console.error('结算失败:', error)
    alert('结算失败，请重试')
  } finally {
    isProcessing.value = false
  }
}

// 跳转到发布求书页面
const goToAskBook = (book) => {
  // 跳转到发布求书页面，携带书籍信息
  router.push({
    path: '/uploadAsk',
    query: {
      prefillTitle: book.title,
      prefillAuthor: book.author || '',
      fromRecommendation: 'true'
    }
  })
}
</script>

<style scoped>
.modern-cart {
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
  width: 80px;
  height: 40px;
  margin-right: 0.75rem;
  border-radius: 8px;
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
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

.cart-container {
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

/* 空购物车状态 */
.empty-cart {
  text-align: center;
  padding: 4rem 2rem;
  color: #64748b;
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
}

.empty-cart h3 {
  font-size: 1.5rem;
  color: #1a202c;
  margin-bottom: 1rem;
}

.empty-cart p {
  font-size: 1.125rem;
  margin-bottom: 2rem;
}

.empty-action-btn {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  text-decoration: none;
  padding: 1rem 2rem;
  border-radius: 12px;
  font-weight: 600;
  transition: all 0.3s ease;
}

.empty-action-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
}

/* 购物车内容 */
.cart-content {
  padding: 2rem;
}

/* 购物车工具栏 */
.cart-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem 1.5rem;
  background: #f8f9fa;
  border-radius: 12px;
  margin-bottom: 1.5rem;
  border: 1px solid #e2e8f0;
}

.select-all-container {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  cursor: pointer;
  font-weight: 500;
  color: #2d3748;
}

.toolbar-info {
  flex: 1;
  text-align: center;
  color: #64748b;
}

.selected-count {
  font-weight: 500;
}

.clear-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: none;
  border: 1px solid #ef4444;
  color: #ef4444;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  font-size: 0.875rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.clear-btn:hover {
  background: #ef4444;
  color: white;
}

/* 复选框样式 */
.checkbox-input {
  display: none;
}

.checkbox-custom {
  width: 18px;
  height: 18px;
  border: 2px solid #cbd5e0;
  border-radius: 4px;
  display: inline-block;
  position: relative;
  transition: all 0.3s ease;
}

.checkbox-input:checked + .checkbox-custom {
  background: #667eea;
  border-color: #667eea;
}

.checkbox-input:checked + .checkbox-custom::after {
  content: '✓';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: white;
  font-size: 12px;
  font-weight: bold;
}

/* 购物车商品列表 */
.cart-items {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-bottom: 2rem;
}

.cart-item {
  display: flex;
  align-items: center;
  gap: 1.5rem;
  padding: 1.5rem;
  background: white;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
  transition: all 0.3s ease;
}

.cart-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.item-select {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.item-image {
  flex-shrink: 0;
}

.book-cover {
  width: 80px;
  height: 120px;
  object-fit: cover;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.item-details {
  flex: 1;
  min-width: 0;
}

.book-title {
  font-size: 1.125rem;
  font-weight: 600;
  color: #1a202c;
  margin: 0 0 0.5rem 0;
  line-height: 1.4;
}

.book-meta {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
  margin-bottom: 0.5rem;
}

.book-author,
.book-press {
  color: #64748b;
  font-size: 0.875rem;
}

.condition-badge {
  background: linear-gradient(135deg, #10b981, #059669);
  color: white;
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
  width: fit-content;
}

.item-price {
  text-align: center;
}

.current-price {
  font-size: 1.25rem;
  font-weight: 700;
  color: #ef4444;
}

.item-quantity {
  text-align: center;
}

.quantity-controls {
  display: flex;
  align-items: center;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  overflow: hidden;
}

.quantity-btn {
  width: 32px;
  height: 32px;
  border: none;
  background: #f8f9fa;
  color: #64748b;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.quantity-btn:hover:not(:disabled) {
  background: #e2e8f0;
  color: #475569;
}

.quantity-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.quantity-input {
  width: 60px;
  height: 32px;
  border: none;
  text-align: center;
  background: white;
  color: #1a202c;
  font-weight: 500;
}

.quantity-input:focus {
  outline: 2px solid #3b82f6;
  outline-offset: -2px;
}

.item-subtotal {
  text-align: center;
  min-width: 100px;
}

.subtotal-price {
  font-size: 1.125rem;
  font-weight: 700;
  color: #1a202c;
}

.item-actions {
  text-align: center;
}

.remove-btn {
  width: 36px;
  height: 36px;
  border: none;
  background: #fee2e2;
  color: #ef4444;
  border-radius: 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.remove-btn:hover {
  background: #fecaca;
}

/* AI推荐区域 */
.ai-recommendation-section {
  background: linear-gradient(135deg, #f8fafc, #e2e8f0);
  border-radius: 16px;
  padding: 2rem;
  margin-bottom: 2rem;
  border: 1px solid #e2e8f0;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.recommendation-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.recommendation-title {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  font-size: 1.25rem;
  font-weight: 700;
  color: #1a202c;
}

.recommendation-title svg {
  color: #f59e0b;
}

.recommendation-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: linear-gradient(135deg, #f59e0b, #d97706);
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 10px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.recommendation-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(245, 158, 11, 0.3);
}

.recommendation-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.recommendations-list {
  margin-top: 1.5rem;
}

.recommendation-reason {
  background: rgba(245, 158, 11, 0.1);
  border-left: 4px solid #f59e0b;
  padding: 1rem;
  border-radius: 8px;
  margin-bottom: 1.5rem;
}

.recommendation-reason p {
  margin: 0;
  color: #92400e;
  font-weight: 500;
  line-height: 1.6;
}

.recommendation-tip {
  margin: 0.5rem 0 0 0;
  color: #64748b;
  font-size: 0.875rem;
  line-height: 1.5;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem;
  background: rgba(100, 116, 139, 0.1);
  border-radius: 8px;
  border-left: 3px solid #667eea;
}

.recommended-books {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1rem;
}

.recommended-book {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  border: 1px solid #e2e8f0;
  transition: all 0.3s ease;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.recommended-book:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
}

.recommended-book.in-stock {
  border-color: #10b981;
  background: linear-gradient(135deg, #f0fdf4, #dcfce7);
}

.book-info {
  flex: 1;
}

.book-name {
  font-weight: 700;
  color: #1a202c;
  margin-bottom: 0.5rem;
  font-size: 1.1rem;
}

.book-author {
  color: #64748b;
  font-size: 0.9rem;
  margin-bottom: 0.5rem;
}

.book-reason {
  color: #475569;
  font-size: 0.875rem;
  line-height: 1.5;
  margin-bottom: 0.75rem;
}

.book-price {
  color: #ef4444;
  font-weight: 700;
  font-size: 1.1rem;
}

.book-actions {
  margin-left: 1rem;
  display: flex;
  align-items: center;
}

.add-to-cart-btn {
  background: linear-gradient(135deg, #10b981, #059669);
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 0.875rem;
}

.add-to-cart-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.3);
}

.out-of-stock-section {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.out-of-stock {
  color: #64748b;
  font-size: 0.875rem;
  font-style: italic;
}

.ask-book-btn {
  background: linear-gradient(135deg, #667eea, #764ba2);
  border: none;
  color: white;
  cursor: pointer;
  font-size: 0.875rem;
  font-weight: 500;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  display: flex;
  align-items: center;
  gap: 0.25rem;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);
}

.ask-book-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.recommendation-error {
  background: #fef2f2;
  border: 1px solid #fecaca;
  border-radius: 8px;
  padding: 1rem;
  text-align: center;
}

.recommendation-error p {
  margin: 0 0 1rem 0;
  color: #dc2626;
}

.retry-btn {
  background: #ef4444;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s ease;
}

.retry-btn:hover {
  background: #dc2626;
}

/* 结算区域 */
.checkout-section {
  background: #f8f9fa;
  border-radius: 12px;
  padding: 1.5rem;
  border: 1px solid #e2e8f0;
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  gap: 2rem;
}

.checkout-summary {
  flex: 1;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

.summary-row.total {
  margin-top: 1rem;
  padding-top: 1rem;
  border-top: 1px solid #e2e8f0;
  font-size: 1.125rem;
  font-weight: 700;
}

.summary-label {
  color: #64748b;
}

.summary-value {
  color: #1a202c;
  font-weight: 600;
}

.summary-value.free {
  color: #10b981;
}

.total-price {
  color: #ef4444;
  font-size: 1.5rem;
}

.checkout-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  border: none;
  padding: 1rem 2rem;
  border-radius: 12px;
  font-weight: 600;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 160px;
  justify-content: center;
}

.checkout-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
}

.checkout-btn:disabled {
  opacity: 0.6;
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
  
  .cart-content {
    padding: 1rem;
  }
  
  .cart-toolbar {
    flex-direction: column;
    gap: 1rem;
    text-align: center;
  }
  
  .cart-item {
    flex-direction: column;
    text-align: center;
    gap: 1rem;
  }
  
  .item-details {
    text-align: center;
  }
  
  .checkout-section {
    flex-direction: column;
    gap: 1rem;
  }
  
  .checkout-btn {
    width: 100%;
  }
}
</style> 