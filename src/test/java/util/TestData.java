package util;

import ru.clevertec.jsonparser.domain.Customer;
import ru.clevertec.jsonparser.domain.Order;
import ru.clevertec.jsonparser.domain.Product;
import ru.clevertec.jsonparser.domain.ProductMetadata;
import ru.clevertec.jsonparser.domain.ProductType;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TestData {
    public static ProductMetadata generateMetadata() {
        return ProductMetadata.builder()
                .id(UUID.randomUUID())
                .manufacturer("Product manufacturer")
                .weight(4.523)
                .build();
    }

    public static Product generateProduct() {
        Map<ProductType, ProductMetadata> details = new HashMap<>();
        details.put(ProductType.RETAIL, generateMetadata());
        details.put(ProductType.WHOLESALE, generateMetadata());

        return Product.builder()
                .id(UUID.randomUUID())
                .name("Product name")
                .price(5.234)
                .details(details)
                .build();
    }

    public static Order generateOrder() {
        return Order.builder()
                .id(UUID.randomUUID())
                .products(List.of(
                        generateProduct(),
                        generateProduct(),
                        generateProduct()
                ))
                .createDate(OffsetDateTime.now())
                .build();
    }

    public static Customer generateCustomer() {
        return Customer.builder()
                .id(UUID.randomUUID())
                .firstName("Firstname")
                .lastName("Lastname")
                .dateBirth(LocalDate.now())
                .orders(List.of(
                        generateOrder(),
                        generateOrder(),
                        generateOrder()
                ))
                .build();
    }
}
