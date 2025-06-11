import "./Header.css";
import { Link } from "react-router-dom";

const Header = () => {
  return (
    <header className="header">
      <div className="header-content">
        <Link to="/" className="site-title">
          א.א. גובה
        </Link>

        <section>
          <a href="tel:0526126120" className="link-button">
            📞 חייג אלינו
          </a>
          <a href="https://wa.me/972526126120" className="link-button">
            💬 כתוב לנו
          </a>
          <a
            href="https://ul.waze.com/ul?ll=31.993925,34.764165&navigate=yes&zoom=17"
            className="link-button"
          >
            🌏 נווט אלינו
          </a>
        </section>

        <nav className="nav-links">
          <Link to="/booking">הזמנת הדרכה</Link>
        </nav>
      </div>
    </header>
  );
};

export default Header;
