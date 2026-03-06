import { useEffect, useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import ReactMarkdown from 'react-markdown'
import remarkGfm from 'remark-gfm'
import Tag from '../components/common/Tag'
import styles from './PostDetail.module.css'

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
          <ReactMarkdown remarkPlugins={[remarkGfm]}>
            {post.content}
          </ReactMarkdown>
        </article>
      </div>
    </div>
  )
}
