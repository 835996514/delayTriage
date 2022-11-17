package com.rc.temp.service;

import com.rc.temp.repository.AreaRepository;
import com.rc.temp.setting.RedisCache;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
public class TimingOprationService {

    @Autowired
    RedisCache redisCache;

    @Autowired
    AreaRepository areaRepository;

    @Scheduled(cron = "0 0 1 * * ?")
    @Async
    public void clearRegisterCache(){
        List<Integer> ids = areaRepository.getIds();
        List<String> keys = new ArrayList<>();
        String key = ":tempRegisters";
        for (Integer id : ids){
            keys.add(id+key);
        }
        try {
            redisCache.deleteObject(keys);
            log.info("清除临时病人挂号信息成功");
        }catch (Exception e){
            e.printStackTrace();
            log.error("清除临时挂号信息失败");
        }
    }
}
