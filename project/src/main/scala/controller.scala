@Controller
@RequestMapping(Array("/"))
class HelloWorldController @Autowired() (nameService: Name) {

  @RequestMapping(method = Array(RequestMethod.GET))
  def index (model: Model) = {
    model.addAttribute("name", nameService.name)
    "index"
  }
}