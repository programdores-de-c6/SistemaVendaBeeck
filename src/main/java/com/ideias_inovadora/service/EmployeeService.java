package com.ideias_inovadora.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ideias_inovadora.dto.EmployeeDTO;
import com.ideias_inovadora.dto.EmployeeDTOs;
import com.ideias_inovadora.dto.InitialSetupDTO;
import com.ideias_inovadora.dto.PasswordResetDTO;
import com.ideias_inovadora.email.EmailService;
import com.ideias_inovadora.erros.OperationNotAllowedException;
import com.ideias_inovadora.model.*;
import com.ideias_inovadora.repository.*;

import jakarta.transaction.Transactional;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    JobTitleRepository jobTitleRepository;
    @Autowired
    ValidationRepository validationRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    SessionLogRepository sessionLogRepository;
    @Autowired
    FilesService filesService;
    @Autowired
    private EmailService emailService;
    @Autowired
    DistrictRepository districtRepository;
    
    @Autowired
    ShopSevice shopSevice;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ====================================================
    // 1. CRIAÇÃO E ATUALIZAÇÃO (COM HIERARQUIA)
    // ====================================================

    @Transactional(rollbackOn = Exception.class)
    @CacheEvict(value = "employee", allEntries = true)
    public String create(EmployeeDTO dto) throws Exception {

        // ====================================================
        // VALIDAÇÃO OBRIGATÓRIA DO E-MAIL
        // ====================================================
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new OperationNotAllowedException(
                    "O e-mail é obrigatório."
            );
        }

        String email = dto.getEmail().trim().toLowerCase();

        if (employeeRepository.existsByEmailIgnoreCase(email)) {
            throw new OperationNotAllowedException(
                    "Já existe um utilizador com este e-mail."
            );
        }

        dto.setEmail(email);

        // ====================================================
        // RESTANTE DA CRIAÇÃO
        // ====================================================

        Employee executor = (Employee) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        JobTitle requestedJob = getJobTitleBYID(dto.getRole());

        AccessLevel requestedLevel = requestedJob.getAccessLevel();

        // 🛡️ Segurança: Só Admin cria outro Admin
        if (requestedLevel == AccessLevel.ROLE_ADMIN &&
            executor.getAccessLevel() != AccessLevel.ROLE_ADMIN) {

            throw new OperationNotAllowedException(
                    "Acesso negado para criar Administradores."
            );
        }

        // Determinar Loja
        Shop shop = (requestedLevel == AccessLevel.ROLE_ADMIN)
                ? null
                : getShopById(dto.getShop());

        // Salvar foto
        Files file = (dto.getFile() != null && !dto.getFile().isEmpty())
                ? filesService.salvarArquivoFoto(
                        dto.getFile(),
                        FileCategory.EMPLOYEE
                  )
                : null;

        Employee emp = createEmployeeEntity(
                dto,
                requestedJob,
                getLocationById(dto.getLocation()),
                shop
        );

        emp.setFiles(file);
        emp.setDatacriacao(LocalDateTime.now());

        employeeRepository.save(emp);

        // Criar conta de utilizador para login
        if (requestedLevel != AccessLevel.NO_ACCESS) {
            createUserCredentials(emp, dto);
        }

        return "Funcionário registado com sucesso.";
    }

    @Transactional(rollbackOn = Exception.class)
    @CacheEvict(value = "employee", allEntries = true)
    public String update(EmployeeDTO dto) {

        // ====================================================
        // 1. FUNCIONÁRIO EXISTENTE
        // ====================================================
        Employee existing = employeeRepository.findById(dto.getId())
                .orElseThrow(() ->
                    new OperationNotAllowedException(
                        "Funcionário não encontrado."
                    )
                );

        // ====================================================
        // 2. E-MAIL OBRIGATÓRIO
        // ====================================================
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new OperationNotAllowedException(
                    "O e-mail é obrigatório."
            );
        }

        String email = dto.getEmail().trim().toLowerCase();

        // ====================================================
        // 3. E-MAIL ÚNICO
        // ====================================================
        Optional<Employee> employeeWithEmail =
                employeeRepository.findByEmailIgnoreCase(email);

        if (employeeWithEmail.isPresent()
                && employeeWithEmail.get().getId() != existing.getId()) {

            throw new OperationNotAllowedException(
                    "Já existe um utilizador com este e-mail."
            );
        }

        // ====================================================
        // 4. RESTANTE DA ACTUALIZAÇÃO
        // ====================================================
        JobTitle jobTitle = getJobTitleBYID(dto.getRole());

        Location location = getLocationById(dto.getLocation());

        // Se for Admin, a loja pode ser null
        Shop shop = (jobTitle.getAccessLevel() == AccessLevel.ROLE_ADMIN)
                ? null
                : getShopById(dto.getShop());

        existing.setNome(dto.getNome());
        existing.setContactoPrincipal(dto.getContactoPrincipal());
        existing.setContactoSecudario(dto.getContactoSecudario());
        existing.setNumeroContribuinte(dto.getNumeroContribuinte());
        existing.setEmail(email);
        existing.setDataNacimento(dto.getDataNascimento());
        existing.setJobTitle(jobTitle);
        existing.setLocation(location);
        existing.setShop(shop);
        existing.setDataAtualizacao(LocalDateTime.now());
        existing.setGender(dto.getGender());
        existing.setDataAdmissao(dto.getDataAdmissao());
        existing.setNumeroBi(dto.getNumeroBi());

        employeeRepository.save(existing);

        return "Dados atualizados com sucesso.";
    }
    // ====================================================
    // 2. LISTAGEM E PESQUISA (COM ISOLAMENTO)
    // ====================================================
    @Cacheable(value = "employee", key = "'list'")
    public List<EmployeeDTOs> list() {
        Employee executor = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // Admin vê todos, Gerente vê apenas a sua loja
        if (executor.getAccessLevel() == AccessLevel.ROLE_ADMIN) {
            return employeeRepository.findAll().stream().map(this::mapToEmployeeDTOs).collect(Collectors.toList());
        } else {
            return employeeRepository.findByShop(executor.getShop()).stream()
                    .map(this::mapToEmployeeDTOs).collect(Collectors.toList());
        }
    }

    public EmployeeDTOs listid(long id) {
        return employeeRepository.findById(id)
                .map(this::mapToEmployeeDetailDTOs)
                .orElseThrow(() -> new OperationNotAllowedException("Utilizador não encontrado."));
    }

    public List<EmployeeDTOs> search(String nome) {
        Employee executor = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Employee> results = employeeRepository.findByNomeContainingIgnoreCase(nome);

        // Filtra a pesquisa para que gerentes não vejam funcionários de outras lojas
        return results.stream()
                .filter(e -> executor.getAccessLevel() == AccessLevel.ROLE_ADMIN || 
                            (e.getShop() != null && e.getShop().getId() == executor.getShop().getId()))
                .map(this::mapToEmployeeDTOs)
                .collect(Collectors.toList());
    }

  
    @Transactional
    public String forgotpassword(String email) {

        if (email == null || email.isBlank()) {
            throw new OperationNotAllowedException(
                "O e-mail é obrigatório."
            );
        }

        String emailNormalizado = email.trim().toLowerCase();

        Optional<Employee> employee =
            employeeRepository.findByEmailIgnoreCase(emailNormalizado);

        if (employee.isPresent()) {

            String codigo = generateCode();

            Validation validation =
                validationRepository.findByEmail(emailNormalizado);

            if (validation == null) {
                validation = new Validation();
            }

            validation.setEmail(emailNormalizado);
            validation.setCodigo(codigo);

            validation.setDataespiracao(
                LocalDateTime.now().plus(15, ChronoUnit.MINUTES)
            );

            validation.setUsado(false);

            validationRepository.save(validation);

            envioemail(
                "esqueceusenha",
                emailNormalizado,
                codigo
            );
        }

        // IMPORTANTE:
        // A mesma resposta para e-mail existente ou inexistente.
        return "Se existir uma conta associada a este e-mail, receberá um código de recuperação.";
    }

    @Transactional
    public String updatePassword(PasswordResetDTO dto) {

        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new OperationNotAllowedException(
                    "O e-mail é obrigatório."
            );
        }

        if (dto.getCodigo() == null || dto.getCodigo().isBlank()) {
            throw new OperationNotAllowedException(
                    "O código de recuperação é obrigatório."
            );
        }

        if (dto.getNovaSenha() == null || dto.getNovaSenha().isBlank()) {
            throw new OperationNotAllowedException(
                    "A nova palavra-passe é obrigatória."
            );
        }

        String emailNormalizado = dto.getEmail().trim().toLowerCase();
        String codigo = dto.getCodigo().trim();

        // Procura o código e garante que ainda não foi utilizado
        Validation validation =
                validationRepository.findByEmailAndCodigo(
                        emailNormalizado,
                        codigo
                );

        if (validation == null) {
            throw new OperationNotAllowedException(
                    "Código inválido ou expirado."
            );
        }

        // Verifica a validade temporal
        if (validation.getDataespiracao() == null ||
            validation.getDataespiracao().isBefore(LocalDateTime.now())) {

            throw new OperationNotAllowedException(
                    "Código inválido ou expirado."
            );
        }

        // Localiza o funcionário
        Employee user = employeeRepository.findByEmailIgnoreCase(emailNormalizado)
                .orElseThrow(() ->
                    new OperationNotAllowedException(
                        "Utilizador não encontrado."
                    )
                );

        // Encripta a nova palavra-passe
        String novaSenhaEncodada =
                passwordEncoder.encode(dto.getNovaSenha());

        // Actualiza a senha na entidade User
        userRepository.updateSenhaAndFirstLoginByEmployeeId(
                novaSenhaEncodada,
                user.getId()
        );

        // Invalida o código para impedir reutilização
        validationRepository.marcarComoUsado(validation.getId());

        return "Palavra-passe recuperada com sucesso.";
    }
    
    

    @Transactional(rollbackOn = Exception.class)
    public String executeInitialSetup(InitialSetupDTO dto) throws Exception {
        // 🛡️ 1. TRANCA
        if (employeeRepository.count() > 0) {
            throw new OperationNotAllowedException("Configuração já realizada.");
        }
        if (dto.getAdminEmail() == null || dto.getAdminEmail().isBlank()) {
            throw new OperationNotAllowedException(
                    "O e-mail do administrador é obrigatório."
            );
        }
        // 📍 2. CRIAR A LOCALIDADE (Location) PRIMEIRO
        District district = districtRepository.findById(dto.getIdDistrito())
                .orElseThrow(() -> new OperationNotAllowedException("Distrito não encontrado."));

        Location location = new Location();
        location.setNome(dto.getNomeLocalidade().toUpperCase());
        location.setDistrict(district);
        location.setDatacriacao(LocalDateTime.now());
        
        // Lógica para gerar Sigla (Pega as iniciais: "Vila Maria" -> "VM")
        String sigla = Arrays.stream(dto.getNomeLocalidade().split(" "))
                             .filter(w -> !w.isEmpty())
                             .map(w -> String.valueOf(w.charAt(0)))
                             .collect(Collectors.joining())
                             .toUpperCase();
        location.setSigla(sigla);
        location = locationRepository.save(location);

        // 🏗️ 3. CRIAR A LOJA
        Shop shop = new Shop();
        shop.setNome(dto.getShopNome().toUpperCase());
        shop.setNumeroContribuite(dto.getShopNif());
        shop.setShopType(dto.getShopType());
        shop.setLocation(location); // Vincula a localidade criada acima
        shop.setDatacriacao(LocalDateTime.now());
        shop = shopRepository.save(shop);

        // 👔 4. CARGO
        JobTitle adminRole = new JobTitle();
        adminRole.setNomecargo("ADMINISTRADOR MASTER");
        adminRole.setAccessLevel(AccessLevel.ROLE_ADMIN);
        adminRole.setDatacricao(LocalDateTime.now());
        adminRole = jobTitleRepository.save(adminRole);
        String adminEmail = dto.getAdminEmail().trim().toLowerCase();
        // 👤 5. DONO
        Employee owner = new Employee();
        owner.setNome(dto.getAdminNome());
        owner.setEmail(adminEmail);
        owner.setJobTitle(adminRole);
        owner.setShop(null); // Dono é Global
        owner.setStatus(Status.ACTIVO);
        owner.setDatacriacao(LocalDateTime.now());
        owner.setLocation(location);
        owner = employeeRepository.save(owner);
  EmployeeDTO d = new EmployeeDTO(); 
  d.setSenha(dto.getAdminSenha());
  d.setUsername(dto.getAdminUsername());
        // 🔑 6. CREDENCIAIS
        createUserCredentials(owner, d);

        // 🛠️ 7. AUDITORIA DA LOJA
        shop.setEmployee(owner);
        shopRepository.save(shop);

        return "Setup concluído com sucesso!";
    }
    // ====================================================
    // 4. LÓGICA DE SESSÃO (LOGIN/LOGOUT)
    // ====================================================
    @Transactional
    public String logoutBySessionId(Long sessionLogId) {

        SessionLog session = sessionLogRepository.findById(sessionLogId)
                .orElse(null);

        if (session == null) {
            return "Sessão não encontrada";
        }

        if (!session.getLogado()) {
            session.setLogoutTime(LocalDateTime.now());
            session.setLogado(false);

            sessionLogRepository.save(session);
        }

        return "Logout realizado com sucesso";
    }
    
    @Transactional
    public String changeFirstPassword(String novaSenha) {

        if (novaSenha == null || novaSenha.isBlank()) {
            throw new OperationNotAllowedException(
                    "A nova palavra-passe é obrigatória."
            );
        }

        if (novaSenha.length() < 8) {
            throw new OperationNotAllowedException(
                    "A nova palavra-passe deve ter pelo menos 8 caracteres."
            );
        }

        Employee employee =
                (Employee) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        if (employee.getUser() == null) {
            throw new OperationNotAllowedException(
                    "Conta de utilizador não encontrada."
            );
        }

        User user = employee.getUser();

        if (!user.isFirstLogin()) {
            throw new OperationNotAllowedException(
                    "Esta conta não está em primeiro acesso."
            );
        }

        String novaSenhaEncodada =
                passwordEncoder.encode(novaSenha);

        userRepository.updateSenhaAndFirstLoginByEmployeeId(
                novaSenhaEncodada,
                employee.getId()
        );

        return "Palavra-passe alterada com sucesso.";
    }
    
    // ✅ Ajuste também o método de Login para aceitar E-mail ou Username
    public SessionLog Login(String identifier) {
        // Busca o funcionário pelo identificador (E-mail ou Username)
        Employee employee = employeeRepository.findByEmailOrUserUsername(identifier, identifier)
                .orElseThrow(() -> new OperationNotAllowedException("Credenciais inválidas"));

        // Encerra sessões anteriores
        List<SessionLog> activeSessions = sessionLogRepository.findByEmployeeAndLogado(employee, true);
        if (activeSessions != null && !activeSessions.isEmpty()) {
            for (SessionLog session : activeSessions) {
                session.setLogado(false);
                session.setLogoutTime(LocalDateTime.now());
                sessionLogRepository.save(session);
            }
        }

        SessionLog sessionLog = new SessionLog();
        sessionLog.setEmployee(employee);
        sessionLog.setLogado(true);
        sessionLog.setLoginTime(LocalDateTime.now());
        return sessionLogRepository.save(sessionLog);
    }
 
    public String upload_logo(EmployeeDTO dto) throws Exception {
    	
    	// 1. Verificação inicial: existe um arquivo?
        if (dto.getFile() == null || dto.getFile().isEmpty()) {
            return "Nenhum arquivo enviado.";
        }

        // 2. Busca a loja no banco
        Employee employee = getEmploeeID(dto.getId());
        String resultado;

        // 3. CENÁRIO A: Loja ainda não tem foto (Primeira vez)
        if (employee.getFiles() == null) {
            // Salva o arquivo e recebe a entidade Files de volta
            Files novoArquivo = filesService.salvarArquivoFoto(dto.getFile(), FileCategory.EMPLOYEE);
            
            // Faz o vínculo na tabela Shop (file_fk)
            employee.setFiles(novoArquivo);
          employee=  employeeRepository.saveAndFlush(employee);
     
            resultado = "Operação realizada com sucesso";
        } 
        // 4. CENÁRIO B: Loja já tem foto (Substituição)
        else {
            // O seu método atualizarArquivoFoto já faz o update no banco e retorna a String
            resultado = filesService.atualizarArquivoFotos(dto.getFile(), employee, FileCategory.EMPLOYEE);
        }

        return resultado;
	}
    /**
     * ====================================================
     * CONSULTAR SESSÃO POR ID
     * ====================================================
     */
    public SessionLog findSessionById(Long sessionLogId) {

        if (sessionLogId == null) {
            return null;
        }

        return sessionLogRepository
                .findById(sessionLogId)
                .orElse(null);
    }

    /**
     * ====================================================
     * CONSULTAR FUNCIONÁRIO POR E-MAIL
     * ====================================================
     */
    public Employee findByEmail(String email) {

        if (email == null || email.isBlank()) {
            return null;
        }

        return employeeRepository
                .findByEmailIgnoreCase(email.trim())
                .orElse(null);
    }
    // ====================================================
    // 5. MAPEAMENTOS E UTILITÁRIOS
    // ====================================================

    private EmployeeDTOs mapToEmployeeDTOs(Employee e) {
        EmployeeDTOs dto = new EmployeeDTOs();
        dto.setId(e.getId());
        dto.setNome(e.getNome());
        dto.setEmail(e.getEmail());
        dto.setJobTitle(e.getJobTitle().getNomecargo());
        dto.setAccessLevel(e.getAccessLevel());
        dto.setStatus(e.getStatus());
        dto.setNumeroContribuinte(e.getNumeroContribuinte());
        dto.setGender(e.getGender());
        dto.setContactoPrincipal(e.getContactoPrincipal());
        dto.setNumeroBi(e.getNumeroBi());
        
        // Donos/Admins aparecem como Administração Global
        if (e.getShop() != null) {
            dto.setShop(e.getShop().getId());
            dto.setShops(e.getShop().getNome());
        } else {
            dto.setShops("ADMINISTRAÇÃO CENTRAL");
        }
        return dto;
    }

    private EmployeeDTOs mapToEmployeeDetailDTOs(Employee e) {
        EmployeeDTOs dto = mapToEmployeeDTOs(e);
        dto.setContactoPrincipal(e.getContactoPrincipal());
        dto.setContactoSecudario(e.getContactoSecudario());
        dto.setNumeroBi(e.getNumeroBi());
        dto.setNumeroContribuinte(e.getNumeroContribuinte());
        dto.setGender(e.getGender());
        dto.setLocations(e.getLocation().getNome());
        dto.setLogoUrl(buscarLogoBase64(e));
        dto.setJobtitle(e.getJobTitle().getId());
        dto.setJobTitle(e.getJobTitle().getNomecargo());
        dto.setAccessLevel(e.getJobTitle().getAccessLevel());
        dto.setLocation(e.getLocation().getId());
        dto.setLocations(e.getLocation().getNome());
        dto.setDataAdmissao(e.getDataAdmissao());
        dto.setDataNascimento(e.getDataNacimento());
        return dto;
    }

    public String buscarLogoBase64(Employee e) {
        if (e.getFiles() != null) {
            try {
                byte[] bytes = filesService.buscarArquivo(e.getFiles().getId());
                return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
            } catch (Exception err) { return null; }
        }
        return null;
    }

    private Employee createEmployeeEntity(EmployeeDTO dto, JobTitle jt, Location loc, Shop s) {
        Employee e = new Employee();
        e.setNome(dto.getNome());
        e.setEmail(dto.getEmail());
        e.setContactoPrincipal(dto.getContactoPrincipal());
        e.setNumeroContribuinte(dto.getNumeroContribuinte());
        e.setNumeroBi(dto.getNumeroBi());
        e.setGender(dto.getGender());
        e.setDataNacimento(dto.getDataNascimento());
        e.setDataAdmissao(dto.getDataAdmissao());
        e.setJobTitle(jt);
        e.setLocation(loc);
        e.setShop(s);
        e.setStatus(Status.ACTIVO);
        return e;
    }

    private void createUserCredentials(Employee e, EmployeeDTO dto) {

        if (dto.getUsername() == null || dto.getUsername().isBlank()) {
            throw new OperationNotAllowedException(
                    "O nome de utilizador é obrigatório."
            );
        }

        String username = dto.getUsername().trim();

        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new OperationNotAllowedException(
                    "Já existe um utilizador com este nome de utilizador."
            );
        }

        if (dto.getSenha() == null || dto.getSenha().isBlank()) {
            throw new OperationNotAllowedException(
                    "A palavra-passe é obrigatória."
            );
        }

        if (dto.getSenha().length() < 8) {
            throw new OperationNotAllowedException(
                    "A palavra-passe deve ter pelo menos 8 caracteres."
            );
        }

        User u = new User();
        u.setUsername(username);
        u.setSenha(passwordEncoder.encode(dto.getSenha()));
        u.setEmployee(e);
        u.setFirstLogin(true);

        userRepository.save(u);
    }
    
  

    public String generateCode() {
        SecureRandom sr = new SecureRandom();
        byte[] bytes = new byte[10];
        sr.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    @Async
    public void envioemail(String acao, String email, String codigo) {
        String assunto = acao.equals("validacao") ? "Confirmação de Registo" : "Recuperação de Senha";
        emailService.enviarEmail(email, assunto, "O seu código é: " + codigo);
    }

    private JobTitle getJobTitleBYID(long id) {
        return jobTitleRepository.findById(id).orElseThrow(() -> new OperationNotAllowedException("Cargo inválido."));
    }

    private Shop getShopById(long id) {
        return shopRepository.findById(id).orElseThrow(() -> new OperationNotAllowedException("Loja inválida."));
    }

    private Location getLocationById(long id) {
        return locationRepository.findById(id).orElseThrow(() -> new OperationNotAllowedException("Localidade inválida."));
    }

    public SessionLog getSessionLog(Employee employee) {
        return sessionLogRepository.findByEmployeeAndLogadoTrue(employee);
    }
    
    private Employee getEmploeeID(long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new OperationNotAllowedException("Unidade ID " + id + " não funcionario."));
    }

    public long countEmployees() {
        return employeeRepository.count();
    }

	
}