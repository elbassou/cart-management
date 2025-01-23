package app.micros.cartservice.model;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", nullable = false)
    private Long clientId; // Chaque panier est associé à un utilisateur

    @ElementCollection
    @CollectionTable(name = "cart_cart_Item_Ids", joinColumns = @JoinColumn(name="cart_id"))
    @Column(name="cart_Item_id")
    private List<Long> cartItemIds = new ArrayList<>();   // ref List<CartItem> items;

    LocalDateTime dateAchat;

   public Cart(Long clientId) {
       this.clientId = clientId;
   }
}
