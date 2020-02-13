--
-- PostgreSQL database dump
--

-- Dumped from database version 11.4 (Debian 11.4-1.pgdg90+1)
-- Dumped by pg_dump version 11.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP INDEX IF EXISTS public.t_user_user_id_uindex;
DROP INDEX IF EXISTS public.t_system_config_config_id_uindex;
DROP INDEX IF EXISTS public.t_station_schedule_id_uindex;
DROP INDEX IF EXISTS public.t_station_leave_id_uindex;
DROP INDEX IF EXISTS public.t_station_id_uindex;
DROP INDEX IF EXISTS public.t_station_attendance_id_uindex;
DROP INDEX IF EXISTS public.t_school_school_id_uindex;
DROP INDEX IF EXISTS public.t_route_record_id_uindex;
DROP INDEX IF EXISTS public.t_route_id_uindex;
DROP INDEX IF EXISTS public.t_role_role_id_uindex;
DROP INDEX IF EXISTS public.t_menu_menu_id_uindex;
DROP INDEX IF EXISTS public.t_login_log_id_uindex;
DROP INDEX IF EXISTS public.t_log_id_uindex;
DROP INDEX IF EXISTS public.t_job_log_log_id_uindex;
DROP INDEX IF EXISTS public.t_job_job_id_uindex;
DROP INDEX IF EXISTS public.t_dict_id_uindex;
DROP INDEX IF EXISTS public.t_dept_id_uindex;
DROP INDEX IF EXISTS public.t_bus_id_uindex;
ALTER TABLE IF EXISTS ONLY public.sys_user
    DROP CONSTRAINT IF EXISTS t_user_pk;
ALTER TABLE IF EXISTS ONLY public.sys_config
    DROP CONSTRAINT IF EXISTS t_system_config_pk;
ALTER TABLE IF EXISTS ONLY public.t_station_schedule
    DROP CONSTRAINT IF EXISTS t_station_schedule_pk;
ALTER TABLE IF EXISTS ONLY public.t_station
    DROP CONSTRAINT IF EXISTS t_station_pk;
ALTER TABLE IF EXISTS ONLY public.t_station_attendance
    DROP CONSTRAINT IF EXISTS t_station_attendance_pk;
ALTER TABLE IF EXISTS ONLY public.t_school
    DROP CONSTRAINT IF EXISTS t_school_pk;
ALTER TABLE IF EXISTS ONLY public.t_route_record
    DROP CONSTRAINT IF EXISTS t_route_record_pk;
ALTER TABLE IF EXISTS ONLY public.t_route
    DROP CONSTRAINT IF EXISTS t_route_pk;
ALTER TABLE IF EXISTS ONLY public.sys_role
    DROP CONSTRAINT IF EXISTS t_role_pk;
ALTER TABLE IF EXISTS ONLY public.sys_menu
    DROP CONSTRAINT IF EXISTS t_menu_pk;
ALTER TABLE IF EXISTS ONLY public.sys_login_log
    DROP CONSTRAINT IF EXISTS t_login_log_pk;
ALTER TABLE IF EXISTS ONLY public.sys_log
    DROP CONSTRAINT IF EXISTS t_log_pk;
ALTER TABLE IF EXISTS ONLY public.sys_job
    DROP CONSTRAINT IF EXISTS t_job_pk;
ALTER TABLE IF EXISTS ONLY public.sys_job_log
    DROP CONSTRAINT IF EXISTS t_job_log_pk;
ALTER TABLE IF EXISTS ONLY public.sys_dict
    DROP CONSTRAINT IF EXISTS t_dict_pk;
ALTER TABLE IF EXISTS ONLY public.t_device_repair
    DROP CONSTRAINT IF EXISTS t_device_repair_pk;
ALTER TABLE IF EXISTS ONLY public.t_device_relation
    DROP CONSTRAINT IF EXISTS t_device_relation_pk;
ALTER TABLE IF EXISTS ONLY public.t_device
    DROP CONSTRAINT IF EXISTS t_device_pk;
ALTER TABLE IF EXISTS ONLY public.sys_dept
    DROP CONSTRAINT IF EXISTS t_dept_pk;
ALTER TABLE IF EXISTS ONLY public.t_bus
    DROP CONSTRAINT IF EXISTS t_bus_pk;
DROP TABLE IF EXISTS public.t_station_schedule;
DROP TABLE IF EXISTS public.t_station_leave;
DROP TABLE IF EXISTS public.t_station_attendance;
DROP TABLE IF EXISTS public.t_station;
DROP TABLE IF EXISTS public.t_school;
DROP TABLE IF EXISTS public.t_route_record;
DROP TABLE IF EXISTS public.t_route;
DROP TABLE IF EXISTS public.t_device_repair;
DROP TABLE IF EXISTS public.t_device_relation;
DROP TABLE IF EXISTS public.t_device;
DROP TABLE IF EXISTS public.t_bus;
DROP TABLE IF EXISTS public.sys_user_role;
DROP TABLE IF EXISTS public.sys_user_config;
DROP TABLE IF EXISTS public.sys_user;
DROP TABLE IF EXISTS public.sys_role_menu;
DROP TABLE IF EXISTS public.sys_role;
DROP TABLE IF EXISTS public.sys_menu;
DROP TABLE IF EXISTS public.sys_login_log;
DROP TABLE IF EXISTS public.sys_log;
DROP TABLE IF EXISTS public.sys_job_log;
DROP TABLE IF EXISTS public.sys_job;
DROP TABLE IF EXISTS public.sys_dict;
DROP TABLE IF EXISTS public.sys_dept;
DROP TABLE IF EXISTS public.sys_config;
SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: sys_config; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sys_config
(
    config_id   bigint                      NOT NULL,
    key         character varying(50)       NOT NULL,
    value       character varying(50)       NOT NULL,
    description character varying(1000),
    create_time timestamp without time zone NOT NULL,
    create_by   bigint,
    modify_time timestamp without time zone,
    modify_by   bigint
);


ALTER TABLE public.sys_config
    OWNER TO postgres;

--
-- Name: sys_dept; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sys_dept
(
    dept_id     bigint                 NOT NULL,
    parent_id   bigint                 NOT NULL,
    dept_name   character varying(100) NOT NULL,
    order_num   double precision,
    create_time timestamp without time zone,
    modify_time timestamp without time zone
);


ALTER TABLE public.sys_dept
    OWNER TO postgres;

--
-- Name: TABLE sys_dept; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.sys_dept IS '组织机构';


--
-- Name: COLUMN sys_dept.dept_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sys_dept.dept_id IS '部门ID';


--
-- Name: COLUMN sys_dept.parent_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sys_dept.parent_id IS '上级部门ID';


--
-- Name: COLUMN sys_dept.dept_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sys_dept.dept_name IS '部门名称';


--
-- Name: COLUMN sys_dept.order_num; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sys_dept.order_num IS '排序';


--
-- Name: COLUMN sys_dept.create_time; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sys_dept.create_time IS '创建时间
';


--
-- Name: COLUMN sys_dept.modify_time; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sys_dept.modify_time IS '修改时间';


--
-- Name: sys_dict; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sys_dict
(
    dict_id    bigint NOT NULL,
    keyy       bigint,
    valuee     character varying(100),
    field_name character varying(100),
    table_name character varying(100)
);


ALTER TABLE public.sys_dict
    OWNER TO postgres;

--
-- Name: COLUMN sys_dict.dict_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sys_dict.dict_id IS '字典ID';


--
-- Name: COLUMN sys_dict.keyy; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sys_dict.keyy IS '键';


--
-- Name: COLUMN sys_dict.valuee; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sys_dict.valuee IS '值';


--
-- Name: COLUMN sys_dict.field_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sys_dict.field_name IS '字段名称';


--
-- Name: COLUMN sys_dict.table_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sys_dict.table_name IS '表名';


--
-- Name: sys_job; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sys_job
(
    job_id          bigint                 NOT NULL,
    bean_name       character varying(100) NOT NULL,
    method_name     character varying(100) NOT NULL,
    params          character varying(200) DEFAULT NULL::character varying,
    cron_expression character varying(100) NOT NULL,
    status          character(1)           NOT NULL,
    remark          character varying(200) DEFAULT NULL::character varying,
    create_time     timestamp without time zone
);


ALTER TABLE public.sys_job
    OWNER TO postgres;

--
-- Name: sys_job_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sys_job_log
(
    log_id      bigint                 NOT NULL,
    job_id      bigint                 NOT NULL,
    bean_name   character varying(100) NOT NULL,
    method_name character varying(100) NOT NULL,
    params      character varying(200) DEFAULT NULL::character varying,
    status      character(2)           NOT NULL,
    error       text,
    times       numeric(11, 0)         DEFAULT NULL::numeric,
    create_time timestamp without time zone
);


ALTER TABLE public.sys_job_log
    OWNER TO postgres;

--
-- Name: sys_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sys_log
(
    id          bigint NOT NULL,
    username    character varying(50) DEFAULT NULL::character varying,
    operation   text,
    "time"      numeric(11, 0)        DEFAULT NULL::numeric,
    method      text,
    params      text,
    ip          character varying(64) DEFAULT NULL::character varying,
    create_time timestamp without time zone,
    location    character varying(50) DEFAULT NULL::character varying
);


ALTER TABLE public.sys_log
    OWNER TO postgres;

--
-- Name: sys_login_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sys_login_log
(
    id         bigint                      NOT NULL,
    username   character varying(100)      NOT NULL,
    login_time timestamp without time zone NOT NULL,
    location   character varying(255) DEFAULT NULL::character varying,
    ip         character varying(100) DEFAULT NULL::character varying,
    login_way  character(1)
);


ALTER TABLE public.sys_login_log
    OWNER TO postgres;

--
-- Name: COLUMN sys_login_log.login_way; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sys_login_log.login_way IS '登录方式';


--
-- Name: sys_menu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sys_menu
(
    menu_id     bigint                      NOT NULL,
    parent_id   bigint                      NOT NULL,
    menu_name   character varying(50)       NOT NULL,
    path        character varying(255) DEFAULT NULL::character varying,
    component   character varying(255) DEFAULT NULL::character varying,
    perms       character varying(50)  DEFAULT NULL::character varying,
    icon        character varying(50)  DEFAULT NULL::character varying,
    type        character(2)                NOT NULL,
    order_num   double precision,
    create_time timestamp without time zone NOT NULL,
    modify_time timestamp without time zone
);


ALTER TABLE public.sys_menu
    OWNER TO postgres;

--
-- Name: sys_role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sys_role
(
    role_id     bigint                              NOT NULL,
    role_name   character varying(10)               NOT NULL,
    remark      character varying(100) DEFAULT NULL::character varying,
    create_time timestamp without time zone         NOT NULL,
    modify_time timestamp without time zone,
    sys_role    boolean                DEFAULT true NOT NULL
);


ALTER TABLE public.sys_role
    OWNER TO postgres;

--
-- Name: sys_role_menu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sys_role_menu
(
    role_id bigint NOT NULL,
    menu_id bigint NOT NULL
);


ALTER TABLE public.sys_role_menu
    OWNER TO postgres;

--
-- Name: sys_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sys_user
(
    user_id         bigint                           NOT NULL,
    username        character varying(50)            NOT NULL,
    password        character varying(128),
    dept_id         bigint,
    email           character varying(128) DEFAULT NULL::character varying,
    mobile          character varying(20)  DEFAULT NULL::character varying,
    status          character(1)                     NOT NULL,
    create_time     timestamp without time zone      NOT NULL,
    modify_time     timestamp without time zone,
    last_login_time timestamp without time zone,
    ssex            character(1)           DEFAULT NULL::bpchar,
    description     character varying(100) DEFAULT NULL::character varying,
    avatar          character varying(100) DEFAULT NULL::character varying,
    user_type       character(1)           DEFAULT 0 NOT NULL
);


ALTER TABLE public.sys_user
    OWNER TO postgres;

--
-- Name: COLUMN sys_user.user_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sys_user.user_type IS '0: 后台管理员 1: 前台注册用户';


--
-- Name: sys_user_config; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sys_user_config
(
    user_id      bigint NOT NULL,
    theme        character varying(10) DEFAULT NULL::character varying,
    layout       character varying(10) DEFAULT NULL::character varying,
    multi_page   character(1)          DEFAULT NULL::bpchar,
    fix_siderbar character(1)          DEFAULT NULL::bpchar,
    fix_header   character(1)          DEFAULT NULL::bpchar,
    color        character varying(20) DEFAULT NULL::character varying
);


ALTER TABLE public.sys_user_config
    OWNER TO postgres;

--
-- Name: sys_user_role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sys_user_role
(
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE public.sys_user_role
    OWNER TO postgres;

--
-- Name: t_bus; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_bus
(
    id           bigint                 NOT NULL,
    number_plate character varying(50)  NOT NULL,
    model        character varying(255) NOT NULL,
    brand        character varying(50),
    chassis      character varying(50),
    engine_model character varying(255),
    power        character varying(10),
    emission     character varying(10),
    length       character varying(50),
    width        character varying(50),
    height       character varying(50),
    seat         character varying(50),
    fuel_tank    character varying(50),
    dept_id      bigint,
--     service_org bigint,
    route_id     bigint,
    create_time  timestamp without time zone,
    create_by    bigint,
    modify_time  timestamp without time zone,
    modify_by    bigint,
    deleted      boolean                NOT NULL
);


ALTER TABLE public.t_bus
    OWNER TO postgres;

--
-- Name: TABLE t_bus; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.t_bus IS '车辆信息';


--
-- Name: COLUMN t_bus.number_plate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_bus.number_plate IS '车牌号';


--
-- Name: COLUMN t_bus.model; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_bus.model IS '车辆型号';


--
-- Name: COLUMN t_bus.brand; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_bus.brand IS '车辆厂商';


--
-- Name: COLUMN t_bus.chassis; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_bus.chassis IS '底盘';


--
-- Name: COLUMN t_bus.engine_model; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_bus.engine_model IS '发动机型号';


--
-- Name: COLUMN t_bus.power; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_bus.power IS '额定功率';


--
-- Name: COLUMN t_bus.emission; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_bus.emission IS '排放标准';


--
-- Name: COLUMN t_bus.length; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_bus.length IS '长(mm)';


--
-- Name: COLUMN t_bus.width; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_bus.width IS '宽(mm)';


--
-- Name: COLUMN t_bus.height; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_bus.height IS '高(mm)';


--
-- Name: COLUMN t_bus.seat; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_bus.seat IS '作为数量';


--
-- Name: COLUMN t_bus.fuel_tank; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_bus.fuel_tank IS '燃料箱';


--
-- Name: COLUMN t_bus.dept_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_bus.dept_id IS '所属部门';


--
-- Name: COLUMN t_bus.service_org; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_bus.service_org IS '服务机构';


--
-- Name: COLUMN t_bus.route_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_bus.route_id IS '所属路线';


--
-- Name: COLUMN t_bus.deleted; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_bus.deleted IS 'false';


--
-- Name: t_device; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_device
(
    id          bigint       NOT NULL,
    device_name character varying(100),
    device_code character varying(100),
    device_type character(1),
    create_by   bigint,
    create_time timestamp without time zone,
    update_by   bigint,
    update_time bigint,
    status      character(1) NOT NULL,
    deleted     boolean DEFAULT false
);


ALTER TABLE public.t_device
    OWNER TO postgres;

--
-- Name: TABLE t_device; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.t_device IS '设备信息表';


--
-- Name: COLUMN t_device.id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_device.id IS '主键';


--
-- Name: COLUMN t_device.device_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_device.device_name IS '设备名称';


--
-- Name: COLUMN t_device.device_code; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_device.device_code IS '设备标识码';


--
-- Name: COLUMN t_device.device_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_device.device_type IS '设备类型';


--
-- Name: COLUMN t_device.create_by; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_device.create_by IS '创建人';


--
-- Name: COLUMN t_device.create_time; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_device.create_time IS '创建时间';


--
-- Name: COLUMN t_device.update_by; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_device.modify_by IS '操作人';


--
-- Name: COLUMN t_device.update_time; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_device.modify_time IS '操作时间';


--
-- Name: COLUMN t_device.status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_device.device_status IS '设备状态(0:正常,1:维修中,2损坏)';


--
-- Name: COLUMN t_device.deleted; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_device.deleted IS '是否删除';


--
-- Name: t_device_relation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_device_relation
(
    id          bigint NOT NULL,
    device_id   bigint,
    student_id  bigint,
    create_by   bigint,
    create_time timestamp without time zone,
    update_by   bigint,
    update_time timestamp without time zone,
    deleted     boolean DEFAULT false
);


ALTER TABLE public.t_device_relation
    OWNER TO postgres;

--
-- Name: TABLE t_device_relation; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.t_device_relation IS '设备信息关系表';


--
-- Name: COLUMN t_device_relation.id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_device_relation.id IS '主键';


--
-- Name: COLUMN t_device_relation.device_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_device_relation.device_code IS '设备id';


--
-- Name: COLUMN t_device_relation.student_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_device_relation.student_id IS '学生id';


--
-- Name: COLUMN t_device_relation.create_by; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_device_relation.create_by IS '创建人';


--
-- Name: COLUMN t_device_relation.create_time; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_device_relation.create_time IS '创建时间';


--
-- Name: COLUMN t_device_relation.update_by; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_device_relation.modify_by IS '操作人';


--
-- Name: COLUMN t_device_relation.update_time; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_device_relation.modify_time IS '操作时间';


--
-- Name: COLUMN t_device_relation.deleted; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_device_relation.deleted IS '是否删除';


--
-- Name: t_device_repair; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_device_repair
(
    id          bigint NOT NULL,
    device_id   bigint,
    comment     character varying(200),
    create_time timestamp without time zone,
    update_by   bigint,
    update_time timestamp without time zone,
    create_by   bigint,
    status      character(1),
    deleted     boolean DEFAULT false
);


ALTER TABLE public.t_device_repair
    OWNER TO postgres;

--
-- Name: TABLE t_device_repair; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.t_device_repair IS '设备维修表';


--
-- Name: COLUMN t_device_repair.id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_device_repair.id IS '设备维修id';


--
-- Name: COLUMN t_device_repair.device_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_device_repair.device_id IS '设备id';


--
-- Name: COLUMN t_device_repair.comment; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_device_repair.comment IS '维修原因';


--
-- Name: COLUMN t_device_repair.create_time; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_device_repair.create_time IS '创建时间';


--
-- Name: COLUMN t_device_repair.update_by; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_device_repair.modify_by IS '操作人';


--
-- Name: COLUMN t_device_repair.update_time; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_device_repair.modify_time IS '操作时间';


--
-- Name: COLUMN t_device_repair.create_by; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_device_repair.create_by IS '创建人';


--
-- Name: COLUMN t_device_repair.status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_device_repair.status IS '维修状态';


--
-- Name: COLUMN t_device_repair.deleted; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_device_repair.deleted IS '是否删除';


--
-- Name: t_route; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_route
(
    id             bigint                 NOT NULL,
    route_name     character varying(255) NOT NULL,
    route_schedule character(1)           NOT NULL,
    create_time    timestamp without time zone,
    create_by      bigint,
    modify_time    timestamp without time zone,
    modify_by      bigint,
    deleted        boolean DEFAULT false  NOT NULL
);


ALTER TABLE public.t_route
    OWNER TO postgres;

--
-- Name: TABLE t_route; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.t_route IS '路线';


--
-- Name: COLUMN t_route.route_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_route.route_name IS '路线名称';


--
-- Name: t_route_record; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_route_record
(
    id             bigint                NOT NULL,
    route_id       bigint,
    start_time     timestamp without time zone,
    end_time       timestamp without time zone,
    notify_status  character(1),
    notify_error   text,
    bus_teacher_id bigint,
    deleted        boolean DEFAULT false NOT NULL
);


ALTER TABLE public.t_route_record
    OWNER TO postgres;

--
-- Name: TABLE t_route_record; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.t_route_record IS '路线运行记录';


--
-- Name: COLUMN t_route_record.route_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_route_record.route_id IS '路线ID ';


--
-- Name: COLUMN t_route_record.start_time; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_route_record.start_time IS '开始时间';


--
-- Name: COLUMN t_route_record.end_time; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_route_record.end_time IS '结束时间';


--
-- Name: COLUMN t_route_record.notify_status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_route_record.notify_status IS '通知状态(0:待通知，1:通知成功，2:通知失败)';


--
-- Name: COLUMN t_route_record.notify_error; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_route_record.notify_error IS '通知失败错误信息';


--
-- Name: COLUMN t_route_record.bus_teacher_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_route_record.bus_teacher_id IS '随车老师ID，操作人 ';


--
-- Name: t_school; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_school
(
    id          bigint                NOT NULL,
    school_name character varying(100),
    user_id     bigint,
    dept_id     bigint,
    create_time timestamp without time zone,
    create_by   bigint,
    modify_by   bigint,
    deleted     boolean DEFAULT false NOT NULL,
    modify_time timestamp without time zone
);


ALTER TABLE public.t_school
    OWNER TO postgres;

--
-- Name: TABLE t_school; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.t_school IS '服务机构';


--
-- Name: t_station; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_station
(
    id           bigint                 NOT NULL,
    station_name character varying(255) NOT NULL,
    longitude    character varying(100),
    latitude     character varying(100),
    create_time  timestamp without time zone,
    create_by    bigint,
    modify_time  timestamp without time zone,
    modify_by    bigint,
    deleted      boolean DEFAULT false  NOT NULL
);


ALTER TABLE public.t_station
    OWNER TO postgres;

--
-- Name: TABLE t_station; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.t_station IS '站点';


--
-- Name: COLUMN t_station.longitude; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_station.longitude IS '经度';


--
-- Name: COLUMN t_station.latitude; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_station.latitude IS '纬度';


--
-- Name: t_station_attendance; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_station_attendance
(
    id            bigint                NOT NULL,
    student_id    bigint                NOT NULL,
    start_time    timestamp without time zone,
    end_time      timestamp without time zone,
    start_station bigint,
    end_station   bigint,
    bus_id        bigint                NOT NULL,
    deleted       boolean DEFAULT false NOT NULL
);


ALTER TABLE public.t_station_attendance
    OWNER TO postgres;

--
-- Name: t_station_leave; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_station_leave
(
    id          bigint                NOT NULL,
    student_id  bigint                NOT NULL,
    reason      text,
    start_time  timestamp without time zone,
    end_time    timestamp without time zone,
    station_id  bigint,
    create_time timestamp without time zone,
    apply_id    bigint,
    deleted     boolean DEFAULT false NOT NULL
);


ALTER TABLE public.t_station_leave
    OWNER TO postgres;

--
-- Name: TABLE t_station_leave; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.t_station_leave IS '站点请假';


--
-- Name: COLUMN t_station_leave.apply_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_station_leave.apply_id IS '提交人ID';


--
-- Name: t_station_schedule; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_station_schedule
(
    id           bigint                NOT NULL,
    station_id   bigint,
    bus_id       bigint,
    arrival_time time without time zone,
    create_time  timestamp without time zone,
    create_by    bigint,
    modify_time  timestamp without time zone,
    modify_by    bigint,
    deleted      boolean DEFAULT false NOT NULL
);


ALTER TABLE public.t_station_schedule
    OWNER TO postgres;

--
-- Name: COLUMN t_station_schedule.station_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_station_schedule.station_id IS '站点ID';


--
-- Name: COLUMN t_station_schedule.bus_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_station_schedule.bus_id IS '校车ID';


--
-- Name: COLUMN t_station_schedule.arrival_time; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.t_station_schedule.arrival_time IS '预计到达时间';


--
-- Data for Name: sys_config; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.sys_config (config_id, key, value, description, create_time, create_by, modify_time, modify_by)
VALUES (1154594164293582849, 'cache-expire', '300', '验证码有效期时间(5分钟)', '2019-07-26 11:27:54.729', NULL, NULL, NULL);


--
-- Data for Name: sys_dept; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.sys_dept (dept_id, parent_id, dept_name, order_num, create_time, modify_time)
VALUES (1, 0, '开发部', 1, '2018-01-04 15:42:26', '2019-01-05 21:08:27');
INSERT INTO public.sys_dept (dept_id, parent_id, dept_name, order_num, create_time, modify_time)
VALUES (2, 1, '开发一部', 1, '2018-01-04 15:42:34', '2019-01-18 00:59:37');
INSERT INTO public.sys_dept (dept_id, parent_id, dept_name, order_num, create_time, modify_time)
VALUES (3, 1, '开发二部', 2, '2018-01-04 15:42:29', '2019-01-05 14:09:39');
INSERT INTO public.sys_dept (dept_id, parent_id, dept_name, order_num, create_time, modify_time)
VALUES (4, 0, '市场部', 2, '2018-01-04 15:42:36', '2019-01-23 06:27:56');
INSERT INTO public.sys_dept (dept_id, parent_id, dept_name, order_num, create_time, modify_time)
VALUES (5, 0, '人事部', 3, '2018-01-04 15:42:32', '2019-01-23 06:27:59');
INSERT INTO public.sys_dept (dept_id, parent_id, dept_name, order_num, create_time, modify_time)
VALUES (6, 0, '测试部', 4, '2018-01-04 15:42:38', '2019-01-17 08:15:47');


--
-- Data for Name: sys_dict; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.sys_dict (dict_id, keyy, valuee, field_name, table_name)
VALUES (1, 0, '男', 'ssex', 't_user');
INSERT INTO public.sys_dict (dict_id, keyy, valuee, field_name, table_name)
VALUES (2, 1, '女', 'ssex', 't_user');
INSERT INTO public.sys_dict (dict_id, keyy, valuee, field_name, table_name)
VALUES (3, 2, '保密', 'ssex', 't_user');
INSERT INTO public.sys_dict (dict_id, keyy, valuee, field_name, table_name)
VALUES (4, 1, '有效', 'status', 't_user');
INSERT INTO public.sys_dict (dict_id, keyy, valuee, field_name, table_name)
VALUES (5, 0, '锁定', 'status', 't_user');
INSERT INTO public.sys_dict (dict_id, keyy, valuee, field_name, table_name)
VALUES (6, 0, '菜单', 'type', 't_menu');
INSERT INTO public.sys_dict (dict_id, keyy, valuee, field_name, table_name)
VALUES (7, 1, '按钮', 'type', 't_menu');
INSERT INTO public.sys_dict (dict_id, keyy, valuee, field_name, table_name)
VALUES (30, 0, '正常', 'status', 't_job');
INSERT INTO public.sys_dict (dict_id, keyy, valuee, field_name, table_name)
VALUES (31, 1, '暂停', 'status', 't_job');
INSERT INTO public.sys_dict (dict_id, keyy, valuee, field_name, table_name)
VALUES (32, 0, '成功', 'status', 't_job_log');
INSERT INTO public.sys_dict (dict_id, keyy, valuee, field_name, table_name)
VALUES (33, 1, '失败', 'status', 't_job_log');


--
-- Data for Name: sys_job; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.sys_job (job_id, bean_name, method_name, params, cron_expression, status, remark, create_time)
VALUES (2, 'testTask', 'test1', NULL, '0/10 * * * * ?', '1', '无参任务调度测试', '2018-02-24 17:06:23');
INSERT INTO public.sys_job (job_id, bean_name, method_name, params, cron_expression, status, remark, create_time)
VALUES (1, 'testTask', 'test', 'mrbird', '0/1 * * * * ?1', '1', '有参任务调度测试', '2018-02-24 16:26:14');
INSERT INTO public.sys_job (job_id, bean_name, method_name, params, cron_expression, status, remark, create_time)
VALUES (11, 'testTask', 'test2', NULL, '0/5 * * * * ?', '1', '测试异常', '2018-02-26 11:15:30');
INSERT INTO public.sys_job (job_id, bean_name, method_name, params, cron_expression, status, remark, create_time)
VALUES (3, 'testTask', 'test', 'hello world', '0/1 * * * * ?', '1', '有参任务调度测试,每隔一秒触发', '2018-02-26 09:28:26');


--
-- Data for Name: sys_job_log; Type: TABLE DATA; Schema: public; Owner: postgres
--


--
-- Data for Name: sys_log; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.sys_log (id, username, operation, "time", method, params, ip, create_time, location)
VALUES (1154331727677972481, 'admin', '新增角色', 23, 'com.phlink.bus.api.system.controller.RoleController.addRole()',
        ' role: "Role(roleId=1154331727539560449, roleName=老师, remark=学校老师, createTime=Thu Jul 25 18:05:04 CST 2019, modifyTime=null, createTimeFrom=null, createTimeTo=null, menuId=1,3)"',
        '127.0.0.1', '2019-07-25 18:05:04.979', '0|0|0|内网IP|内网IP');
INSERT INTO public.sys_log (id, username, operation, "time", method, params, ip, create_time, location)
VALUES (1154331901200523265, 'admin', '新增角色', 14, 'com.phlink.bus.api.system.controller.RoleController.addRole()',
        ' role: "Role(roleId=1154331901133414401, roleName=家长, remark=学校老师, createTime=Thu Jul 25 18:05:46 CST 2019, modifyTime=null, createTimeFrom=null, createTimeTo=null, menuId=1)"',
        '127.0.0.1', '2019-07-25 18:05:46.351', '0|0|0|内网IP|内网IP');
INSERT INTO public.sys_log (id, username, operation, "time", method, params, ip, create_time, location)
VALUES (1154331996990038017, 'admin', '修改角色', 36, 'com.phlink.bus.api.system.controller.RoleController.updateRole()',
        ' role: "Role(roleId=1154331901133414401, roleName=家长, remark=家长, createTime=null, modifyTime=Thu Jul 25 18:06:09 CST 2019, createTimeFrom=null, createTimeTo=null, menuId=1)"',
        '127.0.0.1', '2019-07-25 18:06:09.189', '0|0|0|内网IP|内网IP');
INSERT INTO public.sys_log (id, username, operation, "time", method, params, ip, create_time, location)
VALUES (1154332191718989825, 'admin', '删除角色', 45, 'com.phlink.bus.api.system.controller.RoleController.deleteRoles()',
        ' roleIds: "72"', '127.0.0.1', '2019-07-25 18:06:55.617', '0|0|0|内网IP|内网IP');
INSERT INTO public.sys_log (id, username, operation, "time", method, params, ip, create_time, location)
VALUES (1154332319204859905, 'admin', '新增角色', 15, 'com.phlink.bus.api.system.controller.RoleController.addRole()',
        ' role: "Role(roleId=1154332319100002306, roleName=游客, remark=没经过后台登记的用户, createTime=Thu Jul 25 18:07:25 CST 2019, modifyTime=null, createTimeFrom=null, createTimeTo=null, menuId=1)"',
        '127.0.0.1', '2019-07-25 18:07:26.011', '0|0|0|内网IP|内网IP');
INSERT INTO public.sys_log (id, username, operation, "time", method, params, ip, create_time, location)
VALUES (1154332487723606017, 'admin', '删除角色', 18, 'com.phlink.bus.api.system.controller.RoleController.deleteRoles()',
        ' roleIds: "1153625717250297858"', '127.0.0.1', '2019-07-25 18:08:06.188', '0|0|0|内网IP|内网IP');
INSERT INTO public.sys_log (id, username, operation, "time", method, params, ip, create_time, location)
VALUES (1154332741856485377, 'admin', '删除角色', 34, 'com.phlink.bus.api.system.controller.RoleController.deleteRoles()',
        ' roleIds: "2"', '127.0.0.1', '2019-07-25 18:09:06.779', '0|0|0|内网IP|内网IP');
INSERT INTO public.sys_log (id, username, operation, "time", method, params, ip, create_time, location)
VALUES (1154332847833964546, 'admin', '新增角色', 11, 'com.phlink.bus.api.system.controller.RoleController.addRole()',
        ' role: "Role(roleId=1154332847783632898, roleName=系统管理员, remark=系统配置，系统监控, createTime=Thu Jul 25 18:09:32 CST 2019, modifyTime=null, createTimeFrom=null, createTimeTo=null, menuId=1)"',
        '127.0.0.1', '2019-07-25 18:09:32.047', '0|0|0|内网IP|内网IP');
INSERT INTO public.sys_log (id, username, operation, "time", method, params, ip, create_time, location)
VALUES (1154333131700264962, 'admin', '新增角色', 10, 'com.phlink.bus.api.system.controller.RoleController.addRole()',
        ' role: "Role(roleId=1154333131649933314, roleName=运营, remark=普通运营人员, createTime=Thu Jul 25 18:10:39 CST 2019, modifyTime=null, createTimeFrom=null, createTimeTo=null, menuId=58)"',
        '127.0.0.1', '2019-07-25 18:10:39.725', '0|0|0|内网IP|内网IP');
INSERT INTO public.sys_log (id, username, operation, "time", method, params, ip, create_time, location)
VALUES (1154337040032423937, 'admin', '新增角色', 26, 'com.phlink.bus.api.system.controller.RoleController.addRole()',
        ' role: "Role(roleId=1154337039831097345, roleName=哈哈哈角色, remark=, createTime=Thu Jul 25 18:26:11 CST 2019, modifyTime=null, sysRole=false, createTimeFrom=null, createTimeTo=null, menuId=1)"',
        '127.0.0.1', '2019-07-25 18:26:11.536', '0|0|0|内网IP|内网IP');
INSERT INTO public.sys_log (id, username, operation, "time", method, params, ip, create_time, location)
VALUES (1154337090141773825, 'admin', '删除角色', 51, 'com.phlink.bus.api.system.controller.RoleController.deleteRoles()',
        ' roleIds: "1154337039831097345"', '127.0.0.1', '2019-07-25 18:26:23.492', '0|0|0|内网IP|内网IP');


--
-- Data for Name: sys_login_log; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.sys_login_log (id, username, login_time, location, ip, login_way)
VALUES (1154331302627205121, 'admin', '2019-07-25 18:03:23.64', '0|0|0|内网IP|内网IP', '127.0.0.1', '0');
INSERT INTO public.sys_login_log (id, username, login_time, location, ip, login_way)
VALUES (1154606871637983233, 'admin', '2019-07-26 12:18:24.384', '0|0|0|内网IP|内网IP', '127.0.0.1', '0');
INSERT INTO public.sys_login_log (id, username, login_time, location, ip, login_way)
VALUES (1154607016333082625, 'admin', '2019-07-26 12:18:58.908', '0|0|0|内网IP|内网IP', '127.0.0.1', '0');
INSERT INTO public.sys_login_log (id, username, login_time, location, ip, login_way)
VALUES (1154607043369566210, 'admin', '2019-07-26 12:19:05.357', '0|0|0|内网IP|内网IP', '127.0.0.1', '0');
INSERT INTO public.sys_login_log (id, username, login_time, location, ip, login_way)
VALUES (1154607944519987201, '18600753024', '2019-07-26 12:22:40.201', '0|0|0|内网IP|内网IP', '127.0.0.1', '1');
INSERT INTO public.sys_login_log (id, username, login_time, location, ip, login_way)
VALUES (1154635277423419394, '18600753024', '2019-07-26 14:11:16.853', '0|0|0|内网IP|内网IP', '127.0.0.1', '1');
INSERT INTO public.sys_login_log (id, username, login_time, location, ip, login_way)
VALUES (1154635790361620482, '18600753024', '2019-07-26 14:13:19.152', '0|0|0|内网IP|内网IP', '127.0.0.1', '1');
INSERT INTO public.sys_login_log (id, username, login_time, location, ip, login_way)
VALUES (1154647309254152193, 'admin', '2019-07-26 14:59:05.467', '0|0|0|内网IP|内网IP', '127.0.0.1', '0');
INSERT INTO public.sys_login_log (id, username, login_time, location, ip, login_way)
VALUES (1154676711157481474, 'admin', '2019-07-26 16:55:55.438', '0|0|0|内网IP|内网IP', '127.0.0.1', '0');
INSERT INTO public.sys_login_log (id, username, login_time, location, ip, login_way)
VALUES (1155036438148227074, 'admin', '2019-07-27 16:45:21.023', '0|0|0|内网IP|内网IP', '127.0.0.1', '0');
INSERT INTO public.sys_login_log (id, username, login_time, location, ip, login_way)
VALUES (1155036971655307266, 'admin', '2019-07-27 16:47:28.243', '0|0|0|内网IP|内网IP', '127.0.0.1', '0');
INSERT INTO public.sys_login_log (id, username, login_time, location, ip, login_way)
VALUES (1155044306533380098, 'admin', '2019-07-27 17:16:36.986', '0|0|0|内网IP|内网IP', '127.0.0.1', '0');
INSERT INTO public.sys_login_log (id, username, login_time, location, ip, login_way)
VALUES (1155048566868295682, 'admin', '2019-07-27 17:33:32.752', '0|0|0|内网IP|内网IP', '127.0.0.1', '0');
INSERT INTO public.sys_login_log (id, username, login_time, location, ip, login_way)
VALUES (1155048653702971394, 'admin', '2019-07-27 17:33:53.465', '0|0|0|内网IP|内网IP', '127.0.0.1', '0');
INSERT INTO public.sys_login_log (id, username, login_time, location, ip, login_way)
VALUES (1155052507182022658, 'admin', '2019-07-27 17:49:12.206', '0|0|0|内网IP|内网IP', '127.0.0.1', '0');
INSERT INTO public.sys_login_log (id, username, login_time, location, ip, login_way)
VALUES (1155055972323045378, 'admin', '2019-07-27 18:02:58.346', '0|0|0|内网IP|内网IP', '127.0.0.1', '0');
INSERT INTO public.sys_login_log (id, username, login_time, location, ip, login_way)
VALUES (1155065388464218113, 'admin', '2019-07-27 18:40:23.31', '0|0|0|内网IP|内网IP', '127.0.0.1', '0');


--
-- Data for Name: sys_menu; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (1, 0, '系统管理', '/system', 'PageView', NULL, 'appstore-o', '0 ', 1, '2017-12-27 16:39:07', '2019-01-05 11:13:14');
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (2, 0, '系统监控', '/monitor', 'PageView', NULL, 'dashboard', '0 ', 2, '2017-12-27 16:45:51', '2019-01-23 06:27:12');
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (3, 1, '用户管理', '/system/user', 'system/user/User', 'user:view', '', '0 ', 1, '2017-12-27 16:47:13',
        '2019-01-22 06:45:55');
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (4, 1, '角色管理', '/system/role', 'system/role/Role', 'role:view', '', '0 ', 2, '2017-12-27 16:48:09',
        '2018-04-25 09:01:12');
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (5, 1, '菜单管理', '/system/menu', 'system/menu/Menu', 'menu:view', '', '0 ', 3, '2017-12-27 16:48:57',
        '2018-04-25 09:01:30');
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (6, 1, '部门管理', '/system/dept', 'system/dept/Dept', 'dept:view', '', '0 ', 4, '2017-12-27 16:57:33',
        '2018-04-25 09:01:40');
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (8, 2, '在线用户', '/monitor/online', 'monitor/Online', 'user:online', '', '0 ', 1, '2017-12-27 16:59:33',
        '2018-04-25 09:02:04');
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (10, 2, '系统日志', '/monitor/systemlog', 'monitor/SystemLog', 'log:view', '', '0 ', 2, '2017-12-27 17:00:50',
        '2018-04-25 09:02:18');
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (11, 3, '新增用户', '', '', 'user:add', NULL, '1 ', NULL, '2017-12-27 17:02:58', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (12, 3, '修改用户', '', '', 'user:update', NULL, '1 ', NULL, '2017-12-27 17:04:07', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (13, 3, '删除用户', '', '', 'user:delete', NULL, '1 ', NULL, '2017-12-27 17:04:58', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (14, 4, '新增角色', '', '', 'role:add', NULL, '1 ', NULL, '2017-12-27 17:06:38', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (15, 4, '修改角色', '', '', 'role:update', NULL, '1 ', NULL, '2017-12-27 17:06:38', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (16, 4, '删除角色', '', '', 'role:delete', NULL, '1 ', NULL, '2017-12-27 17:06:38', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (17, 5, '新增菜单', '', '', 'menu:add', NULL, '1 ', NULL, '2017-12-27 17:08:02', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (18, 5, '修改菜单', '', '', 'menu:update', NULL, '1 ', NULL, '2017-12-27 17:08:02', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (19, 5, '删除菜单', '', '', 'menu:delete', NULL, '1 ', NULL, '2017-12-27 17:08:02', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (20, 6, '新增部门', '', '', 'dept:add', NULL, '1 ', NULL, '2017-12-27 17:09:24', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (21, 6, '修改部门', '', '', 'dept:update', NULL, '1 ', NULL, '2017-12-27 17:09:24', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (22, 6, '删除部门', '', '', 'dept:delete', NULL, '1 ', NULL, '2017-12-27 17:09:24', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (23, 8, '踢出用户', '', '', 'user:kickout', NULL, '1 ', NULL, '2017-12-27 17:11:13', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (24, 10, '删除日志', '', '', 'log:delete', NULL, '1 ', NULL, '2017-12-27 17:11:45', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (58, 0, '网络资源', '/web', 'PageView', NULL, 'compass', '0 ', 4, '2018-01-12 15:28:48', '2018-01-22 19:49:26');
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (59, 58, '天气查询', '/web/weather', 'web/Weather', 'weather:view', '', '0 ', 1, '2018-01-12 15:40:02',
        '2019-01-22 05:43:19');
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (61, 58, '每日一文', '/web/dailyArticle', 'web/DailyArticle', 'article:view', '', '0 ', 2, '2018-01-15 17:17:14',
        '2019-01-22 05:43:27');
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (64, 1, '字典管理', '/system/dict', 'system/dict/Dict', 'dict:view', '', '0 ', 5, '2018-01-18 10:38:25',
        '2018-04-25 09:01:50');
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (65, 64, '新增字典', '', '', 'dict:add', NULL, '1 ', NULL, '2018-01-18 19:10:08', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (66, 64, '修改字典', '', '', 'dict:update', NULL, '1 ', NULL, '2018-01-18 19:10:27', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (67, 64, '删除字典', '', '', 'dict:delete', NULL, '1 ', NULL, '2018-01-18 19:10:47', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (81, 58, '影视资讯', '/web/movie', 'EmptyPageView', NULL, NULL, '0 ', 3, '2018-01-22 14:12:59',
        '2019-01-22 05:43:35');
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (82, 81, '正在热映', '/web/movie/hot', 'web/MovieHot', 'movie:hot', '', '0 ', 1, '2018-01-22 14:13:47',
        '2019-01-22 05:43:52');
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (83, 81, '即将上映', '/web/movie/coming', 'web/MovieComing', 'movie:coming', '', '0 ', 2, '2018-01-22 14:14:36',
        '2019-01-22 05:43:58');
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (101, 0, '任务调度', '/job', 'PageView', NULL, 'clock-circle-o', '0 ', 3, '2018-01-11 15:52:57', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (102, 101, '定时任务', '/job/job', 'quartz/job/Job', 'job:view', '', '0 ', 1, '2018-02-24 15:53:53',
        '2019-01-22 05:42:50');
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (103, 102, '新增任务', '', '', 'job:add', NULL, '1 ', NULL, '2018-02-24 15:55:10', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (104, 102, '修改任务', '', '', 'job:update', NULL, '1 ', NULL, '2018-02-24 15:55:53', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (105, 102, '删除任务', '', '', 'job:delete', NULL, '1 ', NULL, '2018-02-24 15:56:18', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (106, 102, '暂停任务', '', '', 'job:pause', NULL, '1 ', NULL, '2018-02-24 15:57:08', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (107, 102, '恢复任务', '', '', 'job:resume', NULL, '1 ', NULL, '2018-02-24 15:58:21', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (108, 102, '立即执行任务', '', '', 'job:run', NULL, '1 ', NULL, '2018-02-24 15:59:45', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (109, 101, '调度日志', '/job/log', 'quartz/log/JobLog', 'jobLog:view', '', '0 ', 2, '2018-02-24 16:00:45',
        '2019-01-22 05:42:59');
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (110, 109, '删除日志', '', '', 'jobLog:delete', NULL, '1 ', NULL, '2018-02-24 16:01:21', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (113, 2, 'Redis监控', '/monitor/redis/info', 'monitor/RedisInfo', 'redis:view', '', '0 ', 3, '2018-06-28 14:29:42',
        NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (121, 2, '请求追踪', '/monitor/httptrace', 'monitor/Httptrace', NULL, NULL, '0 ', 4, '2019-01-18 02:30:29', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (122, 2, '系统信息', '/monitor/system', 'EmptyPageView', NULL, NULL, '0 ', 5, '2019-01-18 02:31:48',
        '2019-01-18 02:39:46');
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (123, 122, 'Tomcat信息', '/monitor/system/tomcatinfo', 'monitor/TomcatInfo', NULL, NULL, '0 ', 2,
        '2019-01-18 02:32:53', '2019-01-18 02:46:57');
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (124, 122, 'JVM信息', '/monitor/system/jvminfo', 'monitor/JvmInfo', NULL, NULL, '0 ', 1, '2019-01-18 02:33:30',
        '2019-01-18 02:46:51');
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (127, 122, '服务器信息', '/monitor/system/info', 'monitor/SystemInfo', NULL, NULL, '0 ', 3, '2019-01-21 07:53:43',
        '2019-01-21 07:57:00');
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (130, 3, '导出Excel', NULL, NULL, 'user:export', NULL, '1 ', NULL, '2019-01-23 06:35:16', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (131, 4, '导出Excel', NULL, NULL, 'role:export', NULL, '1 ', NULL, '2019-01-23 06:35:36', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (132, 5, '导出Excel', NULL, NULL, 'menu:export', NULL, '1 ', NULL, '2019-01-23 06:36:05', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (133, 6, '导出Excel', NULL, NULL, 'dept:export', NULL, '1 ', NULL, '2019-01-23 06:36:25', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (134, 64, '导出Excel', NULL, NULL, 'dict:export', NULL, '1 ', NULL, '2019-01-23 06:36:43', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (135, 3, '密码重置', NULL, NULL, 'user:reset', NULL, '1 ', NULL, '2019-01-23 06:37:00', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (136, 10, '导出Excel', NULL, NULL, 'log:export', NULL, '1 ', NULL, '2019-01-23 06:37:27', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (137, 102, '导出Excel', NULL, NULL, 'job:export', NULL, '1 ', NULL, '2019-01-23 06:37:59', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (138, 109, '导出Excel', NULL, NULL, 'jobLog:export', NULL, '1 ', NULL, '2019-01-23 06:38:32', NULL);
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (139, 0, '设备管理', NULL, '', 'device:view', NULL, '0 ', NULL, '2019-07-27 09:45:19.227', '2019-07-27 09:45:33.04');
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (140, 139, '新增设备', NULL, NULL, 'device:add', NULL, '1 ', NULL, '2019-07-27 09:47:07.373',
        '2019-07-27 09:48:19.767');
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (141, 139, '修改设备', NULL, NULL, 'device:update', NULL, '1 ', NULL, '2019-07-27 09:47:40.372',
        '2019-07-27 09:48:23.251');
INSERT INTO public.sys_menu (menu_id, parent_id, menu_name, path, component, perms, icon, type, order_num, create_time,
                             modify_time)
VALUES (142, 139, '删除设备', NULL, NULL, 'device:delete', NULL, '1 ', NULL, '2019-07-27 09:48:15.034',
        '2019-07-27 09:48:28.667');


--
-- Data for Name: sys_role; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.sys_role (role_id, role_name, remark, create_time, modify_time, sys_role)
VALUES (1, '超级管理员', '全部权限', '2017-12-27 16:23:11', '2019-07-23 14:56:57.824', true);
INSERT INTO public.sys_role (role_id, role_name, remark, create_time, modify_time, sys_role)
VALUES (2, '系统管理员', '系统配置，系统监控', '2019-07-25 18:09:32.035', NULL, true);
INSERT INTO public.sys_role (role_id, role_name, remark, create_time, modify_time, sys_role)
VALUES (3, '运营', '普通运营人员', '2019-07-25 18:10:39.714', NULL, true);
INSERT INTO public.sys_role (role_id, role_name, remark, create_time, modify_time, sys_role)
VALUES (4, '车队队长', '管理车队', '2019-07-27 08:47:05.802', NULL, true);
INSERT INTO public.sys_role (role_id, role_name, remark, create_time, modify_time, sys_role)
VALUES (5, '司机', '管理自己车辆', '2019-07-27 08:46:33.821', NULL, true);
INSERT INTO public.sys_role (role_id, role_name, remark, create_time, modify_time, sys_role)
VALUES (6, '随车老师', '随车老师', '2019-07-27 08:48:09.301', NULL, true);
INSERT INTO public.sys_role (role_id, role_name, remark, create_time, modify_time, sys_role)
VALUES (10, '游客', '没经过后台登记的用户', '2019-07-25 18:07:25.988', NULL, true);
INSERT INTO public.sys_role (role_id, role_name, remark, create_time, modify_time, sys_role)
VALUES (11, '监护人', '学生家长', '2019-07-25 18:05:46.335', '2019-07-25 18:06:09.173', true);
INSERT INTO public.sys_role (role_id, role_name, remark, create_time, modify_time, sys_role)
VALUES (12, '老师', '学校老师', '2019-07-25 18:05:04.946', NULL, true);


--
-- Data for Name: sys_role_menu; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 1);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 3);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 11);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 12);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 13);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 4);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 14);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 15);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 16);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 5);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 17);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 18);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 19);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 6);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 20);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 21);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 22);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 64);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 65);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 66);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 67);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 2);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 8);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 23);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 10);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 24);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 113);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 121);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 122);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 124);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 123);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 125);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 101);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 102);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 103);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 104);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 105);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 106);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 107);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 108);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 109);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 110);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 127);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 128);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 129);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 130);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 135);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 131);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 132);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 133);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 134);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 136);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 137);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 138);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 58);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 59);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 61);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1, 81);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1154331727539560449, 1);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1154332847783632898, 1);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1154333131649933314, 58);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1154331727539560449, 3);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1154331901133414401, 1);
INSERT INTO public.sys_role_menu (role_id, menu_id)
VALUES (1154332319100002306, 1);


--
-- Data for Name: sys_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.sys_user (user_id, username, password, dept_id, email, mobile, status, create_time, modify_time,
                             last_login_time, ssex, description, avatar, user_type)
VALUES (1153513774531227649, 'admin_test', 'a5ec13374ffcec929c636c991ff5ee18', 6, NULL, NULL, '0',
        '2019-07-23 11:54:49.755', NULL, NULL, '0', NULL, 'default.jpg', '0');
INSERT INTO public.sys_user (user_id, username, password, dept_id, email, mobile, status, create_time, modify_time,
                             last_login_time, ssex, description, avatar, user_type)
VALUES (1154607944272523265, '18600753024', NULL, NULL, NULL, '18600753024', '1', '2019-07-26 12:22:40.14', NULL,
        '2019-07-26 14:13:19.107', '2', '游客', 'default.jpg', '0');
INSERT INTO public.sys_user (user_id, username, password, dept_id, email, mobile, status, create_time, modify_time,
                             last_login_time, ssex, description, avatar, user_type)
VALUES (1153513534776422401, 'admin', '3ee4a28b103216fa2d140d1979297910', NULL, 'admin@bus.com', NULL, '1',
        '2019-07-23 11:53:52.586', NULL, '2019-07-27 18:40:23.15', '0', NULL, 'default.jpg', '0');


--
-- Data for Name: sys_user_config; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.sys_user_config (user_id, theme, layout, multi_page, fix_siderbar, fix_header, color)
VALUES (1153513534776422401, 'dark', 'side', '0', '1', '1', 'rgb(66, 185, 131)');
INSERT INTO public.sys_user_config (user_id, theme, layout, multi_page, fix_siderbar, fix_header, color)
VALUES (1153513774531227649, 'dark', 'side', '0', '1', '1', 'rgb(66, 185, 131)');
INSERT INTO public.sys_user_config (user_id, theme, layout, multi_page, fix_siderbar, fix_header, color)
VALUES (1154607944272523265, 'dark', 'side', '0', '1', '1', 'rgb(66, 185, 131)');


--
-- Data for Name: sys_user_role; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.sys_user_role (user_id, role_id)
VALUES (1153513534776422401, 1);
INSERT INTO public.sys_user_role (user_id, role_id)
VALUES (1153513534776422400, 1);
INSERT INTO public.sys_user_role (user_id, role_id)
VALUES (1153509444398882800, 1);
INSERT INTO public.sys_user_role (user_id, role_id)
VALUES (1154607944272523265, 4);


--
-- Data for Name: t_bus; Type: TABLE DATA; Schema: public; Owner: postgres
--


--
-- Data for Name: t_device; Type: TABLE DATA; Schema: public; Owner: postgres
--


--
-- Data for Name: t_device_relation; Type: TABLE DATA; Schema: public; Owner: postgres
--


--
-- Data for Name: t_device_repair; Type: TABLE DATA; Schema: public; Owner: postgres
--


--
-- Data for Name: t_route; Type: TABLE DATA; Schema: public; Owner: postgres
--


--
-- Data for Name: t_route_record; Type: TABLE DATA; Schema: public; Owner: postgres
--


--
-- Data for Name: t_school; Type: TABLE DATA; Schema: public; Owner: postgres
--


--
-- Data for Name: t_station; Type: TABLE DATA; Schema: public; Owner: postgres
--


--
-- Data for Name: t_station_attendance; Type: TABLE DATA; Schema: public; Owner: postgres
--


--
-- Data for Name: t_station_leave; Type: TABLE DATA; Schema: public; Owner: postgres
--


--
-- Data for Name: t_station_schedule; Type: TABLE DATA; Schema: public; Owner: postgres
--


--
-- Name: t_bus t_bus_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_bus
    ADD CONSTRAINT t_bus_pk PRIMARY KEY (id);


--
-- Name: sys_dept t_dept_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sys_dept
    ADD CONSTRAINT t_dept_pk PRIMARY KEY (dept_id);


--
-- Name: t_device t_device_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_device
    ADD CONSTRAINT t_device_pk PRIMARY KEY (id);


--
-- Name: t_device_relation t_device_relation_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_device_relation
    ADD CONSTRAINT t_device_relation_pk PRIMARY KEY (id);


--
-- Name: t_device_repair t_device_repair_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_device_repair
    ADD CONSTRAINT t_device_repair_pk PRIMARY KEY (id);


--
-- Name: sys_dict t_dict_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sys_dict
    ADD CONSTRAINT t_dict_pk PRIMARY KEY (dict_id);


--
-- Name: sys_job_log t_job_log_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sys_job_log
    ADD CONSTRAINT t_job_log_pk PRIMARY KEY (log_id);


--
-- Name: sys_job t_job_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sys_job
    ADD CONSTRAINT t_job_pk PRIMARY KEY (job_id);


--
-- Name: sys_log t_log_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sys_log
    ADD CONSTRAINT t_log_pk PRIMARY KEY (id);


--
-- Name: sys_login_log t_login_log_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sys_login_log
    ADD CONSTRAINT t_login_log_pk PRIMARY KEY (id);


--
-- Name: sys_menu t_menu_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sys_menu
    ADD CONSTRAINT t_menu_pk PRIMARY KEY (menu_id);


--
-- Name: sys_role t_role_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sys_role
    ADD CONSTRAINT t_role_pk PRIMARY KEY (role_id);


--
-- Name: t_route t_route_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_route
    ADD CONSTRAINT t_route_pk PRIMARY KEY (id);


--
-- Name: t_route_record t_route_record_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_route_record
    ADD CONSTRAINT t_route_record_pk PRIMARY KEY (id);


--
-- Name: t_school t_school_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_school
    ADD CONSTRAINT t_school_pk PRIMARY KEY (id);


--
-- Name: t_station_attendance t_station_attendance_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_station_attendance
    ADD CONSTRAINT t_station_attendance_pk PRIMARY KEY (id);


--
-- Name: t_station t_station_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_station
    ADD CONSTRAINT t_station_pk PRIMARY KEY (id);


--
-- Name: t_station_schedule t_station_schedule_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_station_schedule
    ADD CONSTRAINT t_station_schedule_pk PRIMARY KEY (id);


--
-- Name: sys_config t_system_config_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sys_config
    ADD CONSTRAINT t_system_config_pk PRIMARY KEY (config_id);


--
-- Name: sys_user t_user_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sys_user
    ADD CONSTRAINT t_user_pk PRIMARY KEY (user_id);


--
-- Name: t_bus_id_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX t_bus_id_uindex ON public.t_bus USING btree (id);


--
-- Name: t_dept_id_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX t_dept_id_uindex ON public.sys_dept USING btree (dept_id);


--
-- Name: t_dict_id_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX t_dict_id_uindex ON public.sys_dict USING btree (dict_id);


--
-- Name: t_job_job_id_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX t_job_job_id_uindex ON public.sys_job USING btree (job_id);


--
-- Name: t_job_log_log_id_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX t_job_log_log_id_uindex ON public.sys_job_log USING btree (log_id);


--
-- Name: t_log_id_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX t_log_id_uindex ON public.sys_log USING btree (id);


--
-- Name: t_login_log_id_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX t_login_log_id_uindex ON public.sys_login_log USING btree (id);


--
-- Name: t_menu_menu_id_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX t_menu_menu_id_uindex ON public.sys_menu USING btree (menu_id);


--
-- Name: t_role_role_id_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX t_role_role_id_uindex ON public.sys_role USING btree (role_id);


--
-- Name: t_route_id_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX t_route_id_uindex ON public.t_route USING btree (id);


--
-- Name: t_route_record_id_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX t_route_record_id_uindex ON public.t_route_record USING btree (id);


--
-- Name: t_school_school_id_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX t_school_school_id_uindex ON public.t_school USING btree (id);


--
-- Name: t_station_attendance_id_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX t_station_attendance_id_uindex ON public.t_station_attendance USING btree (id);


--
-- Name: t_station_id_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX t_station_id_uindex ON public.t_station USING btree (id);


--
-- Name: t_station_leave_id_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX t_station_leave_id_uindex ON public.t_station_leave USING btree (id);


--
-- Name: t_station_schedule_id_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX t_station_schedule_id_uindex ON public.t_station_schedule USING btree (id);


--
-- Name: t_system_config_config_id_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX t_system_config_config_id_uindex ON public.sys_config USING btree (config_id);


--
-- Name: t_user_user_id_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX t_user_user_id_uindex ON public.sys_user USING btree (user_id);


--
-- PostgreSQL database dump complete
--

