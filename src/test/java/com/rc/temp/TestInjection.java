package com.rc.temp;

import com.rc.temp.repository.AreaRepository;
//import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class TestInjection {

    @Resource
    AreaRepository areaRepository;

    /*@org.junit.Test
    public void test(){
        System.out.println("hh");
    }*/
}
