package com.superapp.activity.base;

import com.superapp.beans.BeanAddMaterial;
import com.superapp.beans.BeanMaterial;
import com.superapp.beans.CoworkerBean;

/**
 * Created by Abhijeet-PC on 08-Feb-17.
 */

public interface DataSenderInterface {

    void sendData(BeanMaterial addMaterial);

    void sendCoWorkerData(CoworkerBean coworkerBean);
}
