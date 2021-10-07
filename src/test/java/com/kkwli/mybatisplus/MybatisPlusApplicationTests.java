package com.kkwli.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kkwli.mybatisplus.entity.User;
import com.kkwli.mybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.*;

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
        user.setName("阿猫");
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

    @Test
    void testOptimisticLocker() {
        //查询
        User user = userMapper.selectById(7L);
        //修改数据
        user.setName("Helen Yao");
        user.setEmail("helen@qq.com");
        user.setVersion(user.getVersion() - 1);
        //执行更新
        userMapper.updateById(user);
    }

    @Test
    void testSelectById() {
        User user = userMapper.selectById(1L);
        System.out.println(user);
    }

    @Test
    void testSelectByIds() {
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
        users.forEach(System.out::println);
    }

    @Test
    void testSelectByMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "阿猫");
        map.put("age", 18);
        List<User> users = userMapper.selectByMap(map);
        users.forEach(System.out::println);
    }

    @Test
    void testPage() {
        //1、创建page对象
        //传入参数：当前页 和 每页显示记录数
        Page<User> userPage = new Page<>(1, 3);
        //调用mp分页查询方法
        //调用mp分页查询过程中，底层会封装，把所有分页数据分装到page对象中
        userMapper.selectPage(userPage, null);
        //通过page对象获取数据
        userPage.getRecords().forEach(System.out::println);//遍历查询的分页数据
        System.out.println(userPage.getCurrent());//获取当前页
        System.out.println(userPage.getSize());//每页显示记录数
        System.out.println(userPage.getTotal());//总记录数
        System.out.println(userPage.getPages());//总页数

        System.out.println(userPage.hasNext());//判断是否有下一页
        System.out.println(userPage.hasPrevious());//判断是否有上一页
    }

    @Test
    void testDeleteById() {
        int result = userMapper.deleteById(7L);
        System.out.println(result);
    }

    @Test
    void testDeleteByIds() {
        userMapper.deleteBatchIds(Arrays.asList(4,5));
    }

    @Test
    void testDeleteByMap() {
        Map<String,Object> map = new HashMap<>();
        map.put("id",1);
        int result = userMapper.deleteByMap(map);
        System.out.println(result);
    }

    @Test
    void testDelete() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("name").ge("age",12).isNotNull("email");
        int result = userMapper.delete(queryWrapper);
        System.out.println(result);
    }

    @Test
    void testSelectOne() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name","Tom");
        User user = userMapper.selectOne(queryWrapper);
        System.out.println(user);
    }

    @Test
    void testSelectCount() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("age",20,30);
        Integer integer = userMapper.selectCount(queryWrapper);
        System.out.println(integer);
    }

    @Test
    void testSelectList2() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        Map<String,Object> map = new HashMap<>();
        map.put("id",2);
        queryWrapper.allEq(map);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    void testSelectMaps() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.notLike("name","e").likeRight("email","t");
        List<Map<String, Object>> maps = userMapper.selectMaps(queryWrapper);
        maps.forEach(System.out::println);

    }

    @Test
    void testSelectObjs() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.inSql("id" ,"SELECT id FROM user WHERE id < 3");
        List<Object> objects = userMapper.selectObjs(queryWrapper);
        objects.forEach(System.out::println);
    }

    @Test
    void testUpdate2() {
        //修改值
        User user = new User();
        user.setAge(99);
        user.setName("Andy");
        //修改条件
        UpdateWrapper<User> UpdateWrapper = new UpdateWrapper<>();
        UpdateWrapper.like("name","h").or().between("age",20,30);
        int update = userMapper.update(user, UpdateWrapper);
        System.out.println(update);
    }

    @Test
    public void testUpdate3() {
        //修改值
        User user = new User();
        user.setAge(99);
        user.setName("Andy");
        //修改条件
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .like("name", "h")
                .or(i -> i.eq("name", "李白").ne("age", 20));
        int result = userMapper.update(user, updateWrapper);
        System.out.println(result);
    }

    @Test
    public void testSelectListOrderBy() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void testUpdateSet() {
        //修改值
        User user = new User();
        user.setAge(99);
        //修改条件
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper
                .like("name", "h")
                .set("name", "老李头")//除了可以查询还可以使用set设置修改的字段
                .setSql(" email = '123@qq.com'");//可以有子查询
        int result = userMapper.update(user, userUpdateWrapper);
    }



}
