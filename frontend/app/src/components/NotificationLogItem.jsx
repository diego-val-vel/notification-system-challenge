import { formatDate } from '../utils/formatDate'

export default function NotificationLogItem({ log }) {
  /*
   * Determines visual status color.
   */
  const statusClass =
    log.status === 'SUCCESS' ? 'status success' : 'status failed'

  return (
    <div className="log-item">
      <div className="log-header">
        <span className={statusClass}>{log.status}</span>
        <span>{formatDate(log.createdAt)}</span>
      </div>

      <div className="log-body">
        <p><strong>Category:</strong> {log.categoryCode}</p>
        <p><strong>Message:</strong> {log.message}</p>
        <p><strong>Channel:</strong> {log.channelCode}</p>
        <p><strong>User:</strong> {log.userName}</p>
        <p><strong>Attempts:</strong> {log.attempts}</p>

        {log.errorMessage && (
          <p className="error"><strong>Error:</strong> {log.errorMessage}</p>
        )}
      </div>
    </div>
  )
}
