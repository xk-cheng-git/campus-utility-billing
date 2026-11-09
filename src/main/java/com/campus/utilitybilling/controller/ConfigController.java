package com.campus.utilitybilling.controller;

import com.campus.utilitybilling.dto.common.ApiResponse;
import com.campus.utilitybilling.dto.request.PriceUpdateRequestDto;
import com.campus.utilitybilling.entity.SysConfig;
import com.campus.utilitybilling.service.ConfigService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @GetMapping("/prices")
    public ApiResponse<SysConfig> getPrices() {
        try {
            SysConfig config = configService.getCurrent();
            return ApiResponse.success(config);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @PutMapping("/prices")
    public ApiResponse<SysConfig> updatePrices(@RequestBody PriceUpdateRequestDto request) {
        try {
            SysConfig config = configService.updatePrices(request);
            return ApiResponse.success("单价更新成功", config);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }
}
