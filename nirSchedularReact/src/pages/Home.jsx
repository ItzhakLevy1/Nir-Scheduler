import "./Home.css";
import Carousel from "../components/Carousel";
import "react-responsive-carousel/lib/styles/carousel.min.css";

function Home() {
  return (
    <div className="home">
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

      <section>
        <Carousel />
      </section>

      <section className="features">
        <div className="feature-card yellow">
          <h3> 🎓 מה לומדים בקורס</h3>
          <p>
            נושא הבטיחות בעבודה עולה בשנים האחרונות יותר ויותר למודעות כל חברה
            שבטיחותם ושלומם של עובדיה חשובים לה, דואגת להעסיק ממונה בטיחות לצורך
            קידום וניהול נושא הבטיחות, קבלת הדרכות וכן ייעוץ וליווי להנהלה
            ולעובדים בנושאי הבטיחות השונים.
          </p>
        </div>
        <div className="feature-card orange">
          <h3> 🦺 הדרכה לקבוצות ויחידים</h3>
          <p>
            מרבית תאונות העבודה מתרחשות כתוצאה ישירה מעבודה בגובה. הדרכת בטיחות
            לעובדים בנושאי עבודה לגובה מבטיחה שמירה על בטיחות העובד וראש שקט
            למעסיק. תקנת הבטיחות לעבודה בגובה מחייבת את המעסיקים להעביר הדרכת
            בטיחות לעבודה בגובה לכל עובד המוגדר כעובד בגובה.
          </p>
        </div>
        <div className="feature-card blue">
          <h3> 👷‍♂️ הדרכת עבודה בגובה הדרך הנכונה והבטוחה</h3>
          <p>
            ההדרכה מיועדת לכלל העובדים במשק הנדרשים לעבוד על סולמות ארוכים מ-2
            מטרים, סלים להרמת אדם, במות מתרוממות ניידות, פיגומים נייחים, פיגומים
            ממוכנים, קונסטרוקציות, מבני מתכת, וגגות.
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
          סלי הרמה, פיגומים ממוכנים, חלל מוקף ועוד.
        </p>
      </section>

    </div>
  );
}

export default Home;
