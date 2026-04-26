import { useState } from 'react'
import { categories } from '../constants/categories'

export default function NotificationForm({ onSubmit, loading }) {
  const [categoryCode, setCategoryCode] = useState('')
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')

  /*
   * Handles form submission with basic validation.
   */
  function handleSubmit(event) {
    event.preventDefault()

    if (!categoryCode) {
      setError('Category is required.')
      return
    }

    if (!message.trim()) {
      setError('Message is required.')
      return
    }

    setError('')

    onSubmit({
      categoryCode,
      message,
    })

    setMessage('')
  }

  return (
    <div className="card">
      <h2>Send Notification</h2>

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Category</label>
          <select
            value={categoryCode}
            onChange={(e) => setCategoryCode(e.target.value)}
          >
            <option value="">Select category</option>
            {categories.map((category) => (
              <option key={category.value} value={category.value}>
                {category.label}
              </option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label>Message</label>
          <textarea
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            placeholder="Enter your message..."
          />
        </div>

        {error && <p className="error">{error}</p>}

        <button type="submit" disabled={loading}>
          {loading ? 'Sending...' : 'Send Notification'}
        </button>
      </form>
    </div>
  )
}
