package org.cabbage.torrent.task;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.concurrent.Callable;

public class SearchTask implements Callable<Document> {
    private String url;

    public SearchTask(String url) {
        this.url = url;
    }

    public Document call() {
        try {
            return Jsoup.connect(url).header("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2").timeout(1000 * 15).get();
        } catch (Exception ex) {
            return null;
        }
    }
}
