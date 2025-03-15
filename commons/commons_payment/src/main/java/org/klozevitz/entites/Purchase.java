package org.klozevitz.entites;

import com.pengrad.telegrambot.model.OrderInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
public class Purchase {
    private String id;

    private String chatId;

    private String currency;

    private Subscription subscription;

    private Instant purchaseDate;

    private OrderInfo orderInfo;
}
