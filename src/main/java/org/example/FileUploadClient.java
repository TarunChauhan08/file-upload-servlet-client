package org.example;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class FileUploadClient {
    public static void main(String[] args) throws Exception {
        String url = "http://localhost:8080/ServletCreation/create-file";
        String username = "admin";
        String password = "secret";
        File file = new File("C:/test/sample.txt");
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder()
                .encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        post.setHeader("Authorization", "Basic " + encodedAuth);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("file", file);
        post.setEntity(builder.build());
        CloseableHttpResponse response = client.execute(post);
        System.out.println("Status: " + response.getStatusLine());
        System.out.println(EntityUtils.toString(response.getEntity()));
        client.close();
    }
}
