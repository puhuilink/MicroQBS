create table t_fence
(
    id           bigint
        constraint t_fence_pk
            primary key,
    fence_name   varchar(100),
    entity_names json,
    vertexes     json,
    offsets      int,
    fence_type   char,
    create_by    bigint,
    create_time  timestamp,
    update_by    bigint,
    update_time  timestamp
);

comment on table t_fence is '电子围栏';

comment on column t_fence.id is '主键';

comment on column t_fence.fence_name is '围栏名称';

comment on column t_fence.entity_names is '终端';

comment on column t_fence.vertexes is '围栏坐标点';

comment on column t_fence.offsets is '偏离距离（米）';

comment on column t_fence.fence_type is '围栏名称';

comment on column t_fence.create_by is '创建人';

comment on column t_fence.create_time is '创建时间';

comment on column t_fence.modify_by is '操作人';

comment on column t_fence.modify_time is '操作时间';

alter table t_fence
    add center varchar(50);

comment on column t_fence.center is '圆心经纬度';

alter table t_fence
    add radius integer;

comment on column t_fence.radius is '半径（米）';

alter table t_fence
    add alert_condition varchar(10);

comment on column t_fence.alert_condition is '触发动作(enter:进出,leave:离开,enter;leave:全部)';

