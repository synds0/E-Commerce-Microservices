package microservice.Order.service;


import microservice.Order.dto.OrderItemDto;
import microservice.Order.dto.OrderResponse;
import microservice.Order.model.*;
import microservice.Order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import microservice.Order.model.CartItem;
import microservice.Order.model.Order;
import microservice.Order.model.OrderItem;
import microservice.Order.model.OrderStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CartService cartService;
    private final OrderRepository orderRepository;

    public Optional<OrderResponse> createOrder(String userId) {
        //Validate for cart item
        List<CartItem> cartItems = cartService.getCart(userId);
        if (cartItems.isEmpty()){
            return Optional.empty();
        }

//        //Validate the User
//        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
//        if (userOptional.isEmpty()){
//            return Optional.empty();
//        }
//        User user = userOptional.get();

        //Calculate total price
        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO , BigDecimal::add);

//        BigDecimal totalPrice = cartItems.stream()
//                .map(item -> item.getPrice()
//                        .multiply(BigDecimal.valueOf(item.getQuantity())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);



        //Create Order
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);

        List<OrderItem> orderItems = cartItems.stream()     // cartItem to orderItem transformation
                .map(cartItem -> new OrderItem(
                        null,
                        cartItem.getProductId(),
                        cartItem.getQuantity(),
                        cartItem.getPrice(),
                        order
                )).toList();
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        //Clear the cart
        cartService.clearCart(userId);

        return Optional.of(mapToOrderResponse(savedOrder));     // Optional.of(value) ---> Wraps the value
    }

    private OrderResponse mapToOrderResponse(Order order) {     // Order to OrderResponse
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getItems().stream()       // Because OrderResponse Item is -- List<OrderItemDto> type So need to convert it
                        .map(orderItem -> new OrderItemDto(
                                orderItem.getId(),
                                orderItem.getProductId(),
                                orderItem.getQuantity(),
                                orderItem.getPrice(),
                                orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))
                        )).toList(),
                order.getCreatedAt()

        );
    }
}
