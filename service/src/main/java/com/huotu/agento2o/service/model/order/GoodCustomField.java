/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.agento2o.service.model.order;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * 商品自定义字段
 * Created by allan on 3/18/16.
 */
@Data
public class GoodCustomField {
    @JSONField(name = "goodsId")
    private int goodId;
    private int productId;
    @JSONField(name = "data")
    private List<FiledAndValue> filedAndValues;
}
