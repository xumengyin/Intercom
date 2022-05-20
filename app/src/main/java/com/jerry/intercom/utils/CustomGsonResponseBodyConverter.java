package com.jerry.intercom.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {

        String response = value.string();

        CerResponse cerResponse = null;
        try {
            cerResponse = gson.fromJson(response, CerResponse.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        if (cerResponse != null) {
        } else {
            try {
                response = AESUtil.decryptAES(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Reader reader = new StringReader(response);
        JsonReader jsonReader = gson.newJsonReader(reader);

        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}
