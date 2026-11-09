package com.campus.utilitybilling.service;

import com.campus.utilitybilling.dto.request.PriceUpdateRequestDto;
import com.campus.utilitybilling.entity.SysConfig;

public interface ConfigService {

    /**
     * 获取当前水电单价
     */
    SysConfig getCurrent();

    /**
     * 更新水电单价（仅管理员可操作，根据 operatorNo 校验权限）
     */
    SysConfig updatePrices(PriceUpdateRequestDto request);
}
