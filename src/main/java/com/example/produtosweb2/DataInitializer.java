package com.example.produtosweb2;

import com.example.produtosweb2.model.entity.Role;
import com.example.produtosweb2.model.entity.Usuario;
import com.example.produtosweb2.model.repository.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager em;

    @EventListener(ApplicationReadyEvent.class) // Executa sempre que a app inicia
    @Transactional
    public void onApplicationEvent() {

        // 1. Criar ou buscar a ROLE_ADMIN
        Role adminRole = roleRepository.buscarPorNome("ROLE_ADMIN");
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setNome("ROLE_ADMIN");
            em.persist(adminRole);
        }

        // 2. Criar ou buscar a ROLE_USER
        if (roleRepository.buscarPorNome("ROLE_USER") == null) {
            Role userRole = new Role();
            userRole.setNome("ROLE_USER");
            em.persist(userRole);
        }

        // 3. Verificar se o admin já existe
        Long count = em.createQuery("select count(u) from Usuario u where u.username = 'admin'", Long.class)
                .getSingleResult();

        if (count == 0) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            // Define a senha como 'admin' (ou a que você preferir)
            admin.setPassword(passwordEncoder.encode("admin"));

            // Adiciona a Role ao usuário
            admin.getRoles().add(adminRole);

            em.persist(admin);
            System.out.println(">>> Usuário ADMIN criado com sucesso (admin/admin)");
        }
    }
}
