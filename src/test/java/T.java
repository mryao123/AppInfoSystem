import com.jbit.service.DevUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:applicaiton.xml")
public class T {
    @Resource
    private DevUserService devUserService;

//    @Test
//    public void test_login(){
//
//
//        System.out.println( devUserService.queryLogin("test001", "123456"));
//    }
}
