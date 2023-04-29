package ma.enset.test.web;

import jakarta.validation.Valid;
import ma.enset.test.entities.Abonnement;
import ma.enset.test.entities.Client;
import ma.enset.test.entities.TypeAbonnement;
import ma.enset.test.services.AbonnementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AbonnementController {
    @Autowired
    private AbonnementService abonnementService;

    @GetMapping(path = "/user/abonnements/index")
    public String abonnements(Model model,
                              @RequestParam(name = "page", defaultValue = "0") int page,
                              @RequestParam(name = "size", defaultValue = "5") int size,
                              @RequestParam(name = "keyword", defaultValue = "") TypeAbonnement keyword,
                              @RequestParam(name = "client", defaultValue = "0") Client client) {
        Page<Abonnement> pageAbonnements = abonnementService.findAbonnements(client, PageRequest.of(page, size));
        model.addAttribute("listAbonnements", pageAbonnements.getContent());
        model.addAttribute("pages", new int[pageAbonnements.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("client", client);
        return "abonnements";
    }
    @GetMapping("/admin/deleteAbonnement")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteAbonnement(Long id,String keyword,int page){
        abonnementService.deleteAbonnement(id);
        return "redirect:/user/abonnements/index?page="+page+"&keyword="+keyword;
    }

    @GetMapping("/admin/formAbonnements")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String formAbonnements(Model model){
        model.addAttribute("abonnement",new Abonnement());
        return "formAbonnements";
    }
    @PostMapping("/admin/saveAbonnement")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String saveAbonnement(Model model, @Valid Abonnement abonnement,Client client, BindingResult bindingResult, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "")String keyword){
        if(bindingResult.hasErrors()) return "formAbonnements";
        abonnementService.saveAbonnementToClient(abonnement.getDateAbonnement(),abonnement.getTypeAbonnement(),abonnement.getSold(),abonnement.getMontantMensuel(),client);
        return "redirect:/user/abonnements/index?page="+page+"&keyword="+keyword;
    }

    @GetMapping("/admin/editAbonnement")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editAbonnement(Model model,Client c,String keyword,int page){
        Abonnement abonnement= abonnementService.findAbonnement(c.getId());
        if(abonnement==null) throw new RuntimeException("abonnement introuvable");
        model.addAttribute("abonnement",abonnement);
        model.addAttribute("page",page);
        model.addAttribute("keyword",keyword);
        return "editAbonnements";
    }
}
