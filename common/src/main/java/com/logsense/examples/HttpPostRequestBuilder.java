package com.logsense.examples;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpHeaders.USER_AGENT;

public class HttpPostRequestBuilder {
    final private static Logger logger = LoggerFactory.getLogger(HttpPostRequestBuilder.class);

    private Service service;
    private String id;
    private String payload;

    public HttpPostRequestBuilder withService(Service service) {
        this.service = service;
        return this;
    }

    public HttpPostRequestBuilder withPayload(String payload) {
        this.payload = payload;
        return this;
    }

    public HttpPostRequestBuilder withId(String id) {
        this.id = id;
        return this;
    }

    private String url() {
        return "http://localhost:" + service.getPort() + service.getPath() + "/some-resource";
    }

    public String build() {
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(url());

            // add header
            post.setHeader("User-Agent", USER_AGENT);

            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("payload", payload));
            urlParameters.add(new BasicNameValuePair("id", id));

            post.setEntity(new UrlEncodedFormEntity(urlParameters));

            HttpResponse response = client.execute(post);

            logger.debug("Request with id {} to: {} code: {}", id, url(), response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            return result.toString();
        } catch (Exception ex) {
            logger.error("Request to " + url() + " failed", ex);
            return null;
        }
    }

}
