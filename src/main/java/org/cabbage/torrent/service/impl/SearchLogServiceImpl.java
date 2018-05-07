package org.cabbage.torrent.service.impl;

import org.cabbage.torrent.dao.SearchLogMapper;
import org.cabbage.torrent.entity.SearchLog;
import org.cabbage.torrent.service.SearchLogService;
import org.cabbage.torrent.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchLogServiceImpl implements SearchLogService {

    final Logger logger = LoggerFactory.getLogger(SearchLogServiceImpl.class);
    @Autowired
    private SearchLogMapper searchLogMapper;

    @Override
    public void insert(String keywork) {
        try {
            SearchLog searchLog = new SearchLog();
            searchLog.setKeyword(keywork);
            searchLog.setIp(HttpUtils.getIpAddr());
            searchLogMapper.insertSelective(searchLog);
        } catch (Exception ex) {
            logger.error("插入搜索日志异常", ex);
        }
    }
}
