package com.phlink.bus.api.iot;


import com.phlink.bus.api.ApiApplication;
import com.phlink.bus.api.device.manager.IotManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles("test")
public class MyIotTest {

    @Autowired
    private IotManager iotManager;

    @Test
    public void testLogin() {
        iotManager.loginToIotServer();
    }

    @Test
    public void testCall() {
        iotManager.call("9612481149", "18600753024");
    }

    @Test
    public void testCr() {
        iotManager.cr("9612481149");
    }

}
