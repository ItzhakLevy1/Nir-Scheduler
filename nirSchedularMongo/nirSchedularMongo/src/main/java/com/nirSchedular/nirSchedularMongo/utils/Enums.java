package com.nirSchedular.nirSchedularMongo.utils;

public class Enums {
    public enum TimeSlot {
        MORNING("בוקר"),
        AFTERNOON("אחר הצהריים"),
        EVENING("ערב");

        private final String hebrewLabel;

        TimeSlot(String hebrewLabel) {
            this.hebrewLabel = hebrewLabel;
        }

        public String getHebrewLabel() {
            return hebrewLabel;
        }
    }
}
