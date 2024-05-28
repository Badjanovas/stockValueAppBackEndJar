package jar.StockValueApp.repository;


import jar.StockValueApp.model.DcfModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcfModelRepository extends JpaRepository<DcfModel, Long> {

    List<DcfModel> findByUserId(Long id);
}
