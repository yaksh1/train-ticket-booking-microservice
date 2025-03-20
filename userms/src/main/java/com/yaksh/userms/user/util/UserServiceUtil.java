package com.yaksh.userms.user.util;


public interface UserServiceUtil {

    String hashPassword(String password);
    boolean checkPassword(String password, String hashedPassword);

}
