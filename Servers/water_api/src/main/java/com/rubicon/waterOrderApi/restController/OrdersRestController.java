package com.rubicon.waterOrderApi.restController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rubicon.waterOrderApi.modal.Order;
import com.rubicon.waterOrderApi.modal.OrderStatus.Status;
import com.rubicon.waterOrderApi.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrdersRestController {

	Map<String, List<Order>> orderMap = new HashMap<>();
	Set<Order> orderSet = new HashSet<>();
	List<Order> waterOrderList = new ArrayList<>();
	HttpStatus responseStatus = HttpStatus.NO_CONTENT;
	HttpHeaders responseHeaders = new HttpHeaders();

	@Autowired
	OrderService orderService;

	// ALL ORDERS
	@GetMapping
	public ResponseEntity<Object> getAllOrders() {
		if (!CollectionUtils.isEmpty(waterOrderList)) {
			orderService.updateStatus(waterOrderList);
			return new ResponseEntity<>(waterOrderList, responseHeaders, HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>("", responseHeaders, HttpStatus.NO_CONTENT);
	}

	// ORDER BASED ON ORDERID
	@GetMapping("/{orderId}")
	public ResponseEntity<Object> getOrderForOrderId(@PathVariable String orderId) {
		orderService.updateStatus(waterOrderList);
		Order orderDetails = orderService.getWaterOrderForOrderId(waterOrderList, orderId);

		if (orderDetails != null) {
			return new ResponseEntity<>(orderDetails, responseHeaders, HttpStatus.ACCEPTED);

		}

		return new ResponseEntity<>("", responseHeaders, HttpStatus.NO_CONTENT);
	}

    //PLACE NEW ORDER
	@PostMapping
	public ResponseEntity<String> placeOrders(@RequestBody List<Order> orderRequestList) throws IOException {
		responseStatus = HttpStatus.CONFLICT;
		if (orderService.createOrder(orderRequestList, waterOrderList)) {
			return new ResponseEntity<String>(Status.REQUESTED.description(), responseHeaders, HttpStatus.CREATED);
		}
		return new ResponseEntity<String>("", responseHeaders, HttpStatus.CONFLICT);
	}

	//CANCEL ORDER
	@DeleteMapping("/{orderId}")
	public ResponseEntity<String> cancelOrder(@PathVariable String orderId) {
		if (orderService.cancelOrder(orderId, waterOrderList)) {
			return new ResponseEntity<String>(Status.CANCELLED.description(), responseHeaders, HttpStatus.ACCEPTED);
		}

		return new ResponseEntity<String>("", responseHeaders, HttpStatus.CONFLICT);
	}

}
