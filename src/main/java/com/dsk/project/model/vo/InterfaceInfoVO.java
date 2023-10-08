package com.dsk.project.model.vo;

import com.dsk.kongapicommon.model.entity.InterfaceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 接口信息视图
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoVO extends InterfaceInfo implements Serializable{

    /**
     * 调用次数
     */
    private Integer totalNum;


    private static final long serialVersionUID = 1L;
}