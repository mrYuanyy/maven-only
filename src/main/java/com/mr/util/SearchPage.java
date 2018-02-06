package com.mr.util;

import java.io.Serializable;
/** 
 *  
 * 功能描述： 查询条件基类 
 * 
 */  
public class SearchPage implements Serializable {  
    
    private static final long serialVersionUID = 1L;  
  
    /** 页码 */  
    private int   pageNumber       = 1;  
  
    /** 每页记录数 */  
    private int    pageSize         = 10;  
  
    /** 总记录数 */  
    private int     totalCount;  
  
    /** 排序字段 */  
    private String[]   orderType;  
  
    /** 
     * 查询关键字 
     */  
    private String   keyword;  
  
    /** 多条件查询 */  
    private String    selectParam;  
  
    /** 默认查询字段 */  
    private String  field;  
  
    private int    Start    = 0;

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String[] getOrderType() {
		return orderType;
	}

	public void setOrderType(String[] orderType) {
		this.orderType = orderType;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getSelectParam() {
		return selectParam;
	}

	public void setSelectParam(String selectParam) {
		this.selectParam = selectParam;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public int getStart() {
		return Start;
	}

	public void setStart(int start) {
		Start = start;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}  
      
}  