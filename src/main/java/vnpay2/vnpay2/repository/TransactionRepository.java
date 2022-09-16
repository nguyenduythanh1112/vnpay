package vnpay2.vnpay2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnpay2.vnpay2.entity.TransactionEntity;
@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity,Integer> {
}
