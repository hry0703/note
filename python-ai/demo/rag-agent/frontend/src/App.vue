<script setup>
import { ref } from 'vue'

const question = ref('')
const answer = ref('')
const sources = ref([])
const loading = ref(false)
const error = ref('')

// 开发时走 Vite 代理 /api -> http://localhost:8002
const API_BASE = '/api'

async function ask() {
  if (!question.value.trim()) return
  loading.value = true
  error.value = ''
  answer.value = ''
  sources.value = []

  try {
    const res = await fetch(`${API_BASE}/ask`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ question: question.value.trim() }),
    })
    const data = await res.json()
    if (!res.ok) throw new Error(data.detail || '请求失败')
    answer.value = data.answer
    sources.value = data.sources || []
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="app">
    <header>
      <h1>文档问答 RAG Agent</h1>
      <p>基于本地文档的智能问答</p>
    </header>

    <main>
      <div class="input-wrap">
        <textarea
          v-model="question"
          placeholder="输入你的问题..."
          rows="3"
          :disabled="loading"
          @keydown.ctrl.enter="ask"
        />
        <button :disabled="loading" @click="ask">
          {{ loading ? '思考中...' : '提问' }}
        </button>
      </div>

      <p class="hint">Ctrl + Enter 发送</p>

      <div v-if="error" class="error">{{ error }}</div>

      <div v-if="answer" class="answer">
        <h3>回答</h3>
        <div class="answer-text">{{ answer }}</div>
      </div>

      <div v-if="sources.length" class="sources">
        <h3>参考来源</h3>
        <ul>
          <li v-for="(s, i) in sources" :key="i">
            <span class="source-name">{{ s.source }}</span>
            <span class="source-text">{{ s.text }}...</span>
          </li>
        </ul>
      </div>
    </main>
  </div>
</template>

<style>
* {
  box-sizing: border-box;
}
body {
  margin: 0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  background: #f5f5f5;
}
.app {
  max-width: 720px;
  margin: 0 auto;
  padding: 2rem;
}
header {
  text-align: center;
  margin-bottom: 2rem;
}
header h1 {
  margin: 0;
  font-size: 1.75rem;
  color: #333;
}
header p {
  margin: 0.5rem 0 0;
  color: #666;
  font-size: 0.95rem;
}
.input-wrap {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}
textarea {
  width: 100%;
  padding: 0.75rem 1rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 1rem;
  resize: vertical;
}
textarea:focus {
  outline: none;
  border-color: #0ea5e9;
}
button {
  padding: 0.6rem 1.2rem;
  background: #0ea5e9;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  cursor: pointer;
}
button:hover:not(:disabled) {
  background: #0284c7;
}
button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
.hint {
  margin: 0.25rem 0 0;
  font-size: 0.8rem;
  color: #999;
}
.error {
  margin-top: 1rem;
  padding: 0.75rem;
  background: #fef2f2;
  color: #dc2626;
  border-radius: 8px;
}
.answer, .sources {
  margin-top: 1.5rem;
  padding: 1rem;
  background: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.08);
}
.answer h3, .sources h3 {
  margin: 0 0 0.75rem;
  font-size: 0.95rem;
  color: #666;
}
.answer-text {
  white-space: pre-wrap;
  line-height: 1.6;
}
.sources ul {
  margin: 0;
  padding-left: 1.25rem;
}
.sources li {
  margin-bottom: 0.5rem;
}
.source-name {
  font-weight: 500;
  color: #0ea5e9;
}
.source-text {
  color: #666;
  font-size: 0.9rem;
}
</style>
