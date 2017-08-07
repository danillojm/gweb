package com.br.melo.entidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.br.melo.interfaces.EntidadeBean;

 

@Entity
@Table(name = "tb_bairro")
public class Bairro extends EntidadeBean{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "nom_bairro")
	private String nomBairro;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cidade")
	private Cidade cidade;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_logradouro")
	private Logradouro logradouro;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomBairro() {
		return nomBairro;
	}

	public void setNomBairro(String nomBairro) {
		this.nomBairro = nomBairro;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public Logradouro getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(Logradouro logradouro) {
		this.logradouro = logradouro;
	}

	@Override
	public boolean validar() throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setValoresPadroes() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
