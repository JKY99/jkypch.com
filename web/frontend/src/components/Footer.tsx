import styles from './Footer.module.css'

export default function Footer() {
  return (
    <footer className={styles.footer}>
      <div className={`${styles.inner} container`}>
        <span className={styles.copy}>
          &copy; {new Date().getFullYear()} 장관영
        </span>
        <div className={styles.links}>
          <a href="https://github.com/JKY99" target="_blank" rel="noopener noreferrer">
            GitHub
          </a>
          <a href="mailto:jkypch@gmail.com">Email</a>
        </div>
      </div>
    </footer>
  )
}
