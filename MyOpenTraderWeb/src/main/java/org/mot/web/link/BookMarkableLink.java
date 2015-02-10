package org.mot.web.link;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class BookMarkableLink extends BookmarkablePageLink<Object> { 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	String stock;
	String symbol; 
	String PageTitle;
	

	public String getStock() {
		return stock;
	}



	public void setStock(String stock) {
		this.stock = stock;
	}



	public String getSymbol() {
		return symbol;
	}



	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}



	public String getPageTitle() {
		return PageTitle;
	}



	public void setPageTitle(String pageTitle) {
		PageTitle = pageTitle;
	}



	public BookMarkableLink(String id, Class pageClass, String stock, String symbol, String PageTitle, String _paramKey, String _paramValue, String _wicketHrefTextId, String _hrefText) {
		super(id, pageClass); 
		// TODO Auto-generated constructor stub
		
		this.stock = stock; 
		this.symbol = symbol;
		this.PageTitle = PageTitle;
		
		PageParameters params = new PageParameters();
		params.add("stock", stock);
		params.add("symbol", symbol);
		params.add("pageTitle", PageTitle);
		params.add(_paramKey, _paramValue);
		
		super.add(new Label(_wicketHrefTextId, _hrefText));
		
		super.parameters = params;
		
	}
	
	
	public BookMarkableLink(String id, Class pageClass, String PageTitle) {
		super(id, pageClass); 
		// TODO Auto-generated constructor stub
		
		this.PageTitle = PageTitle;
		
		PageParameters params = new PageParameters();
		params.add("pageTitle", PageTitle);
		
		super.parameters = params;
		
	}
	
	
	public BookMarkableLink(String id, Class pageClass, String PageTitle, String symbol) {
		super(id, pageClass); 
		// TODO Auto-generated constructor stub
		
		this.PageTitle = PageTitle;
		
		PageParameters params = new PageParameters();
		params.add("pageTitle", PageTitle);
		params.add("symbol", symbol);
		
		super.parameters = params;
		
	}

}
