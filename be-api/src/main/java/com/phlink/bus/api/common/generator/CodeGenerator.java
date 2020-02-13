package com.phlink.bus.api.common.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.Scanner;

/**
 * mybatis plus 提供的代码生成器
 * 可以快速生成 Entity、Mapper、Mapper XML、Service、Controller 等各个模块的代码
 *
 * @link https://mp.baomidou.com/guide/generator.html
 */
public class CodeGenerator {

    // 数据库 URL
    private static final String URL = "jdbc:postgresql://111.200.216.79:10543/za_bus_api";
    // 数据库驱动
    private static final String DRIVER_NAME = "org.postgresql.Driver";
    // 数据库用户名
    private static final String USERNAME = "postgres";
    // 数据库密码
    private static final String PASSWORD = "postgres";
    // @author 值
    private static final String AUTHOR = System.getProperty("user.name");
    // 包的基础路径
    private static final String BASE_URL = "main";
    // 使用之前需确认是不是这个包
    private static final String PACKAGE = "fence";

    private static final String BASE_PACKAGE_URL = "java.com.phlink.bus.api." + PACKAGE;
    // xml文件路径
    private static final String XML_PACKAGE_URL = "resources.mapper." + PACKAGE;
    // xml 文件模板
    private static final String XML_MAPPER_TEMPLATE_PATH = "/templates/mapper.xml";
    // dao 文件模板
    private static final String MAPPER_TEMPLATE_PATH = "/templates/mapper.java";
    // domain 文件模板
    private static final String ENTITY_TEMPLATE_PATH = "/templates/entity.java";
    // service 文件模板
    private static final String SERVICE_TEMPLATE_PATH = "/templates/service.java";
    // serviceImpl 文件模板
    private static final String SERVICE_IMPL_TEMPLATE_PATH = "/templates/serviceImpl.java";
    // controller 文件模板
    private static final String CONTROLLER_TEMPLATE_PATH = "/templates/controller.java";

    public static void main(String[] args) {
        AutoGenerator generator = new AutoGenerator();
        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        globalConfig.setOutputDir(projectPath + "/be-api/src");
        globalConfig.setAuthor(AUTHOR);
        globalConfig.setOpen(true);
        globalConfig.setFileOverride(true);
        globalConfig.setIdType(IdType.ID_WORKER);
//        globalConfig.setSwagger2(true);
        generator.setGlobalConfig(globalConfig);

        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(URL);
        dataSourceConfig.setDriverName(DRIVER_NAME);
        dataSourceConfig.setUsername(USERNAME);
        dataSourceConfig.setPassword(PASSWORD);
        dataSourceConfig.setDbType(DbType.POSTGRE_SQL);
        generator.setDataSource(dataSourceConfig);

        // 包配置
        PackageConfig packageConfig = new PackageConfig();
//        packageConfig.setModuleName("bus");
        packageConfig.setParent(BASE_URL);
        packageConfig.setController(BASE_PACKAGE_URL+".controller");
        packageConfig.setEntity(BASE_PACKAGE_URL+".domain");
        packageConfig.setMapper(BASE_PACKAGE_URL+".dao");
        packageConfig.setService(BASE_PACKAGE_URL+".service");
        packageConfig.setServiceImpl(BASE_PACKAGE_URL+".service.impl");
        packageConfig.setXml(XML_PACKAGE_URL);
        generator.setPackageInfo(packageConfig);

        // 配置自定义代码模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(XML_MAPPER_TEMPLATE_PATH);
        templateConfig.setMapper(MAPPER_TEMPLATE_PATH);
        templateConfig.setEntity(ENTITY_TEMPLATE_PATH);
        templateConfig.setService(SERVICE_TEMPLATE_PATH);
        templateConfig.setServiceImpl(SERVICE_IMPL_TEMPLATE_PATH);
        templateConfig.setController(CONTROLLER_TEMPLATE_PATH);
        generator.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setInclude(scanner("表名"));
        strategy.setSuperEntityColumns("id", "deleted");
        strategy.setSuperEntityClass(ApiBaseEntity.class);
        strategy.setSuperControllerClass(BaseController.class.getName());
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix("t_");
//        strategy.setTablePrefix("sys_");
        generator.setStrategy(strategy);
        generator.setTemplateEngine(new FreemarkerTemplateEngine());
        generator.execute();
    }

    private static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(("请输入" + tip + "："));
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }
}
