package com.dsk.project.model.dto.interfaceinfo;

import com.dsk.project.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoInvokeRequest extends PageRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;


    /**
     * 请求参数
     */
    private String requestParams;

    private static final long serialVersionUID = 1L;
}