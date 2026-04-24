import { Link, useLocation } from 'react-router-dom';
import './NavBar.css';

export default function NavBar() {
  const location = useLocation();

  return (
    <nav className="navbar">
      <div className="navbar-brand">
        <Link to="/">Training Courses</Link>
      </div>
      <div className="navbar-links">
        <Link
          to="/"
          className={location.pathname === '/' ? 'active' : ''}
        >
          Courses
        </Link>
        <Link
          to="/admin"
          className={location.pathname.startsWith('/admin') ? 'active' : ''}
        >
          Admin
        </Link>
      </div>
    </nav>
  );
}
