package com.myweddi.utils;

import android.util.Log;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BasicAuthInterceptor implements Interceptor {

//        @Override
//        public ClientHttpResponse intercept(
//                HttpRequest request,
//        byte[] body,
//        ClientHttpRequestExecution execution) throws IOException {
//
//            ClientHttpResponse response = execution.execute(request, body);
//            response.getHeaders().add("Foo", "bar");
//            return response;
//        }

    private String username;
    private String password;
    private String credentials;

    public BasicAuthInterceptor(String username, String password){
        this.username = username;
        this.password = password;
        this.credentials = Credentials.basic(username, password);

        Log.i("dd", credentials);
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        Request authorization = request.newBuilder().header("Authorization", credentials).build();
        //Request authorization = request.newBuilder().header("Authorization", "Basic "+username+":"+password).build();
        return chain.proceed(authorization);
    }

}
