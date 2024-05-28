package jar.StockValueApp.repository;


import jar.StockValueApp.model.GrahamsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface GrahamsModelRepository extends JpaRepository<GrahamsModel, Long> {

    List<GrahamsModel> findByUserId(Long id);
}
