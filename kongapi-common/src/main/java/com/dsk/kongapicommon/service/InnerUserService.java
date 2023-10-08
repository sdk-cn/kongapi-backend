package com.dsk.kongapicommon.service;


import com.dsk.kongapicommon.model.entity.User;

/**
 * 用户服务
 */
public interface InnerUserService {

    /**
     * 数据库中是否已分配给用户密匙（accessKey）
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);

}
