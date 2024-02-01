package com.abdul.abcbank.bank.service.imple;

import com.abdul.abcbank.bank.dto.EmailRequest;
import com.abdul.abcbank.bank.entity.Transaction;
import com.abdul.abcbank.bank.entity.User;
import com.abdul.abcbank.bank.repository.TransactionRepository;
import com.abdul.abcbank.bank.repository.UserRepository;
import com.abdul.abcbank.bank.service.EmailService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BankStatement {

    private static final String FILE = "C:\\Users\\PC\\Documents\\\\MyStatement-abc.pdf";

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

//    get transaction list
    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

        List<Transaction> transactionList = transactionRepository.findAll()
                .stream().filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> transaction.getDate().isEqual(start))
                .filter(transaction -> transaction.getDate().isEqual(end))
                .toList();

        User user = userRepository.findByAccountNumber(accountNumber);
        String customerName = user.getName();
        String customerAddress = user.getAddress();


        //PDF content
        Rectangle statement = new Rectangle(PageSize.A4);
        Document document = new Document(statement);

        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document, outputStream);

        document.open();

//        Table content
//        Table info, table 1
        PdfPTable bankInfoTable = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("ABC Bank"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.CYAN);
        bankName.setPadding(10f);

        PdfPCell address = new PdfPCell(new Phrase("No 5 GRA road Opposite Govt house, Gombe State"));
        address.setBorder(0);

//        Table 2
        PdfPTable statementInfo = new PdfPTable(2);
        PdfPCell sDate = new PdfPCell(new Phrase("State Date: "+startDate));
        sDate.setBorder(0);

        PdfPCell statementAccount = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
        statementAccount.setBorder(0);

        PdfPCell eDate = new PdfPCell(new Phrase("End Date: "+endDate));
        eDate.setBorder(0);

        PdfPCell customerInfoName = new PdfPCell(new Phrase("Name: "+customerName));
        customerInfoName.setBorder(0);

        PdfPCell space = new PdfPCell();
        space.setBorder(0);

        PdfPCell customerInfoAddress = new PdfPCell(new Phrase("Address: "+customerAddress));
        customerInfoAddress.setBorder(0);


//        Table 3, Transaction
        PdfPTable transactionTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase("DATE"));
        date.setBorder(0);
        date.setBackgroundColor(BaseColor.CYAN);

        PdfPCell type = new PdfPCell(new Phrase("TRANSACTION TYPE"));
        type.setBorder(0);
        type.setBackgroundColor(BaseColor.CYAN);

        PdfPCell amount = new PdfPCell(new Phrase("AMOUNT"));
        amount.setBorder(0);
        amount.setBackgroundColor(BaseColor.CYAN);

        PdfPCell status = new PdfPCell(new Phrase("STATUS"));
        status.setBorder(0);
        status.setBackgroundColor(BaseColor.CYAN);

//        table 1
        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(address);

//        table 2
        statementInfo.addCell(sDate);
        statementInfo.addCell(statementAccount);
        statementInfo.addCell(eDate);
        statementInfo.addCell(customerInfoName);
        statementInfo.addCell(customerInfoAddress);
        statementInfo.addCell(space);

//        table 3
        transactionTable.addCell(date);
        transactionTable.addCell(type);
        transactionTable.addCell(amount);
        transactionTable.addCell(status);

//        fetch transactions
        transactionList.forEach(transaction -> {
            transactionTable.addCell(new Phrase(startDate));
            transactionTable.addCell(new Phrase(transaction.getTransactionType()));
            transactionTable.addCell(new Phrase(transaction.getAmount().toString()));
            transactionTable.addCell(new Phrase(transaction.getStatus()));
        });

//        add the table to document and close the document
        document.add(bankInfoTable);
        document.add(statementInfo);
        document.add(transactionTable);

        document.close();

        EmailRequest emailRequest = EmailRequest.builder()
                .recipient(user.getEmail())
                .subject("BANK ACCOUNT STATEMENT")
                .message("Kindly find your requested account statement attached")
                .attachment(FILE)
                .build();

        emailService.sendEmailAlertWithAttachment(emailRequest);

        return transactionList;

    }
}
