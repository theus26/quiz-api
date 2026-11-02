package com.mathe.quiz_biblico_api.model;

import com.mathe.quiz_biblico_api.Enum.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "senha_hash", nullable = false, length = 255)
    private String senhaHash;

    @Column(name = "criado_em", nullable = false)
    private Instant criadoEm;

    @Column(nullable = false)
    private boolean ativo = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @ColumnDefault("'USER'")
    private Role role = Role.USER;

    public long getId(){
        return id;
    }
    public void setId(long id){
        this.id = id;
    }
    public String getNome(){
        return nome;
    }
    public void setNome(String nome){
        this.nome = nome;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getSenha() {
        return senhaHash;
    }
    public boolean getAtivo(){
        return ativo;
    }
    public void setSenha(String senha) {
        this.senhaHash = senha;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    public Usuario(String nome, String email, String senha){
        this.nome = nome;
        this.email = email;
        this.senhaHash = senha;
    }
    @PrePersist
    void preencherCriacao() {
        if (criadoEm == null) {
            criadoEm = Instant.now();
        }
        if (role == null) {
            role = Role.USER;
        }
    }
}
