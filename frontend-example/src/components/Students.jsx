import { useState } from 'react';
import { studentsApi } from '../api';

export default function Students({ onRefresh }) {
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
  });

  const fetchStudents = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await studentsApi.getAll();
      setStudents(data);
    } catch (err) {
      setError(err.message || 'Error al cargar estudiantes');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      await studentsApi.create(formData);
      setFormData({ firstName: '', lastName: '', email: '' });
      setShowForm(false);
      fetchStudents();
      onRefresh?.();
    } catch (err) {
      setError(err.message || 'Error al crear estudiante');
    } finally {
      setLoading(false);
    }
  };

  const toggleStatus = async (student) => {
    const newStatus = student.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
    try {
      await studentsApi.updateStatus(student.id, newStatus);
      fetchStudents();
      onRefresh?.();
    } catch (err) {
      setError(err.message || 'Error al actualizar estado');
    }
  };

  return (
    <div className="bg-white rounded-xl shadow-lg p-6">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold text-gray-800 flex items-center gap-2">
          <span className="text-3xl">👨‍🎓</span> Estudiantes
        </h2>
        <div className="flex gap-2">
          <button
            onClick={fetchStudents}
            className="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors"
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
              placeholder="Nombre"
              value={formData.firstName}
              onChange={(e) => setFormData({ ...formData, firstName: e.target.value })}
              className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              required
            />
            <input
              type="text"
              placeholder="Apellido"
              value={formData.lastName}
              onChange={(e) => setFormData({ ...formData, lastName: e.target.value })}
              className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              required
            />
            <input
              type="email"
              placeholder="Email"
              value={formData.email}
              onChange={(e) => setFormData({ ...formData, email: e.target.value })}
              className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              required
            />
          </div>
          <button
            type="submit"
            disabled={loading}
            className="mt-4 px-6 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 disabled:bg-gray-400 transition-colors"
          >
            {loading ? 'Creando...' : 'Crear Estudiante'}
          </button>
        </form>
      )}

      {loading && !showForm ? (
        <div className="text-center py-8 text-gray-500">Cargando...</div>
      ) : students.length === 0 ? (
        <div className="text-center py-8 text-gray-400">
          No hay estudiantes. Haz clic en "Cargar" para obtener datos.
        </div>
      ) : (
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b border-gray-200">
                <th className="text-left py-3 px-4 text-gray-600 font-semibold">ID</th>
                <th className="text-left py-3 px-4 text-gray-600 font-semibold">Nombre</th>
                <th className="text-left py-3 px-4 text-gray-600 font-semibold">Email</th>
                <th className="text-left py-3 px-4 text-gray-600 font-semibold">Estado</th>
                <th className="text-left py-3 px-4 text-gray-600 font-semibold">Acciones</th>
              </tr>
            </thead>
            <tbody>
              {students.map((student) => (
                <tr key={student.id} className="border-b border-gray-100 hover:bg-gray-50">
                  <td className="py-3 px-4 text-gray-800">{student.id}</td>
                  <td className="py-3 px-4 text-gray-800">
                    {student.firstName} {student.lastName}
                  </td>
                  <td className="py-3 px-4 text-gray-600">{student.email}</td>
                  <td className="py-3 px-4">
                    <span
                      className={`px-3 py-1 rounded-full text-sm font-medium ${
                        student.status === 'ACTIVE'
                          ? 'bg-green-100 text-green-800'
                          : 'bg-red-100 text-red-800'
                      }`}
                    >
                      {student.status}
                    </span>
                  </td>
                  <td className="py-3 px-4">
                    <button
                      onClick={() => toggleStatus(student)}
                      className={`px-3 py-1 rounded-lg text-sm font-medium transition-colors ${
                        student.status === 'ACTIVE'
                          ? 'bg-red-500 text-white hover:bg-red-600'
                          : 'bg-green-500 text-white hover:bg-green-600'
                      }`}
                    >
                      {student.status === 'ACTIVE' ? 'Desactivar' : 'Activar'}
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
