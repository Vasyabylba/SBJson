package util;

import domain.Customer;
import domain.Order;
import domain.Product;
import domain.ProductMetadata;
import domain.ProductType;

import java.math.BigDecimal;
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
                .weight(BigDecimal.valueOf(4.523))
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

    public static String generateCustomerJson() {
        return """
                {"id":"bf5166ee-da80-464c-987e-30a0c074e09a","firstName":"Firstname","lastName":"Lastname","dateBirth":"2024-10-07","orders":[{"id":"a4b84c3f-392c-4aaa-b862-311e50c1f694","products":[{"id":"3959fd87-7500-408e-924b-8198ae401bb1","name":"Product name","price":5.234,"details":{"RETAIL":{"id":"2ccffc23-979d-4b10-8353-cfee9fe6d904","manufacturer":"Product manufacturer","weight":4.523},"WHOLESALE":{"id":"be4da32f-99e7-4ad3-bfcf-6f4efc36ff7f","manufacturer":"Product manufacturer","weight":4.523}}},{"id":"7a10cc63-9cbe-4260-a3f4-85f7c4644f86","name":"Product name","price":5.234,"details":{"RETAIL":{"id":"5fe9b27a-46c0-43a9-93e1-d03932858772","manufacturer":"Product manufacturer","weight":4.523},"WHOLESALE":{"id":"5c4bea42-5c32-4783-bef0-48769d5ea4c4","manufacturer":"Product manufacturer","weight":4.523}}},{"id":"05734b8e-3da5-48b6-bdb8-f08699a750e5","name":"Product name","price":5.234,"details":{"RETAIL":{"id":"0caad256-56f5-4c10-a71d-c691b2576d53","manufacturer":"Product manufacturer","weight":4.523},"WHOLESALE":{"id":"4da49782-676c-417e-9ec7-69d6cbefd654","manufacturer":"Product manufacturer","weight":4.523}}}],"createDate":"2024-10-07T03:44:13.3191252+03:00"},{"id":"6e26a2c0-308a-4753-a1f0-b7a6a2136806","products":[{"id":"b7467ab6-4708-4c00-a991-014190d60470","name":"Product name","price":5.234,"details":{"RETAIL":{"id":"865afd59-ccd5-4d6f-a0c7-dd5d4e5a68fd","manufacturer":"Product manufacturer","weight":4.523},"WHOLESALE":{"id":"5ff1fc8d-0e98-4bec-b7eb-490e43c320d4","manufacturer":"Product manufacturer","weight":4.523}}},{"id":"f2d2ae3e-d79e-4d28-bf56-5d21cd043dfb","name":"Product name","price":5.234,"details":{"RETAIL":{"id":"bedb7195-d9c5-457c-af2f-8c0ff93b5bb5","manufacturer":"Product manufacturer","weight":4.523},"WHOLESALE":{"id":"faa52aa4-cb79-4771-8127-b6036c1c75b2","manufacturer":"Product manufacturer","weight":4.523}}},{"id":"a4999f1c-de02-40d1-aec3-fba70c12fae1","name":"Product name","price":5.234,"details":{"RETAIL":{"id":"d3709926-0f37-4a61-ad8a-13f975618fa2","manufacturer":"Product manufacturer","weight":4.523},"WHOLESALE":{"id":"8dbf7dcc-e9b8-47f5-8b91-395010a99f55","manufacturer":"Product manufacturer","weight":4.523}}}],"createDate":"2024-10-07T03:44:13.3191252+03:00"},{"id":"9cc9b34a-8d2c-4ef9-ae94-982a18bac749","products":[{"id":"b6f478d1-b756-45b8-8964-8ba365ea4d38","name":"Product name","price":5.234,"details":{"RETAIL":{"id":"5f20103d-dde1-4694-b6f3-066582f9b937","manufacturer":"Product manufacturer","weight":4.523},"WHOLESALE":{"id":"3a235b14-5ca2-47c7-9c3b-d7088f60168b","manufacturer":"Product manufacturer","weight":4.523}}},{"id":"bd625ce2-44d6-42de-81bc-5bc18e41fb86","name":"Product name","price":5.234,"details":{"RETAIL":{"id":"346a624f-daa5-4cf6-a5d3-cd5f0740aa50","manufacturer":"Product manufacturer","weight":4.523},"WHOLESALE":{"id":"1b235ab9-346e-4d4a-9c32-003824cc9444","manufacturer":"Product manufacturer","weight":4.523}}},{"id":"11635bf9-8e79-4c56-be16-d435c0b2fb1d","name":"Product name","price":5.234,"details":{"RETAIL":{"id":"12e9e853-472c-4a54-beb7-5343aa2f8b29","manufacturer":"Product manufacturer","weight":4.523},"WHOLESALE":{"id":"fb121e13-1a40-425d-95cc-cb6e693034ed","manufacturer":"Product manufacturer","weight":4.523}}}],"createDate":"2024-10-07T03:44:13.3191252+03:00"}]}""";
    }

    public static String generateInvalidCustomerJson() {
        return """
                {"id":"bf5166ee-da80-464c-987e-30a0c074e09a","firstName":"55","lastName":"Lastname","dateBirth":"2024-10-07","orders":[{"id":"a4b84c3f-392c-4aaa-b862-311e50c1f694","products":[{"id":"3959fd87-7500-408e-924b-8198ae401bb1","name":"Product name","price":[5.234],"details":{"RETAIL":{"id":"2ccffc23-979d-4b10-8353-cfee9fe6d904","manufacturer":"Product manufacturer","weight":4.523},"WHOLESALE":{"id":"be4da32f-99e7-4ad3-bfcf-6f4efc36ff7f","manufacturer":"Product manufacturer","weight":4.523}}},{"id":"7a10cc63-9cbe-4260-a3f4-85f7c4644f86","name":"Product name","price":5.234,"details":{"RETAIL":{"id":"5fe9b27a-46c0-43a9-93e1-d03932858772","manufacturer":"Product manufacturer","weight":4.523},"WHOLESALE":{"id":"5c4bea42-5c32-4783-bef0-48769d5ea4c4","manufacturer":"Product manufacturer","weight":4.523}}},{"id":"05734b8e-3da5-48b6-bdb8-f08699a750e5","name":"Product name","price":5.234,"details":{"RETAIL":{"id":"0caad256-56f5-4c10-a71d-c691b2576d53","manufacturer":"Product manufacturer","weight":4.523},"WHOLESALE":{"id":"4da49782-676c-417e-9ec7-69d6cbefd654","manufacturer":"Product manufacturer","weight":4.523}}}],"createDate":"2024-10-07T03:44:13.3191252+03:00"},{"id":"6e26a2c0-308a-4753-a1f0-b7a6a2136806","products":[{"id":"b7467ab6-4708-4c00-a991-014190d60470","name":"Product name","price":5.234,"details":{"RETAIL":{"id":"865afd59-ccd5-4d6f-a0c7-dd5d4e5a68fd","manufacturer":"Product manufacturer","weight":4.523},"WHOLESALE":{"id":"5ff1fc8d-0e98-4bec-b7eb-490e43c320d4","manufacturer":"Product manufacturer","weight":4.523}}},{"id":"f2d2ae3e-d79e-4d28-bf56-5d21cd043dfb","name":"Product name","price":5.234,"details":{"RETAIL":{"id":"bedb7195-d9c5-457c-af2f-8c0ff93b5bb5","manufacturer":"Product manufacturer","weight":4.523},"WHOLESALE":{"id":"faa52aa4-cb79-4771-8127-b6036c1c75b2","manufacturer":"Product manufacturer","weight":4.523}}},{"id":"a4999f1c-de02-40d1-aec3-fba70c12fae1","name":"Product name","price":5.234,"details":{"RETAIL":{"id":"d3709926-0f37-4a61-ad8a-13f975618fa2","manufacturer":"Product manufacturer","weight":4.523},"WHOLESALE":{"id":"8dbf7dcc-e9b8-47f5-8b91-395010a99f55","manufacturer":"Product manufacturer","weight":4.523}}}],"createDate":"2024-10-07T03:44:13.3191252+03:00"},{"id":"9cc9b34a-8d2c-4ef9-ae94-982a18bac749","products":[{"id":"b6f478d1-b756-45b8-8964-8ba365ea4d38","name":"Product name","price":5.234,"details":{"RETAIL":{"id":"5f20103d-dde1-4694-b6f3-066582f9b937","manufacturer":"Product manufacturer","weight":4.523},"WHOLESALE":{"id":"3a235b14-5ca2-47c7-9c3b-d7088f60168b","manufacturer":"Product manufacturer","weight":4.523}}},{"id":"bd625ce2-44d6-42de-81bc-5bc18e41fb86","name":"Product name","price":5.234,"details":{"RETAIL":{"id":"346a624f-daa5-4cf6-a5d3-cd5f0740aa50","manufacturer":"Product manufacturer","weight":4.523},"WHOLESALE":{"id":"1b235ab9-346e-4d4a-9c32-003824cc9444","manufacturer":"Product manufacturer","weight":4.523}}},{"id":"11635bf9-8e79-4c56-be16-d435c0b2fb1d","name":"Product name","price":5.234,"details":{"RETAIL":{"id":"12e9e853-472c-4a54-beb7-5343aa2f8b29","manufacturer":"Product manufacturer","weight":4.523},"WHOLESALE":{"id":"fb121e13-1a40-425d-95cc-cb6e693034ed","manufacturer":"Product manufacturer","weight":4.523}}}],"createDate":"2024-10-07T03:44:13.3191252+03:00"}]}""";
    }
}
