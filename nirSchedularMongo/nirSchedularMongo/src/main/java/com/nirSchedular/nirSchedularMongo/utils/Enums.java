package com.nirSchedular.nirSchedularMongo.utils;

/**
 * A utility class to hold enums used across the Nir Scheduler application.
 */
public class Enums {

    /**
     * Enum representing different time slots for appointments.
     * Each time slot has a corresponding label in Hebrew.
     */
    public enum TimeSlot {
        MORNING("בוקר"),
        AFTERNOON("אחר הצהריים"),
        EVENING("ערב");

        // The Hebrew representation of the time slot
        private final String hebrewLabel;

        /**
         * Constructor for the enum that assigns the Hebrew label.
         *
         * @param hebrewLabel the label in Hebrew corresponding to the enum constant
         */
        TimeSlot(String hebrewLabel) {
            this.hebrewLabel = hebrewLabel;
        }

        /**
         * Returns the Hebrew label of the time slot.
         *
         * @return Hebrew label as a String
         */
        public String getHebrewLabel() {
            return hebrewLabel;
        }

        /**
         * Converts a given string to the corresponding TimeSlot enum value.
         * Supports both English enum names (case insensitive) and Hebrew labels.
         *
         * @param value the input string representing the time slot
         * @return the corresponding TimeSlot enum constant
         * @throws IllegalArgumentException if the input doesn't match any time slot
         */
        public static TimeSlot fromString(String value) {
            if (value == null) {
                throw new IllegalArgumentException("TimeSlot value cannot be null");
            }

            // Loop through all TimeSlot values and find a match by English name or Hebrew label
            for (TimeSlot slot : TimeSlot.values()) {
                if (slot.name().equalsIgnoreCase(value.trim()) ||  // Match enum name (e.g., "morning")
                        slot.getHebrewLabel().equals(value.trim())) {  // Match Hebrew label (e.g., "בוקר")
                    return slot;
                }
            }

            // If no match was found, throw an exception
            throw new IllegalArgumentException("Invalid TimeSlot value: " + value);
        }
    }
}
