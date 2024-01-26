package com.abdul.abcbank.bank.service;

import com.abdul.abcbank.bank.dto.*;

public interface UserService {

    Response createAccount(UserRequest userRequest);
    Response balanceEnquiry(EnquiryRequest enquiryRequest);
    Response accountEnquiry(NameEnquiry nameEnquiry);
    Response creditAccount(CreditDebitRequest creditDebitRequest);
    Response DebitAccount(CreditDebitRequest creditDebitRequest);

}
