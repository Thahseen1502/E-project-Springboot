package com.e_project.product_service.grpc.service;

import com.e_project.product_service.grpc.*;
import com.e_project.product_service.model.Product;
import com.e_project.product_service.service.ProductService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@GrpcService
public class ProductGrpcService extends ProductServiceGrpc.ProductServiceImplBase {

    @Autowired
    private ProductService productService;

    @Override
    public void getAllProducts(ProductProto.GetAllProductsRequest request,
                               StreamObserver<ProductProto.ProductsResponse> responseObserver) {
        try {
            List<Product> products = productService.getAllProducts();

            ProductProto.ProductsResponse.Builder responseBuilder = ProductProto.ProductsResponse.newBuilder();

            for (Product product : products) {
                ProductProto.Product grpcProduct = convertToGrpcProduct(product);
                responseBuilder.addProducts(grpcProduct);
            }

            responseBuilder.setSuccess(true);
            responseBuilder.setMessage("Products retrieved successfully");

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            ProductProto.ProductsResponse errorResponse = ProductProto.ProductsResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Error retrieving products: " + e.getMessage())
                    .build();

            responseObserver.onNext(errorResponse);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getProductById(ProductProto.GetProductByIdRequest request,
                               StreamObserver<ProductProto.ProductResponse> responseObserver) {
        try {
            Optional<Product> productOptional = productService.getProductById(request.getId());

            ProductProto.ProductResponse.Builder responseBuilder = ProductProto.ProductResponse.newBuilder();

            if (productOptional.isPresent()) {
                ProductProto.Product grpcProduct = convertToGrpcProduct(productOptional.get());
                responseBuilder.setProduct(grpcProduct);
                responseBuilder.setSuccess(true);
                responseBuilder.setMessage("Product found successfully");
            } else {
                responseBuilder.setSuccess(false);
                responseBuilder.setMessage("Product not found with id: " + request.getId());
            }

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            ProductProto.ProductResponse errorResponse = ProductProto.ProductResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Error retrieving product: " + e.getMessage())
                    .build();

            responseObserver.onNext(errorResponse);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void createProduct(ProductProto.CreateProductRequest request,
                              StreamObserver<ProductProto.ProductResponse> responseObserver) {
        try {
            Product product = new Product();
            product.setName(request.getName());
            product.setDescription(request.getDescription());
            product.setPrice(request.getPrice());
            product.setStock(request.getStock());

            Product savedProduct = productService.createProduct(product);

            ProductProto.Product grpcProduct = convertToGrpcProduct(savedProduct);

            ProductProto.ProductResponse response = ProductProto.ProductResponse.newBuilder()
                    .setProduct(grpcProduct)
                    .setSuccess(true)
                    .setMessage("Product created successfully")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            ProductProto.ProductResponse errorResponse = ProductProto.ProductResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Error creating product: " + e.getMessage())
                    .build();

            responseObserver.onNext(errorResponse);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void updateProduct(ProductProto.UpdateProductRequest request,
                              StreamObserver<ProductProto.ProductResponse> responseObserver) {
        try {
            Product product = new Product();
            product.setName(request.getName());
            product.setDescription(request.getDescription());
            product.setPrice(request.getPrice());
            product.setStock(request.getStock());

            Optional<Product> updatedProductOptional = productService.updateProduct(request.getId(), product);

            ProductProto.ProductResponse.Builder responseBuilder = ProductProto.ProductResponse.newBuilder();

            if (updatedProductOptional.isPresent()) {
                ProductProto.Product grpcProduct = convertToGrpcProduct(updatedProductOptional.get());
                responseBuilder.setProduct(grpcProduct);
                responseBuilder.setSuccess(true);
                responseBuilder.setMessage("Product updated successfully");
            } else {
                responseBuilder.setSuccess(false);
                responseBuilder.setMessage("Product not found with id: " + request.getId());
            }

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            ProductProto.ProductResponse errorResponse = ProductProto.ProductResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Error updating product: " + e.getMessage())
                    .build();

            responseObserver.onNext(errorResponse);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void deleteProduct(ProductProto.DeleteProductRequest request,
                              StreamObserver<ProductProto.DeleteProductResponse> responseObserver) {
        try {
            productService.deleteProduct(request.getId());

            ProductProto.DeleteProductResponse response = ProductProto.DeleteProductResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Product deleted successfully")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            ProductProto.DeleteProductResponse errorResponse = ProductProto.DeleteProductResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Error deleting product: " + e.getMessage())
                    .build();

            responseObserver.onNext(errorResponse);
            responseObserver.onCompleted();
        }
    }

    // Helper method to convert JPA Product to gRPC Product
    private ProductProto.Product convertToGrpcProduct(Product product) {
        return ProductProto.Product.newBuilder()
                .setId(product.getId())
                .setName(product.getName())
                .setDescription(product.getDescription() != null ? product.getDescription() : "")
                .setPrice(product.getPrice())
                .setStock(product.getStock())
                .build();
    }
}