package com.dsk.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dsk.kongapicommon.model.entity.UserInterfaceInfo;

/**
* @author Administrator
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service
* @createDate 2023-09-30 12:41:26
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    boolean invokeCount(long interfaceInfoId,long userId);

}
