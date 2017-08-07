package com.br.melo.util;

 

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import com.br.melo.forms.EstadoForm;
import com.br.melo.forms.Formulario;
import com.br.melo.interfaces.EntidadeBean;

public class LifeCycleListener implements PhaseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1590890859007922228L;
	private Formulario<EntidadeBean> formulario = null;

	public LifeCycleListener(Formulario<EntidadeBean> formulario) {
		setFormulario(formulario);
	}

	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

	public void beforePhase(PhaseEvent event) {
//		System.out.println("START PHASE " + event.getPhaseId() + " : " + getFormulario().getEstadoAtual().name());
		// TelaLoginBean.limparFormulariosSessao();
		if (getFormulario() != null) {
			if (getFormulario().getEstadoAtual() == null) {
			 
			} else if (getFormulario().getEstadoAtual().equals(EstadoForm.SELECIONADO) && getFormulario().getCtPesquisar() != null && getFormulario().getIdEntidadePesq() != null) {
			 
			}
		}
	}

	public void afterPhase(PhaseEvent event) {
		// System.out.println("END PHASE " + event.getPhaseId());
	}

	public Formulario<EntidadeBean> getFormulario() {
		return formulario;
	}

	public void setFormulario(Formulario<EntidadeBean> formulario) {
		this.formulario = formulario;
	}

}