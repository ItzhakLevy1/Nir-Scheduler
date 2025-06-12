import "./Header.css";
import { Link } from "react-router-dom";

const Header = () => {
  return (
    <header className="header">
      <div className="header-content">
        <Link to="/" className="site-title">
          א.א. גובה
        </Link>

        <nav className="order-training">
          <Link to="/booking">הזמנת הדרכה</Link>
        </nav>

        <nav className="auth-links">
          <Link to="/login">כניסה</Link>
          <Link to="/register">הרשמה</Link>
        </nav>
      </div>
    </header>
  );
};

export default Header;
