package Rainbow_Frends.test

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.swing.text.html.HTML

@Controller
@RequestMapping("/test")
class MVCController {
    @GetMapping("/fetch/remaintime")
    fun testremaintime():String{
        return "RemainTimeAPI"
    }
}