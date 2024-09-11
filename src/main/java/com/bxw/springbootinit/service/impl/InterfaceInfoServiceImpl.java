package com.bxw.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bxw.springbootinit.common.ErrorCode;
import com.bxw.springbootinit.constant.CommonConstant;
import com.bxw.springbootinit.exception.BusinessException;
import com.bxw.springbootinit.exception.ThrowUtils;
import com.bxw.springbootinit.mapper.InterfaceInfoMapper;
import com.bxw.springbootinit.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.bxw.springbootinit.model.entity.*;
import com.bxw.springbootinit.model.vo.InterfaceInfoVO;
import com.bxw.springbootinit.model.vo.InterfaceInfoVO;
import com.bxw.springbootinit.model.vo.UserVO;
import com.bxw.springbootinit.service.InterfaceInfoService;
import com.bxw.springbootinit.service.UserService;
import com.bxw.springbootinit.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Lenovo
 * @description 针对表【interface_info(接口信息)】的数据库操作Service实现
 * @createDate 2024-09-11 20:41:28
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
		implements InterfaceInfoService {

	@Resource
	private UserService userService;

	/**
	 * 校验
	 *
	 * @param interfaceInfo
	 * @param add
	 */
	@Override
	public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
		if (interfaceInfo == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		String name = interfaceInfo.getName();
		// 创建时，参数不能为空
		if (add) {
			ThrowUtils.throwIf(StringUtils.isAnyBlank(name), ErrorCode.PARAMS_ERROR);
		}
		// 有参数则校验
		if (StringUtils.isNotBlank(name) && name.length() > 50) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口名称过长");
		}
	}

	/**
	 * 获取查询包装类
	 *
	 * @param interfaceInfoQueryRequest
	 * @return
	 */
	@Override
	public QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
		QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
		if (interfaceInfoQueryRequest == null) {
			return queryWrapper;
		}
		String sortField = interfaceInfoQueryRequest.getSortField();
		String sortOrder = interfaceInfoQueryRequest.getSortOrder();
		Long id = interfaceInfoQueryRequest.getId();
		Long userId = interfaceInfoQueryRequest.getUserId();
		queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
		queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
		queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
				sortField);
		return queryWrapper;
	}

	@Override
	public Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage, HttpServletRequest request) {
		List<InterfaceInfo> interfaceInfoList = interfaceInfoPage.getRecords();
		Page<InterfaceInfoVO> interfaceInfoVOPage = new Page<>(interfaceInfoPage.getCurrent(), interfaceInfoPage.getSize(), interfaceInfoPage.getTotal());
		if (CollUtil.isEmpty(interfaceInfoList)) {
			return interfaceInfoVOPage;
		}
		// 1. 关联查询用户信息
		Set<Long> userIdSet = interfaceInfoList.stream().map(InterfaceInfo::getUserId).collect(Collectors.toSet());
		Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
				.collect(Collectors.groupingBy(User::getId));
		// 2. 已登录，获取接口信息列表
		User loginUser = userService.getLoginUserPermitNull(request);
		if (loginUser != null) {
			Set<Long> interfaceInfoIdSet = interfaceInfoList.stream().map(InterfaceInfo::getId).collect(Collectors.toSet());
			loginUser = userService.getLoginUser(request);
		}
		// 填充信息
		List<InterfaceInfoVO> interfaceInfoVOList = interfaceInfoList.stream().map(interfaceInfo -> {
			InterfaceInfoVO interfaceInfoVO = InterfaceInfoVO.objToVo(interfaceInfo);
			Long userId = interfaceInfo.getUserId();
			User user = null;
			if (userIdUserListMap.containsKey(userId)) {
				user = userIdUserListMap.get(userId).get(0);
			}
			interfaceInfoVO.setUserId(userId);
			return interfaceInfoVO;
		}).collect(Collectors.toList());
		interfaceInfoVOPage.setRecords(interfaceInfoVOList);
		return interfaceInfoVOPage;
	}

	@Override
	public InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo, HttpServletRequest request) {
		InterfaceInfoVO interfaceInfoVO = InterfaceInfoVO.objToVo(interfaceInfo);
		long interfaceInfoId = interfaceInfo.getId();
		// 1. 关联查询用户信息
		Long userId = interfaceInfo.getUserId();
		User user = null;
		if (userId != null && userId > 0) {
			user = userService.getById(userId);
		}
		interfaceInfoVO.setUserId(userId);
		return interfaceInfoVO;
	}
}




