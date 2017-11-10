package com.sogou.bizwork.task.api.task.convertor;

import com.sogou.bizwork.task.api.common.to.OrderType;
import com.sogou.bizwork.task.api.common.to.PaginationTo;
import com.sogou.bizwork.task.api.core.task.dto.PaginationDTO;

/**
 * @description Pagination的thrift对象与DTO的转换
 * @author yangbing@sogou-inc.com
 * @since 2016-7-28
 * @version 1.0.0
 */
public class PaginationConvertor {

	public static PaginationDTO convertTo2DTO(PaginationTo paginationTo) {
		if (paginationTo == null)
			return null;

		PaginationDTO paginationDTO = new PaginationDTO();
		if(paginationTo.getPageIndex()==0){
			paginationTo.setPageIndex(1);
		}
		if(paginationTo.getPageSize()==0){
			paginationTo.setPageSize(Integer.MAX_VALUE);
		}
		paginationDTO.setPageIndex(paginationTo.getPageIndex());
		paginationDTO.setPageSize(paginationTo.getPageSize());
		paginationDTO.setPageStart((paginationTo.getPageIndex() - 1) * paginationTo.getPageSize());
		paginationDTO.setOrderFieldStr(paginationTo.getOrderFieldStr());

		OrderType orderType = paginationTo.getOrderDirection();
		if (orderType != null) {
			paginationDTO.setOrderDirectionStr(orderType == OrderType.ASC ? "ASC" : "DESC");
		}

		return paginationDTO;
	}

	public static PaginationTo convertDTO2To(PaginationDTO paginationDTO) {
		if (paginationDTO == null)
			return null;

		PaginationTo paginationTo = new PaginationTo();
		paginationTo.setPageIndex(paginationDTO.getPageIndex());
		paginationTo.setPageSize(paginationDTO.getPageSize());
		paginationTo.setPageStart(paginationDTO.getPageStart());
		paginationTo.setOrderFieldStr(paginationDTO.getOrderDirectionStr());

		String orderStr = paginationDTO.getOrderDirectionStr();
		if (orderStr != null) {
			if ("ASC".equals(orderStr)) {
				paginationTo.setOrderDirection(OrderType.ASC);
			} else if ("DESC".equals(orderStr)) {
				paginationTo.setOrderDirection(OrderType.DESC);
			} else {
				throw new IllegalArgumentException("convert failed, illegal orderDirectionStr: " + orderStr);
			}
		}

		return paginationTo;
	}
}
