import { useEffect, useState } from 'react'
import NotificationForm from './components/NotificationForm'
import NotificationLogList from './components/NotificationLogList'
import { createNotification, getNotificationLogs } from './api/notificationApi'

export default function App() {
  const [logs, setLogs] = useState([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [successMessage, setSuccessMessage] = useState('')

  /*
   * Loads notification logs from the backend.
   */
  async function loadLogs() {
    const data = await getNotificationLogs()
    setLogs(data)
  }

  /*
   * Sends a notification and refreshes the history after completion.
   */
  async function handleCreateNotification(payload) {
    try {
      setLoading(true)
      setError('')
      setSuccessMessage('')

      await createNotification(payload)
      await loadLogs()

      setSuccessMessage('Notification processed successfully.')
    } catch (exception) {
      setError(exception.message)
    } finally {
      setLoading(false)
    }
  }

  /*
   * Loads the initial notification history when the app starts.
   */
  useEffect(() => {
    loadLogs().catch((exception) => {
      setError(exception.message)
    })
  }, [])

  return (
    <main className="app-shell">
      <section className="hero-section">
        <div>
          <p className="eyebrow">Software Engineer Challenge</p>
          <h1>Notification System</h1>
          <p className="hero-description">
            Send categorized messages to subscribed users and review delivery
            logs ordered from newest to oldest.
          </p>
        </div>
      </section>

      <section className="dashboard-grid">
        <div className="left-panel">
          <NotificationForm onSubmit={handleCreateNotification} loading={loading} />

          {successMessage && (
            <div className="feedback success-feedback">
              {successMessage}
            </div>
          )}

          {error && (
            <div className="feedback error-feedback">
              {error}
            </div>
          )}
        </div>

        <div className="right-panel">
          <NotificationLogList logs={logs} />
        </div>
      </section>
    </main>
  )
}
