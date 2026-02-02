package com.example.produtosweb2.service;

import com.example.produtosweb2.model.entity.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    @PersistenceContext
    private EntityManager em;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return em.createQuery("from Usuario where username = :u", Usuario.class)
                    .setParameter("u", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }
    }
}