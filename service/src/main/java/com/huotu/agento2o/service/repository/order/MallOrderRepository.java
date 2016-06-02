package com.huotu.agento2o.service.repository.order;


import com.huotu.agento2o.service.common.OrderEnum;
import com.huotu.agento2o.service.entity.author.Shop;
import com.huotu.agento2o.service.entity.order.MallOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by allan on 12/31/15.
 */
@Repository
public interface MallOrderRepository extends JpaRepository<MallOrder, String>, JpaSpecificationExecutor<MallOrder> {
    @Query("update MallOrder set payStatus=?1 where orderId=?2")
    @Modifying
    void updatePayStatus(OrderEnum.PayStatus payStatus, String orderId);

    //    @Query("update MallOrder  set shipStatus=?1 where orderId=?2")
//    @Modifying
//    void updateShipStatus(OrderEnum.ShipStatus shipStatus, String orderId);
//
//    int countByAgentIdAndCreateTimeBetween(int customerId, Date start, Date end);
//
//    int countByAgentIdAndPayStatusAndShipStatusAndCreateTimeBetween(int customerId, OrderEnum.PayStatus payStatus, OrderEnum.ShipStatus shipStatus, Date start, Date end);
//
    MallOrder findByShopAndOrderId(Shop shop, String orderId);

    /**
     * 按日期统计由门店发货或受益方为门店的订单数
     *
     * @param shopId
     * @param beneficialShopId
     * @param start
     * @param end
     * @return
     */
    int countByShop_IdOrBeneficiaryShop_IdAndCreateTimeBetween(Integer shopId, Integer beneficialShopId, Date start, Date end);

    /**
     * 按日期统计由下级门店发货或受益方为下级门店的订单数
     *
     * @param agentId
     * @param beneficialShopAgentId
     * @param start
     * @param end
     * @return
     */
    int countByShop_ParentAuthor_IdAndBeneficiaryShop_ParentAuthor_IdAndCreateTimeBetween(Integer agentId, Integer beneficialShopAgentId, Date start, Date end);

    /**
     * 按日期统计门店待发货订单数
     * @param shopId
     * @param payStatus
     * @param shipStatus
     * @return
     */
    int countByShop_IdAndPayStatusAndShipStatus(Integer shopId, OrderEnum.PayStatus payStatus, OrderEnum.ShipStatus shipStatus);
}
