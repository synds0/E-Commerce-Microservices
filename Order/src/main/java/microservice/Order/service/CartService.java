package microservice.Order.service;

import microservice.Order.dto.CartItemRequest;
import microservice.Order.model.CartItem;
import microservice.Order.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartItemRepository cartItemRepository;


    public boolean addToCart(String userId, CartItemRequest request) {

        // Looking for product
//        Optional<Product> productOpt = productRepository.findById(request.getProductId());
//
//        if (productOpt.isEmpty()){
//            return false;
//        }
//
//        Product product = productOpt.get();
//
//        if (product.getStockQuantity() < request.getQuantity()){
//            return false;
//        }
//
//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
//        if (userOpt.isEmpty()){
//            return false;
//        }
//
//        User user = userOpt.get();



        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId , request.getProductId());
        if (existingCartItem != null){              //Means item already in cart, want to Update the quantity and price

            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepository.save(existingCartItem);
        }else {
            // create new cart for the user
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepository.save(cartItem);
        }
        return true;

    }

    public boolean deleteItemFromCart(String userId, String  productId) {
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);

        if (cartItem != null){
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;
    }

    public List<CartItem> getCart(String userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
