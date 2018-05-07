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

@Service(value = "zhongziso")
public class ZhongzisoSearchServiceImpl implements SearchService {

    final Logger logger = LoggerFactory.getLogger(ZhongzisoSearchServiceImpl.class);
    private final String baseUrl = "https://zhongziso.com/";
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
                String url = baseUrl + "/list/" + searchDTO.getKeyword() + "/" + i;
                results.add(exec.submit(new SearchTask(url)));

            }
            for (Future<Document> future : results) {
                Document doc = future.get();
                Elements elements = doc.select("tbody");
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
        dataDto.setType(1);

        Element a = element.select("a").first();
        dataDto.setName(a.text());

        Elements strongs = element.select("strong");
        String time = strongs.get(0).text();
        String size = strongs.get(1).text();
        String hot = strongs.get(2).text();

        dataDto.setTime(time);
        dataDto.setSize(size);
        dataDto.setHot(hot);

        String magnetUrl = element.select(".ls-magnet").select("a").first().attr("href");
        dataDto.setUrl(magnetUrl);
        return dataDto;
    }

    @Override
    public BaseResult torrent(String url, String type) throws Exception {

        Document doc = Jsoup.connect(url).header("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2").timeout(1000 * 60).get();
        Elements elements = doc.select(".btn-success");
        Element a = elements.get(Integer.parseInt(type));
        String href = a.attr("href");
        return new BaseResult(1, "成功", href);
    }
}
