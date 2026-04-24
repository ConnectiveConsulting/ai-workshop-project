import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './AdminCourseList.css';

export default function AdminCourseList() {
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const loadCourses = () => {
    setLoading(true);
    fetch('/api/courses')
      .then((res) => {
        if (!res.ok) throw new Error('Failed to load courses');
        return res.json();
      })
      .then((data) => {
        setCourses(data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  };

  useEffect(() => {
    loadCourses();
  }, []);

  const handleDelete = (id, title) => {
    if (!window.confirm(`Delete course "${title}"? This cannot be undone.`)) return;

    fetch(`/api/courses/${id}`, { method: 'DELETE' })
      .then((res) => {
        if (!res.ok) throw new Error('Failed to delete course');
        loadCourses();
      })
      .catch((err) => alert(`Error: ${err.message}`));
  };

  const handleSeed = () => {
    if (!window.confirm('This will clear all existing courses and registrations and load sample data. Continue?')) return;

    fetch('/api/seed', { method: 'POST' })
      .then((res) => res.json())
      .then(() => loadCourses())
      .catch((err) => alert(`Seed failed: ${err.message}`));
  };

  if (loading) return <p className="status-message">Loading courses...</p>;
  if (error) return <p className="status-message error">Error: {error}</p>;

  return (
    <div className="admin-course-list">
      <div className="admin-header">
        <h1>Admin — Courses</h1>
        <div className="admin-actions">
          <button className="btn btn-secondary" onClick={handleSeed}>
            Seed Sample Data
          </button>
          <Link to="/admin/courses/new" className="btn btn-primary">
            + New Course
          </Link>
        </div>
      </div>

      {courses.length === 0 ? (
        <p className="status-message">No courses yet. Create one or seed sample data.</p>
      ) : (
        <table className="admin-table">
          <thead>
            <tr>
              <th>Title</th>
              <th>Category</th>
              <th>Date</th>
              <th>Instructor</th>
              <th>Capacity</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {courses.map((course) => (
              <tr key={course.id}>
                <td>{course.title}</td>
                <td>{course.category}</td>
                <td>
                  {course.date
                    ? new Date(course.date).toLocaleDateString('en-US', {
                        year: 'numeric',
                        month: 'short',
                        day: 'numeric',
                      })
                    : '—'}
                </td>
                <td>{course.instructor}</td>
                <td>{course.capacity ?? '—'}</td>
                <td className="action-cell">
                  <Link
                    to={`/admin/courses/${course.id}/registrations`}
                    className="btn btn-small btn-secondary"
                  >
                    Registrations
                  </Link>
                  <Link
                    to={`/admin/courses/${course.id}/edit`}
                    className="btn btn-small btn-secondary"
                  >
                    Edit
                  </Link>
                  <button
                    className="btn btn-small btn-danger"
                    onClick={() => handleDelete(course.id, course.title)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
