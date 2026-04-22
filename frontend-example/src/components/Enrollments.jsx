import { useState, useEffect } from 'react';
import { enrollmentsApi, studentsApi, coursesApi } from '../api';

export default function Enrollments({ refreshTrigger }) {
  const [enrollments, setEnrollments] = useState([]);
  const [students, setStudents] = useState([]);
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    studentId: '',
    courseId: '',
  });

  const fetchEnrollments = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await enrollmentsApi.getAll();
      setEnrollments(data);
    } catch (err) {
      setError(err.message || 'Error al cargar matrículas');
    } finally {
      setLoading(false);
    }
  };

  const fetchStudentsAndCourses = async () => {
    try {
      const [studentsData, coursesData] = await Promise.all([
        studentsApi.getAll(),
        coursesApi.getAll(),
      ]);
      setStudents(studentsData);
      setCourses(coursesData);
    } catch (err) {
      console.error('Error loading data:', err);
    }
  };

  useEffect(() => {
    if (showForm) {
      fetchStudentsAndCourses();
    }
  }, [showForm, refreshTrigger]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setSuccess(null);
    try {
      await enrollmentsApi.create({
        studentId: parseInt(formData.studentId),
        courseId: parseInt(formData.courseId),
      });
      setFormData({ studentId: '', courseId: '' });
      setShowForm(false);
      setSuccess('¡Matrícula creada exitosamente!');
      fetchEnrollments();
      setTimeout(() => setSuccess(null), 3000);
    } catch (err) {
      setError(err.message || 'Error al crear matrícula');
    } finally {
      setLoading(false);
    }
  };

  const cancelEnrollment = async (enrollment) => {
    try {
      await enrollmentsApi.updateStatus(enrollment.id, 'CANCELLED');
      fetchEnrollments();
    } catch (err) {
      setError(err.message || 'Error al cancelar matrícula');
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'ACTIVE':
        return 'bg-green-100 text-green-800';
      case 'CANCELLED':
        return 'bg-red-100 text-red-800';
      case 'COMPLETED':
        return 'bg-blue-100 text-blue-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  return (
    <div className="bg-white rounded-xl shadow-lg p-6">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold text-gray-800 flex items-center gap-2">
          <span className="text-3xl">📝</span> Matrículas
        </h2>
        <div className="flex gap-2">
          <button
            onClick={fetchEnrollments}
            className="px-4 py-2 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition-colors"
          >
            Cargar
          </button>
          <button
            onClick={() => setShowForm(!showForm)}
            className="px-4 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600 transition-colors"
          >
            {showForm ? 'Cancelar' : '+ Nueva'}
          </button>
        </div>
      </div>

      {error && (
        <div className="mb-4 p-4 bg-red-100 border border-red-400 text-red-700 rounded-lg">
          <strong>Error:</strong> {error}
        </div>
      )}

      {success && (
        <div className="mb-4 p-4 bg-green-100 border border-green-400 text-green-700 rounded-lg">
          {success}
        </div>
      )}

      {showForm && (
        <form onSubmit={handleSubmit} className="mb-6 p-4 bg-gradient-to-r from-orange-50 to-yellow-50 rounded-lg border border-orange-200">
          <h3 className="font-semibold text-gray-700 mb-4">Crear Nueva Matrícula</h3>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-600 mb-1">
                Estudiante
              </label>
              <select
                value={formData.studentId}
                onChange={(e) => setFormData({ ...formData, studentId: e.target.value })}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent"
                required
              >
                <option value="">Seleccionar estudiante...</option>
                {students
                  .filter((s) => s.status === 'ACTIVE')
                  .map((student) => (
                    <option key={student.id} value={student.id}>
                      {student.firstName} {student.lastName} ({student.email})
                    </option>
                  ))}
              </select>
              {students.filter((s) => s.status !== 'ACTIVE').length > 0 && (
                <p className="text-xs text-gray-500 mt-1">
                  * Solo se muestran estudiantes activos
                </p>
              )}
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-600 mb-1">
                Curso
              </label>
              <select
                value={formData.courseId}
                onChange={(e) => setFormData({ ...formData, courseId: e.target.value })}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent"
                required
              >
                <option value="">Seleccionar curso...</option>
                {courses
                  .filter((c) => c.status === 'ACTIVE')
                  .map((course) => (
                    <option key={course.id} value={course.id}>
                      {course.name} (Cupo: {course.enrolledCount || 0}/{course.capacity})
                    </option>
                  ))}
              </select>
              {courses.filter((c) => c.status !== 'ACTIVE').length > 0 && (
                <p className="text-xs text-gray-500 mt-1">
                  * Solo se muestran cursos activos
                </p>
              )}
            </div>
          </div>
          <button
            type="submit"
            disabled={loading || !formData.studentId || !formData.courseId}
            className="mt-4 px-6 py-2 bg-orange-500 text-white rounded-lg hover:bg-orange-600 disabled:bg-gray-400 transition-colors"
          >
            {loading ? 'Creando...' : 'Crear Matrícula'}
          </button>
        </form>
      )}

      {loading && !showForm ? (
        <div className="text-center py-8 text-gray-500">Cargando...</div>
      ) : enrollments.length === 0 ? (
        <div className="text-center py-8 text-gray-400">
          No hay matrículas. Haz clic en "Cargar" para obtener datos.
        </div>
      ) : (
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b border-gray-200">
                <th className="text-left py-3 px-4 text-gray-600 font-semibold">ID</th>
                <th className="text-left py-3 px-4 text-gray-600 font-semibold">Estudiante</th>
                <th className="text-left py-3 px-4 text-gray-600 font-semibold">Curso</th>
                <th className="text-left py-3 px-4 text-gray-600 font-semibold">Fecha</th>
                <th className="text-left py-3 px-4 text-gray-600 font-semibold">Estado</th>
                <th className="text-left py-3 px-4 text-gray-600 font-semibold">Acciones</th>
              </tr>
            </thead>
            <tbody>
              {enrollments.map((enrollment) => (
                <tr key={enrollment.id} className="border-b border-gray-100 hover:bg-gray-50">
                  <td className="py-3 px-4 text-gray-800">{enrollment.id}</td>
                  <td className="py-3 px-4 text-gray-800">
                    ID: {enrollment.studentId}
                  </td>
                  <td className="py-3 px-4 text-gray-800">
                    ID: {enrollment.courseId}
                  </td>
                  <td className="py-3 px-4 text-gray-600">
                    {new Date(enrollment.enrollmentDate).toLocaleDateString()}
                  </td>
                  <td className="py-3 px-4">
                    <span
                      className={`px-3 py-1 rounded-full text-sm font-medium ${getStatusColor(
                        enrollment.status
                      )}`}
                    >
                      {enrollment.status}
                    </span>
                  </td>
                  <td className="py-3 px-4">
                    {enrollment.status === 'ACTIVE' && (
                      <button
                        onClick={() => cancelEnrollment(enrollment)}
                        className="px-3 py-1 bg-red-500 text-white rounded-lg text-sm font-medium hover:bg-red-600 transition-colors"
                      >
                        Cancelar
                      </button>
                    )}
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
