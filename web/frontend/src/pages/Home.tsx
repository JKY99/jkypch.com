import { Link } from 'react-router-dom'
import Button from '../components/common/Button'
import styles from './Home.module.css'

export default function Home() {
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
          <div className={styles.emptyState}>
            <div className={styles.emptyIcon}>✍️</div>
            <p>포스팅 준비 중입니다.</p>
          </div>
        </div>
      </section>
    </div>
  )
}
