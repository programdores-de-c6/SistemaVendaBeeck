package com.ideias_inovadora.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.ideias_inovadora.model.Employee;
import com.ideias_inovadora.model.Shop;
import com.ideias_inovadora.model.Status;
import jakarta.transaction.Transactional;

@Transactional
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    // Verifica se existe algum funcionário vinculado a uma localidade específica
    boolean existsByLocationId(Long localidadeId);
    
    // Lista todos os funcionários de uma loja (Gráfica ou Livraria)
    List<Employee> findByShop(Shop shop);
    
    // Busca funcionários filtrando por estado (Ativo/Inativo) e pela Loja
    @Query("SELECT e FROM Employee e WHERE e.status = ?1 AND e.shop.id = ?2")
    List<Employee> findByStatusAndShop(Status status, Long shopId);

    // ✅ NOVO MÉTODO DE LOGIN HÍBRIDO:
    // Busca por Email (da classe Person) OU por Username (da classe User vinculada)
    // O Spring Data JPA faz o Join automaticamente através de 'UserUsername'
    Optional<Employee> findByEmailOrUserUsername(String email, String username);

    // Busca rápida por email (retornando Optional para evitar NullPointerException)
    Optional<Employee> findByEmailIgnoreCase(String email);
    
    // Busca por parte do nome para filtros de pesquisa
    List<Employee> findByNomeContainingIgnoreCase(String nome);
    boolean existsByEmailIgnoreCase(String email);
    // Sobrescrita do findById para garantir o uso de Optional
    Optional<Employee> findById(long id);
   
 

    Optional<Employee> findByEmail(String email);
}