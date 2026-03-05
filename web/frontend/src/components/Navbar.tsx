import { useState } from 'react'
import { Link, NavLink } from 'react-router-dom'
import styles from './Navbar.module.css'

const NAV_LINKS = [
  { to: '/', label: 'Home' },
  { to: '/posts', label: 'Posts' },
]

export default function Navbar() {
  const [open, setOpen] = useState(false)

  return (
    <header className={styles.header}>
      <nav className={`${styles.nav} container`}>
        <Link to="/" className={styles.logo}>
          <span className={styles.logoBracket}>&gt;</span>
          <span className={styles.logoText}>jkypch</span>
          <span className={styles.logoCursor}>_</span>
        </Link>

        <ul className={`${styles.links} ${open ? styles.open : ''}`}>
          {NAV_LINKS.map(({ to, label }) => (
            <li key={to}>
              <NavLink
                to={to}
                className={({ isActive }) =>
                  `${styles.link} ${isActive ? styles.active : ''}`
                }
                end={to === '/'}
                onClick={() => setOpen(false)}
              >
                {label}
              </NavLink>
            </li>
          ))}
          <li>
            <a
              href="https://jky99.github.io/"
              target="_blank"
              rel="noopener noreferrer"
              className={styles.link}
              onClick={() => setOpen(false)}
            >
              Portfolio
            </a>
          </li>
          <li>
            <a
              href="/grafana"
              target="_blank"
              rel="noopener noreferrer"
              className={styles.link}
              onClick={() => setOpen(false)}
            >
              Grafana
            </a>
          </li>
          <li>
            <a
              href="https://github.com/JKY99"
              target="_blank"
              rel="noopener noreferrer"
              className={styles.link}
              onClick={() => setOpen(false)}
            >
              GitHub
            </a>
          </li>
        </ul>

        <button
          className={styles.burger}
          onClick={() => setOpen(v => !v)}
          aria-label="메뉴 열기"
          aria-expanded={open}
        >
          <span />
          <span />
          <span />
        </button>
      </nav>
    </header>
  )
}
