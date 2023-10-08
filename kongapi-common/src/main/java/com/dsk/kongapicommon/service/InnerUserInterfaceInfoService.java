package com.dsk.kongapicommon.service;


import com.dsk.kongapicommon.model.entity.InterfaceInfo;
import com.dsk.kongapicommon.model.entity.User;

/**
* @author Administrator
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service
* @createDate 2023-09-30 12:41:26
*/
public interface InnerUserInterfaceInfoService {


    /**
     * 接口调用统计
     * @param interfaceInfoId 接口id
     * @param userId 用户id
     * @return
     */
    boolean invokeCount(long interfaceInfoId,long userId);

}
