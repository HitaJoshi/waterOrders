package com.rubicon.waterOrderApi.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.rubicon.waterOrderApi.modal.Order;
import com.rubicon.waterOrderApi.modal.OrderStatus.Status;

@Service
public class OrderService {

	private static Logger logger = LogManager.getLogger();

	
	public Order getWaterOrderForOrderId(List<Order> waterOrderList, String orderId) {

		return waterOrderList.stream().filter(a -> orderId.equals(a.getOrderId())).findFirst().orElse(null);

	}
	public List<Order> getWaterOrderForFarmId(List<Order> waterOrderList, String farmId) {

		return waterOrderList.stream().filter(a -> farmId.equals(a.getFarmId())).collect(Collectors.toList());
	}

	public boolean createOrder(List<Order> orderRequestList, List<Order> waterOrderList) {
		boolean orderCreated = false;
		for (Order order : orderRequestList) {
			if (isOrderInFuture(order.getStartDateTime())) {
				Integer hours = (int) order.getDuration().longValue();

				Integer minutes = (int) ((order.getDuration() - hours) * 60);
				LocalDateTime endTimeHrsadded = order.getStartDateTime();

				endTimeHrsadded = endTimeHrsadded.plusHours(hours);
				endTimeHrsadded = endTimeHrsadded.plusMinutes(minutes);
				order.setEndDateTime(endTimeHrsadded);
				List<Order> waterOrderForFarmList= getWaterOrderForFarmId(waterOrderList,order.getFarmId());
				if (checkForOverlapTime(order,waterOrderForFarmList)) {
					waterOrderList.add(order);
					java.util.UUID uniqueOrderId = java.util.UUID.randomUUID();
					order.setOrderStatus(Status.REQUESTED.toString());
					order.setOrderId(uniqueOrderId.toString());
					logger.info("New water order for farm:" + order.getFarmId() + " created with order Id: "
							+ order.getOrderId());
					orderCreated = true;

				}
			}
		}

		updateStatus(waterOrderList);
		return orderCreated;
	}

	public boolean cancelOrder(String orderId, List<Order> waterOrderList) {
		updateStatus(waterOrderList);
		boolean orderDeleted = false;
		Order orderToBeCancelled = getWaterOrderForOrderId(waterOrderList,orderId);
		 if(orderToBeCancelled != null && !(orderToBeCancelled.getOrderStatus().equals(Status.DELIVERED.toString()))) {
			orderToBeCancelled.setOrderStatus(Status.CANCELLED.toString());
			logger.info("Water order for farm:" + orderToBeCancelled.getFarmId() + " has been Cancelled for order Id: "
					+ orderToBeCancelled.getOrderId());
			orderDeleted = true;
		}
	return orderDeleted;

	}

	private boolean isNotOverlapping(LocalDateTime start1, LocalDateTime end1, List<Order> existingOrderList) {

		for (Order orderItem : existingOrderList) {
			if (start1.isBefore(orderItem.getEndDateTime()) && orderItem.getStartDateTime().isBefore(end1)) {
				return false;
			}
		}
		return true;

	}

	private boolean checkForOverlapTime(Order order, List<Order> waterOrderForFarmList) {

		List<Order> existingOrderList = waterOrderForFarmList.stream()
				.filter(a -> Objects.equals(a.getFarmId(), order.getFarmId())
						&& a.getOrderStatus() != Status.CANCELLED.toString())
				.collect(Collectors.toList());

		if (!CollectionUtils.isEmpty(existingOrderList)) {

			return isNotOverlapping(order.getStartDateTime(), order.getEndDateTime(), existingOrderList);
		}

		return true;
	}

	private boolean isOrderInFuture(LocalDateTime startDateTime) {
		return startDateTime.isAfter(LocalDateTime.now());
	}

	public void updateStatus(List<Order> orderList) {
		List<Order> ordersToBeUpdated = orderList.stream().filter(a -> a.getOrderStatus() == Status.REQUESTED.toString()
				|| a.getOrderStatus() == Status.INPROGRESS.toString()).collect(Collectors.toList());
		//all orders which are not cancelled and delivered status
		
		for (Order orderItem : ordersToBeUpdated) {
			LocalDateTime date = LocalDateTime.now();

			if (date.isAfter(orderItem.getEndDateTime())){
				orderItem.setOrderStatus(Status.DELIVERED.toString());
				logger.info("Water delivery to farm:" + orderItem.getFarmId() + " has stopped: for OrderId: "
						+ orderItem.getOrderId());

			} else if (date.isAfter(orderItem.getStartDateTime())
					&& (!(orderItem.getOrderStatus().equals(Status.INPROGRESS.toString())))) {
				orderItem.setOrderStatus(Status.INPROGRESS.toString());
				logger.info("Water delivery to farm:" + orderItem.getFarmId() + " has started: for OrderId: "
						+ orderItem.getOrderId());

			}
		}
	}

}
