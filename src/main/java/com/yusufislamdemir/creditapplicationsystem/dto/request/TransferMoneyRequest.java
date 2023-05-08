package com.yusufislamdemir.creditapplicationsystem.dto.request;


import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransferMoneyRequest {
    private long senderAccountId;
    private long receiverAccountId;
    private long amount;
}
