import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Header from "./components/Header";
import Footer from "./components/Footer";
import Home from "./components/pages/Home";
import Booking from "./components/pages/Booking";
import Admin from "./components/Admin";
import RegisterPage from "./components/auth/RegisterPage";
import LoginPage from "./components/auth/LoginPage";
import toastr from "toastr";
import "toastr/build/toastr.min.css";

// Configure toastr settings
toastr.options = {
  positionClass: "toast-top-center",
  preventDuplicates: true,
  closeButton: true,
  progressBar: true,
  timeOut: "5000",
  extendedTimeOut: "1000", // Removed toastClass to allow default styling
};

function App() {
  return (
    <Router>
      <Header />
      <main>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/booking" element={<Booking />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/admin" element={<Admin />} />
        </Routes>
      </main>
      <Footer />
    </Router>
  );
}

export default App;
