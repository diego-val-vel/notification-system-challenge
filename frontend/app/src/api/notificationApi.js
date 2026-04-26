const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

async function parseResponse(response) {
  const data = await response.json()

  if (!response.ok) {
    const message = data?.message || 'Unexpected API error.'
    throw new Error(message)
  }

  return data
}

export async function createNotification(payload) {
  const response = await fetch(`${API_BASE_URL}/api/notifications`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
  })

  return parseResponse(response)
}

export async function getNotificationLogs() {
  const response = await fetch(`${API_BASE_URL}/api/notifications/logs`)

  return parseResponse(response)
}
