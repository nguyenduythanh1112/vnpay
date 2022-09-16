package vnpay2.vnpay2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vnpay2.vnpay2.entity.PaymentEntity;
import vnpay2.vnpay2.entity.TransactionEntity;
import vnpay2.vnpay2.repository.PaymentRepository;
import vnpay2.vnpay2.repository.TransactionRepository;

import java.util.List;

@RestController
@RequestMapping("/IPN")
public class IPNController {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private PaymentRepository paymentRepository;


    @GetMapping
    public ResponseEntity<TransactionEntity> showRespond(
            @RequestParam String vnp_Amount,
            @RequestParam String vnp_BankCode,
            @RequestParam String vnp_BankTranNo,
            @RequestParam String vnp_CardType,
            @RequestParam String vnp_OrderInfo,
            @RequestParam String vnp_PayDate,
            @RequestParam String vnp_ResponseCode,
            @RequestParam String vnp_TmnCode,
            @RequestParam String vnp_TransactionNo,
            @RequestParam String vnp_TransactionStatus,
            @RequestParam String vnp_TxnRef,
            @RequestParam String vnp_SecureHash){

        TransactionEntity transactionEntity=new TransactionEntity();
        transactionEntity.setTransactionNo(vnp_TransactionNo);
        transactionEntity.setTxnRef(vnp_TxnRef);
        transactionEntity.setAmount(vnp_Amount);
        transactionEntity.setTransactionStatus(vnp_TransactionStatus);
        transactionEntity.setBankTranNo(vnp_BankTranNo);
        transactionRepository.save(transactionEntity);

        if(vnp_TransactionStatus.equals("00")){
            PaymentEntity paymentEntity =  paymentRepository.getByTxnRef(vnp_TxnRef);
            paymentEntity.setStatus("YES");
            paymentRepository.save(paymentEntity);
        }

        System.out.println("THANH TOAN THANH CONG");

        return ResponseEntity.ok(transactionEntity);
    }
}
