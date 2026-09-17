package com.ideias_inovadora.config;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.ideias_inovadora.model.Country;
import com.ideias_inovadora.model.Customer;
import com.ideias_inovadora.model.District;
import com.ideias_inovadora.repository.CountryRepository;
import com.ideias_inovadora.repository.CustomerRepository;
import com.ideias_inovadora.repository.DistrictRepository;

import jakarta.transaction.Transactional;

@Component
public class DataLoader implements CommandLineRunner {

    private final CountryRepository countryRepository;
    private final DistrictRepository districtRepository;
    private final CustomerRepository customerRepository;

    private static final Logger logger =
            LoggerFactory.getLogger(DataLoader.class);

    public DataLoader(
            CountryRepository countryRepository,
            DistrictRepository districtRepository,
            CustomerRepository customerRepository) {

        this.countryRepository = countryRepository;
        this.districtRepository = districtRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        try {
            carregarDados();
        } catch (Exception e) {
            logger.error("Erro ao carregar dados iniciais", e);
            throw e;
        }
    }

    private void carregarDados() {

        // ==================================================
        // PAÍSES
        // ==================================================

        if (countryRepository.count() == 0) {

            List<Country> paises = List.of(
                    new Country(
                            "SÃO TOMÉ E PRINCÍPE",
                            "STP",
                            LocalDateTime.now()
                    ),
                    new Country(
                            "BRASIL",
                            "BR",
                            LocalDateTime.now()
                    ),
                    new Country(
                            "PORTUGAL",
                            "PT",
                            LocalDateTime.now()
                    ),
                    new Country(
                            "ANGOLA",
                            "AO",
                            LocalDateTime.now()
                    ),
                    new Country(
                            "MOÇAMBIQUE",
                            "MZ",
                            LocalDateTime.now()
                    )
            );

            countryRepository.saveAll(paises);

            logger.info("Países cadastrados com sucesso!");
        }

        // ==================================================
        // MAPA DE PAÍSES
        // ==================================================

        Map<String, Country> paisMap = new HashMap<>();

        countryRepository.findAll().forEach(
                pais -> paisMap.put(pais.getNome(), pais)
        );

        // ==================================================
        // DISTRITOS
        // ==================================================

        if (districtRepository.count() == 0) {

            List<District> districts = List.of(

                    new District(
                            "ÁGUA GRANDE",
                            "AG",
                            paisMap.get("SÃO TOMÉ E PRINCÍPE"),
                            LocalDateTime.now()
                    ),

                    new District(
                            "MÉ-ZÓCHI",
                            "MZ",
                            paisMap.get("SÃO TOMÉ E PRINCÍPE"),
                            LocalDateTime.now()
                    ),

                    new District(
                            "CANTAGALO",
                            "CT",
                            paisMap.get("SÃO TOMÉ E PRINCÍPE"),
                            LocalDateTime.now()
                    ),

                    new District(
                            "LOBATA",
                            "LB",
                            paisMap.get("SÃO TOMÉ E PRINCÍPE"),
                            LocalDateTime.now()
                    ),

                    new District(
                            "CAUÉ",
                            "CE",
                            paisMap.get("SÃO TOMÉ E PRINCÍPE"),
                            LocalDateTime.now()
                    ),

                    new District(
                            "LEMBÁ",
                            "LB",
                            paisMap.get("SÃO TOMÉ E PRINCÍPE"),
                            LocalDateTime.now()
                    ),

                    new District(
                            "REGIÃO AUTONOMA DO PRINCIPE",
                            "RAP",
                            paisMap.get("SÃO TOMÉ E PRINCÍPE"),
                            LocalDateTime.now()
                    )
            );

            districtRepository.saveAll(districts);

            logger.info("Distritos cadastrados com sucesso!");
        }

        // ==================================================
        // CLIENTE PADRÃO
        // ==================================================

        criarClientePadrao();
    }

    // ==================================================
    // CLIENTE PADRÃO
    // ==================================================

    private void criarClientePadrao() {

        final String nomeClientePadrao = "Venda ao Público";
        final String nifClientePadrao = "999999999";

        boolean clienteExiste = customerRepository
                .findAll()
                .stream()
                .anyMatch(customer ->
                        nifClientePadrao.equals(
                                customer.getNumeroContribuinte()
                        )
                );

        if (!clienteExiste) {

            Customer customer = new Customer();

            customer.setNome(nomeClientePadrao);
            customer.setNumeroContribuinte(nifClientePadrao);

            customerRepository.save(customer);

            logger.info(
                    "Cliente padrão criado com sucesso: {} - NIF {}",
                    nomeClientePadrao,
                    nifClientePadrao
            );

        } else {

            logger.info(
                    "Cliente padrão já existe: {} - NIF {}",
                    nomeClientePadrao,
                    nifClientePadrao
            );
        }
    }
}