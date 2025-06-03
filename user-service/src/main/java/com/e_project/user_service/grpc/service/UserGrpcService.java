package com.e_project.user_service.grpc.service;

import com.e_project.user_service.grpc.*;
import com.e_project.user_service.model.User;
import com.e_project.user_service.service.UserService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@GrpcService
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private UserService userService;

    @Override
    public void getAllUsers(UserProto.GetAllUsersRequest request,
                            StreamObserver<UserProto.UsersResponse> responseObserver) {
        try {
            List<User> users = userService.getAllUsers();

            UserProto.UsersResponse.Builder responseBuilder = UserProto.UsersResponse.newBuilder();

            for (User user : users) {
                UserProto.User grpcUser = convertToGrpcUser(user);
                responseBuilder.addUsers(grpcUser);
            }

            responseBuilder.setSuccess(true);
            responseBuilder.setMessage("Users retrieved successfully");

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            UserProto.UsersResponse errorResponse = UserProto.UsersResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Error retrieving users: " + e.getMessage())
                    .build();

            responseObserver.onNext(errorResponse);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getUserById(UserProto.GetUserByIdRequest request,
                            StreamObserver<UserProto.UserResponse> responseObserver) {
        try {
            Optional<User> userOptional = userService.getUserById(request.getId());

            UserProto.UserResponse.Builder responseBuilder = UserProto.UserResponse.newBuilder();

            if (userOptional.isPresent()) {
                UserProto.User grpcUser = convertToGrpcUser(userOptional.get());
                responseBuilder.setUser(grpcUser);
                responseBuilder.setSuccess(true);
                responseBuilder.setMessage("User found successfully");
            } else {
                responseBuilder.setSuccess(false);
                responseBuilder.setMessage("User not found with id: " + request.getId());
            }

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            UserProto.UserResponse errorResponse = UserProto.UserResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Error retrieving user: " + e.getMessage())
                    .build();

            responseObserver.onNext(errorResponse);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void createUser(UserProto.CreateUserRequest request,
                           StreamObserver<UserProto.UserResponse> responseObserver) {
        try {
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setPhone(request.getPhone());
            user.setAddress(request.getAddress());
            user.setRole(request.getRole().isEmpty() ? "USER" : request.getRole());

            User savedUser = userService.createUser(user);

            UserProto.User grpcUser = convertToGrpcUser(savedUser);

            UserProto.UserResponse response = UserProto.UserResponse.newBuilder()
                    .setUser(grpcUser)
                    .setSuccess(true)
                    .setMessage("User created successfully")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            UserProto.UserResponse errorResponse = UserProto.UserResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Error creating user: " + e.getMessage())
                    .build();

            responseObserver.onNext(errorResponse);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void updateUser(UserProto.UpdateUserRequest request,
                           StreamObserver<UserProto.UserResponse> responseObserver) {
        try {
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setPhone(request.getPhone());
            user.setAddress(request.getAddress());
            user.setRole(request.getRole());

            Optional<User> updatedUserOptional = userService.updateUser(request.getId(), user);

            UserProto.UserResponse.Builder responseBuilder = UserProto.UserResponse.newBuilder();

            if (updatedUserOptional.isPresent()) {
                UserProto.User grpcUser = convertToGrpcUser(updatedUserOptional.get());
                responseBuilder.setUser(grpcUser);
                responseBuilder.setSuccess(true);
                responseBuilder.setMessage("User updated successfully");
            } else {
                responseBuilder.setSuccess(false);
                responseBuilder.setMessage("User not found with id: " + request.getId());
            }

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            UserProto.UserResponse errorResponse = UserProto.UserResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Error updating user: " + e.getMessage())
                    .build();

            responseObserver.onNext(errorResponse);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void deleteUser(UserProto.DeleteUserRequest request,
                           StreamObserver<UserProto.DeleteUserResponse> responseObserver) {
        try {
            userService.deleteUser(request.getId());

            UserProto.DeleteUserResponse response = UserProto.DeleteUserResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("User deleted successfully")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            UserProto.DeleteUserResponse errorResponse = UserProto.DeleteUserResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Error deleting user: " + e.getMessage())
                    .build();

            responseObserver.onNext(errorResponse);
            responseObserver.onCompleted();
        }
    }

    // Helper method to convert JPA User to gRPC User
    private UserProto.User convertToGrpcUser(User user) {
        return UserProto.User.newBuilder()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setPhone(user.getPhone() != null ? user.getPhone() : "")
                .setAddress(user.getAddress() != null ? user.getAddress() : "")
                .setRole(user.getRole())
                .build();
    }
}