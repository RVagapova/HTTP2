package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main {
    public static final String URL = "https://api.nasa.gov/planetary/apod?api_key=N4U2dRRoBtM5llrwjUmiXfnIOel1uZcUsXZPj1uA";

    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        //создание клиента
        CloseableHttpClient client = HttpClientBuilder.create()
                .setUserAgent("Agent")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000) //макс время ожидания от сервера
                        .setSocketTimeout(30000) //макс время ожидания получения данных
                        .setRedirectsEnabled(false) //возможность следовать редиректу
                        .build())
                .build();

        HttpGet request = new HttpGet(URL);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

        CloseableHttpResponse response = client.execute(request);

        InputStream content = response.getEntity().getContent();
        Post post = mapper.readValue(content, new TypeReference<>() {
        });
        //считать массив байт из url
        byte[] urlResult = post.getUrl().getBytes();

        //имя файла
        String[] fileName = post.getUrl().split("/");

        //сохранить массив как файл
        try {
            FileOutputStream fos = new FileOutputStream(fileName[fileName.length - 1]);
            fos.write(urlResult);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
