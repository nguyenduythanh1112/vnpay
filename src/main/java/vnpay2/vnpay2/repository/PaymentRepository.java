package vnpay2.vnpay2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnpay2.vnpay2.entity.PaymentEntity;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity,Integer> {

    public PaymentEntity getByTxnRef(String txnRef);

}
