import { useState } from 'react';
import { coursesApi } from '../api';

export default function Courses({ onRefresh }) {
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    capacity: 30,
  });

  const fetchCourses = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await coursesApi.getAll();
      setCourses(data);
    } catch (err) {
      setError(err.message || 'Error al cargar cursos');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      await coursesApi.create(formData);
      setFormData({ name: '', description: '', capacity: 30 });
      setShowForm(false);
      fetchCourses();
      onRefresh?.();
    } catch (err) {
      setError(err.message || 'Error al crear curso');
    } finally {
      setLoading(false);
    }
  };

  const toggleStatus = async (course) => {
    const newStatus = course.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
    try {
      await coursesApi.updateStatus(course.id, newStatus);
      fetchCourses();
      onRefresh?.();
    } catch (err) {
      setError(err.message || 'Error al actualizar estado');
    }
  };

  return (
    <div className="bg-white rounded-xl shadow-lg p-6">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold text-gray-800 flex items-center gap-2">
          <span className="text-3xl">📚</span> Cursos
        </h2>
        <div className="flex gap-2">
          <button
            onClick={fetchCourses}
            className="px-4 py-2 bg-purple-500 text-white rounded-lg hover:bg-purple-600 transition-colors"
          >
            Cargar
          </button>
          <button
            onClick={() => setShowForm(!showForm)}
            className="px-4 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600 transition-colors"
          >
            {showForm ? 'Cancelar' : '+ Nuevo'}
          </button>
        </div>
      </div>

      {error && (
        <div className="mb-4 p-4 bg-red-100 border border-red-400 text-red-700 rounded-lg">
          {error}
        </div>
      )}

      {showForm && (
        <form onSubmit={handleSubmit} className="mb-6 p-4 bg-gray-50 rounded-lg">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <input
              type="text"
              placeholder="Nombre del curso"
              value={formData.name}
              onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
              required
            />
            <input
              type="text"
              placeholder="Descripción"
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
              required
            />
            <input
              type="number"
              placeholder="Capacidad"
              value={formData.capacity}
              onChange={(e) => setFormData({ ...formData, capacity: parseInt(e.target.value) })}
              className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
              min="1"
              required
            />
          </div>
          <button
            type="submit"
            disabled={loading}
            className="mt-4 px-6 py-2 bg-purple-500 text-white rounded-lg hover:bg-purple-600 disabled:bg-gray-400 transition-colors"
          >
            {loading ? 'Creando...' : 'Crear Curso'}
          </button>
        </form>
      )}

      {loading && !showForm ? (
        <div className="text-center py-8 text-gray-500">Cargando...</div>
      ) : courses.length === 0 ? (
        <div className="text-center py-8 text-gray-400">
          No hay cursos. Haz clic en "Cargar" para obtener datos.
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {courses.map((course) => (
            <div
              key={course.id}
              className="border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow"
            >
              <div className="flex justify-between items-start mb-2">
                <h3 className="font-semibold text-gray-800">{course.name}</h3>
                <span
                  className={`px-2 py-1 rounded-full text-xs font-medium ${
                    course.status === 'ACTIVE'
                      ? 'bg-green-100 text-green-800'
                      : 'bg-red-100 text-red-800'
                  }`}
                >
                  {course.status}
                </span>
              </div>
              <p className="text-sm text-gray-600 mb-3">{course.description}</p>
              <div className="flex justify-between items-center">
                <div className="text-sm">
                  <span className="text-gray-500">Capacidad:</span>{' '}
                  <span className="font-medium text-gray-700">
                    {course.enrolledCount || 0}/{course.capacity}
                  </span>
                </div>
                <button
                  onClick={() => toggleStatus(course)}
                  className={`px-3 py-1 rounded-lg text-xs font-medium transition-colors ${
                    course.status === 'ACTIVE'
                      ? 'bg-red-500 text-white hover:bg-red-600'
                      : 'bg-green-500 text-white hover:bg-green-600'
                  }`}
                >
                  {course.status === 'ACTIVE' ? 'Desactivar' : 'Activar'}
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
