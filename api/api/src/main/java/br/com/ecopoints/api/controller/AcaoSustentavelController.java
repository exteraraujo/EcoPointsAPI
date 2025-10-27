package br.com.ecopoints.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ecopoints.api.model.AcaoSustentavel;
import br.com.ecopoints.api.model.Usuario;
import br.com.ecopoints.api.repository.AcaoSustentavelRepository;

@RestController
@RequestMapping("/api/acoes")
public class AcaoSustentavelController {

    @Autowired
    private AcaoSustentavelRepository acaoRepo;

    @PostMapping("/criar")
    public ResponseEntity<AcaoSustentavel> criarAcao(@AuthenticationPrincipal Usuario usuario,
            @RequestBody AcaoSustentavel acao) {
        // associa o usuário autenticado e define os pontos pelo tipo
        acao.setUsuario(usuario);
        if (acao.getTipo() != null) {
            acao.setPontos(acao.getTipo().getPontos());
        }
        AcaoSustentavel salva = acaoRepo.save(acao);
        return ResponseEntity.ok(salva);
    }

    @GetMapping("/minhas")
    public ResponseEntity<List<AcaoSustentavel>> minhasAcoes(@AuthenticationPrincipal Usuario usuario) {
        List<AcaoSustentavel> lista = acaoRepo.findByUsuarioId(usuario.getId());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/pontos")
    public ResponseEntity<Map<String, Integer>> meusPontos(@AuthenticationPrincipal Usuario usuario) {
        Integer total = acaoRepo.sumPontosByUsuarioId(usuario.getId());
        return ResponseEntity.ok(Map.of("pontos", total == null ? 0 : total));
    }
}
