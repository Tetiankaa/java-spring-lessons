//package com.example.javaspringlessons.controller;
//
//import com.example.javaspringlessons.dto.OrderDTO;
//import com.example.javaspringlessons.service.OrderService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/order")
//@RequiredArgsConstructor
//public class OrderController {



//    @GetMapping("")
//    public ResponseEntity<List<OrderDTO>> getOrders(){
//        List<OrderDTO> orders = service.getAll();
//
//        return  ResponseEntity.ok(orders);
//    }
//
//    @PostMapping("")
//    public ResponseEntity<OrderDTO> confirmOrder(@RequestBody OrderDTO order){
//
//        return ResponseEntity.ok(service.save(order));
//    }

//}
