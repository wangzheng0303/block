package limo.block.community.service;

import limo.block.community.entity.User;

/**
 * 用户service接口
 */
public interface UserService {

    /**
     * 根据用户名查找用户实体
     */
    public User findByUserName(String userName);

    /**
     * 根据邮箱查找用户实体
     */
    public User findByEmail(String email);

    /**
     * 添加或修改用户信息
     */
    public void save(User user);

    /**
     * 根据Id获取用户信息
     */
    public User getUserById(Integer id);
}
