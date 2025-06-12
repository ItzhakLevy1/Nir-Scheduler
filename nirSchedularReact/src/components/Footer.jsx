import "./Footer.css";

const Footer = () => (
  <footer className="footer">
    <section>
      <a href="tel:0526126120" className="contact-link">
        📞 חייג אלינו
      </a>
      <a href="https://wa.me/972526126120" className="contact-link">
        💬 כתוב לנו
      </a>
      <a
        href="https://ul.waze.com/ul?ll=31.993925,34.764165&navigate=yes&zoom=17"
        className="contact-link"
      >
        🌏 נווט אלינו
      </a>
    </section>

    <div className="footer-bottom">
      <p>© {new Date().getFullYear()} א.א.גובה</p>
    </div>
  </footer>
);

export default Footer;
