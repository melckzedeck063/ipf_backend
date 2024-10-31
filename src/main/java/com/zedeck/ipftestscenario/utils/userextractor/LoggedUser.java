package com.zedeck.ipftestscenario.utils.userextractor;


import com.zedeck.ipftestscenario.models.UserAccount;

public interface LoggedUser {

    UserInfo getInfo();

    UserAccount getUser();
}
