package in.hocg.web.modules.system.controller.system;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.base.BaseController;
import in.hocg.web.modules.base.body.Results;
import in.hocg.web.modules.base.filter.group.Insert;
import in.hocg.web.modules.base.filter.group.Update;
import in.hocg.web.modules.base.filter.lang.IdsFilter;
import in.hocg.web.modules.system.domain.Member;
import in.hocg.web.modules.system.filter.MemberDataTablesInputFilter;
import in.hocg.web.modules.system.filter.MemberFilter;
import in.hocg.web.modules.system.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hocgin on 2017/11/26.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/admin/system/member")
public class MemberController extends BaseController {
    public final String BASE_TEMPLATES_PATH = "/admin/system/member/%s";
    
    private MemberService memberService;
    @Autowired
    public MemberController(MemberService memberSercice) {
        this.memberService = memberSercice;
    }
    
    
    @GetMapping({"/index.html", "/"})
    public String vIndex(Model model) {
        return String.format(BASE_TEMPLATES_PATH, "index");
    }
    
    @GetMapping("/query-modal.html")
    public String vQueryModal() {
        return String.format(BASE_TEMPLATES_PATH, "query-modal");
    }
    
    @PostMapping("/data")
    @ResponseBody
    public DataTablesOutput<Member> data(@RequestBody MemberDataTablesInputFilter input) {
        return memberService.data(input);
    }
    
    @GetMapping("/add-view.html")
    public String vAdd(Model model) {
        return String.format(BASE_TEMPLATES_PATH, "add-view");
    }
    
    @GetMapping("/update-view/{id}")
    public String vUpdate(@PathVariable("id") String id, Model model) {
        Member member = memberService.find(id);
        model.addAttribute("member", member);
        return String.format(BASE_TEMPLATES_PATH, "update-view");
    }
    
    
    /**
     * 删除角色
     *
     * @param filter
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
//    @PreAuthorize("hasPermission(null, 'sys.user.delete')")
    public Results delete(@Validated IdsFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        memberService.delete(checkError, filter.getId());
        return Results.check(checkError, "删除成功");
    }
    
    /**
     * 新建角色
     *
     * @param filter
     * @return
     */
    @RequestMapping("/insert")
    @ResponseBody
//    @PreAuthorize("hasPermission(null, 'sys.user.add')")
    public Results insert(@Validated({Insert.class}) MemberFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        memberService.insert(filter, checkError);
        return Results.check(checkError, "增加成功");
    }
    
    @PostMapping("/available/{id}")
    @ResponseBody
//    @PreAuthorize("hasPermission(null, 'sys.user.edit')")
    public Results available(@PathVariable("id") String id, boolean available) {
        memberService.updateAvailable(id, available);
        return Results.success()
                .setMessage(String.format("%s成功", available ? "开启" : "禁用"));
    }
    
    
    @RequestMapping("/detail/{id}")
    public String vDetail(@PathVariable("id") String id, Model model) {
        Member member = memberService.find(id);
        model.addAttribute("member", member);
        return String.format(BASE_TEMPLATES_PATH, "detail-modal");
    }
    
    @RequestMapping("/update")
    @ResponseBody
//    @PreAuthorize("hasPermission(null, 'sys.user.edit')")
    public Results update(@Validated({Update.class}) MemberFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        memberService.update(filter, checkError);
        return Results.check(checkError, "修改信息成功");
    }
    
}