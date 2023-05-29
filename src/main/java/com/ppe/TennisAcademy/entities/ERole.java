package com.ppe.TennisAcademy.entities;

public enum ERole {

    ROLE_SUPER_ADMIN {
        @Override
        public String toString() {
            return "ROLE_SUPER_ADMIN";
        }
    },

    ROLE_ADMIN {
        @Override
        public String toString() {
            return "ROLE_ADMIN";
        }
    },

    ROLE_ADHERENT {
        @Override
        public String toString() {
            return "ROLE_ADHERENT";
        }
    },
    ROLE_COACH {
        @Override
        public String toString() {
            return "ROLE_COACH";
        }
    },


}

