package com.supermap.datashare.service.impl;

import com.supermap.datashare.dao.CommonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("test")
public class TestServiceImpl {

    @Autowired
    private CommonDao dao;

    public void test() {
        String sql = "selecT * FROM BDCK.BDCS_XMXX WHERE ROWNUM<2";
        List<Map> list = dao.getDataListByFullSql(sql);
        if (list.size() > 0) {
            System.out.println("good");
        }


//        dao.get
    }
}
