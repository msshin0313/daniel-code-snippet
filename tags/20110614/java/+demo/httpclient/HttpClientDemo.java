package httpclient;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class HttpClientDemo {
    public static void main(String[] args) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        // Prepare a request object
        HttpGet httpget = new HttpGet("http://www.tianya.cn/publicforum/content/news/1/91217.shtml");
        // Execute the request
        HttpResponse response = httpclient.execute(httpget);
        // Examine the response status
        System.out.println(response.getStatusLine());
        // Get hold of the response entity
        HttpEntity entity = response.getEntity();
        // If the response does not enclose an entity, there is no need to worry about connection release
        if (entity != null) {
            InputStream instream = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "GBK"));
            String line;
            while ((line=reader.readLine()) != null) {
                System.out.println(line);// Closing the input stream will trigger connection release
            }
            instream.close();
        }
    }
}
