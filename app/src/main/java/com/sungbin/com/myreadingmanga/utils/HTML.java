package com.sungbin.com.myreadingmanga.utils;

import android.os.StrictMode;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class HTML{

    public static String get(String adress){
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36";
            Connection conn = Jsoup.connect(adress).userAgent(USER_AGENT);
            Document doc = conn.get();
            return doc.toString();
        }
        catch(IOException e){
            return null;
        }
    }
}

