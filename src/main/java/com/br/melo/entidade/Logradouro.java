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

import org.hibernate.annotations.ManyToAny;

@Entity
@Table(name = "tb_logradouro")
public class Logradouro {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "nom_logradouro")
	private String nomLogradouro;

	@Column(name = "num_cep")
	private String num_cep;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_bairro")
	private Bairro bairro;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomLogradouro() {
		return nomLogradouro;
	}

	public void setNomLogradouro(String nomLogradouro) {
		this.nomLogradouro = nomLogradouro;
	}

	public String getNum_cep() {
		return num_cep;
	}

	public void setNum_cep(String num_cep) {
		this.num_cep = num_cep;
	}

	public Bairro getBairro() {
		return bairro;
	}

	public void setBairro(Bairro bairro) {
		this.bairro = bairro;
	}

}
