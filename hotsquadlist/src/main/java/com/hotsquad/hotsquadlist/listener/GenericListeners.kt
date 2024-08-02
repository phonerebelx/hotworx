package com.hotsquad.hotsquadlist.listener

interface GenericListeners {

    /**
     * when user tap on sign up button
     */
    fun onTapNewAccount() {}

    /*
    * When login button click
    * */
    fun onTapLogin() {}

    /**
     * When user click on Back Button
     */
    fun onTapBack() {}

    /*
    * When user click on register
    * */
    fun onTapRegister() {}

    /*
    * When forget password button click
    * */
    fun onTapForgot() {}

    /**
     * When User click profile icon
     */
    fun onTapProfile() {}

    /**
     * When User click on Logout
     */
    fun onTapLogout() {}
}