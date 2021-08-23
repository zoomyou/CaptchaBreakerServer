package com.example.captchabreaker.mapper;

import com.example.captchabreaker.bean.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    /**
     * 向user表中插入数据
     * @param user 新用户的的user对象数据
     * @return 插入是否成功
     */
    @Insert("insert into user values(#{user_id},#{user_name},#{password},#{mail},#{role},#{status},#{token},#{mark},#{phone},#{hasPhone})")
    @Options(useGeneratedKeys = true, keyProperty = "user_id")
    public int addUser(User user);

    /**
     * 根据用户的id选择用户
     * @param user_id
     * @return 返回一个用户对象
     */
    @Select("select * from user where user_id = #{user_id}")
    public User selectUser(@Param("user_id") int user_id);

    /**
     * 根据用户的用户名选择用户
     * @param user_name 被选择用户的用户名
     * @return 返回一个用户对象
     */
    @Select("select * from user where user_name = #{user_name}")
    public User selectUserByName(@Param("user_name") String user_name);

}
