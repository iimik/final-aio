package org.ifinalframework.plugins.aio.spring.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * JavaFeignLineMarkerClient
 *
 * @author iimik
 * @since 0.0.5
 **/
@FeignClient(name = "nana", path = "/api/feign")
public interface JavaFeignLineMarkerClient {
    @GetMapping("/get")
    String get();
}
