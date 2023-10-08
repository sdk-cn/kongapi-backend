package com.dsk.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dsk.kongapicommon.model.entity.User;
import com.dsk.kongapicommon.service.InnerUserService;
import com.dsk.project.common.ErrorCode;
import com.dsk.project.exception.BusinessException;
import com.dsk.project.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getInvokeUser(String accessKey) {
        //参数校验
        if(StringUtils.isAnyBlank(accessKey)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //创建查询条件包装器
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("accessKey",accessKey);
        //使用userMapper的selectOne方法
        return userMapper.selectOne(queryWrapper);
    }
}
