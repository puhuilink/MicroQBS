package com.phlink.core.controller.manage;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.phlink.core.common.exception.BizException;
import com.phlink.core.common.utils.ResultUtil;
import com.phlink.core.common.vo.PageVO;
import com.phlink.core.common.vo.Result;
import com.phlink.core.entity.Dict;
import com.phlink.core.entity.DictData;
import com.phlink.core.service.DictDataService;
import com.phlink.core.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "字典数据管理接口")
@RequestMapping("/manage/dictData")
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
    public PageInfo<DictData> pageByCondition(DictData dictData,
                                              PageVO pageVo) {
        PageHelper.startPage(pageVo.getPageNumber(), pageVo.getPageSize());
        List<DictData> dictDataList = dictDataService.listByCondition(dictData);
        PageInfo<DictData> pageInfo = new PageInfo(dictDataList);
        return pageInfo;
    }

    @GetMapping(value = "/type/{type}")
    @ApiOperation(value = "通过类型获取")
    @Cacheable(key = "#type")
    public List<DictData> getByType(@PathVariable String type) {

        Dict dict = dictService.getByType(type);
        if (dict == null) {
            throw new BizException("字典类型 " + type + " 不存在");
        }
        return dictDataService.listByDictId(dict.getId());
    }

    @PostMapping(value = "/save")
    @ApiOperation(value = "保存")
    public String save(DictData dictData) {

        Dict dict = dictService.getById(dictData.getDictId());
        if (dict == null) {
            throw new BizException("字典类型id不存在");
        }
        dictDataService.save(dictData);
        // 删除缓存
        redissonClient.getKeys().delete("dictData::" + dict.getType());
        return "添加成功";
    }

    @PutMapping(value = "/update")
    @ApiOperation(value = "编辑")
    public String update(DictData dictData) {
        dictDataService.updateById(dictData);
        // 删除缓存
        Dict dict = dictService.getById(dictData.getDictId());
        redissonClient.getKeys().delete("dictData::" + dict.getType());
        return "编辑成功";
    }

    @DeleteMapping(value = "/delete/{ids}")
    @ApiOperation(value = "批量通过id删除")
    public String delByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            DictData dictData = dictDataService.getById(id);
            Dict dict = dictService.getById(dictData.getDictId());
            dictDataService.removeById(id);
            // 删除缓存
            redissonClient.getKeys().delete("dictData::" + dict.getType());
        }
        return "批量通过id删除数据成功";
    }
}