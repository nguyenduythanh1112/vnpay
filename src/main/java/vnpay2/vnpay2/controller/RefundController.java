package vnpay2.vnpay2.controller;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import vnpay2.vnpay2.DTO.RefundRequestDTO;
import vnpay2.vnpay2.DTO.RefundRespondDTO;
import vnpay2.vnpay2.repository.TransactionRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

@RestController
@RequestMapping("/refund")
public class RefundController {

    @Autowired
    TransactionRepository transactionRepository;
    @PostMapping
    public String refund(@ModelAttribute RefundRequestDTO refundRequestDTO,HttpServletRequest request, HttpServletResponse resp) throws IOException {

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");


        if(transactionRepository.findByTransactionNo(refundRequestDTO.getVnp_TransactionNo())==null){
            return "Loi";
        }

        refundRequestDTO=refundRequestDTO.builder()
                .vnp_RequestId(Config.getRandomNumber(8))
                .vnp_Version("2.1.0")
                .vnp_Command("refund")
                .vnp_TmnCode(Config.vnp_TmnCode)
                .vnp_TxnRef(transactionRepository.findByTransactionNo(refundRequestDTO.getVnp_TransactionNo()).getTxnRef())
                .vnp_OrderInfo("Hello")
                .vnp_TransactionNo(refundRequestDTO.getVnp_TransactionNo())
                .vnp_TransactionDate(formatter.format(cld.getTime()))
                .vnp_createDate(formatter.format(cld.getTime()))
                .vnp_IpAddr(Config.getIpAddress(request))
                .build();

        if(refundRequestDTO.getVnp_Amount()==null||refundRequestDTO.getVnp_Amount().trim().equals("")) refundRequestDTO.setVnp_Amount("0");

        System.out.println("refundRequestDTO: "+refundRequestDTO.getVnp_TransactionNo());

        refundRequestDTO.generateHash();
        System.out.println(refundRequestDTO);

        String paymentUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction" + "?" + refundRequestDTO.generateUrl();

        return paymentUrl;

    }
}
