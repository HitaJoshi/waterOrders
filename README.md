# waterOrders
1.	Place New water order
End points:  http://localhost:8080/orders
Request Body
[
    
    {
        "farmId": "11",
        "startDateTime": "2020-01-19T13:43:00",
        "duration": "0.1"
       
    },
     {
        "farmId": "12",
        "startDateTime": "2020-01-19T13:43:00",
        "duration": "0.1"
       
    },
   
]
 
2.	Get all Orders

End Points: http://localhost:8080/orders
Method:GET


 
2.1 Get details of given orderId
End Points: http://localhost:8080/orders/{orderId}
METHOD:GET

3.	Cancel order

End Points: http://localhost:8080/orders/{orderId}
METHOD:DELETE







