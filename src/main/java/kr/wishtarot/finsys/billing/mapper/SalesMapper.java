package kr.wishtarot.finsys.billing.mapper;

import kr.wishtarot.finsys.billing.model.Sale;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SalesMapper {
    void insertSale(Sale sale);
    Sale selectSaleById(Long saleId);
    // 필요한 추가 메서드들 (예: 사용자별 매출 조회 등)
}
