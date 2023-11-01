package com.example.myapplication;

import com.example.myapplication.model.AgendaModel;
import com.example.myapplication.model.NewsModel;
import com.example.myapplication.model.ResourceModel;
import com.example.myapplication.model.SignInPayload;
import com.example.myapplication.model.SignUpPayload;
import com.example.myapplication.model.AuthResponse;
import com.example.myapplication.model.UserModel;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpService {

    //tod: better error handling
    private static String apiUrl = "https://68a7-195-57-116-254.ngrok-free.app/";

    public static CompletableFuture<List<ResourceModel>> getActiveResourcesAsync() {
        CompletableFuture<List<ResourceModel>> future = new CompletableFuture<>();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl + "resource/get-active")
                .build();

        Call call = client.newCall(request);

        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<ResourceModel>>(){}.getType();
                    List<ResourceModel> resourceList = gson.fromJson(jsonResponse, listType);
                    future.complete(resourceList);
                } else {
                    // Handle an unsuccessful response
                    future.completeExceptionally(new IOException("Request was not successful. Response code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // Handle network request failure
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    public static CompletableFuture<List<AgendaModel>> getActiveAgendasAsync() {
        CompletableFuture<List<AgendaModel>> future = new CompletableFuture<>();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl + "agenda/get-active")
                .build();

        Call call = client.newCall(request);

        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<AgendaModel>>(){}.getType();
                    List<AgendaModel> agendaList = gson.fromJson(jsonResponse, listType);
                    future.complete(agendaList);
                } else {
                    // Handle an unsuccessful response
                    future.completeExceptionally(new IOException("Request was not successful. Response code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // Handle network request failure
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    public static CompletableFuture<List<NewsModel>> getActiveNewsAsync() {
        CompletableFuture<List<NewsModel>> future = new CompletableFuture<>();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl + "news/get-active")
                .build();

        Call call = client.newCall(request);

        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<NewsModel>>(){}.getType();
                    List<NewsModel> newsList = gson.fromJson(jsonResponse, listType);
                    future.complete(newsList);
                } else {
                    // Handle an unsuccessful response
                    future.completeExceptionally(new IOException("Request was not successful. Response code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // Handle network request failure
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    public static CompletableFuture<AuthResponse> signUpAsync(SignUpPayload signUpPayload) {
        CompletableFuture<AuthResponse> future = new CompletableFuture<>();

        OkHttpClient client = new OkHttpClient();

        // Define the API endpoint for sign-up
        String signUpEndpoint = apiUrl + "auth/sign-up";

        // Create a JSON request body from the SignUpPayload object
        Gson gson = new Gson();
        String jsonPayload = gson.toJson(signUpPayload);
        RequestBody requestBody = RequestBody.create(jsonPayload, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(signUpEndpoint)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    Gson gson = new Gson();
                    AuthResponse signInResponse = gson.fromJson(jsonResponse, AuthResponse.class);
                    future.complete(signInResponse);
                } else {
                    String errorResponse = response.body().string();
                    JsonObject errorJson = gson.fromJson(errorResponse, JsonObject.class);
                    JsonElement errorElement = errorJson.get("message");

                    if (errorElement != null) {
                        String errorMessage = errorElement.getAsString();

                        if (errorMessage.isEmpty()) {
                            errorMessage = "Empty error message";
                        }

                        future.completeExceptionally(new IOException(errorMessage));
                    } else {
                        future.completeExceptionally(new IOException("Unknown error"));
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // Handle network request failure
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    public static CompletableFuture<AuthResponse> signInAsync(SignInPayload signInPayload) {
        CompletableFuture<AuthResponse> future = new CompletableFuture<>();

        OkHttpClient client = new OkHttpClient();

        String signInEndpoint = apiUrl + "auth/sign-in";

        Gson gson = new Gson();
        String jsonPayload = gson.toJson(signInPayload);
        RequestBody requestBody = RequestBody.create(jsonPayload, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(signInEndpoint)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    Gson gson = new Gson();
                    AuthResponse signInResponse = gson.fromJson(jsonResponse, AuthResponse.class);
                    future.complete(signInResponse);
                } else {
                    String errorResponse = response.body().string();
                    JsonObject errorJson = gson.fromJson(errorResponse, JsonObject.class);
                    JsonElement errorElement = errorJson.get("message");

                    if (errorElement != null) {
                        String errorMessage = errorElement.getAsString();

                        if (errorMessage.isEmpty()) {
                            errorMessage = "Empty error message";
                        }

                        future.completeExceptionally(new IOException(errorMessage));
                    } else {
                        future.completeExceptionally(new IOException("Unknown error"));
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // Handle network request failure
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    public static CompletableFuture<UserModel> getMeAsync(String jwtToken) {
        CompletableFuture<UserModel> future = new CompletableFuture<>();

        Gson gson = new Gson();
        OkHttpClient client = new OkHttpClient();

        String getMeEndpoint = apiUrl + "user/get-me";

        Request request = new Request.Builder()
                .url(getMeEndpoint)
                .get()
                .addHeader("Authorization", "Bearer " + jwtToken) // Include JWT token in the Authorization header
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    Gson gson = new Gson();
                    UserModel userModel = gson.fromJson(jsonResponse, UserModel.class);
                    future.complete(userModel);
                } else {
                    String errorResponse = response.body().string();
                    JsonObject errorJson = gson.fromJson(errorResponse, JsonObject.class);
                    JsonElement errorElement = errorJson.get("message");

                    if (errorElement != null) {
                        String errorMessage = errorElement.getAsString();

                        if (errorMessage.isEmpty()) {
                            errorMessage = "Empty error message";
                        }

                        future.completeExceptionally(new IOException(errorMessage));
                    } else {
                        future.completeExceptionally(new IOException("Unknown error"));
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // Handle network request failure
                future.completeExceptionally(e);
            }
        });

        return future;
    }

}
