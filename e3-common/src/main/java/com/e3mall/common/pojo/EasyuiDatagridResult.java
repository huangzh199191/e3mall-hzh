package com.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;

//easyui中datagrid组件的返回结果
public class EasyuiDatagridResult implements Serializable{
	private Long total;//总记录数
	private List rows;//每页数据
	
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public List getRows() {
		return rows;
	}
	public void setRows(List rows) {
		this.rows = rows;
	}
	
}
