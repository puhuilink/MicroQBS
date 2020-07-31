/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:04:51
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:07:44
 */
package com.puhuilink.qbs.core.web.controller.manage;

import com.puhuilink.qbs.core.base.annotation.SystemLogTrace;
import com.puhuilink.qbs.core.base.enums.LogType;
import com.puhuilink.qbs.core.base.enums.ResultCode;
import com.puhuilink.qbs.core.base.exception.WarnException;
import com.puhuilink.qbs.core.base.vo.Result;
import com.puhuilink.qbs.core.web.entity.Dict;
import com.puhuilink.qbs.core.web.service.DictDataService;
import com.puhuilink.qbs.core.web.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "字典管理接口")
@RequestMapping("/api/manage/dict")
@Transactional
public class DictController {

    @Autowired
    private DictService dictService;

    @Autowired
    private DictDataService dictDataService;

    @Autowired
    private RedissonClient redissonClient;

    @GetMapping(value = "")
    @ApiOperation(value = "获取全部数据")
    public Result listAll() {
        List<Dict> dicts = dictService.listAllOrderBySortOrder();
        return Result.ok().data(dicts);
    }

    @PostMapping(value = "")
    @ApiOperation(value = "添加")
    @SystemLogTrace(description = "添加字典", type = LogType.OPERATION)
    public Result save(Dict dict) {

        if (dictService.getByType(dict.getType()) != null) {
            throw new WarnException(ResultCode.BAD_REQUEST_PARAMS.getCode(), "字典类型Type已存在");
        }
        dictService.save(dict);
        return Result.ok("添加成功");
    }

    @PutMapping(value = "")
    @ApiOperation(value = "编辑")
    @SystemLogTrace(description = "编辑字典", type = LogType.OPERATION)
    public Result update(Dict dict) {

        Dict old = dictService.getById(dict.getId());
        // 若type修改判断唯一
        if (!old.getType().equals(dict.getType()) && dictService.getByType(dict.getType()) != null) {
            throw new WarnException(ResultCode.BAD_REQUEST_PARAMS.getCode(), "字典类型Type已存在");
        }
        dictService.updateById(dict);
        return Result.ok("编辑成功");
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "通过id删除")
    @SystemLogTrace(description = "删除字典", type = LogType.OPERATION)
    public Result delAllByIds(@PathVariable String id) {

        Dict dict = dictService.getById(id);
        dictService.removeById(id);
        dictDataService.deleteByDictId(id);
        // 删除缓存
        redissonClient.getKeys().delete("dictData::" + dict.getType());
        return Result.ok("删除成功");
    }

    @GetMapping(value = "/search")
    @ApiOperation(value = "搜索字典")
    public Result searchPermissionList(@RequestParam String key) {
        List<Dict> dicts = dictService.listByTitleOrTypeLike(key);
        return Result.ok().data(dicts);
    }
}
