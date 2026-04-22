const API_BASE_URL = 'http://localhost:8080/api/v1';

// ==========================================
// Students API
// ==========================================
export const studentsApi = {
  getAll: async () => {
    const res = await fetch(`${API_BASE_URL}/students`);
    if (!res.ok) throw await res.json();
    return res.json();
  },

  getById: async (id) => {
    const res = await fetch(`${API_BASE_URL}/students/${id}`);
    if (!res.ok) throw await res.json();
    return res.json();
  },

  create: async (data) => {
    const res = await fetch(`${API_BASE_URL}/students`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
    if (!res.ok) throw await res.json();
    return res.json();
  },

  updateStatus: async (id, status) => {
    const res = await fetch(`${API_BASE_URL}/students/${id}/status`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status }),
    });
    if (!res.ok) throw await res.json();
    return res.json();
  },
};

// ==========================================
// Courses API
// ==========================================
export const coursesApi = {
  getAll: async () => {
    const res = await fetch(`${API_BASE_URL}/courses`);
    if (!res.ok) throw await res.json();
    return res.json();
  },

  getById: async (id) => {
    const res = await fetch(`${API_BASE_URL}/courses/${id}`);
    if (!res.ok) throw await res.json();
    return res.json();
  },

  create: async (data) => {
    const res = await fetch(`${API_BASE_URL}/courses`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
    if (!res.ok) throw await res.json();
    return res.json();
  },

  updateStatus: async (id, status) => {
    const res = await fetch(`${API_BASE_URL}/courses/${id}/status`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status }),
    });
    if (!res.ok) throw await res.json();
    return res.json();
  },

  updateCapacity: async (id, capacity) => {
    const res = await fetch(`${API_BASE_URL}/courses/${id}/capacity`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ capacity }),
    });
    if (!res.ok) throw await res.json();
    return res.json();
  },
};

// ==========================================
// Enrollments API
// ==========================================
export const enrollmentsApi = {
  getAll: async () => {
    const res = await fetch(`${API_BASE_URL}/enrollments`);
    if (!res.ok) throw await res.json();
    return res.json();
  },

  getById: async (id) => {
    const res = await fetch(`${API_BASE_URL}/enrollments/${id}`);
    if (!res.ok) throw await res.json();
    return res.json();
  },

  create: async (data) => {
    const res = await fetch(`${API_BASE_URL}/enrollments`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
    if (!res.ok) throw await res.json();
    return res.json();
  },

  updateStatus: async (id, status) => {
    const res = await fetch(`${API_BASE_URL}/enrollments/${id}/status`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status }),
    });
    if (!res.ok) throw await res.json();
    return res.json();
  },
};
