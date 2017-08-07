package com.br.melo.entidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.br.melo.interfaces.EntidadeBean;

@Entity
@Table(name = "tb_uf")
public class Uf extends EntidadeBean {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "nom_uf")
	private String nomUf;

	@Column(name = "sigla")
	private String sigla;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomUf() {
		return nomUf;
	}

	public void setNomUf(String nomUf) {
		this.nomUf = nomUf;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
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
