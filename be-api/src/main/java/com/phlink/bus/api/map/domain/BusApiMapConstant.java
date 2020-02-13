package com.phlink.bus.api.map.domain;

/**
 * 地图URL
 */
public class BusApiMapConstant {

    public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";

    public static class Amap {
        private static final String TERMINAL = "https://tsapi.amap.com/v1/track/terminal/";
        /**
         * 创建服务--高德地图
         */
        public static final String CREATE_SERVICE = "https://tsapi.amap.com/v1/track/service/add";
        /**
         * 创建终端--高德地图
         */
        public static final String CREATE_TERMINAL = TERMINAL + "add";
        /**
         * 查询终端--高德地图
         */
        public static final String LIST_TERMINAL = TERMINAL + "list";
        /**
         * 删除终端--高德地图
         */
        public static final String DELETE_TERMINAL = TERMINAL + "delete";
        /**
         * 电子围栏--高德地图
         */
        public static final String FENCE_URL = "https://restapi.amap.com/v4/geofence/meta?key=";
        public static final String FENCE_URL_BASE = "https://restapi.amap.com/v4/geofence/meta";
        /**
         * 查询设备与附近的围栏交互状态--高德地图
         */
        public static final String DISCOVER_FENCE = "https://restapi.amap.com/v4/geofence/status";
        /**
         * 创建轨迹
         */
        public static final String CREATE_TRAJECTORY = "https://tsapi.amap.com/v1/track/trace/add";
        /**
         * 删除轨迹
         */
        public static final String DELETE_TRAJECTORY = "https://tsapi.amap.com/v1/track/trace/delete";
        /**
         * 查询轨迹信息
         */
        public static final String TRSEARCH_URL = "https://tsapi.amap.com/v1/track/terminal/trsearch";

        /**
         * 上传轨迹信息
         */
        public static final String UPLOAD_TRAJECTORY = "https://tsapi.amap.com/v1/track/point/upload";

        /**
         * 查询坐标与围栏状态
         */
        public static final String GET_STATUS_BY_FENCE = "https://restapi.amap.com/v4/geofence/status";
        /**
         * 逆地理编码
         */
        public static final String GET_REGEO = "https://restapi.amap.com/v3/geocode/regeo";
        /**
         * 高德坐标系转换
         */
        public static final String GET_GAODE_COORDINATE = "https://restapi.amap.com/v3/assistant/coordinate/convert";
        /**
         * 距离测量
         */
        public static final String GET_DISTANCE = "https://restapi.amap.com/v3/distance";
    }

    public static class Baidu {
        //终端URL
        private static final String TERMINAL = "http://yingyan.baidu.com/api/v3/entity/";
        //电子围栏URL
        private static final String FENCE = "http://yingyan.baidu.com/api/v3/fence/";
        /**
         * 百度坐标系
         */
        public static final String COORD_TYPE = "bd09ll";
        /**
         * 创建终端--百度地图
         */
        public static final String CREATE_TERMINAL = TERMINAL + "add";
        /**
         * 创建终端--百度地图
         */
        public static final String DELETE_TERMINAL = TERMINAL + "delete";
        /**
         * 创建线型电子围栏--百度地图
         */
        public static final String CREATE_FENCE_LINE = FENCE + "createpolylinefence";
        /**
         * 修改电子围栏--百度地图
         */
        public static final String UPDATE_FENCE_LINE = FENCE + "updatepolylinefence";
        /**
         * 获得电子围栏--百度地图
         */
        public static final String LIST_FENCE = FENCE + "list";
        /**
         * 删除电子围栏--百度地图
         */
        public static final String DELETE_FENCE_LINE = FENCE + "delete";

        /**
         * 根据坐标查询监控对象相对围栏的状态
         */
        public static final String QUERY_STATUS_BY_LOCATION = "http://yingyan.baidu.com/api/v3/fence/querystatusbylocation";
        public static final String GET_BAIDU_COORDINATE = "http://api.map.baidu.com/geoconv/v1";

    }


}
