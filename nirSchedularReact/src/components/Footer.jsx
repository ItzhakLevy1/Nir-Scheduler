import "./Footer.css";

const Footer = () => (
  <footer className="footer">
    {/* <div className="footer-content"> */}
      {/* <div className="footer-section">
        <h4>כתובת</h4>
        <p>🚩 אליהו איתן 3, בית גירון - אולם 107, ראשל"צ</p>
      </div>
      <div className="footer-section">
        <h4>טלפון</h4>
        <p>📞 052-612-612-0</p>
      </div>
      <div className="footer-section">
        <h4>אימייל</h4>
        <p>✉️ Gova.osh@gmail.com</p>
      </div> */}

      <div className="footer-bottom">
        <p>© {new Date().getFullYear()} א.א.גובה</p>
      </div>
    {/* </div> */}
  </footer>
);

export default Footer;
