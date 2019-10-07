package com.gdc.graphqlpart2.data;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import static com.gdc.graphqlpart2.utils.Constants.BASE_URL;

public class ApiClient {

    private static ApolloClient apolloClient;

    public static ApolloClient getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .addInterceptor(interceptor)
                .build();

        if (apolloClient == null) {
            apolloClient = ApolloClient.builder()
                    .serverUrl(BASE_URL)
                    .okHttpClient(okHttpClient)
                    .build();
        }

        return apolloClient;
    }

    public static ApolloClient getClientSampleHeader(final String gdcToken) {
        OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {
                        Request original = chain.request();

                        Request request = original.newBuilder()
                                .header("gdc-token", gdcToken)
                                .header("Content-Type", "application/json")
                                .method(original.method(), original.body())
                                .build();

                        return chain.proceed(request);
                    }
                }).build();

        if (apolloClient == null) {
            apolloClient = ApolloClient.builder()
                    .serverUrl(BASE_URL)
                    .okHttpClient(client)
                    .build();
        }

        return apolloClient;
    }

}
