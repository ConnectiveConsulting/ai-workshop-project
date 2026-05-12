import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import './RegistrationList.css';

export default function RegistrationList() {
  const { id } = useParams();

  const [course, setCourse] = useState(null);
  const [registrations, setRegistrations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const loadData = () => {
    Promise.all([
      fetch(`/api/courses/${id}`).then((res) => {
        if (!res.ok) throw new Error('Course not found');
        return res.json();
      }),
      fetch(`/api/courses/${id}/registrations`).then((res) => {
        if (!res.ok) throw new Error('Failed to load registrations');
        return res.json();
      }),
    ])
      .then(([courseData, regData]) => {
        setCourse(courseData);
        setRegistrations(regData);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  };

  useEffect(() => {
    loadData();
  }, [id]);

  const handleCancel = (regId, name) => {
    if (!window.confirm(`Cancel registration for "${name}"?`)) return;

    fetch(`/api/registrations/${regId}`, { method: 'DELETE' })
      .then((res) => {
        if (!res.ok) throw new Error('Failed to cancel registration');
        loadData();
      })
      .catch((err) => alert(`Error: ${err.message}`));
  };

  if (loading) return <p className="status-message">Loading...</p>;
  if (error) return <p className="status-message error">Error: {error}</p>;

  return (
    <div className="registration-list-page">
      <Link to="/admin" className="back-link">&larr; Back to Admin</Link>
      <h1>Registrations</h1>
      <h2 className="course-subtitle">{course?.title}</h2>

      <p className="registration-count">
        {registrations.length} registered
        {course?.capacity ? ` / ${course.capacity} capacity` : ''}
      </p>

      {registrations.length === 0 ? (
        <p className="status-message">No registrations yet for this course.</p>
      ) : (
        <table className="admin-table">
          <thead>
            <tr>
              <th>#</th>
              <th>Name</th>
              <th>Email</th>
              <th>Phone</th>
              <th>Registered At</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {registrations.map((reg, index) => (
              <tr key={reg.id}>
                <td>{index + 1}</td>
                <td>{reg.name}</td>
                <td>
                  <a href={`mailto:${reg.email}`}>{reg.email}</a>
                </td>
                <td>{reg.phone || '—'}</td>
                <td>
                  {reg.registeredAt
                    ? new Date(reg.registeredAt).toLocaleString('en-US', {
                        year: 'numeric',
                        month: 'short',
                        day: 'numeric',
                        hour: '2-digit',
                        minute: '2-digit',
                      })
                    : '—'}
                </td>
                <td>
                  <button
                    className="btn btn-small btn-danger"
                    onClick={() => handleCancel(reg.id, reg.name)}
                  >
                    Cancel
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
