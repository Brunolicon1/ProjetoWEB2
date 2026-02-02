package com.example.produtosweb2.service;

import com.example.produtosweb2.model.entity.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UsuarioService implements UserDetailsService {

    @PersistenceContext
    private EntityManager em;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Usuario usuario = em.createQuery("from Usuario u where u.username = :user", Usuario.class)
                    .setParameter("user", username)
                    .getSingleResult();

            return User.builder()
                    .username(usuario.getUsername())
                    .password(usuario.getPassword())
                    .roles(usuario.getRoles().stream().map(r -> r.getNome().replace("ROLE_", "")).toArray(String[]::new))
                    .build();

        } catch (Exception e) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }
    }
}