import { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import './CourseDetail.css';

export default function CourseDetail() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [course, setCourse] = useState(null);
  const [registrationCount, setRegistrationCount] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Registration form state
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [formError, setFormError] = useState(null);

  useEffect(() => {
    Promise.all([
      fetch(`/api/courses/${id}`).then((res) => {
        if (!res.ok) throw new Error('Course not found');
        return res.json();
      }),
      fetch(`/api/courses/${id}/registrations`).then((res) =>
        res.ok ? res.json() : []
      ),
    ])
      .then(([courseData, registrations]) => {
        setCourse(courseData);
        setRegistrationCount(registrations.length);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, [id]);

  const handleRegister = (e) => {
    e.preventDefault();
    setSubmitting(true);
    setFormError(null);

    fetch(`/api/courses/${id}/registrations`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, email }),
    })
      .then((res) => {
        if (res.status === 409) throw new Error('This course is full.');
        if (!res.ok) throw new Error('Registration failed. Please try again.');
        return res.json();
      })
      .then((registration) => {
        navigate(`/courses/${id}/confirm`, {
          state: { name, courseName: course.title, registrationId: registration.id },
        });
      })
      .catch((err) => {
        setFormError(err.message);
        setSubmitting(false);
      });
  };

  if (loading) return <p className="status-message">Loading course...</p>;
  if (error) return <p className="status-message error">Error: {error}</p>;

  const seatsRemaining =
    course.capacity != null ? course.capacity - registrationCount : null;
  const isFull = seatsRemaining !== null && seatsRemaining <= 0;

  const formattedDate = course.date
    ? new Date(course.date).toLocaleDateString('en-US', {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric',
      })
    : 'Date TBD';

  return (
    <div className="course-detail">
      <Link to="/" className="back-link">&larr; All Courses</Link>

      <div className="course-header">
        <div className="course-meta-tags">
          <span className="tag category">{course.category}</span>
          <span className="tag duration">{course.duration}</span>
          {seatsRemaining !== null && (
            <span className={`tag seats ${isFull ? 'full' : ''}`}>
              {isFull ? 'Full' : `${seatsRemaining} seats remaining`}
            </span>
          )}
        </div>
        <h1>{course.title}</h1>
        <p className="course-instructor">Instructor: <strong>{course.instructor}</strong></p>
        <p className="course-date">{formattedDate}</p>
      </div>

      <div className="course-body">
        <section className="course-description-section">
          <h2>About This Course</h2>
          <p>{course.description}</p>
        </section>

        {course.content && (
          <section className="course-content-section">
            <h2>Course Content</h2>
            <pre className="course-content">{course.content}</pre>
          </section>
        )}
      </div>

      <div className="registration-section">
        <h2>Register for This Course</h2>

        {isFull ? (
          <p className="course-full-message">
            Sorry, this course is currently full. Check back later for availability.
          </p>
        ) : (
          <form className="registration-form" onSubmit={handleRegister}>
            {formError && <p className="form-error">{formError}</p>}
            <div className="form-group">
              <label htmlFor="name">Full Name</label>
              <input
                id="name"
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="Your full name"
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="email">Email Address</label>
              <input
                id="email"
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="you@example.com"
                required
              />
            </div>
            <button type="submit" className="btn btn-primary" disabled={submitting}>
              {submitting ? 'Registering...' : 'Register Now'}
            </button>
          </form>
        )}
      </div>
    </div>
  );
}
