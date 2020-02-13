# API服务

## 视图

```sql
-- 车辆和dvr设备摄像头的对应关系
drop view if exists v_dvr_bus_location_info;
create view v_dvr_bus_location_info as
select t.*, camera.guardian_open, camera.location_desc, bus.bus_code, bus.vin_code, bus.number_plate, bus.brand, dvr.channel_number, dvr.dvr_server_id
from t_dvr_camera_config as t
  left join t_camera_location as camera on t.location_code = camera.location_code
  left join t_dvr as dvr on t.dvr_code = dvr.dvr_code
  left join t_bus as bus on bus.id = dvr.bus_id
where t.deleted = false;

-- 绑定车辆详细信息
drop view if exists v_bindbus_detail_info;
create view v_bindbus_detail_info as
select b.*,
       d.dvr_code,
       d.channel_number,
       d.dvr_server_id,
       d.online,
       ro.route_id,
       r.school_id,
       r.route_name,
       r.route_type,
       ro.id as route_operation_id,
       ro.bind_driver_id,
       u1.realname driver_name,
       u1.mobile driver_mobile,
       ro.bind_bus_teacher_id,
       u2.realname bus_teacher_name,
       u2.mobile bus_teacher_mobile
from t_bus b
         right join t_route_operation ro on b.id = ro.bind_bus_id and ro.deleted = false
         left join t_dvr d on d.bus_id = b.id and d.deleted = false
         left join t_route r on r.id = ro.route_id and r.deleted = false
         left join sys_user u1 on u1.user_id = ro.bind_driver_id
         left join sys_user u2 on u2.user_id = ro.bind_bus_teacher_id
where b.deleted = false;

-- 车辆的详细信息
drop view if exists v_bus_detail_info;
create view v_bus_detail_info as
select b.*,
       d.dvr_code,
       d.channel_number,
       d.dvr_server_id,
       d.online,
       ro.route_id,
       r.school_id,
       r.route_name,
       r.route_type,
       ro.id as route_operation_id,
       ro.bind_driver_id,
       u1.realname driver_name,
       u1.mobile driver_mobile,
       ro.bind_bus_teacher_id,
       u2.realname bus_teacher_name,
       u2.mobile bus_teacher_mobile
from t_bus b
         left join t_route_operation ro on b.id = ro.bind_bus_id and ro.deleted = false
         left join t_dvr d on d.bus_id = b.id and d.deleted = false
         left join t_route r on r.id = ro.route_id and r.deleted = false
         left join sys_user u1 on u1.user_id = ro.bind_driver_id
         left join sys_user u2 on u2.user_id = ro.bind_bus_teacher_id
where b.deleted = false;


-- 设备关联的详情 
drop view if exists v_device_detailinfo;
create view v_device_detailinfo as
select t.*, s.name, s.main_guardian_id, s.id as student_id, sc.id as school_id, sc.school_name
from t_device t
left join t_device_relation r on r.device_code = t.device_code and r.deleted = false
left join t_student s on s.id = r.student_id and s.deleted = false
left join t_school sc on sc.id = s.school_id and sc.deleted = false
where t.deleted = false;

-- 告警统计
drop view if exists v_alarm_statistic;
create view v_alarm_statistic as
select a.alarm_type, a.alarm_level, count(a.alarm_level) as count
from t_alarm_bus a
where a.create_time::date = CURRENT_DATE
  and a.deleted = false
group by a.alarm_type, a.alarm_level
union all
select d.alarm_type, d.alarm_level, count(d.alarm_level) as count
from t_alarm_device d
where d.create_time::date = CURRENT_DATE
  and d.deleted = false
group by d.alarm_type, d.alarm_level;

-- 可用站点
drop view if exists v_stop;
create view v_stop as
select st.*, r.route_name, r.school_id
from t_stop st
left join t_route r on st.route_id = r.id and r.deleted = false
where st.deleted = false
order by st.route_id, st.stop_sequence


-- 路线行程及时刻表
drop view if exists v_route_time;
create view v_route_time as
select r.id as              route_id,
       r.route_name,
       r.route_type,
       t.id as              trip_id,
       t.trip_time,
       r.school_id,
       t.direction_id,
       min(st.arrival_time) start_time,
       max(st.arrival_time) end_time
from t_route r
       left join t_trip t on r.id = t.route_id and t.deleted = false
       left join t_stop_time st on t.id = st.trip_id
       left join t_stop s on s.id = st.stop_id and s.route_id = r.id
where r.deleted = false
  and st.deleted = false
  and s.deleted = false
group by r.id, t.id
order by r.route_name, t.trip_time
;

-- 车辆站点时刻表
drop view if exists v_bindbus_stoptime_info;
create view v_bindbus_stoptime_info as
select b.*,
       d.dvr_code,
       d.channel_number,
       ro.route_id,
       r.school_id,
       r.route_name,
       r.route_type,
       ro.id as route_operation_id,
       ro.bind_bus_teacher_id,
       ro.bind_driver_id,
       tr.id as trip_id,
       tr.direction_id,
       s.id as stop_id,
       s.stop_name,
       s.stop_sequence,
       st.id as stop_time_id,
       st.arrival_time,
       s.stop_lat,
       s.stop_lon
from t_bus b
       right join t_route_operation ro on b.id = ro.bind_bus_id and ro.deleted = false
       left join t_route r on r.id = ro.route_id and r.deleted = false
       left join t_trip tr on tr.route_id = r.id
       left join t_stop s on s.route_id = r.id
       left join t_stop_time st on st.trip_id = tr.id and s.id = st.stop_id and st.deleted = false
       left join t_dvr d on d.bus_id = b.id and d.deleted = false
where b.deleted = false
and tr.deleted = false
and s.deleted = false
order by tr.id asc, st.stop_sequence*tr.direction_id asc
;

drop view if exists v_guardian_student;
create view v_guardian_student
as
SELECT t.id,
       t.user_id,
       t.leave_permissions,
       t.main_guardian,
       t.create_by,
       t.create_time,
       t.modify_by,
       t.modify_time,
       t.idcard,
       s.id as student_id,
       s.name            AS student_name,
       s.school_id,
       s.stop_id,
       s.route_operation_id,
       s.main_guardian_id,
       s.leave_guardian_id,
       s.service_start_date,
       s.service_end_date,
       s.service_status,
       u.username        AS guardian_username,
       u.realname        AS guardian_realname,
       u.mobile          AS guardian_mobile,
       u.registration_id AS guardian_registration_id
FROM (((t_guardian t
    LEFT JOIN LATERAL unnest(t.student_id) WITH ORDINALITY a(stu_id, ordinality) ON (true))
    LEFT JOIN t_student s ON ((s.id = a.stu_id)))
         LEFT JOIN sys_user u ON ((u.user_id = t.user_id)))
WHERE (t.deleted = false);
```