package com.dsk.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsk.kongapiclientsdk.client.KongApiClient;
import com.dsk.project.annotation.AuthCheck;
import com.dsk.project.common.*;
import com.dsk.project.constant.CommonConstant;
import com.dsk.project.constant.UserConstant;
import com.dsk.project.exception.BusinessException;
import com.dsk.project.exception.ThrowUtils;
import com.dsk.project.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.dsk.project.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import com.dsk.project.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.dsk.project.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.dsk.kongapicommon.model.entity.InterfaceInfo;
import com.dsk.kongapicommon.model.entity.User;
import com.dsk.project.model.enums.StatusEnum;
import com.dsk.project.service.InterfaceInfoService;
import com.dsk.project.service.UserService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 接口信息 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    @Resource
    private KongApiClient kongApiClient;

    // region 增删改查

    /**
     * 创建
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param InterfaceInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest InterfaceInfoUpdateRequest) {
        if (InterfaceInfoUpdateRequest == null || InterfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo InterfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(InterfaceInfoUpdateRequest, InterfaceInfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(InterfaceInfo, false);
        long id = InterfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = interfaceInfoService.updateById(InterfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 管理员上线接口
     * @param idRequest id请求对象
     * @return Boolean
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //先查询表里有没有这个接口
        //接口id
        Long id = idRequest.getId();
        //利用service查询接口信息
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        //如果接口信息为空，抛出不存在异常
        if(interfaceInfo==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        //接着判断接口是否可用，这里需要使用之前开发的sdk客户端
        com.dsk.kongapiclientsdk.model.User user = new com.dsk.kongapiclientsdk.model.User();
        user.setUserName("kongkong");
        String userName = kongApiClient.getUserNameByPost(user);
        if(StringUtils.isBlank(userName)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"接口不可调用");
        }
        InterfaceInfo newInterfaceInfo = new InterfaceInfo();
        newInterfaceInfo.setId(id);
        newInterfaceInfo.setStatus(StatusEnum.OPEN.getStatus());
        boolean result = interfaceInfoService.updateById(newInterfaceInfo);
        return ResultUtils.success(result);
    }

    @PostMapping("/offline")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //先查询表里有没有这个接口
        //接口id
        Long id = idRequest.getId();
        //利用service查询接口信息
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        //如果接口信息为空，抛出不存在异常
        if(interfaceInfo==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        //接着判断接口是否可用，这里需要使用之前开发的sdk客户端
        com.dsk.kongapiclientsdk.model.User user = new com.dsk.kongapiclientsdk.model.User();
        user.setUserName("kongkong");
        String userName = kongApiClient.getUserNameByPost(user);
        if(StringUtils.isBlank(userName)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"接口不可调用");
        }
        InterfaceInfo newInterfaceInfo = new InterfaceInfo();
        newInterfaceInfo.setId(id);
        newInterfaceInfo.setStatus(StatusEnum.CLOSED.getStatus());
        boolean result = interfaceInfoService.updateById(newInterfaceInfo);
        return ResultUtils.success(result);
    }

    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest,HttpServletRequest request) {
        if (interfaceInfoInvokeRequest == null || interfaceInfoInvokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //接口id
        Long id = interfaceInfoInvokeRequest.getId();
        //接口参数
        String requestParams = interfaceInfoInvokeRequest.getRequestParams();
        //利用service查询接口信息
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        //如果接口信息为空，抛出不存在异常
        if(interfaceInfo==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        //如果接口关闭，抛出系统异常
        if(StatusEnum.CLOSED.getStatus()==interfaceInfo.getStatus()){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"接口已关闭");
        }
        //直接从session中获取用户的ak、sk
        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        //创建一个Gson对象，将json字符串转换为object对象
        Gson gson = new Gson();
        com.dsk.kongapiclientsdk.model.User user = gson.fromJson(requestParams, com.dsk.kongapiclientsdk.model.User.class);
        //创建一个临时的KongApiClient对象，传入用户的ak和sk
        KongApiClient  tempClient= new KongApiClient(accessKey, secretKey);
        String userName = tempClient.getUserNameByPost(user);
        return ResultUtils.success(userName);
    }

    @PostMapping("/getById")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(@RequestBody IdRequest idRequest){
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //接口id
        Long id = idRequest.getId();
        //利用service查询接口信息
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if(interfaceInfo==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(interfaceInfo);
    }

    /**
     * 分页获取列表
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        String description = interfaceInfoQuery.getDescription();
        // description 需支持模糊搜索
        interfaceInfoQuery.setDescription(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(interfaceInfoPage);
    }

    // endregion

}
