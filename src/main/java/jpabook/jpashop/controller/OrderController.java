package jpabook.jpashop.controller;

import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
}
