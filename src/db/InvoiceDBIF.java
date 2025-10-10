package db;

import java.time.LocalDate;

public interface InvoiceDBIF {
    void createInvoice(int saleOrderId, LocalDate paymentDate, double amount, double vat, double totalAmount)
        throws DataAccessException;
}
