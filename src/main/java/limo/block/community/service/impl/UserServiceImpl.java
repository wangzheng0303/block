package limo.block.community.service.impl;

import limo.block.community.dao.UserDao;
import limo.block.community.entity.User;
import limo.block.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户service实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User findByUserName(String userName) {
        return userDao.findByUserName(userName);
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public User getUserById(Integer id) {
        return userDao.getOne(id);
    }
}
