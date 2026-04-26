import NotificationLogItem from './NotificationLogItem'

export default function NotificationLogList({ logs }) {
  /*
   * Renders list of notification logs.
   */
  return (
    <div className="card">
      <h2>Notification History</h2>

      {logs.length === 0 && <p>No logs available.</p>}

      <div className="log-list">
        {logs.map((log) => (
          <NotificationLogItem key={log.id} log={log} />
        ))}
      </div>
    </div>
  )
}
