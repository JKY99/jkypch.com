import { useEffect, useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import ReactMarkdown from 'react-markdown'
import remarkGfm from 'remark-gfm'
import rehypeRaw from 'rehype-raw'
import Tag from '../components/common/Tag'
import SpringBootSelector from '../components/SpringBootSelector'
import styles from './PostDetail.module.css'

/**
 * 인터랙티브 컴포넌트를 포함하는 포스트 ID 매핑
 * - 해당 ID의 포스트가 렌더링될 때 마크다운 위에 컴포넌트를 삽입
 * - 새로운 인터랙티브 포스트 추가 시 여기에 등록
 */
const POST_COMPONENTS: Record<string, React.FC> = {
  'spring-boot-version-guide': SpringBootSelector,
}

interface Post {
  id: string
  title: string
  date: string
  tags: string[]
  content: string
}

export default function PostDetail() {
  const { id } = useParams<{ id: string }>()
  const [post, setPost] = useState<Post | null>(null)
  const [loading, setLoading] = useState(true)
  const [notFound, setNotFound] = useState(false)

  useEffect(() => {
    fetch(`/api/posts/${id}`)
      .then(res => {
        if (res.status === 404) { setNotFound(true); return null }
        return res.json()
      })
      .then(data => { if (data) setPost(data) })
      .finally(() => setLoading(false))
  }, [id])

  /* details 일괄 펼침/접기 버튼 바인딩 */
  useEffect(() => {
    if (!post) return
    const toggleBtn = document.getElementById('nldd-toggle-all')
    if (!toggleBtn) return

    const handler = () => {
      const section = document.getElementById('nldd-guide-section')
      if (!section) return
      const details = section.querySelectorAll('details')
      const allOpen = Array.from(details).every(d => d.open)
      details.forEach(d => (d.open = !allOpen))
      toggleBtn.textContent = allOpen ? '모두 펼치기' : '모두 접기'
    }

    toggleBtn.addEventListener('click', handler)
    return () => toggleBtn.removeEventListener('click', handler)
  }, [post])

  if (loading) return (
    <div className={styles.page}>
      <div className="container">
        <div className={styles.loading}>로딩 중...</div>
      </div>
    </div>
  )

  if (notFound || !post) return (
    <div className={styles.page}>
      <div className="container">
        <div className={styles.notFound}>
          <p className={styles.notFoundCode}>404</p>
          <p className={styles.notFoundMsg}>포스트를 찾을 수 없습니다.</p>
          <Link to="/posts" className={styles.backLink}>← 목록으로</Link>
        </div>
      </div>
    </div>
  )

  /* 포스트 ID에 매칭되는 인터랙티브 컴포넌트가 있으면 렌더링 */
  const InteractiveComponent = post.id ? POST_COMPONENTS[post.id] : undefined

  return (
    <div className={styles.page}>
      <div className="container">
        <div className={styles.header}>
          <Link to="/posts" className={styles.backLink}>← Posts</Link>
          <time className={styles.date}>{post.date}</time>
          <h1 className={styles.title}>{post.title}</h1>
          <div className={styles.tags}>
            {post.tags.map(t => <Tag key={t}>{t}</Tag>)}
          </div>
        </div>
        <article className={styles.body}>
          {InteractiveComponent && <InteractiveComponent />}
          <ReactMarkdown remarkPlugins={[remarkGfm]} rehypePlugins={[rehypeRaw]}>
            {post.content}
          </ReactMarkdown>
        </article>
      </div>
    </div>
  )
}
