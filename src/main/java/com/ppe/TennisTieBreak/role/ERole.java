package com.ppe.TennisTieBreak.role;

public enum ERole {

	 ROLE_ADMIN {
	        @Override
	        public String toString() {
	            return "ROLE_ADMIN";
	        }
	    },

	    ROLE_JOUEUR {
	        @Override
	        public String toString() {
	            return "ROLE_JOUEUR";
	        }
	    },
	    ROLE_ENTRAINEUR {
	        @Override
	        public String toString() {
	            return "ROLE_ENTRAINEUR";
	        }
	    },

}

