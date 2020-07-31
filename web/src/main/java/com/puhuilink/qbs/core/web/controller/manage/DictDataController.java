/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:04:54
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:07:46
 */
package com.puhuilink.qbs.core.web.controller.manage;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.puhuilink.qbs.core.base.enums.ResultCode;
import com.puhuilink.qbs.core.base.exception.WarnException;
import com.puhuilink.qbs.core.base.vo.PageVO;
import com.puhuilink.qbs.core.base.vo.Result;
import com.puhuilink.qbs.core.web.entity.Dict;
import com.puhuilink.qbs.core.web.entity.DictData;
import com.puhuilink.qbs.core.web.service.DictDataService;
import com.puhuilink.qbs.core.web.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "字典数据管理接口")
@RequestMapping("/api/manage/dict-data")
@CacheConfig(cacheNames = "dictData")
@Transactional
public class DictDataController {

    @Autowired
    private DictService dictService;
    @Autowired
    private DictDataService dictDataService;
    @Autowired
    private RedissonClient redissonClient;

    @GetMapping(value = "/page")
    @ApiOperation(value = "多条件分页获取用户列表")
    public Result pageByCondition(DictData dictData, PageVO pageVo) {
        PageInfo<DictData> pageInfo = PageHelper
            .startPage(pageVo.getPageNumber(), pageVo.getPageSize(), pageVo.getSort() + " " + pageVo.getOrder())
            .doSelectPageInfo(() -> dictDataService.listByCondition(dictData));
        return Result.ok().data(pageInfo);
    }

    @GetMapping(value = "/type/{type}")
    @ApiOperation(value = "通过类型获取")
    @Cacheable(key = "#type")
    public Result getByType(@PathVariable String type) {

        Dict dict = dictService.getByType(type);
        if (dict == null) {
            throw new WarnException(ResultCode.BAD_REQUEST_PARAMS.getCode(), "字典类型 " + type + " 不存在");
        }
        List<DictData> dictDataList = dictDataService.listByDictId(dict.getId());
        return Result.ok().data(dictDataList);
    }

    @PostMapping(value = "")
    @ApiOperation(value = "保存")
    public Result save(DictData dictData) {

        Dict dict = dictService.getById(dictData.getDictId());
        if (dict == null) {
            throw new WarnException(ResultCode.BAD_REQUEST_PARAMS.getCode(), "字典类型id不存在");
        }
        dictDataService.save(dictData);
        // 删除缓存
        redissonClient.getKeys().delete("dictData::" + dict.getType());
        return Result.ok("添加成功");
    }

    @PutMapping(value = "")
    @ApiOperation(value = "编辑")
    public Result update(DictData dictData) {
        dictDataService.updateById(dictData);
        // 删除缓存
        Dict dict = dictService.getById(dictData.getDictId());
        redissonClient.getKeys().delete("dictData::" + dict.getType());
        return Result.ok("添加成功");
    }

    @DeleteMapping(value = "/{ids}")
    @ApiOperation(value = "批量通过id删除")
    public Result delByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            DictData dictData = dictDataService.getById(id);
            Dict dict = dictService.getById(dictData.getDictId());
            dictDataService.removeById(id);
            // 删除缓存
            redissonClient.getKeys().delete("dictData::" + dict.getType());
        }
        return Result.ok("批量通过id删除数据成功");
    }
}
