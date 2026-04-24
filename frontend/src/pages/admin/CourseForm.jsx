import { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import './CourseForm.css';

const EMPTY_FORM = {
  title: '',
  description: '',
  instructor: '',
  date: '',
  duration: '',
  capacity: '',
  category: '',
  content: '',
};

export default function CourseForm() {
  const { id } = useParams(); // present when editing, absent when creating
  const navigate = useNavigate();
  const isEditing = Boolean(id);

  const [form, setForm] = useState(EMPTY_FORM);
  const [loading, setLoading] = useState(isEditing);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState(null);

  // Load existing data when editing
  useEffect(() => {
    if (!isEditing) return;

    fetch(`/api/courses/${id}`)
      .then((res) => {
        if (!res.ok) throw new Error('Course not found');
        return res.json();
      })
      .then((course) => {
        setForm({
          title: course.title ?? '',
          description: course.description ?? '',
          instructor: course.instructor ?? '',
          date: course.date ?? '',
          duration: course.duration ?? '',
          capacity: course.capacity ?? '',
          category: course.category ?? '',
          content: course.content ?? '',
        });
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, [id, isEditing]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setSubmitting(true);
    setError(null);

    const payload = {
      ...form,
      capacity: form.capacity === '' ? null : Number(form.capacity),
    };

    const url = isEditing ? `/api/courses/${id}` : '/api/courses';
    const method = isEditing ? 'PUT' : 'POST';

    fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
      .then((res) => {
        if (!res.ok) throw new Error('Failed to save course');
        return res.json();
      })
      .then(() => navigate('/admin'))
      .catch((err) => {
        setError(err.message);
        setSubmitting(false);
      });
  };

  if (loading) return <p className="status-message">Loading course...</p>;

  return (
    <div className="course-form-page">
      <Link to="/admin" className="back-link">&larr; Back to Admin</Link>
      <h1>{isEditing ? 'Edit Course' : 'New Course'}</h1>

      <form className="course-form" onSubmit={handleSubmit}>
        {error && <p className="form-error">{error}</p>}

        <div className="form-group">
          <label htmlFor="title">Title *</label>
          <input
            id="title"
            name="title"
            type="text"
            value={form.title}
            onChange={handleChange}
            maxLength={200}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="description">Description *</label>
          <textarea
            id="description"
            name="description"
            value={form.description}
            onChange={handleChange}
            maxLength={2000}
            rows={4}
            required
          />
        </div>

        <div className="form-row">
          <div className="form-group">
            <label htmlFor="instructor">Instructor</label>
            <input
              id="instructor"
              name="instructor"
              type="text"
              value={form.instructor}
              onChange={handleChange}
            />
          </div>

          <div className="form-group">
            <label htmlFor="category">Category</label>
            <input
              id="category"
              name="category"
              type="text"
              value={form.category}
              onChange={handleChange}
              placeholder="e.g. Development, DevOps, Cloud"
            />
          </div>
        </div>

        <div className="form-row">
          <div className="form-group">
            <label htmlFor="date">Date</label>
            <input
              id="date"
              name="date"
              type="date"
              value={form.date}
              onChange={handleChange}
            />
          </div>

          <div className="form-group">
            <label htmlFor="duration">Duration</label>
            <input
              id="duration"
              name="duration"
              type="text"
              value={form.duration}
              onChange={handleChange}
              placeholder="e.g. 2 days, 4 hours"
            />
          </div>

          <div className="form-group">
            <label htmlFor="capacity">Capacity</label>
            <input
              id="capacity"
              name="capacity"
              type="number"
              value={form.capacity}
              onChange={handleChange}
              min={1}
              placeholder="Max students"
            />
          </div>
        </div>

        <div className="form-group">
          <label htmlFor="content">Course Content</label>
          <textarea
            id="content"
            name="content"
            value={form.content}
            onChange={handleChange}
            rows={10}
            placeholder="Course outline, syllabus, or description (plain text or Markdown)"
          />
        </div>

        <div className="form-actions">
          <Link to="/admin" className="btn btn-secondary">Cancel</Link>
          <button type="submit" className="btn btn-primary" disabled={submitting}>
            {submitting ? 'Saving...' : isEditing ? 'Save Changes' : 'Create Course'}
          </button>
        </div>
      </form>
    </div>
  );
}
