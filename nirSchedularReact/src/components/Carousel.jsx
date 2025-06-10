import React, { useState, useEffect, useRef } from "react";
import "./Carousel.css";

const images = [
  "/images/carousel1.jpg",
  "/images/carousel2.jpg",
  "/images/carousel3.jpg",
];

export default function Carousel() {
  const [index, setIndex] = useState(0); // Track the current image index
  const touchStartX = useRef(null); // Ref to store touch start position

  // Automatically slide images every 3 seconds
  useEffect(() => {
    const interval = setInterval(() => {
      setIndex((prev) => (prev + 1) % images.length); // Move to next image, prev is the previous index value, % images.length ensures the index wraps around to 0 when it reaches the end, so the slideshow loops
    }, 3000);
    return () => clearInterval(interval); // Cleanup interval on unmount
  }, []);

  // Capture the starting touch position for swipe detection
  const handleTouchStart = (e) => {
    touchStartX.current = e.touches[0].clientX; // Store the initial touch position
  };

  // Determine swipe direction and update the index accordingly
  const handleTouchEnd = (e) => {
    if (!touchStartX.current) return;   // If touchStartX is not set, do nothing
    const touchEndX = e.changedTouches[0].clientX;  // Get the final touch position
    const diff = touchStartX.current - touchEndX;   // Calculate the difference between start and end positions

    if (diff > 50) setIndex((index + 1) % images.length); // Swipe left (next)
    if (diff < -50) setIndex((index - 1 + images.length) % images.length); // Swipe right (previous)

    touchStartX.current = null; // Reset touch reference
  };

  return (
    <div className="carousel-container">
      {/* Display the current image */}
      <img
        src={images[index]} // Current image based on index
        alt="carousel"
        className="carousel-image"
        onTouchStart={handleTouchStart} // Mobile swipe start
        onTouchEnd={handleTouchEnd} // Mobile swipe end
      />

      {/* Left navigation button */}
      <button
        className="carousel-button left"
        onClick={() => setIndex((index - 1 + images.length) % images.length)}
      >
        &#8592; {/* Left arrow */}
      </button>

      {/* Right navigation button */}
      <button
        className="carousel-button right"
        onClick={() => setIndex((index + 1) % images.length)}
      >
        &#8594; {/* Right arrow */}
      </button>
    </div>
  );
}
