package org.cabbage.torrent.dao;

import org.cabbage.torrent.entity.SearchLog;

public interface SearchLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SearchLog record);

    int insertSelective(SearchLog record);

    SearchLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SearchLog record);

    int updateByPrimaryKey(SearchLog record);
}