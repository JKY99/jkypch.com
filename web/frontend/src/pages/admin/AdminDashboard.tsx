import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { authFetch, logout } from '../../hooks/useAuth'
import styles from './AdminDashboard.module.css'

interface PageStat { path: string; count: number }
interface DailyStat { date: string; count: number }
interface VisitStats {
  total: number
  today: number
  uniqueIps: number
  topPages: PageStat[]
  dailyTrend: DailyStat[]
}
interface VisitLog {
  id: number
  path: string
  method: string
  ip: string
  statusCode: number
  userAgent: string
  createdAt: string
}

export default function AdminDashboard() {
  const navigate = useNavigate()
  const [stats, setStats] = useState<VisitStats | null>(null)
  const [logs, setLogs] = useState<VisitLog[]>([])
  const [username, setUsername] = useState('')
  const [pwCurrent, setPwCurrent] = useState('')
  const [pwNew, setPwNew] = useState('')
  const [pwMsg, setPwMsg] = useState('')
  const [tab, setTab] = useState<'dashboard' | 'logs'>('dashboard')

  useEffect(() => {
    authFetch('/api/admin/me').then(r => r.json()).then(d => setUsername(d.username)).catch(() => navigate('/admin/login'))
    authFetch('/api/admin/visits/stats').then(r => r.json()).then(setStats).catch(() => {})
    authFetch('/api/admin/visits/logs?limit=100').then(r => r.json()).then(setLogs).catch(() => {})
  }, [navigate])

  async function handleLogout() {
    await logout()
    navigate('/admin/login')
  }

  async function handleChangePassword(e: { preventDefault(): void }) {
    e.preventDefault()
    setPwMsg('')
    const res = await authFetch('/api/admin/password', {
      method: 'PUT',
      body: JSON.stringify({ currentPassword: pwCurrent, newPassword: pwNew }),
    })
    if (res.ok) {
      setPwMsg('비밀번호가 변경되었습니다.')
      setPwCurrent('')
      setPwNew('')
    } else {
      const msg = res.status === 400 ? '현재 비밀번호가 올바르지 않습니다.' : '변경 실패'
      setPwMsg(msg)
    }
  }

  return (
    <div className={styles.page}>
      <header className={styles.header}>
        <div className={styles.logoLine}>
          <span className={styles.prompt}>&gt;</span>
          <span className={styles.logoText}> admin</span>
          <span className={styles.cursor}>_</span>
        </div>
        <div className={styles.userBar}>
          <span className={styles.username}>{username}</span>
          <button className={styles.logoutBtn} onClick={handleLogout}>logout</button>
        </div>
      </header>

      <nav className={styles.tabs}>
        <button className={`${styles.tab} ${tab === 'dashboard' ? styles.activeTab : ''}`} onClick={() => setTab('dashboard')}>Dashboard</button>
        <button className={`${styles.tab} ${tab === 'logs' ? styles.activeTab : ''}`} onClick={() => setTab('logs')}>Logs</button>
      </nav>

      {tab === 'dashboard' && (
        <div className={styles.content}>
          <div className={styles.statsGrid}>
            <StatCard label="총 방문" value={stats?.total ?? '—'} />
            <StatCard label="오늘 방문" value={stats?.today ?? '—'} />
            <StatCard label="고유 IP" value={stats?.uniqueIps ?? '—'} />
          </div>

          <div className={styles.section}>
            <h2 className={styles.sectionTitle}>7일 방문 추이</h2>
            <div className={styles.trendTable}>
              {stats?.dailyTrend.map(d => (
                <div key={d.date} className={styles.trendRow}>
                  <span className={styles.trendDate}>{d.date}</span>
                  <div className={styles.trendBar}>
                    <div
                      className={styles.trendFill}
                      style={{ width: `${Math.min(100, (d.count / Math.max(...(stats.dailyTrend.map(x => x.count) || [1]))) * 100)}%` }}
                    />
                  </div>
                  <span className={styles.trendCount}>{d.count}</span>
                </div>
              ))}
            </div>
          </div>

          <div className={styles.section}>
            <h2 className={styles.sectionTitle}>Top Pages</h2>
            <table className={styles.table}>
              <thead>
                <tr><th>Path</th><th>Count</th></tr>
              </thead>
              <tbody>
                {stats?.topPages.map(p => (
                  <tr key={p.path}><td>{p.path}</td><td>{p.count}</td></tr>
                ))}
              </tbody>
            </table>
          </div>

          <div className={styles.section}>
            <h2 className={styles.sectionTitle}>비밀번호 변경</h2>
            <form onSubmit={handleChangePassword} className={styles.pwForm}>
              <input
                className={styles.input}
                type="password"
                placeholder="현재 비밀번호"
                value={pwCurrent}
                onChange={e => setPwCurrent(e.target.value)}
                required
              />
              <input
                className={styles.input}
                type="password"
                placeholder="새 비밀번호"
                value={pwNew}
                onChange={e => setPwNew(e.target.value)}
                required
              />
              <button className={styles.btn} type="submit">변경</button>
              {pwMsg && <p className={styles.pwMsg}>{pwMsg}</p>}
            </form>
          </div>
        </div>
      )}

      {tab === 'logs' && (
        <div className={styles.content}>
          <h2 className={styles.sectionTitle}>방문 로그</h2>
          <div className={styles.logsWrapper}>
            <table className={styles.table}>
              <thead>
                <tr><th>Time</th><th>Method</th><th>Path</th><th>Status</th><th>IP</th></tr>
              </thead>
              <tbody>
                {logs.map(log => (
                  <tr key={log.id}>
                    <td className={styles.mono}>{log.createdAt?.replace('T', ' ').substring(0, 19)}</td>
                    <td className={styles.mono}>{log.method}</td>
                    <td className={styles.mono}>{log.path}</td>
                    <td className={`${styles.mono} ${log.statusCode >= 400 ? styles.errStatus : ''}`}>{log.statusCode}</td>
                    <td className={styles.mono}>{log.ip}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  )
}

function StatCard({ label, value }: { label: string; value: number | string }) {
  return (
    <div className={styles.statCard}>
      <div className={styles.statValue}>{value}</div>
      <div className={styles.statLabel}>{label}</div>
    </div>
  )
}
