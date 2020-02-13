package com.phlink.bus.api.serviceorg.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.serviceorg.domain.Guardian;

import java.util.List;

/**
 * @author wen
 */
public interface IGuardianService extends IService<Guardian> {

	/**
	 * 查询列表
	 * 
	 * @param request
	 * @param guardian
	 * @return
	 */
	IPage<Guardian> listGuardians(QueryRequest request, Guardian guardian);

	/**
	 * 新增
	 * 
	 * @param guardian
	 */
	void createGuardian(Guardian guardian) throws Exception;

	/**
	 * 修改
	 * 
	 * @param guardian
	 */
	void modifyGuardian(Guardian guardian);

	/**
	 * 批量删除
	 * 
	 * @param guardianIds
	 */
	void deleteGuardianIds(String[] guardianIds) throws Exception;

	void removeGuardianIds(Long studentId, String[] ids);

	void addOtherGuardian(Long studentId, String username, String mobile, String idCard) throws Exception;

    void intoGroup(Long routeOperationId, Long[] guardianIds);

    /**
	 * 请假权限修改
	 * @param guardianId
	 * @param studentId
	 * @param isLeavePermission
	 * @throws Exception
	 */
	void updateLeavePermissions(Long guardianId, Long studentId, Boolean isLeavePermission);

    List<Guardian> listByStudentId(Long studentId);

	/**
	 * 根据用户ID获取监护人信息
	 * @param guardianId
	 */
	Guardian getByGuardianId(Long guardianId);

	/**
	 * 检查是否是主责任人
	 */
	void checkMainGuardian(Long studentId) throws BusApiException;
//	void updateLeaveApplyStatus(@Valid GuarDianLeaveApplyStatusVO bean)throws Exception;

    /**
     * 获取学生监护人信息
     *
	 * @param id
     * @return
     */
	Guardian getGuardianDetail(Long id);

	/**
	 * 根据idcard获取责任人信息
	 *
	 * @param idcard
	 * @return
	 */
	Guardian getGuardianByIdcard(String idcard);

	/**
	 * 小孩的监护人列表
	 *
	 * @param studentId
	 * @return
	 */
    List<Guardian> listOtherGuardian(Long studentId);

	/**
	 * 监护人账号冻结
	 * @param guardianId
	 */
	void invalidate(Long guardianId);

	/**
	 * 监护人账号恢复
	 * @param guardianId
	 */
	void effective(Long guardianId);

	/**
	 * 根据老师ID获取该条路线的所有家长
	 * @param busTeacherId
	 * @return
	 */
    List<Guardian> listOnRouteByWorkerId(Long busTeacherId);

	/**
	 * 检查用户是不是学生的监护人
	 * @param studentId
	 * @param userId
	 */
	void checkGuardian(Long studentId, Long userId) throws BusApiException;

	/**
	 * 检查该用户是不是家长
	 * @param userId
	 */
    Guardian getByUserId(Long userId);
}
