package com.campus.utilitybilling.mapper;

import com.campus.utilitybilling.entity.SysConfig;

public interface SysConfigMapper {

    /** 获取当前系统配置（price_id=1） */
    SysConfig selectCurrent();

    int update(SysConfig sysConfig);
}
