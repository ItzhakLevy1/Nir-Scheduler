import React, { useState } from 'react';
import './Header.css';
import { Link } from 'react-router-dom';

const Header = () => {

  return (
    <header className="header">
      <div className="header-content">
        <h1 className="site-title">מערכת לקביעת תורים</h1>

        <nav className="nav-links">
          <Link to="/">דף הבית</Link>
          <Link to="/booking">הזמנת תור</Link>
          <Link to="/admin">אדמין</Link>
        </nav>
      </div>

    </header>
  );
};

export default Header;
