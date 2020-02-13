package com.phlink.bus.api.map.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@ApiModel("对轨迹进行处理参数")
public class Correction {

    @ApiModelProperty("0：不进行抽稀去噪,1：进行抽稀去噪")
    private Integer denoise=1;

    @ApiModelProperty("0：不进行轨迹纠偏;1：进行轨迹纠偏")
    private Integer  mapmatch=0;

    /**
     * 属性拟合：指是否将去噪、纠偏后的部分原始定位点的属性信息一同返回，设置绑路纠偏生效后，设置属性拟合才会生效。
     * 说明：当前参数效果并不能将所有纠偏后的点的附加属性信息全部返回，返回部分原始点的属性信息；
     */
    @ApiModelProperty("0：不进行拟合;1：进行拟合")
    private Integer  attribute=0;

    /**
     *  定位精度过滤，用于过滤掉定位精度较差的轨迹点，当denoise取值为1时此参数才会生效。
     *  说明：当取值=0时，则不过滤；当取值大于0的整数时，则过滤掉radius大于设定值的轨迹点。例如：若只需保留 GPS 定位点，则建议设为：20；若需保留 GPS 和 Wi-Fi 定位点，去除基站定位点，则建议设为：100
     */
    @ApiModelProperty("0：不过滤;100：过滤掉定位精度 Radius 大于100的点")
    private Integer  threshold=0;

    /**
     * 交通方式，猎鹰将根据不同交通工具选择不同的纠偏策略，目前支持驾车
     */
    @ApiModelProperty()
    private String  mode="driving";

    @Override
    public String toString() {
        return "denoise="+this.getDenoise()+",mapmatch="+this.getMapmatch()+",attribute="+this.getAttribute()+",threshold="+this.getThreshold()+",mode="+this.getMapmatch();
    }

    public static void main(String[] args) {
        System.out.println(new Correction().toString());
    }
}
