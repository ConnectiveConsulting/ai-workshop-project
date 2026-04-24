import { useLocation, Link } from 'react-router-dom';

export default function RegistrationConfirmation() {
  const location = useLocation();
  const { name, courseName } = location.state || {};

  return (
    <div className="confirmation-page">
      <div className="confirmation-box">
        <div className="confirmation-icon">&#10003;</div>
        <h1>You&apos;re registered!</h1>
        {name && courseName ? (
          <p>
            Thanks, <strong>{name}</strong>! You&apos;re now registered for{' '}
            <strong>{courseName}</strong>. We&apos;ll be in touch with details soon.
          </p>
        ) : (
          <p>Your registration was successful.</p>
        )}
        <Link to="/" className="btn btn-primary">
          Browse More Courses
        </Link>
      </div>
    </div>
  );
}
