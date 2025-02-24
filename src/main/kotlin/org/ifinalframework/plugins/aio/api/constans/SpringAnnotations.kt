package org.ifinalframework.plugins.aio.api.constans;


/**
 * SpringAnnotations
 *
 * @author iimik
 * @since 0.0.1
 **/
class SpringAnnotations {
    companion object {
        const val REQUEST_MAPPING = "org.springframework.web.bind.annotation.RequestMapping"
        const val GET_MAPPING = "org.springframework.web.bind.annotation.GetMapping"
        const val POST_MAPPING = "org.springframework.web.bind.annotation.PostMapping"
        const val PUT_MAPPING = "org.springframework.web.bind.annotation.PutMapping"
        const val DELETE_MAPPING = "org.springframework.web.bind.annotation.DeleteMapping"
        const val PATCH_MAPPING = "org.springframework.web.bind.annotation.PatchMapping"

        const val FEIGN_CLIENT = "org.springframework.cloud.openfeign.FeignClient"

        val REQUEST_MAPPINGS =
            setOf(REQUEST_MAPPING, GET_MAPPING, POST_MAPPING, PUT_MAPPING, PATCH_MAPPING, DELETE_MAPPING)
    }
}