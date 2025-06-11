<template>
  <div id="edit-book">
    <!-- 省略导航 -->
    <div class="container">
      <form @submit.prevent="handleUpdate" class="upload-form">
        <p>
          <span class="pic-icon"></span>
          修改书图片： <input type="file" ref="fileInput" @change="previewImage" />
          <img :src="previewUrl" class="preview" />
        </p>
        <p><span class="name-icon"></span>书名：<input v-model="form.name" type="text" /></p>
        <p><span class="author-icon"></span>作者：<input v-model="form.author" type="text" /></p>
        <p><span class="name-icon"></span>分类：
          <select v-model="form.cid">
            <option v-for="cat in categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
          </select>
        </p>
        <p><span class="origin-icon"></span>原价：<input v-model="form.originalPrice" type="text" /></p>
        <p><span class="price-icon"></span>售价：<input v-model="form.price" type="text" /></p>
        <p><span class="press-icon"></span>出版社：<input v-model="form.press" type="text" /></p>
        <p><span class="time-icon"></span>出版时间：<input v-model="form.publishDate" type="month" /></p>
        <p><span class="version-icon"></span>版本：<input v-model="form.version" type="text" /></p>
        <p><span class="condition-icon"></span>品相：
          <select v-model="form.degree">
            <option value="">请选择品相</option>
            <option value="9.9">全新（近10成新）</option>
            <option value="9.0">九成新</option>
            <option value="8.0">八成新</option>
            <option value="7.0">七成新</option>
            <option value="6.0">六成新</option>
            <option value="5.0">五成新及以下</option>
          </select>
        </p>
        <p><span class="desc-icon"></span>描述：<input v-model="form.description" type="text" /></p>
        <input class="submit-btn" type="submit" value="更新" />
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const categories = ref([])
const form = ref({ id: null, name: '', author: '', cid: null, originalPrice: '', price: '', press: '', publishDate: '', version: '', degree: '', description: '' })
const previewUrl = ref('')
const fileInput = ref(null)

async function fetchCategories() {
  const res = await axios.get('/api/category/list')
  categories.value = res.data.data
}

async function fetchBook() {
  const res = await axios.get(`/api/book/${route.params.id}`)
  const book = res.data.data
  form.value = { id: book.id, name: book.name, author: book.author, cid: book.cid, originalPrice: book.originalPrice, price: book.price, press: book.press, publishDate: book.publishDate, version: book.version, degree: book.degree, description: book.description }
  previewUrl.value = `/img/book-list/article/${book.bookImage.id}.jpg`
}

function previewImage() {
  const file = fileInput.value.files[0]
  previewUrl.value = URL.createObjectURL(file)
}

async function handleUpdate() {
  // 更新书本信息
  await axios.put('/api/book', form.value)
  // 如果选择了新图片则上传
  if (fileInput.value.files.length) {
    const data = new FormData()
    data.append('file', fileInput.value.files[0])
    await axios.post('/api/file/upload', data, { headers: { 'Content-Type': 'multipart/form-data' } })
    await axios.post('/api/bookImage', { bid: form.value.id, type: 1 })
  }
  alert('更新成功')
  router.push('/myBookshelf')
}

onMounted(async () => {
  await fetchCategories()
  await fetchBook()
})
</script>

<style scoped>
@import '/css/reset.css';
@import '/css/upload.css';
</style> 