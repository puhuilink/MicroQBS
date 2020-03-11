package com.phlink.core.controller.manage;

import com.phlink.core.common.exception.BizException;
import com.phlink.core.common.utils.ResultUtil;
import com.phlink.core.common.vo.Result;
import com.phlink.core.entity.Dict;
import com.phlink.core.service.DictDataService;
import com.phlink.core.service.DictService;
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
@RequestMapping("/manage/dict")
@Transactional
public class DictController {

    @Autowired
    private DictService dictService;

    @Autowired
    private DictDataService dictDataService;

    @Autowired
    private RedissonClient redissonClient;

    @GetMapping(value = "/all")
    @ApiOperation(value = "获取全部数据")
    public List<Dict> getAll() {
        return dictService.listAllOrderBySortOrder();
    }

    @PostMapping(value = "/save")
    @ApiOperation(value = "添加")
    public String save(Dict dict) {

        if (dictService.getByType(dict.getType()) != null) {
            throw new BizException("字典类型Type已存在");
        }
        dictService.save(dict);
        return "添加成功";
    }

    @PutMapping(value = "/update")
    @ApiOperation(value = "编辑")
    public String update(Dict dict) {

        Dict old = dictService.getById(dict.getId());
        // 若type修改判断唯一
        if (!old.getType().equals(dict.getType()) && dictService.getByType(dict.getType()) != null) {
            throw new BizException("字典类型Type已存在");
        }
        dictService.updateById(dict);
        return "编辑成功";
    }

    @DeleteMapping(value = "/delete/{id}")
    @ApiOperation(value = "通过id删除")
    public String delAllByIds(@PathVariable String id) {


        Dict dict = dictService.getById(id);
        dictService.removeById(id);
        dictDataService.deleteByDictId(id);
        // 删除缓存
        redissonClient.getKeys().delete("dictData::" + dict.getType());
        return "删除成功";
    }

    @GetMapping(value = "/search")
    @ApiOperation(value = "搜索字典")
    public List<Dict> searchPermissionList(@RequestParam String key) {
        return dictService.listByTitleOrTypeLike(key);
    }
}