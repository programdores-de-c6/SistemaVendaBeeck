package com.ideias_inovadora.controller;

import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ideias_inovadora.dto.EmployeeDTO;
import com.ideias_inovadora.dto.EmployeeDTOs;
import com.ideias_inovadora.dto.InitialSetupDTO;
import com.ideias_inovadora.dto.Login;
import com.ideias_inovadora.dto.PasswordResetDTO;
import com.ideias_inovadora.model.Employee;
import com.ideias_inovadora.model.SessionLog;
import com.ideias_inovadora.security.Tokenservice;
import com.ideias_inovadora.service.EmployeeService;
import com.ideias_inovadora.util.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

/**
 * CONTROLADOR DE FUNCIONÁRIOS E SEGURANÇA
 * Gere o ciclo de vida dos colaboradores e o processo de autenticação JWT.
 */
@RestController
@RequestMapping("api/sales-system/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private ApiResponse apiResponse;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private Tokenservice tokenService;

    
    
    
    /**
     * Executa a configuração inicial do sistema (Dono + Loja).
     * Rota: POST /api/sales-system/employee/setup-initial
     */
    @PostMapping("/setup-initial")
    public ResponseEntity<ApiResponse> setupInitial(@RequestBody  @Valid InitialSetupDTO setupDTO) throws Exception {
        // Chama o serviço de ignição
        apiResponse.setMessage(employeeService.executeInitialSetup(setupDTO));
        apiResponse.setStatus("Sucesso");
        
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    
    
    /**
     * Regista um novo colaborador. Suporta upload de foto via Multipart.
     */
    @PostMapping(value = "/create")
    public ResponseEntity<ApiResponse> create(
            @RequestPart(value = "file", required = false) MultipartFile file, 
            @RequestPart("employeeDTO") @Valid EmployeeDTO employeeDTO) throws Exception {
        
        employeeDTO.setFile(file); // Anexa o ficheiro ao DTO
        apiResponse.setMessage(employeeService.create(employeeDTO)); // Chama o serviço de criação
        apiResponse.setStatus("Sucesso");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    /**
     * Verifica se o sistema é novo (sem utilizadores).
     * Usado pelo Frontend para decidir se mostra a tela de Login ou Setup Inicial.
     */
    @GetMapping("/check-setup")
    public ResponseEntity<Map<String, Boolean>> checkSetup() {
        long count = employeeService.countEmployees();

        return ResponseEntity.ok(
            Map.of("needsSetup", count == 0)
        );
    }

    /**
     * Atualiza dados cadastrais. O Service validará a hierarquia (Admin vs Gerente).
     */
    @PutMapping(value = "/update")
    public ResponseEntity<ApiResponse> update(@Valid @RequestBody EmployeeDTO employeeDTO) {
        apiResponse.setMessage(employeeService.update(employeeDTO)); 
        apiResponse.setStatus("Sucesso");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    /**
     * Listagem de Funcionários.
     * Implementa filtro automático: Admin vê todos, Gerente vê apenas a sua unidade.
     */
    @GetMapping(value = "/list")
    public ResponseEntity<List<EmployeeDTOs>> list() throws Exception {
        // O serviço utiliza o SecurityContextHolder interno para filtrar por loja
        return ResponseEntity.ok(this.employeeService.list());
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Login login,
            HttpServletResponse response) {

        // 1. Autenticar utilizador
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        login.email(),
                        login.senha()
                );

        Authentication authenticate =
                authenticationManager.authenticate(authToken);

        // 2. Obter funcionário autenticado
        Employee user = (Employee) authenticate.getPrincipal();

        // 3. Registar nova sessão
        SessionLog sessionLog =
                this.employeeService.Login(login.email());

        // 4. Gerar Access Token
        String token =
                tokenService.generateAccessToken(
                        user,
                        sessionLog.getId()
                );

        // 5. Gerar Refresh Token associado à sessão
        String refreshToken =
                tokenService.gerarRefreshToken(
                        user,
                        sessionLog.getId()
                );

        // 6. Guardar Refresh Token no Cookie HttpOnly
        Cookie refreshCookie =
                new Cookie("refreshToken", refreshToken);

        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(30 * 24 * 60 * 60);

        response.addCookie(refreshCookie);

        // 7. Verificar se é o primeiro acesso
        boolean firstLogin =
                user.getUser() != null
                        && user.getUser().isFirstLogin();

        // 8. Resposta para o frontend
        return ResponseEntity.ok(
                Map.of(
                        "accessToken", token,
                        "nome", user.getNome(),
                        "email", user.getEmail(),
                        "nivelAcesso", user.getAccessLevel().getDescricao(),
                        "lojaid", user.getShop() != null
                                ? user.getShop().getId()
                                : 0,
                        "loja", user.getShop() != null
                                ? user.getShop().getNome()
                                : "ADMINISTRAÇÃO",
                        "firstLogin", firstLogin
                )
        );
    }
    @GetMapping("/listid/{id}") 
    public ResponseEntity<EmployeeDTOs> findByid(@PathVariable Long id) {
        // Chama o serviço que faz o mapeamento para DTO detalhado
        EmployeeDTOs dto = this.employeeService.listid(id);
        return ResponseEntity.ok(dto); 
    }
    /**
     * Encerra a sessão, invalida o log no banco e limpa o Cookie.
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response) {

        try {

            if (refreshToken != null && !refreshToken.isBlank()) {

                // Valida o refresh token antes de utilizá-lo
                if (tokenService.isTokenValid(refreshToken)) {

                    Long sessionLogId =
                            tokenService.getSessionLogId(refreshToken);

                    // Encerra exactamente a sessão associada ao token
                    employeeService.logoutBySessionId(sessionLogId);
                }
            }

        } catch (Exception e) {

            // O logout local deve continuar mesmo que o
            // token já esteja expirado ou seja inválido.
            // Não devolvemos erro ao utilizador por isso.
            System.out.println(
                    "Não foi possível encerrar a sessão no servidor: "
                            + e.getMessage()
            );

        } finally {

            // Remove sempre o cookie do navegador
            Cookie cookie = new Cookie("refreshToken", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            cookie.setHttpOnly(true);

            response.addCookie(cookie);
        }

        apiResponse.setMessage("Sessão encerrada com sucesso.");
        apiResponse.setStatus("Sucesso");

        return ResponseEntity.ok(apiResponse);
    }
    
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(
            @CookieValue(
                    name = "refreshToken",
                    required = false
            ) String refreshToken) {

        // ==================================================
        // 1. Verificar existência do refresh token
        // ==================================================

        if (refreshToken == null || refreshToken.isBlank()) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message",
                            "Refresh token não encontrado."
                    ));
        }

        try {

            // ==================================================
            // 2. Validar refresh token
            // ==================================================

            if (!tokenService.isTokenValid(refreshToken)) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of(
                                "message",
                                "Refresh token inválido ou expirado."
                        ));
            }

            // ==================================================
            // 3. Obter dados do token
            // ==================================================

            String email =
                    tokenService.getSubject(refreshToken);

            Long sessionLogId =
                    tokenService.getSessionLogId(refreshToken);

            if (sessionLogId == null) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of(
                                "message",
                                "Sessão inválida."
                        ));
            }

            // ==================================================
            // 4. Procurar sessão
            // ==================================================

            SessionLog session =
                    employeeService.findSessionById(sessionLogId);

            if (session == null) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of(
                                "message",
                                "Sessão não encontrada."
                        ));
            }

            // ==================================================
            // 5. Verificar se a sessão ainda está activa
            // ==================================================

            if (!session.getLogado()) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of(
                                "message",
                                "Sessão encerrada. Faça login novamente."
                        ));
            }

            // ==================================================
            // 6. Procurar funcionário
            // ==================================================

            Employee user =
                    employeeService.findByEmail(email);

            if (user == null) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of(
                                "message",
                                "Utilizador não encontrado."
                        ));
            }

            // ==================================================
            // 7. Confirmar que a sessão pertence ao utilizador
            // ==================================================

            if (session.getEmployee() == null ||
                    session.getEmployee().getId() != user.getId()) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of(
                                "message",
                                "Sessão inválida."
                        ));
            }

            // ==================================================
            // 8. Gerar novo Access Token
            // ==================================================

            String newAccessToken =
                    tokenService.generateAccessToken(
                            user,
                            session.getId()
                    );

            // ==================================================
            // 9. Devolver novo Access Token
            // ==================================================

            return ResponseEntity.ok(
                    Map.of(
                            "accessToken",
                            newAccessToken
                    )
            );

        } catch (Exception e) {

            // Não expor detalhes internos ao cliente
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message",
                            "Não foi possível renovar a sessão."
                    ));
        }
    }
    @PostMapping(value = "/upload-photo")
    public ResponseEntity<ApiResponse> uploadLogo(
            @RequestPart("id") String id, // Recebemos como String para evitar erro de parse
            @RequestPart("file") MultipartFile file) throws Exception {

        // Converte o ID para Long e monta o DTO para o serviço
EmployeeDTO dto = new EmployeeDTO();
        dto.setId(Long.parseLong(id));
        dto.setFile(file);

        // Chama a lógica de negócio
        apiResponse.setMessage(employeeService.upload_logo(dto));
        apiResponse.setStatus("Sucesso");

        return ResponseEntity.ok(apiResponse);
    }
    
    /**
     * Solicita recuperação da palavra-passe através do e-mail.
     * Não exige autenticação.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(
            @RequestBody Map<String, String> request) {

        String email = request.get("email");

        apiResponse.setMessage(
                employeeService.forgotpassword(email)
        );
        apiResponse.setStatus("Sucesso");

        return ResponseEntity.ok(apiResponse);
    }


    /**
     * Define uma nova palavra-passe através do código
     * enviado para o e-mail.
     * Não exige autenticação.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(
            @RequestBody PasswordResetDTO dto) {

        apiResponse.setMessage(
                employeeService.updatePassword(dto)
        );
        apiResponse.setStatus("Sucesso");

        return ResponseEntity.ok(apiResponse);
    }
    
    @PostMapping("/change-first-password")
    public ResponseEntity<ApiResponse> changeFirstPassword(
            @RequestBody Map<String, String> request) {

        String novaSenha = request.get("novaSenha");

        apiResponse.setMessage(
                employeeService.changeFirstPassword(novaSenha)
        );
        apiResponse.setStatus("Sucesso");

        return ResponseEntity.ok(apiResponse);
    }
}