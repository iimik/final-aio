import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

class KotlinAnnotationResolver {
    @RequestMapping(method=[RequestMethod.GET])
    fun singleMethod(){}
    @RequestMapping(method=[RequestMethod.GET,RequestMethod.POST])
    fun multiMethod(){}
}