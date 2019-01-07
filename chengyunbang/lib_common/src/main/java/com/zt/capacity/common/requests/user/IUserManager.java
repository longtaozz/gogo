package com.zt.capacity.common.requests.user;


import com.zt.capacity.common.data.listener.OnNetResultListener;

/**
 * Created by Administrator on 2018/4/16.
 */

public interface IUserManager {

      //登录
      void login(String user, String password, OnNetResultListener OnNetResultListener);
}
