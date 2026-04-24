import { BrowserRouter, Routes, Route } from 'react-router-dom';
import NavBar from './components/NavBar.jsx';
import CourseList from './pages/CourseList.jsx';
import CourseDetail from './pages/CourseDetail.jsx';
import RegistrationConfirmation from './pages/RegistrationConfirmation.jsx';
import AdminCourseList from './pages/admin/AdminCourseList.jsx';
import CourseForm from './pages/admin/CourseForm.jsx';
import RegistrationList from './pages/admin/RegistrationList.jsx';

export default function App() {
  return (
    <BrowserRouter>
      <NavBar />
      <main className="main-content">
        <Routes>
          {/* Public routes */}
          <Route path="/" element={<CourseList />} />
          <Route path="/courses/:id" element={<CourseDetail />} />
          <Route path="/courses/:id/confirm" element={<RegistrationConfirmation />} />

          {/* Admin routes */}
          <Route path="/admin" element={<AdminCourseList />} />
          <Route path="/admin/courses/new" element={<CourseForm />} />
          <Route path="/admin/courses/:id/edit" element={<CourseForm />} />
          <Route path="/admin/courses/:id/registrations" element={<RegistrationList />} />
        </Routes>
      </main>
    </BrowserRouter>
  );
}
