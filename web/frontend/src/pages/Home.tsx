import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import Button from '../components/common/Button'
import Tag from '../components/common/Tag'
import styles from './Home.module.css'

interface PostSummary {
  id: string
  title: string
  date: string
  tags: string[]
  excerpt: string
}

export default function Home() {
  const [posts, setPosts] = useState<PostSummary[]>([])

  useEffect(() => {
    fetch('/api/posts')
      .then(res => res.json())
      .then((data: PostSummary[]) => setPosts(data.slice(0, 3)))
      .catch(() => {})
  }, [])

  return (
    <div className={styles.page}>
      {/* ── Hero ── */}
      <section className={styles.hero}>
        <div className="container">
          <div className={styles.terminalLine}>
            <span className={styles.prompt}>$</span>
            <span className={styles.cmd}> whoami</span>
          </div>

          <h1 className={styles.title}>
            <span className={styles.accent}>jkypch</span>.com
          </h1>

          <p className={styles.subtitle}>My personal site</p>

          <div className={styles.actions}>
            <Button
              as="a"
              href="https://jky99.github.io/"
              target="_blank"
              rel="noopener noreferrer"
              variant="primary"
            >
              Portfolio
            </Button>
            <Button as={Link} to="/posts" variant="secondary">
              Posts
            </Button>
          </div>
        </div>
      </section>

      {/* ── Recent Posts ── */}
      <section className={`${styles.section} section`}>
        <div className="container">
          <div className={styles.sectionHeader}>
            <h2 className={styles.sectionTitle}>
              <span className={styles.prompt}>#</span> Recent Posts
            </h2>
            <Link to="/posts" className={styles.viewAll}>모두 보기 →</Link>
          </div>
          {posts.length === 0 ? (
            <div className={styles.emptyState}>
              <div className={styles.emptyIcon}>✍️</div>
              <p>포스팅 준비 중입니다.</p>
            </div>
          ) : (
            <div className={styles.grid}>
              {posts.map(post => (
                <Link key={post.id} to={`/posts/${post.id}`} className={styles.cardLink}>
                  <article className={styles.card}>
                    <div className={styles.cardMeta}>
                      <time className={styles.date}>{post.date}</time>
                    </div>
                    <h3 className={styles.cardTitle}>{post.title}</h3>
                    <p className={styles.excerpt}>{post.excerpt}</p>
                    <div className={styles.tags}>
                      {post.tags.map(t => <Tag key={t}>{t}</Tag>)}
                    </div>
                  </article>
                </Link>
              ))}
            </div>
          )}
        </div>
      </section>
    </div>
  )
}
