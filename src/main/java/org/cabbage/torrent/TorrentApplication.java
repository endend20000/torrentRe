package org.cabbage.torrent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("org.cabbage.torrent.dao")
@SpringBootApplication
public class TorrentApplication {

    public static void main(String[] args) {
        SpringApplication.run(TorrentApplication.class, args);
    }
}
