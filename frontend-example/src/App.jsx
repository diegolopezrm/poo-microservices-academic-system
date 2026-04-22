import { useState } from 'react';
import Students from './components/Students';
import Courses from './components/Courses';
import Enrollments from './components/Enrollments';

function App() {
  const [activeTab, setActiveTab] = useState('students');
  const [refreshTrigger, setRefreshTrigger] = useState(0);

  const handleRefresh = () => {
    setRefreshTrigger((prev) => prev + 1);
  };

  const tabs = [
    { id: 'students', label: 'Estudiantes', icon: '👨‍🎓' },
    { id: 'courses', label: 'Cursos', icon: '📚' },
    { id: 'enrollments', label: 'Matrículas', icon: '📝' },
  ];

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-purple-50 to-pink-50">
      {/* Header */}
      <header className="bg-white shadow-md">
        <div className="max-w-7xl mx-auto px-4 py-6">
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-blue-600 to-purple-600 bg-clip-text text-transparent">
                Sistema Académico
              </h1>
              <p className="text-gray-500 mt-1">
                Microservicios con Spring Boot + React
              </p>
            </div>
            <div className="flex items-center gap-4">
              <div className="flex items-center gap-2 px-4 py-2 bg-green-100 rounded-full">
                <span className="w-2 h-2 bg-green-500 rounded-full animate-pulse"></span>
                <span className="text-green-700 text-sm font-medium">API Connected</span>
              </div>
            </div>
          </div>
        </div>
      </header>

      {/* Navigation Tabs */}
      <nav className="bg-white border-b">
        <div className="max-w-7xl mx-auto px-4">
          <div className="flex gap-1">
            {tabs.map((tab) => (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id)}
                className={`px-6 py-4 text-sm font-medium transition-colors relative ${
                  activeTab === tab.id
                    ? 'text-blue-600'
                    : 'text-gray-500 hover:text-gray-700'
                }`}
              >
                <span className="flex items-center gap-2">
                  <span className="text-xl">{tab.icon}</span>
                  {tab.label}
                </span>
                {activeTab === tab.id && (
                  <span className="absolute bottom-0 left-0 right-0 h-0.5 bg-blue-600"></span>
                )}
              </button>
            ))}
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-4 py-8">
        {activeTab === 'students' && <Students onRefresh={handleRefresh} />}
        {activeTab === 'courses' && <Courses onRefresh={handleRefresh} />}
        {activeTab === 'enrollments' && <Enrollments refreshTrigger={refreshTrigger} />}
      </main>

      {/* Footer */}
      <footer className="bg-white border-t mt-auto">
        <div className="max-w-7xl mx-auto px-4 py-4">
          <div className="flex justify-between items-center text-sm text-gray-500">
            <p>Proyecto POO - Sistema Académico con Microservicios</p>
            <p>Febrero 2026</p>
          </div>
        </div>
      </footer>
    </div>
  );
}

export default App;
