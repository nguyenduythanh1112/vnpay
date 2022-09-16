package vnpay2.vnpay2.controller;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import vnpay2.vnpay2.DTO.RefundRequestDTO;
import vnpay2.vnpay2.DTO.RefundRespondDTO;

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

    @PostMapping
    public void refund(@ModelAttribute RefundRequestDTO refundRequestDTO,HttpServletRequest request, HttpServletResponse resp) throws IOException {

        if(refundRequestDTO.getVnp_Amount()==null||refundRequestDTO.getVnp_Amount().trim().equals("")) refundRequestDTO.setVnp_Amount("0");
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");





        refundRequestDTO=refundRequestDTO.builder()
                .vnp_RequestId(Config.getRandomNumber(8))
                .vnp_Version("2.1.0")
                .vnp_Command("refund")
                .vnp_TmnCode(Config.vnp_TmnCode)
                .vnp_TxnRef(Config.getRandomNumber(8))
                .vnp_OrderInfo("")
                .vnp_TransactionNo("")
                .vnp_TransactionDate(formatter.format(cld.getTime()))
                .vnp_createDate(formatter.format(cld.getTime()))
                .vnp_IpAddr(Config.getIpAddress(request))
                .build();
        refundRequestDTO.generateHash();

        System.out.println(refundRequestDTO);
        System.out.println(refundRequestDTO.generateUrl());

        String paymentUrl = Config.vnp_PayUrl + "?" + refundRequestDTO.generateUrl();
        com.google.gson.JsonObject job = new JsonObject();
        job.addProperty("data", paymentUrl);
        Gson gson = new Gson();
        resp.getWriter().write(gson.toJson(job));

    }
}
