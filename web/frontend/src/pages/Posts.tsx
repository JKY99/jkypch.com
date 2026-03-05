import styles from './Posts.module.css'
import Tag from '../components/common/Tag'

// 추후 API 연동으로 교체
const POSTS: {
  id: number
  title: string
  date: string
  tags: string[]
  excerpt: string
}[] = []

export default function Posts() {
  return (
    <div className={styles.page}>
      <div className="container">
        <div className={styles.header}>
          <h1 className={styles.title}>Posts</h1>
          <p className={styles.subtitle}>개발 과정에서 배운 것들을 기록합니다.</p>
        </div>

        {POSTS.length === 0 ? (
          <div className={styles.emptyState}>
            <div className={styles.emptyTerminal}>
              <div className={styles.terminalDots}>
                <span />
                <span />
                <span />
              </div>
              <div className={styles.terminalBody}>
                <p className={styles.terminalLine}>
                  <span className={styles.prompt}>$</span>
                  <span className={styles.cmd}> ls posts/</span>
                </p>
                <p className={styles.terminalOutput}>총 0개</p>
                <p className={styles.terminalOutput}>아직 작성된 포스트가 없습니다.</p>
                <p className={styles.terminalCursor}>
                  <span className={styles.prompt}>$</span>
                  <span className={styles.cursor}>█</span>
                </p>
              </div>
            </div>
          </div>
        ) : (
          <div className={styles.grid}>
            {POSTS.map(post => (
              <article key={post.id} className={styles.card}>
                <div className={styles.cardMeta}>
                  <time className={styles.date}>{post.date}</time>
                </div>
                <h2 className={styles.cardTitle}>{post.title}</h2>
                <p className={styles.excerpt}>{post.excerpt}</p>
                <div className={styles.tags}>
                  {post.tags.map(t => <Tag key={t}>{t}</Tag>)}
                </div>
              </article>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}
