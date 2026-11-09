package com.campus.utilitybilling.service.impl;

import com.campus.utilitybilling.dto.request.PriceUpdateRequestDto;
import com.campus.utilitybilling.entity.SysConfig;
import com.campus.utilitybilling.mapper.SysConfigMapper;
import com.campus.utilitybilling.service.ConfigService;
import com.campus.utilitybilling.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfigServiceImpl implements ConfigService {

    private final SysConfigMapper sysConfigMapper;
    private final UserService userService;

    public ConfigServiceImpl(SysConfigMapper sysConfigMapper,
                             UserService userService) {
        this.sysConfigMapper = sysConfigMapper;
        this.userService = userService;
    }

    @Override
    public SysConfig getCurrent() {
        SysConfig config = sysConfigMapper.selectCurrent();
        if (config == null) {
            throw new RuntimeException("系统配置不存在");
        }
        return config;
    }

    @Override
    @Transactional
    public SysConfig updatePrices(PriceUpdateRequestDto request) {
        // 校验操作人是否为管理员
        userService.verifyAdmin(request.getOperatorNo());

        SysConfig config = sysConfigMapper.selectCurrent();
        if (config == null) {
            throw new RuntimeException("系统配置不存在");
        }
        if (request.getWaterPrice() != null) {
            config.setWaterPrice(request.getWaterPrice());
        }
        if (request.getElectricPrice() != null) {
            config.setElectricPrice(request.getElectricPrice());
        }
        sysConfigMapper.update(config);
        return config;
    }
}
