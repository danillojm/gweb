package com.br.melo.entidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.br.melo.interfaces.EntidadeBean;

 
@Entity
@Table(schema = "admin", name = "tb_dia_semana")
public class DiaSemana extends EntidadeBean {

	public static final String strNumDiaSemana = "numDiaSemana";
	public static final String strNomDiaSemana = "nomDiaSemana";
	public static final String strSglDiaSemana = "sglDiaSemana";

	public static final String DOMINGO = "DOM";
	public static final String SEGUNDA = "SEG";
	public static final String TERCA = "TER";
	public static final String QUARTA = "QUA";
	public static final String QUINTA = "QUI";
	public static final String SEXTA = "SEX";
	public static final String SABADO = "SAB";

	@Id
	@Column(name = "num_dia_semana")
	private Integer numDiaSemana;

	@Column(name = "nom_dia_semana")
	private String nomDiaSemana;

	@Column(name = "sgl_dia_semana")
	private String sglDiaSemana;

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof DiaSemana) {

			if (this.getNumDiaSemana() == null || ((DiaSemana) obj).getNumDiaSemana() == null) {
				return super.equals(obj);
			}

			if (((DiaSemana) obj).getNumDiaSemana().equals(this.getNumDiaSemana())) {
				return true;
			}

		}

		return false;
	}

	public String getDiaSemanaExtenso() {
		if (getSglDiaSemana().equals("DOM")) {
			return "Domingo";
		} else if (getSglDiaSemana().equals("SEG")) {
			return "Segunda";
		} else if (getSglDiaSemana().equals("TER")) {
			return "Terça";
		} else if (getSglDiaSemana().equals("QUA")) {
			return "Quarta";
		} else if (getSglDiaSemana().equals("QUI")) {
			return "Quinta";
		} else if (getSglDiaSemana().equals("SEX")) {
			return "Sexta";
		}
		return "Sábado";
	}

	public String toString() {
		return getNomDiaSemana();
	}

	public Integer getNumDiaSemana() {
		return numDiaSemana;
	}

	public void setNumDiaSemana(Integer numDiaSemana) {
		this.numDiaSemana = numDiaSemana;
	}

	public String getNomDiaSemana() {
		return nomDiaSemana;
	}

	public void setNomDiaSemana(String nomDiaSemana) {
		this.nomDiaSemana = nomDiaSemana;
	}

	public String getSglDiaSemana() {
		return sglDiaSemana;
	}

	public void setSglDiaSemana(String sglDiaSemana) {
		this.sglDiaSemana = sglDiaSemana;
	}

	@Override
	public Long getId() {
		return getNumDiaSemana() == null ? null : getNumDiaSemana().longValue();
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean validar() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setValoresPadroes() throws Exception {
		// TODO Auto-generated method stub

	}
}
