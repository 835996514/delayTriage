package com.rc.temp.service.impl;

import com.rc.temp.db.PatientRegister;
import com.rc.temp.db.entity.ResponseResult;
import com.rc.temp.repository.AreaRepository;
import com.rc.temp.repository.PatientRegisterRepository;
import com.rc.temp.repository.PlanRepository;
import com.rc.temp.service.RegisterService;
import com.rc.temp.setting.RedisCache;
import com.rc.temp.setting.ResultState;
import io.netty.util.internal.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Log4j2
@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    PatientRegisterRepository registerRepository;

    @Autowired
    PlanRepository planRepository;

    @Autowired
    AreaRepository areaRepository;

    @Autowired
    RedisCache redisCache;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public ResponseResult tempRegister(PatientRegister patientRegister,Integer areaId) {
        if(areaId != null){
            String registerId = UUID.randomUUID().toString().replaceAll("-", "");
            patientRegister.setRegisterId(registerId);
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(sdf.format(new Date()));
                patientRegister.setRegisterTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
                log.error("日期转换异常",e.getMessage());
                return new ResponseResult(ResultState.FAIL,"日期转换异常",e.getMessage());
            }
            try {
                //解决事务使用的异常：ERR EXEC without MULTI（没有开启事务支持，导致每一个redisConnection都是重新获取的，造成exec和mutil不在同一个连接里）
                redisTemplate.execute(new SessionCallback() {
                    @Override
                    public Object execute(RedisOperations redisOperations) throws DataAccessException {
                        redisTemplate.multi();
                        registerRepository.save(patientRegister);
                        redisCache.setCacheList(areaId+":tempRegisters",patientRegister);
                        log.info("患者信息登记成功："+patientRegister.getName());
                        return redisOperations.exec();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
                log.error("登记患者临时挂号信息失败",e.getMessage());
                throw new RuntimeException("登记患者临时挂号信息失败");
            }

        }

        return new ResponseResult(ResultState.SUCCESS,"登记患者临时挂号信息成功");
    }

    @Override
    public ResponseResult tempRedisCache(PatientRegister patientRegister,Integer areaId) {
        List<PatientRegister> list = null;
        List<PatientRegister> cacheList = redisCache.getCacheList(areaId + ":tempRegisters");
        list = selectRegister(patientRegister,cacheList);
        return new ResponseResult(ResultState.SUCCESS,"查询缓存患者信息成功",list);
    }

    @Transactional
    @Override
    public ResponseResult updateTempRegister(PatientRegister patientRegister, Integer areaId) {
        String key = areaId + ":tempRegisters";
        List<PatientRegister> cacheList = redisCache.getCacheList(key);
        for (int i=0; i<cacheList.size(); i++){
            PatientRegister register = cacheList.get(i);
            if(patientRegister.getRegisterId().equals(register.getRegisterId())){
                redisTemplate.multi();
                try {
                    registerRepository.save(patientRegister);
                    redisCache.setItem(key,i,patientRegister);
                    redisTemplate.exec();
                    return new ResponseResult(ResultState.SUCCESS,"编辑成功",null);
                }catch (Exception e){
                    redisTemplate.discard();
                    e.printStackTrace();
                    log.error("编辑错误",e.getMessage());
                    return new ResponseResult(ResultState.FAIL,"编辑失败",e.getMessage());
                }
            }
        }
        return new ResponseResult(ResultState.WARNING,"编辑异常",null);
    }

    @Transactional
    @Override
    public ResponseResult deleteTempCache(String registerId, Integer areaId) {
        Integer existQueue = registerRepository.existQueue(registerId);
        if (existQueue != null){
            return new ResponseResult(ResultState.WARNING,"该临时挂号信息已用于签到，不可删除！！！");
        }
        redisTemplate.multi();
        String key = areaId + ":tempRegisters";
        try {
            List<PatientRegister> cacheList = redisCache.getCacheList(key);
            cacheList.stream().forEach(item->{
                if(registerId.equals(item.getRegisterId())){
                    redisCache.remList(key,1, item);
                }
            });
            registerRepository.deleteById(registerId);
            redisTemplate.exec();
            log.info("删除临时挂号信息成功！！！");
        }catch (Exception e){
            e.printStackTrace();
            log.error("删除临时挂号信息失败！！！");
            redisTemplate.discard();
            return new ResponseResult(ResultState.FAIL,"删除失败",e.getMessage());
        }
        return new ResponseResult(ResultState.SUCCESS,"删除成功",null);
    }

    @Transactional
    @Override
    public ResponseResult deleteAll(String[] registerIds, Integer areaId) {
        String key = areaId+":tempRegisters";
        List<PatientRegister> registerList = redisCache.getCacheList(key);
        List<String> list = Arrays.asList(registerIds);
        redisTemplate.multi();
        try {
            registerList.stream().forEach(item -> {
                if(list.contains(item.getRegisterId())){
                    redisCache.remList(key,1,item);
                }
            });
            registerRepository.deleteAllById(Arrays.asList(registerIds));
            redisTemplate.exec();
            return new ResponseResult(ResultState.SUCCESS,"删除所有已选患者信息成功",null);
        }catch (Exception e){
            log.info("删除操作异常",e.getMessage());
            redisTemplate.discard();
            e.printStackTrace();
            return new ResponseResult(ResultState.FAIL,"删除所有已选患者信息失败",e.getMessage());
        }
    }

    private List<PatientRegister> selectRegister(PatientRegister register, List<PatientRegister> cacheList){
        String name = register.getName();
        String cardNo = register.getCardNo();
        String idNo = register.getIdNo();
        String shift = register.getShift();
        String planId = register.getPlanId();
        List<PatientRegister> list = new ArrayList<>();
        cacheList.stream().forEach(item -> {

            Integer existQueue = registerRepository.existQueue(item.getRegisterId());
            if (existQueue != null){
                item.setSource("已使用");
            }
            /*
            true：
                1.参数为空都符合
                2.参数不为空比对符合
            false：
                1.参数不为空，但是对象属性本身为空
                2.参数不为空，比对不符合
            * */
            boolean flag = true;
            if(!StringUtil.isNullOrEmpty(planId)){
                flag = item.getPlanId().equals(planId);
            }
            if(!StringUtil.isNullOrEmpty(name)){
                if(item.getName() == null){
                    flag = false;
                }else {
                    flag = item.getName().contains(name);
                }
            }
            if(!StringUtil.isNullOrEmpty(cardNo)){
                if(item.getCardNo() == null){
                    flag = false;
                }else {
                    flag = item.getCardNo().contains(cardNo);
                }
            }
            if(!StringUtil.isNullOrEmpty(idNo)){
                if(item.getIdNo() == null){
                    flag = false;
                }else {
                    flag = item.getIdNo().contains(idNo);
                }
            }
            if(!StringUtil.isNullOrEmpty(shift)){
                if(item.getShift() == null){
                    flag = false;
                }else {
                    flag = item.getShift().equals(shift);
                }
            }
            if(flag) {
                list.add(item);
            }
        });
        return list;
    }
}
