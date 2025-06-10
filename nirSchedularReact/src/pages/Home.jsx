import "./Home.css";
import Carousel from "../components/Carousel";
// import { Carousel } from "react-responsive-carousel";
import "react-responsive-carousel/lib/styles/carousel.min.css";

function Home() {
  return (
    <div className="home">
      {/* <header className="top-bar"> */}
      {/* <div className="logo">א.א. גובה</div> */}
      {/* <div className="contact"> */}
      {/* <div>052-6520102</div> */}
      {/* <div>Govaus@gmail.com</div> */}
      {/* </div> */}
      {/* </header> */}

      {/* <section className="contact-links">
        <a href="tel:0526520102" className="link-button">
          📞 0526520102
        </a>
        <a href="https://wa.me/972526520102" className="link-button">
          💬 כתוב לנו בוואטסאפ
        </a>
        <a
          href="https://ul.waze.com/ul?ll=31.993925,34.764165&navigate=yes&zoom=17"
          className="link-button"
        >
          📍 אליהו איתן 3, בית גירון - אולם 107, ראשל"צ
        </a>
      </section> */}

      <section className="main-sale">
        <img
          src="../../images/saleBanner.jpg"
          alt="מבצע כלי עבודה"
          className="tools-image"
        />
        <div className="sale-text">
          מבצע 250 ש"ח פלוס מע"מ <br />
          לחברי כלי עבודה יד ראשונה
        </div>
      </section>

      {/* <section> */}
      <Carousel />
      {/* </section> */}
      {/* <section className="carousel-section" style={{ backgroundColor: "pink" }}>
        <div
          style={{ maxWidth: 500, margin: "0 auto", backgroundColor: "orange" }}
        >
          <Carousel
            autoPlay
            infiniteLoop
            showThumbs={false}
            showStatus={false}
            dynamicHeight={false}
            emulateTouch
            swipeable
            centerMode={false}
            interval={3500}
            stopOnHover
            style={{ height: 300 }}
            useKeyboardArrows={true}
            lazyLoad={false}
          >
            <div>
              <img
                src="https://picsum.photos/id/237/200/300"
                alt="carousel1"
                style={{ maxHeight: 300, objectFit: "cover" }}
              />
            </div>
            <div>
              <img
                src="../../images/carousel2.jpg"
                alt="carousel2"
                style={{ maxHeight: 300, objectFit: "cover" }}
              />
            </div>
            <div>
              <img
                src="../../images/carousel3.jpg"
                alt="carousel3"
                style={{ maxHeight: 300, objectFit: "cover" }}
              />
            </div>
          </Carousel>
        </div>
      </section> */}

      <section className="features">
        <div className="feature-card yellow">
          <h3> 🎓 מה לומדים בקורס</h3>
          <p>
            נושא הבטיחות בעבודה עולה בשנים האחרונות יותר ויותר למודעות כל חברה
            שבטיחותם ושלומם של עובדיה חשובים לה, דואגת להעסיק ממונה בטיחות לצורך
            קידום וניהול נושא הבטיחות, קבלת הדרכות וכן ייעוץ וליווי להנהלה
            ולעובדים בנושאי הבטיחות השונים
          </p>
        </div>
        <div className="feature-card orange">
          <h3> 🦺 הדרכה לקבוצות ויחידים</h3>
          <p>
            ניתן להזמין הדרכה קבוצתית באתר הלקוח או להגיע להדרכה אישית על פי לוח
            הזמנים.
            <br />
            <br />
            מרבית תאונות העבודה מתרחשות כתוצאה ישירה מעבודה בגובה. הדרכת בטיחות
            לעובדים בנושאי עבודה לגובה מבטיחה שמירה על בטיחות העובד וראש שקט
            למעסיק. תקנת הבטיחות לעבודה בגובה מחייבת את המעסיקים להעביר הדרכת
            בטיחות לעבודה בגובה לכל עובד המוגדר כעובד בגובה
          </p>
        </div>
        <div className="feature-card blue">
          <h3> 👷‍♂️ הדרכת עבודה בגובה הדרך הנכונה והבטוחה</h3>
          <p>
            ההדרכה מיועדת לכלל העובדים במשק הנדרשים לעבוד על סולמות ארוכים מ-2
            מטרים, סלים להרמת אדם, במות מתרוממות ניידות, פיגומים נייחים, פיגומים
            ממוכנים, קונסטרוקציות, מבני מתכת, וגגות
          </p>
        </div>
      </section>

      <section className="about-section">
        <img src="../../images/about.jpg" alt="construction team" />
        <h2>בטיחות</h2>
        <p>
          הדרכות עבודה בגובה מספקות את כל החומר שהעובד חייב לדעת בטרם הוא עולה
          לגובה לבצע עבודות מסוימות. סוגי ההדרכות הם התאם לעבודתו של העובד בגובה
          וכוללות: מבוא כללי, הדרכה על סולמות, גגות, קונסטרוקציות, במות הרמה,
          סלי הרמה, פיגומים ממוכנים, חלל מוקף ועוד
        </p>
      </section>

      <footer className="footer">
        <h4>פרטים ליצירת קשר</h4>
        <p>מעלה אפרים, רחוב התעשיה 107</p>
        <p>📞 052-6520102</p>
        <p>✉️ Govaus@gmail.com</p>
      </footer>
    </div>
  );
}

export default Home;
