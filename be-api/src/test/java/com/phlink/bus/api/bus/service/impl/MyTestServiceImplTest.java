package com.phlink.bus.api.bus.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.phlink.bus.api.ApiApplication;
import com.phlink.bus.api.bus.domain.MyTest;
import com.phlink.bus.api.bus.service.IMyTestService;
import com.phlink.bus.api.serviceorg.domain.Student;
import com.wuwenze.poi.ExcelKit;
import com.wuwenze.poi.handler.ExcelReadHandler;
import com.wuwenze.poi.pojo.ExcelErrorField;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles("test")
public class MyTestServiceImplTest {

    @Autowired
    private IMyTestService myTestService;

    @Test
    public void testInsert() {
        MyTest myTest = new MyTest();
        myTest.setArrayTest(new Integer[]{1,2,3,4,5});
        JSONObject jo = new JSONObject();
        jo.put("a", "A");
        jo.put("c", "C");
        jo.put("d", "D");
        myTest.setJsonTest(jo);
        myTest.setCreateTime(LocalDateTime.now());
        myTestService.save(myTest);
        Assert.assertNotNull(myTest.getId());
    }

    @Test
    public void readExcel() {
        File file = new File("/Users/wen/Dropbox/普惠互联/车联网/数据/学生信息表-导入模板-已填.xlsx");
        ExcelKit.$Import(Student.class).readXlsx(file, new ExcelReadHandler<Student>() {
            @Override
            public void onSuccess(int i, int i1, Student o) {
                System.out.println(o.toString());
            }

            @Override
            public void onError(int i, int i1, List<ExcelErrorField> list) {

            }
        });
    }

    @Test
    public void testBeanUtilSetProperties() throws InvocationTargetException, IllegalAccessException {
        Student student = new Student();
        BeanUtils.setProperty(student, "name", "啊哈哈");
        System.out.println(student.toString());
    }
}