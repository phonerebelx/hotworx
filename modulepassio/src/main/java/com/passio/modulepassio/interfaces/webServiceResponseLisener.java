package com.passio.modulepassio.interfaces;

/**
 * Created on 7/17/2017.
 */

public interface webServiceResponseLisener {
    public void ResponseSuccess(String result, String Tag);
    public void ResponseFailure(String message, String tag);
    public void ResponseNoInternet(String tag);

}
