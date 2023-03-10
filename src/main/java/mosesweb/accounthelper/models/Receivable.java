package mosesweb.accounthelper.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * A receivable, associated with a credit sale and an existing customer. Has a
 * non-negative invoice number that is unique. Has a non-negative amount and a
 * date not more than a day in the future.
 *
 * @author Ben Moses
 */
@Entity
@Table(name = "receivable")
public class Receivable
{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "amount")
    @NotNull(message = "cannot be null")
    @PositiveOrZero(message = "must not be negative")
    @Digits(integer = 9, fraction = 2, message = "maximum of two decimal places")
    private BigDecimal amount;

    @Column(name = "date")
    @NotNull(message = "cannot be null")
    @PastOrPresent(message = "date cannot be in the future")
    private LocalDate date;

    @Column(name = "invoice_number")
    @NotNull(message = "cannot be null")
    @Positive(message = "must be positive")
    private Integer invoiceNumber;

    @ManyToOne()
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;

    protected Receivable()
    {
    }

    public Receivable(BigDecimal amount, LocalDate date, Integer invoiceNumber,
                      Customer customer)
    {
        this.amount = amount;
        this.date = date;
        this.invoiceNumber = invoiceNumber;
        this.customer = customer;
    }

    public Integer getId()
    {
        return id;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public Integer getInvoiceNumber()
    {
        return invoiceNumber;
    }

    public Customer getCustomer()
    {
        return customer;
    }
}
