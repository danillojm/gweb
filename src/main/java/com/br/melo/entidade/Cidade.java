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
@Table(name = "tb_cidade")
public class Cidade extends EntidadeBean{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "nom_cidade")
	private String nomeCidade;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_uf")
	private Uf uf;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeCidade() {
		return nomeCidade;
	}

	public void setNomeCidade(String nomeCidade) {
		this.nomeCidade = nomeCidade;
	}

	public Uf getUf() {
		return uf;
	}

	public void setUf(Uf uf) {
		this.uf = uf;
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
