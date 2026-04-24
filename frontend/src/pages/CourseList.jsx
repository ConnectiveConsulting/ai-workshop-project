import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import './CourseList.css';

export default function CourseList() {
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
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
  }, []);

  if (loading) return <p className="status-message">Loading courses...</p>;
  if (error) return <p className="status-message error">Error: {error}</p>;

  return (
    <div className="course-list">
      <h1>Available Courses</h1>
      {courses.length === 0 ? (
        <p className="status-message">
          No courses yet.{' '}
          <a href="#" onClick={(e) => { e.preventDefault(); seedData(); }}>
            Seed sample data
          </a>
        </p>
      ) : (
        <div className="course-grid">
          {courses.map((course) => (
            <CourseCard key={course.id} course={course} />
          ))}
        </div>
      )}
    </div>
  );
}

function CourseCard({ course }) {
  const formattedDate = course.date
    ? new Date(course.date).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
      })
    : 'Date TBD';

  return (
    <Link to={`/courses/${course.id}`} className="course-card">
      <div className="course-card-header">
        <span className="course-category">{course.category}</span>
        <span className="course-duration">{course.duration}</span>
      </div>
      <h2 className="course-title">{course.title}</h2>
      <p className="course-instructor">Instructor: {course.instructor}</p>
      <p className="course-date">{formattedDate}</p>
      <p className="course-description">{course.description}</p>
      <div className="course-card-footer">
        <span className="course-capacity">
          {course.capacity != null ? `${course.capacity} seats` : 'Open enrollment'}
        </span>
        <span className="course-link">View Details &rarr;</span>
      </div>
    </Link>
  );
}
