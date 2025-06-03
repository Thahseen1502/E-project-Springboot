//package com.e_project.order_service.config;
//
//import com.e_project.product_service.grpc.ProductServiceGrpc;
//import com.e_project.user_service.grpc.UserServiceGrpc;
//import io.grpc.ManagedChannel;
//import io.grpc.ManagedChannelBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class SimpleGrpcConfig {
//
//    @Bean
//    public UserServiceGrpc.UserServiceBlockingStub userServiceStub() {
//        ManagedChannel channel = ManagedChannelBuilder
//                .forAddress("user-service", 9091)
//                .usePlaintext()
//                .build();
//        return UserServiceGrpc.newBlockingStub(channel);
//    }
//
//    @Bean
//    public ProductServiceGrpc.ProductServiceBlockingStub productServiceStub() {
//        ManagedChannel channel = ManagedChannelBuilder
//                .forAddress("product-service", 9092)
//                .usePlaintext()
//                .build();
//        return ProductServiceGrpc.newBlockingStub(channel);
//    }
//}
package com.e_project.order_service.config;

import com.e_project.product_service.grpc.ProductServiceGrpc;
import com.e_project.user_service.grpc.UserServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {

    @Bean
    public ManagedChannel userServiceChannel() {
        return ManagedChannelBuilder.forAddress("user-service", 9091)
                .usePlaintext()
                .build();
    }

    @Bean
    public UserServiceGrpc.UserServiceBlockingStub userServiceStub(ManagedChannel userServiceChannel) {
        return UserServiceGrpc.newBlockingStub(userServiceChannel);
    }

    @Bean
    public ManagedChannel productServiceChannel() {
        return ManagedChannelBuilder.forAddress("product-service", 9092)
                .usePlaintext()
                .build();
    }

    @Bean
    public ProductServiceGrpc.ProductServiceBlockingStub productServiceStub(ManagedChannel productServiceChannel) {
        return ProductServiceGrpc.newBlockingStub(productServiceChannel);
    }
}

