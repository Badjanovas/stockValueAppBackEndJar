package jar.StockValueApp.repository;


import jar.StockValueApp.model.DividendDiscountModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DividendDiscountRepository extends JpaRepository<DividendDiscountModel, Long> {

    List<DividendDiscountModel> findByUserId(Long id);

}
