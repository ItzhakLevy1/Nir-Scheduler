import React, { useEffect, useState } from "react";
import "./Header.css";
import { Link, useNavigate } from "react-router-dom";

const Header = () => {
  const [userName, setUserName] = useState("");         // Stores the logged-in user's name
  const [isLoggedIn, setIsLoggedIn] = useState(false);  // Boolean state to track login status
  const navigate = useNavigate();                       // Hook to programmatically navigate between routes

  // Helper function to update state based on localStorage authentication values
  const updateAuthState = () => {
    const token = localStorage.getItem("token");         // JWT token from localStorage
    const userId = localStorage.getItem("userId");       // Logged-in user's ID from localStorage
    setIsLoggedIn(!!token);                              // Set login state based on token existence

    if (token && userId) {
      const storedProfile = localStorage.getItem("userProfile"); // Try to retrieve full user profile
      if (storedProfile) {
        try {
          const profile = JSON.parse(storedProfile);     // Parse profile from JSON
          setUserName(profile.name || "");               // Extract user's name
        } catch {
          setUserName("");                               // If parsing fails, reset name
        }
      } else {
        setUserName("");                                 // If profile not found, reset name
      }
    } else {
      setUserName("");                                   // If not logged in, reset name
    }
  };

  useEffect(() => {   // Run code when component mounts and thus Eliminate the need to manually refresh the page to see the logged in user name
    updateAuthState();                                   // Check auth state when component mounts

    // Listen for custom global event to update auth state after login/logout
    const handler = () => updateAuthState();
    window.addEventListener("nir-auth-change", handler);

    // Clean up listener when component unmounts
    return () => window.removeEventListener("nir-auth-change", handler);
  }, []);

  // Handles logout by clearing user data and navigating to homepage
  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("userId");
    localStorage.removeItem("userProfile");
    window.dispatchEvent(new Event("nir-auth-change")); // Notify other components
    navigate("/"); // Redirect to home
  };

  return (
    <header className="header">
      <div className="header-content">
        {/* Logo Section */}
        <div className="logo-section">
          <Link to="/">
            <img
              src="../images/logo.jpg"
              alt="A.A. Gova Logo"
              className="logo-img"
            />
          </Link>
        </div>

        {/* Link to training booking page */}
        <nav className="order-training">
          <Link to="/booking">הזמנת הדרכה</Link>
        </nav>

        {/* Authentication Section */}
        <nav className="auth-links">
          {!isLoggedIn ? (
            // Show login/register links if user is not logged in
            <>
              <Link to="/login">כניסה</Link>
              <Link to="/register">הרשמה</Link>
            </>
          ) : (
            // Show username and logout button if logged in
            <>
              <span className="user-name">
                {userName ? userName : "משתמש מחובר"}
              </span>
              <button className="logout-btn" onClick={handleLogout}>
                התנתק
              </button>
            </>
          )}
        </nav>
      </div>
    </header>
  );
};

export default Header;
