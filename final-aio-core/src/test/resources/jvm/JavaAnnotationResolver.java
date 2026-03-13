import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

class JavaAnnotationResolver{
    @RequestMapping(method= RequestMethod.GET)
    public void singleMethod(){}
    @RequestMapping(method= {RequestMethod.GET,RequestMethod.POST})
    public void multiMethod(){}
}