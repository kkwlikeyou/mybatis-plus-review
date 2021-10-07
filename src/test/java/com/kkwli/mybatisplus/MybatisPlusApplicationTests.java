package com.kkwli.mybatisplus;

import com.kkwli.mybatisplus.entity.User;
import com.kkwli.mybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class MybatisPlusApplicationTests {

    @Resource
    private UserMapper userMapper;

    @Test
    void testSelectList() {
        System.out.println(("----- selectAll method test ------"));
        //UserMapper 中的 selectList() 方法的参数为 MP 内置的条件封装器 Wrapper
        //所以不填写就是无任何条件
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }
    @Test
    void testInsert() {
        User user = new User();
        user.setAge(18);
        user.setName("阿昌");
        user.setEmail("995931576@qq.com");
        int result = userMapper.insert(user);

        System.out.println(result); //影响的行数
        System.out.println(user); //id自动回填
    }

    @Test
    void testUpdate() {
        User user = new User();
        user.setId(1L);
        user.setAge(28);
        int result = userMapper.updateById(user);
        System.out.println(result);
    }

}
