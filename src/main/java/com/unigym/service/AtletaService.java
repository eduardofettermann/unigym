package com.unigym.service;

import com.unigym.model.Atleta;
import com.unigym.repository.AtletaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AtletaService {

    private final AtletaRepository atletaRepository;
    private final PasswordEncoder passwordEncoder;

    public Atleta cadastrar(Atleta atleta) {
        if (atletaRepository.existsByEmail(atleta.getEmail())) {
            throw new RuntimeException("Email já cadastrado!");
        }
        atleta.setSenha(passwordEncoder.encode(atleta.getSenha()));
        return atletaRepository.save(atleta);
    }

    public List<Atleta> listarTodos() {
        return atletaRepository.findAll();
    }

    public Atleta buscarPorId(Long id) {
        return atletaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Atleta não encontrado!"));
    }

    public Atleta atualizar(Long id, Atleta atletaAtualizado) {
        Atleta atleta = buscarPorId(id);
        atleta.setNome(atletaAtualizado.getNome());
        atleta.setObjetivo(atletaAtualizado.getObjetivo());
        atleta.setNivelExperiencia(atletaAtualizado.getNivelExperiencia());
        atleta.setPeso(atletaAtualizado.getPeso());
        atleta.setAltura(atletaAtualizado.getAltura());
        atleta.setRestricoesFisicas(atletaAtualizado.getRestricoesFisicas());
        return atletaRepository.save(atleta);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        atletaRepository.deleteById(id);
    }
}