package au.com.sixtree.mule.modules.xero;

/**
 * 
 * 
 * @author Sixtree
 *
 */
public class XeroObjectTypes {
	
	public enum XeroGetTypes {
	    Accounts, 
	    BankTransactions, 
	    BrandingThemes, 
	    Contacts, 
	    CreditNotes, 
	    Currencies, 
	    Employees, 
	    ExpenseClaims, 
	    Invoices, 
	    Items, 
	    Journals, 
	    ManualJournals, 
	    Organisation, 
	    Payments,
	    Receipts,
	    //Reports, 
	    TaxRates, 
	    TrackingCategories, 
	    Users;	    
	}
	
	public enum XeroPostTypes {
	    BankTransactions, 
	    Contacts, 
	    CreditNotes,  
	    Employees, 
	    ExpenseClaims, 
	    Invoices, 
	    Items, 
	    ManualJournals, 
	    Receipts;
	}
	
	public enum XeroPutTypes {
	    BankTransactions, 
	    Contacts, 
	    CreditNotes,  
	    Employees, 
	    ExpenseClaims, 
	    Invoices, 
	    Items, 
	    ManualJournals,
	    Payments,
	    Receipts;
	}
	
	public enum XeroAllTypes {
		Accounts, 
	    BankTransactions, 
	    BrandingThemes, 
	    Contacts, 
	    CreditNotes, 
	    Currencies, 
	    Employees, 
	    ExpenseClaims, 
	    Invoices, 
	    Items, 
	    Journals, 
	    ManualJournals, 
	    Organisation, 
	    Payments,
	    Receipts,
	    //Reports, 
	    TaxRates, 
	    TrackingCategories, 
	    Users;
	}
}
