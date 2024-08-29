package Rainbow_Frends.domain.account.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "계정", description = "계정정보 관련 API")
@RequestMapping("/api/account")
class AccountController {
}