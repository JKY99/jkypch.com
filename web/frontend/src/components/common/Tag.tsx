import styles from './Tag.module.css'

type TagColor = 'orange' | 'green' | 'blue' | 'purple' | 'red' | 'yellow' | 'default'

interface TagProps {
  children: React.ReactNode
  color?: TagColor
}

export default function Tag({ children, color = 'default' }: TagProps) {
  return (
    <span className={`${styles.tag} ${styles[color]}`}>
      {children}
    </span>
  )
}
