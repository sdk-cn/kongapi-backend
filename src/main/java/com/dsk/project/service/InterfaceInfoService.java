package com.dsk.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dsk.project.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.dsk.project.model.entity.InterfaceInfo;

/**
* @author Administrator
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-09-19 14:58:48
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
