import styles from './Button.module.css'

type ButtonVariant = 'primary' | 'secondary' | 'ghost'

// eslint-disable-next-line @typescript-eslint/no-explicit-any
type AnyProps = Record<string, any>

interface ButtonProps extends AnyProps {
  variant?: ButtonVariant
  as?: string | React.ElementType
  children: React.ReactNode
  className?: string
}

export default function Button({
  variant = 'primary',
  as: Component = 'a',
  children,
  className,
  ...props
}: ButtonProps) {
  return (
    <Component
      className={`${styles.btn} ${styles[variant]} ${className ?? ''}`}
      {...props}
    >
      {children}
    </Component>
  )
}
