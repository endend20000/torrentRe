package org.cabbage.torrent.service.impl;

import org.cabbage.torrent.dto.DataDTO;
import org.cabbage.torrent.dto.SearchDTO;
import org.cabbage.torrent.entity.BaseResult;
import org.cabbage.torrent.service.SearchLogService;
import org.cabbage.torrent.service.SearchService;
import org.cabbage.torrent.task.SearchTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service(value = "sobt")
public class SobtSearchServiceImpl implements SearchService {

    final Logger logger = LoggerFactory.getLogger(SobtSearchServiceImpl.class);
    private final String baseUrl = "http://www.sobt8.com/";
    @Autowired
    private SearchLogService searchLogService;

    @Override
    public List<DataDTO> data(SearchDTO searchDTO) throws Exception {
        if (searchDTO.getKeyword() == null) {
            return new ArrayList<DataDTO>();
        }

        searchLogService.insert(searchDTO.getKeyword());
        List<DataDTO> data = new ArrayList<DataDTO>();

        int rate = searchDTO.getSize() / 10;
        int begin = rate * (searchDTO.getIndex() - 1) + 1;
        int end = rate * searchDTO.getIndex();

        ExecutorService exec = Executors.newCachedThreadPool();
        ArrayList<Future<Document>> results = new ArrayList<Future<Document>>();
        try {

            for (int i = begin; i <= end; i++) {
                String url = baseUrl + "q/" + searchDTO.getKeyword() + ".html?sort=rel&page=" + i;
                results.add(exec.submit(new SearchTask(url)));

            }
            for (Future<Document> future : results) {
                Document doc = future.get();
                Elements elements = doc.select(".search-item");
                for (Element element : elements) {
                    data.add(element2data(element));
                }
            }

        } catch (Exception ex) {
            return data;
        } finally {
            exec.shutdown();
        }
        return data;
    }

    private DataDTO element2data(Element element) {
        DataDTO dataDto = new DataDTO();
        dataDto.setType(2);

        String name = element.select(".item-title").select("a").first().text();
        dataDto.setName(name);

        String time = element.select(".item-bar").select("span").first().text().split("：")[1];
        dataDto.setTime(time);

        String size = element.select(".item-bar").select("span").get(1).text().split("：")[1];
        dataDto.setSize(size);

        String hot = element.select(".item-bar").select("span").get(2).text().split("：")[1];
        dataDto.setHot(hot);

        String magnetUrl = baseUrl + element.select(".item-title").select("a").first().attr("href");
        dataDto.setUrl(magnetUrl);
        return dataDto;
    }

    @Override
    public BaseResult torrent(String url, String type) throws Exception {
        Document doc = Jsoup.connect(url).header("User-Agent",
                "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
                .timeout(1000 * 60).get();
        String href = doc.select(".download").first().attr("href");
        return new BaseResult(1, "成功", href);
    }
}
