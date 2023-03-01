package mosesweb.accounthelper.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import mosesweb.accounthelper.exceptions.CustomerNeededException;

/**
 *
 * A sale, either cash or credit.
 * Has a non-negative amount and a date not more than a day in the future.
 * A cash sale has an associated bank debit.
 * A credit sale has an associated receivable.
 * 
 * @author Ben Moses
 */
@Entity
@Table(name="sales")
public class Sale
{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Integer id;
    
    private LocalDate date;
    private BigDecimal amount;
    private boolean isCash;
    
    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="bank_debit_id")
    private BankDebit bankDebit;
    
    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="receivable_id")
    private Receivable receivable;

    public Sale()
    {
    }
    
    public Sale(BigDecimal amount, LocalDate date)
    {
        this(amount, date, true);
    }
    
    public Sale(BigDecimal amount, LocalDate date, boolean isCash)
    {
        this.date = date;
        this.amount = amount;
        this.isCash = isCash;
        if (isCash) {
            this.bankDebit = new BankDebit(amount, date);
            this.receivable = null;
        } else {
            throw new CustomerNeededException();
        }
    }
            
    public Sale(BigDecimal amount, LocalDate date, boolean isCash, Integer invoiceNumber, Customer customer)
    {
        this.date = date;
        this.amount = amount;
        this.isCash = isCash;
        if (isCash) {
            this.bankDebit = new BankDebit(amount, date);
            this.receivable = null;
        } else {
            this.receivable = new Receivable(amount, date, invoiceNumber, customer);
            this.bankDebit = null;
        }
    }

    /**
     *
     * @return the ID of the Sale. null if not saved in the repository.
     */
    public Integer getId()
    {
        return id;
    }

    /**
     *
     * @return the date of the sale if cash, the date of the invoice if credit.
     */
    public LocalDate getDate()
    {
        return date;
    }

    /**
     *
     * @return the amount of the sale in GBP.
     */
    public BigDecimal getAmount()
    {
        return amount;
    }

    /**
     *
     * @return true if the sale if a cash sale, otherwise false.
     */
    public boolean isCash()
    {
        return isCash;
    }
    
    /**
     *
     * @return the bank debit object associated with this cash sale
     */
    public BankDebit getBankDebit()
    {
        return bankDebit;
    }
    
    /**
     *
     * @return the receivable object associated with this credit sale.
     */
    public Receivable getReceivable()
    {
        return receivable;
    }
    
    /**
     *
     * @param invoiceNumber the target invoice number. This will be checked
     * against this sale's invoice number if it is a credit sale, and a
     * RuntimeException will be thrown if the invoice numbers match.
     */
    public boolean invoiceNumberMatches(Integer invoiceNumber)
    {
        if (!isCash() && receivable != null) {
            // This is a credit sale with a valid receivable attached
            if (receivable.getInvoiceNumber().equals(invoiceNumber)) {
                // The target invoice number is the same as this invoice number...
                return true;
            }
        }
        return false;
    }
}