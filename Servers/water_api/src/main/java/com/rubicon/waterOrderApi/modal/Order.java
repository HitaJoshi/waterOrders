package com.rubicon.waterOrderApi.modal;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;


@Component
public class Order {
	@NotNull
	private String farmId;
	@NotNull
	private String orderId;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private Double duration;
	private String orderStatus;
	
	public Order() {
		super();
	}
	
	public Order(@NotNull String farmId, @NotNull String orderId, LocalDateTime startDateTime,
			LocalDateTime endDateTime, Double duration, String orderStatus) {
		super();
		this.farmId = farmId;
		this.orderId = orderId;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.duration = duration;
		this.orderStatus = orderStatus;
	}

	public String getFarmId() {
		return farmId;
	}

	public void setFarmId(String farmId) {
		this.farmId = farmId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(LocalDateTime endDateTime) {
		this.endDateTime = endDateTime;
	}

	public Double getDuration() {
		return duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	

}