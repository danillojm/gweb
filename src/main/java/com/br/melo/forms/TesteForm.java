package com.br.melo.forms;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.dialog.Dialog;
import org.primefaces.component.panel.Panel;
import org.primefaces.context.RequestContext;

import com.br.melo.exception.RestricaoException;
import com.br.melo.interfaces.ExclusaoLogica;

public abstract class TesteForm implements Serializable {

	private static final long serialVersionUID = -3447208948910430820L;

	private CommandButton cbNovo = new CommandButton();
	private CommandButton cbEditar = new CommandButton();
	private CommandButton cbExcluir = new CommandButton();
	private CommandButton cbReativar = new CommandButton();
	private CommandButton cbCancelar = new CommandButton();
	private CommandButton cbSalvarNovo = new CommandButton();
	private CommandButton cbSalvarEditar = new CommandButton();
	private CommandButton cbLimpar = new CommandButton();
	private CommandButton cbPesquisar = new CommandButton();
	private CommandButton cbRecarrregar = new CommandButton();
	private HtmlForm formPrincipal = new HtmlForm();
	private DataTable dtPrincipal = new DataTable();
	private Dialog dlgPrincipal = new Dialog();
	private Panel pnPrincipal = new Panel();
	private String msgPadraoGridVazio = "Sem registros...";
	private String msgPadraoErroDesconhecido = "Ops! Ocorreu alguma situação não esperada! Tente novamente, caso persista, entre em contato com nossa equipe técnica.";
	private String msgPadraoCDialog = "Confirma a ação ?";
	private Long idEntidadePesq = null;
	private Boolean permissaoEditar = false;
	private Boolean permissaoNovo = false;
	private Boolean permissaoExcluir = false;
	private Boolean permissaoPesquisar = false;

	public abstract void novo() throws Exception;

	public abstract void editar() throws Exception;

	public abstract void excluir() throws Exception;

	public abstract void cancelar() throws Exception;

	public void reativar() throws Exception {
		System.out.println("Implemente o método reativar em seu formulário!");
	};

	public abstract void salvarNovo() throws Exception;

	public abstract void salvarEditar() throws Exception;

	public abstract void limpar() throws Exception;

	public abstract void pesquisar() throws Exception;

	public abstract void selecionar(boolean isTrue) throws Exception;

	public abstract void recarregar() throws Exception;

	public abstract void checaRestricoesNovo() throws Exception;

	public abstract void checaRestricoesEditar() throws Exception;

	public abstract String getNomTituloForm() throws Exception;

	public void checaRestricoesExcluir() throws Exception {

	};

	public void recaregar(ActionEvent ae) {
		try {
			recarregar();

		} catch (RestricaoException e) {
			enviarMensagem(((RestricaoException) e).getMsg(), null, FacesMessage.SEVERITY_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			enviarMensagem("Erro ao recarregar o registro.");
		}
	}

	public void excluir(ActionEvent ae) {
		try {
			checaRestricoesExcluir();
			excluir();

			limpar();

			enviarMensagem("Registro excluído com sucesso.");

		} catch (

		RestricaoException e) {
			enviarMensagem(((RestricaoException) e).getMsg(), null, FacesMessage.SEVERITY_ERROR);
		} catch (Exception e) {
			if (e instanceof ConstraintViolationException) {
				if (((ConstraintViolationException) e).getSQLException().getMessage().contains("violates foreign key constraint") || ((ConstraintViolationException) e).getSQLException().getMessage().contains("viola restrição de chave estrangeira")) {
					enviarMensagem("Existem registros vinculados esse, não é possível excluir.");
					e.printStackTrace();
				} else {
					enviarMensagem("Não é possível excluir.");
				}
			} else {
				enviarMensagem("Erro ao excluir registro.");
				e.printStackTrace();
			}
		}
	}

	public void editar(ActionEvent ae) {
		try {

			editar();
		} catch (RestricaoException e) {
			enviarMensagem(((RestricaoException) e).getMsg(), null, FacesMessage.SEVERITY_ERROR);
		} catch (Exception e) {
			enviarMensagem(getMsgPadraoErroDesconhecido());
			e.printStackTrace();
		}
	}

	public void salvarEditar(ActionEvent ae) {
		try {
			checaRestricoesEditar();
			salvarEditar();

			enviarMensagem("Registro alterado com sucesso.");
		} catch (RestricaoException e) {
			enviarMensagem(((RestricaoException) e).getMsg(), null, FacesMessage.SEVERITY_ERROR);
		} catch (Exception e) {
			if (e instanceof ConstraintViolationException && ((ConstraintViolationException) e).getSQLException().getMessage().contains("violates foreign key constraint")) {
				enviarMensagem("Existem registros vinculados, não é possível alterar.");
			} else {
				enviarMensagem("Erro ao alterar registro.");
				e.printStackTrace();
			}
		}
	}

	public void salvarNovo(ActionEvent ae) {
		try {
			checaRestricoesNovo();

			enviarMensagem("Registro salvo com sucesso.");
		} catch (RestricaoException e) {
			enviarMensagem(((RestricaoException) e).getMsg(), null, FacesMessage.SEVERITY_ERROR);
		} catch (Exception e) {
			enviarMensagem(getMsgPadraoErroDesconhecido());
			e.printStackTrace();
		}
	}

	public void novo(ActionEvent ae) {
		try {
			novo();

		} catch (RestricaoException e) {
			enviarMensagem(((RestricaoException) e).getMsg(), null, FacesMessage.SEVERITY_ERROR);
		} catch (Exception e) {
			enviarMensagem(getMsgPadraoErroDesconhecido());
			e.printStackTrace();
		}
	}

	public void cancelar(ActionEvent ae) {
		try {
			cancelar();

		} catch (RestricaoException e) {
			enviarMensagem(((RestricaoException) e).getMsg(), null, FacesMessage.SEVERITY_ERROR);
		} catch (Exception e) {
			enviarMensagem(getMsgPadraoErroDesconhecido());
			e.printStackTrace();
		}
	}

	public void enviarMensagem(String mensagem) {
		enviarMensagem(mensagem, null, FacesMessage.SEVERITY_INFO);
	}

	public void enviarMensagem(String mensagem, String detalhe, Severity nivel) {
		if (nivel.equals(FacesMessage.SEVERITY_INFO) || nivel.equals(FacesMessage.SEVERITY_ERROR) || nivel.equals(FacesMessage.SEVERITY_FATAL)) {
			RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");
		}

		FacesContext context = FacesContext.getCurrentInstance();

		context.addMessage(null, new FacesMessage(nivel, detalhe, mensagem));
	}

	public CommandButton getCbNovo() {
		return cbNovo;
	}

	public void setCbNovo(CommandButton cbNovo) {
		this.cbNovo = cbNovo;
	}

	public CommandButton getCbEditar() {
		return cbEditar;
	}

	public void setCbEditar(CommandButton cbEditar) {
		this.cbEditar = cbEditar;
	}

	public CommandButton getCbExcluir() {
		return cbExcluir;
	}

	public void setCbExcluir(CommandButton cbExcluir) {
		this.cbExcluir = cbExcluir;
	}

	public CommandButton getCbReativar() {
		return cbReativar;
	}

	public void setCbReativar(CommandButton cbReativar) {
		this.cbReativar = cbReativar;
	}

	public CommandButton getCbCancelar() {
		return cbCancelar;
	}

	public void setCbCancelar(CommandButton cbCancelar) {
		this.cbCancelar = cbCancelar;
	}

	public CommandButton getCbSalvarNovo() {
		return cbSalvarNovo;
	}

	public void setCbSalvarNovo(CommandButton cbSalvarNovo) {
		this.cbSalvarNovo = cbSalvarNovo;
	}

	public CommandButton getCbSalvarEditar() {
		return cbSalvarEditar;
	}

	public void setCbSalvarEditar(CommandButton cbSalvarEditar) {
		this.cbSalvarEditar = cbSalvarEditar;
	}

	public CommandButton getCbLimpar() {
		return cbLimpar;
	}

	public void setCbLimpar(CommandButton cbLimpar) {
		this.cbLimpar = cbLimpar;
	}

	public CommandButton getCbPesquisar() {
		return cbPesquisar;
	}

	public void setCbPesquisar(CommandButton cbPesquisar) {
		this.cbPesquisar = cbPesquisar;
	}

	public CommandButton getCbRecarrregar() {
		return cbRecarrregar;
	}

	public void setCbRecarrregar(CommandButton cbRecarrregar) {
		this.cbRecarrregar = cbRecarrregar;
	}

	public HtmlForm getFormPrincipal() {
		return formPrincipal;
	}

	public void setFormPrincipal(HtmlForm formPrincipal) {
		this.formPrincipal = formPrincipal;
	}

	public DataTable getDtPrincipal() {
		return dtPrincipal;
	}

	public void setDtPrincipal(DataTable dtPrincipal) {
		this.dtPrincipal = dtPrincipal;
	}

	public Dialog getDlgPrincipal() {
		return dlgPrincipal;
	}

	public void setDlgPrincipal(Dialog dlgPrincipal) {
		this.dlgPrincipal = dlgPrincipal;
	}

	public Panel getPnPrincipal() {
		return pnPrincipal;
	}

	public void setPnPrincipal(Panel pnPrincipal) {
		this.pnPrincipal = pnPrincipal;
	}

	public String getMsgPadraoGridVazio() {
		return msgPadraoGridVazio;
	}

	public void setMsgPadraoGridVazio(String msgPadraoGridVazio) {
		this.msgPadraoGridVazio = msgPadraoGridVazio;
	}

	public String getMsgPadraoErroDesconhecido() {
		return msgPadraoErroDesconhecido;
	}

	public void setMsgPadraoErroDesconhecido(String msgPadraoErroDesconhecido) {
		this.msgPadraoErroDesconhecido = msgPadraoErroDesconhecido;
	}

	public String getMsgPadraoCDialog() {
		return msgPadraoCDialog;
	}

	public void setMsgPadraoCDialog(String msgPadraoCDialog) {
		this.msgPadraoCDialog = msgPadraoCDialog;
	}

	public Long getIdEntidadePesq() {
		return idEntidadePesq;
	}

	public void setIdEntidadePesq(Long idEntidadePesq) {
		this.idEntidadePesq = idEntidadePesq;
	}

	public Boolean getPermissaoEditar() {
		return permissaoEditar;
	}

	public void setPermissaoEditar(Boolean permissaoEditar) {
		this.permissaoEditar = permissaoEditar;
	}

	public Boolean getPermissaoNovo() {
		return permissaoNovo;
	}

	public void setPermissaoNovo(Boolean permissaoNovo) {
		this.permissaoNovo = permissaoNovo;
	}

	public Boolean getPermissaoExcluir() {
		return permissaoExcluir;
	}

	public void setPermissaoExcluir(Boolean permissaoExcluir) {
		this.permissaoExcluir = permissaoExcluir;
	}

	public Boolean getPermissaoPesquisar() {
		return permissaoPesquisar;
	}

	public void setPermissaoPesquisar(Boolean permissaoPesquisar) {
		this.permissaoPesquisar = permissaoPesquisar;
	}

}
