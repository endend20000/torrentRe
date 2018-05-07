package org.cabbage.torrent.service;

import org.cabbage.torrent.dto.DataDTO;
import org.cabbage.torrent.dto.SearchDTO;
import org.cabbage.torrent.entity.BaseResult;

import java.util.List;

public interface SearchService {
    List<DataDTO> data(SearchDTO searchDTO) throws Exception;

    BaseResult torrent(String url, String type) throws Exception;
}
