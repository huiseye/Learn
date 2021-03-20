package com.supermap.datashare.web;

import com.supermap.datashare.dao.CommonDao;
import com.supermap.datashare.service.impl.TestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/test")
@Transactional
public class TestController {
    @Autowired
    private CommonDao dao;
    @Autowired
    private TestServiceImpl testService;

    @RequestMapping(value = "/one",method = RequestMethod.GET)
    public void test() {

        testService.test();


//        dao.get
    }

    @RequestMapping(value = "/two",method = RequestMethod.GET)
    public void test2() {

        testService.test();


//        dao.get
    }

    @RequestMapping(value = "/three",method = RequestMethod.GET)
    public void test3() {

        testService.test();


//        dao.get
    }
    @RequestMapping(value = "/four",method = RequestMethod.GET)
    public void test4() {

        testService.test();


//        dao.get
    }
}
