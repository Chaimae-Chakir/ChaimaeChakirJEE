package ma.enset.test.web;

import jakarta.validation.Valid;
import ma.enset.test.entities.Client;
import ma.enset.test.services.ClientService;
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
public class ClientController {
    @Autowired
  private ClientService clientService;
    @GetMapping(path = "/user/index")
    public String clients(Model model,
                           @RequestParam(name = "page",defaultValue = "0") int page,
                           @RequestParam(name = "size",defaultValue = "5") int size,
                           @RequestParam(name = "keyword",defaultValue = "") String keyword){
        Page<Client> pageClients =clientService.findNomContains(keyword,PageRequest.of(page,size));
        model.addAttribute("listClients",pageClients.getContent());
        model.addAttribute("pages",new int[pageClients.getTotalPages()]);
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",keyword);
        return "clients";
    }
    @GetMapping("/admin/deleteClient")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteClient(Long id,String keyword,int page){
            clientService.deleteClient(id);
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
    }

    @GetMapping("/admin/formClients")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String formClients(Model model){
        model.addAttribute("client",new Client());
        return "formClients";
    }
    @GetMapping("/")
    public String home(){
        return "redirect:/user/index";
    }

    @PostMapping("/admin/saveClient")
    public String saveClient(Model model, @Valid Client client, BindingResult bindingResult,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "")String keyword){
        if(bindingResult.hasErrors()) return "formClients";
        Client clientSaved = clientService.saveClient(client);
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
    }

    @GetMapping("/admin/editClient")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editClient(Model model,Long id,String keyword,int page){
        Client client=clientService.findClientById(id);
        if(client==null) throw new RuntimeException("Client introuvable");
        model.addAttribute("client",client);
        model.addAttribute("page",page);
        model.addAttribute("keyword",keyword);
        return "editClient";
    }
}
