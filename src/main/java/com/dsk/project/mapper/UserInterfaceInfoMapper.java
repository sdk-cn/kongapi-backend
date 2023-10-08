package com.dsk.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dsk.kongapicommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author Administrator
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Mapper
* @createDate 2023-09-30 12:41:26
* @Entity generator.domain.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);

}




