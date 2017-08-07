package com.br.melo.dao;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.br.melo.entidade.Usuario;
import com.br.melo.util.HibernateUtil;

public class UsuarioDAO extends DAOGeral<Usuario> {

	public void salvar(Usuario usuario) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Transaction transacao = null;

		try {
			transacao = sessao.beginTransaction();
			SimpleHash hash = new SimpleHash("md5", usuario.getSenha());
			usuario.setSenha(hash.toHex());
			sessao.save(usuario);
			transacao.commit();
		} catch (RuntimeException erro) {
			if (transacao != null) {
				transacao.rollback();
			}
			throw erro;
		} finally {
			sessao.close();
		}
	}

	public Usuario autenticar(String nome, String senha) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Usuario usuario = null;
		try {

			Criteria ct = sessao.createCriteria(Usuario.class);
			ct.add(Restrictions.eq(Usuario.strNome, nome));

			SimpleHash hash = new SimpleHash("md5", senha);
			ct.add(Restrictions.eq(Usuario.strSenha, hash.toHex()));

			usuario = (Usuario) ct.uniqueResult();

		} catch (RuntimeException e) {

			throw e;

		} finally {
			sessao.close();
		}
		return usuario;

	}

}
