package com.phlink.bus.api.notify.event;

import com.phlink.bus.api.fence.domain.FenceVO;
import lombok.Data;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@ToString
@Data
public class FenceCreateEvent extends ApplicationEvent {

    private FenceVO vo;

    public FenceCreateEvent(Object source, FenceVO vo) {
        super(source);
        this.vo = vo;
    }
}
