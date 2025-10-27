package br.com.ecopoints.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import br.com.ecopoints.api.dto.DadosLogin;
import br.com.ecopoints.api.model.Usuario;
import br.com.ecopoints.api.service.TokenService;
import br.com.ecopoints.api.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/cadastrar")
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.cadastrarUsuario(usuario);
        return new ResponseEntity<>(novoUsuario, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody DadosLogin dados) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
            Authentication authentication = authenticationManager.authenticate(authToken);

            Usuario usuario = (Usuario) authentication.getPrincipal();
            String token = tokenService.gerarToken(usuario);

            return ResponseEntity.ok().body("{\"token\": \"" + token + "\"}");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"erro\": \"Email ou senha incorretos.\"}");
        } catch (Exception e) {
            e.printStackTrace(); // 👈 vai mostrar o erro exato no console
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"erro\": \"Erro interno no servidor.\"}");
        }
    }
}
