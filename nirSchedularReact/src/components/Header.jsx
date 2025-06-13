import "./Header.css";
import { Link } from "react-router-dom";

const Header = () => {
  return (
    <header className="header">
      <div className="header-content">
        <div className="logo-section">
          <Link to="/">
            <img
              src="../images/logo.jpg"
              alt="A.A. Gova Logo"
              className="logo-img"
            />
          </Link>
        </div>

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
