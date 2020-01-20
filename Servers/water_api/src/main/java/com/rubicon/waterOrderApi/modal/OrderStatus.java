package com.rubicon.waterOrderApi.modal;

public  class OrderStatus {
	
	public enum Status {
		REQUESTED("Order has been placed but not yet delivered"), 
		INPROGRESS("Order is being delivered right now"), 
		DELIVERED("Order has been delivered"), 
		CANCELLED("Order was cancelled before delivery");
		
		private String description;
 
		Status(String description) {
			this.description = description;
		}
		public String description() {
		return description;
	}	
}
}
