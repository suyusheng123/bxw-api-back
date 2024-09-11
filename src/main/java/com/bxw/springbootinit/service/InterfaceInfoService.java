package com.bxw.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bxw.springbootinit.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.bxw.springbootinit.model.entity.InterfaceInfo;
import com.bxw.springbootinit.model.entity.InterfaceInfo;
import com.bxw.springbootinit.model.vo.InterfaceInfoVO;
import com.bxw.springbootinit.model.vo.InterfaceInfoVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author Lenovo
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-09-11 20:41:28
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {
	/**
	 * 校验
	 *
	 * @param interfaceInfo
	 * @param add
	 */
	void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

	/**
	 * 获取查询条件
	 *
	 * @param interfaceInfoQueryRequest
	 * @return
	 */
	QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

	/**
	 * 分页获取接口信息封装
	 *
	 * @param interfaceInfoPage
	 * @param request
	 * @return
	 */
	Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage, HttpServletRequest request);

	/**
	 * 获取接口信息封装
	 *
	 * @param interfaceInfo
	 * @param request
	 * @return
	 */
	InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo, HttpServletRequest request);

}
