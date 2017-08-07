package com.br.melo.util;

 

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.br.melo.entidade.DiaSemana;

public class DataUtil {

	public static final Long SEGUNDO =  1000l ;
	public static final Long MINUTO =  SEGUNDO * 60;
	public static final Long HORA =  MINUTO * 60;
	
	private static SimpleDateFormat fDDMMYYYY = new SimpleDateFormat("dd/MM/yyyy");
	

	private static SimpleDateFormat fDDMMYYYYHHMMSS = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	// SimpleDateFormat fYYYYMMDD = new
	// SimpleDateFormat("yyy/MM/dd");

	private static final String DATA_INICIAL = "inicial";

	public static String getDthFormatada(Date data) {
		return fDDMMYYYYHHMMSS.format(data);
	}
	
	
	public static String getDataSemHoraFormatada(Date data) {
		String retorno = "";
		
		if(data != null){
			retorno = fDDMMYYYY.format(data);
		}
		
		return retorno;
	}
	
	
	public static String formatInterval(final long l) {
		final long hr = TimeUnit.MILLISECONDS.toHours(l);
		final long min = TimeUnit.MILLISECONDS.toMinutes(l - TimeUnit.HOURS.toMillis(hr));
		final long sec = TimeUnit.MILLISECONDS.toSeconds(l - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
		final long ms = TimeUnit.MILLISECONDS.toMillis(l - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min) - TimeUnit.SECONDS.toMillis(sec));
		return String.format("%02d:%02d:%02d.%03d", hr, min, sec, ms);
	}
	
	public static String retornaSeHoje(DiaSemana diaSemana) {

		Calendar date = Calendar.getInstance();
		int resultado = 0;
		
		int day = date.get(Calendar.DAY_OF_WEEK);
		
		switch (day) {
		case Calendar.SUNDAY:
			resultado = 1;
			break;
		case Calendar.MONDAY:
			resultado = 2;
			break;
		case Calendar.TUESDAY:
			resultado = 3;
			break;
		case Calendar.WEDNESDAY:
			resultado = 4;
			break;
		case Calendar.THURSDAY:
			resultado = 5;
			break;
		case Calendar.FRIDAY:
			resultado = 6;
			break;
		case Calendar.SATURDAY:
			resultado = 7;
			break;
		}
		
		if (diaSemana.getNumDiaSemana() == resultado) {
			return "S";
		} 
		
		return "N";
	}
	
	public static String getMesCompetenciaExtensoFormatado(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return getMesCompetenciaExtensoFormatado(c);
	}
	public static String getMesCompetenciaExtensoFormatado(Calendar date) {
		StringBuilder resultado = new StringBuilder();
		
		int month = date.get(Calendar.MONTH);
		
		switch (month) {
		case Calendar.JANUARY:
			resultado.append("Janeiro");
			break;
		case Calendar.FEBRUARY:
			resultado.append("Fevereiro");
			break;
		case Calendar.MARCH:
			resultado.append("Março");
			break;
		case Calendar.APRIL:
			resultado.append("Abril");
			break;
		case Calendar.MAY:
			resultado.append("Maio");
			break;
		case Calendar.JUNE:
			resultado.append("Junho");
			break;
		case Calendar.JULY:
			resultado.append("Julho");
			break;
		case Calendar.AUGUST:
			resultado.append("Agosto");
			break;
		case Calendar.SEPTEMBER:
			resultado.append("Setembro");
			break;
		case Calendar.OCTOBER:
			resultado.append("Outubro");
			break;
		case Calendar.NOVEMBER:
			resultado.append("Novembro");
			break;
		case Calendar.DECEMBER:
			resultado.append("Dezembro");
			break;
		}
		
		int year = date.get(Calendar.YEAR);
		resultado.append("/").append(year);
		
		return resultado.toString();
	}

	/**
	 * recupera a data atual no formato Quarta-feira, 26 de Setembro de
	 * 200725/09/2007
	 */
	public static String dataHoraAtualExtensoFormatada() {
		DateFormat dfmt = new SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy");
		Date hoje = Calendar.getInstance(Locale.getDefault()).getTime();
		return dfmt.format(hoje);
	}

	public static String dataAtualExtenso() {
		DateFormat dfmt = new SimpleDateFormat("d 'de' MMMM 'de' yyyy");
		Date hoje = Calendar.getInstance(Locale.getDefault()).getTime();
		return dfmt.format(hoje);
	}

	public static String dataHoraExtenso(Date data) {
		DateFormat dfmt = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm:ss");

		return dfmt.format(data);
	}

	public static String dataHoraAtual() {
		DateFormat dfmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date hoje = Calendar.getInstance(Locale.getDefault()).getTime();
		return dfmt.format(hoje);

	}

	/**
	 * recupera a data atual no formato dd/MM/yyyy às hh:mm:ss de 200725/09/2007
	 */
	public static String dataHoraAtualExtensoFormatada2() {
		DateFormat dfmt = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm:ss");
		Date hoje = Calendar.getInstance(Locale.getDefault()).getTime();
		return dfmt.format(hoje);
	}

	/**
	 * Converte a data (formato do banco) para uma data por extenso.
	 * 
	 * @param data
	 * @return
	 */
	public static String dataHoraExtenso(String data) {

		DateFormat fmtBanco = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat fmtExtenso = new SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy");
		Date dataObj = null;
		try {
			dataObj = fmtBanco.parse(data);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return fmtExtenso.format(dataObj);

	}

	/**
	 * Converte a data (formato do banco) para o formato de data simples
	 * dd/mm/yyyy
	 * 
	 * @param data
	 * @return
	 */
	public static String dataHoraStringFormata3(String data) {

		DateFormat fmtBanco = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat fmtExtenso = new SimpleDateFormat("dd/MM/yyyy");
		Date dataObj = null;
		try {
			dataObj = fmtBanco.parse(data);
		} catch (ParseException e) {
			// e.printStackTrace();
			return "";
		} catch (NullPointerException e) {
			return "";
		}
		return fmtExtenso.format(dataObj);

	}
	
	@SuppressWarnings("deprecation")
	public static String horaMinutoStringFormatada(Date data){
		String string = null;
		
		if(data.getHours() < 10){
			string = "0" + data.getHours();
		}else{
			string = "" + data.getHours();
		}
		
		string += ":";
		
		if(data.getMinutes() < 10){
			string += "0" + data.getMinutes();
		}else{
			string += "" + data.getMinutes();
		}
		
		return string;
	}

	public static String doubleFormatar(String num) {

		Double valor = Double.parseDouble(num);
		DecimalFormat myformat = new DecimalFormat("0.00");
		String novoValor = myformat.format(valor);
		return novoValor;

	}

	/**
	 * Converte a data (formato banco) para o formato MM/yyyy
	 * 
	 * @param data
	 * @return
	 */
	public static String dthMmYyyy(Date data) {

		DateFormat fmtExtenso = new SimpleDateFormat("MM/yyyy");

		return fmtExtenso.format(data);
	}

	/**
	 * Formato DDMMAAAA sem barras
	 * @param data
	 * @return
	 */
	public static String dthMmYyyySimples(Date data) {

		DateFormat fmtExtenso = new SimpleDateFormat("ddMMyyyy");

		return fmtExtenso.format(data);
	}
	
	/**
	 * Formato DDMMAAAA sem barras
	 * @param data
	 * @return
	 */
	public static String dthMmYyyySimplesHora(Date data) {

		DateFormat fmtExtenso = new SimpleDateFormat("ddMMyyyyHHmmss");

		return fmtExtenso.format(data);
	}

	
	/**
	 * Converte a data (formato banco) para o formato MM/yyyy
	 * 
	 * @param data
	 * @return
	 */
	public static String dthMmYyyy(String data) {

		DateFormat fmtBanco = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat fmtExtenso = new SimpleDateFormat("MM/yyyy");
		Date dataObj = null;
		try {
			dataObj = fmtBanco.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return fmtExtenso.format(dataObj);
	}

	/**
	 * recupera a data atual no formato dd/MM/yyyy HH:mm:ss como string
	 */

	public static Date getDataHoraSistema() {

		Calendar calendar = Calendar.getInstance();
		Date dataAtual = calendar.getTime();

		return dataAtual;

	}

	/**
	 * recupera a data atual no formato dd/MM/yyyy como string
	 */

	public static String getDataSistema() {

		Calendar calendar = Calendar.getInstance();
		Date dataAtual = calendar.getTime();

		return fDDMMYYYY.format(dataAtual);

	}

	/**
	 * Retorna o dia anterior so sistema
	 */
	public static String getDiaAnterior() {

		Calendar calendar = Calendar.getInstance();
		Date dataAtual = calendar.getTime();

		return getDiaAnterior(dataAtual);

	}
	
	public static String getDiaAnterior(Date date) {

		Calendar calendar = Calendar.getInstance();
		Date dataAtual = date;

		// tira um dia da dada atual
		calendar.setTime(dataAtual);
		calendar.add(Calendar.DATE, -1);

		Date diaAnterior = calendar.getTime();

		return fDDMMYYYY.format(diaAnterior);

	}

	public static Date getSeteDiasAtras() {
		Calendar calendar = Calendar.getInstance();
		Date dataAtual = calendar.getTime();

		calendar.setTime(dataAtual);
		calendar.add(Calendar.DATE, -7);

		Date diaAnterior = calendar.getTime();

		return diaAnterior;
	}

	public static Date getPrimeiroDiaMesSemHora(Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		
		return cal.getTime();
	}
	
	
	public static Date getUltimoDiaMesSemHora(Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		return cal.getTime();
	}
	
	/**
	 * Retorna o primeiro dia da semana da data passada como argumento. E seta a
	 * hora para 00:00:00
	 * 
	 * @return
	 */
	public static Timestamp getPrimeiroDiaSemana(Date data) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(data);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return new Timestamp(calendar.getTimeInMillis());
	}
	
	/**
	 * Retorna o primeiro dia da semana da data passada como argumento. E seta a
	 * hora para 00:00:00
	 * 
	 * @return
	 */
	public static Timestamp getUltimoDiaSemana(Date data) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DataUtil.adicionarDias(getPrimeiroDiaSemana(data), 7));
		

		return new Timestamp(calendar.getTimeInMillis());
	}
	
	/**
	 * Retorna o primeiro dia do mês da data passada como argumento. E seta a
	 * hora para 00:00:00
	 * 
	 * @return
	 */
	public static Timestamp getPrimeiroDiaMes(Date data) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(data);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return new Timestamp(calendar.getTimeInMillis());
	}
	
	/**
	 * Retorna o ultimo dia do mês da data passada como argumento. E seta a hora
	 * para 00:00:00
	 * 
	 * @return
	 */
	public static Timestamp getUltimoDiaMes(Date data) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(data);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return new Timestamp(calendar.getTimeInMillis());
	}

	/**
	 * Retorna o ultimo dia do mês da data passada como argumento. E seta a hora
	 * para 23:59:59
	 * 
	 * @return
	 */
	public static Date getUltimoDiaMesUltimaHora(Date data) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(data);
//		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//		calendar.add(Calendar.DAY_OF_MONTH, -1);

		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);

		return calendar.getTime();
	}

	/**
	 * Método que mostra a diferença de dias entre 2 datas.
	 * 
	 * @param d1
	 * @param d2
	 * @return Quantidade de dias em diferença (sempre positivo)
	 */
	public static Integer intervaloDias(Date d1, Date d2) {
		try {

			if (d1 == null || d2 == null) {
				return null;
			}

			int result = (int) ((d1.getTime() - d2.getTime()) / 86400000L);

			return result < 0 ? result * -1 : result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static Integer intervaloMinutos(Date d1, Date d2) {
		try {
			
			if (d1 == null || d2 == null) {
				return null;
			}
			
			int result = (int) ((d1.getTime() - d2.getTime()) / (1000*60));
			
			return result < 0 ? result * -1 : result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Método que mostra a diferença de dias entre 2 datas.
	 * 
	 * @param d1
	 * @param d2
	 * @return Quantidade de dias em diferença (sempre positivo)
	 */
	public static String intervaloDiasAtraso(Date d1, Date d2) {
		try {

			if (d1 == null || d2 == null) {
				return null;
			}

			int result = (int) ((d1.getTime() - d2.getTime()) / 86400000L);

			return result < 0 ? "0" : ""+result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Método que mostra a diferença de anos entre 2 datas.
	 * 
	 * @param d1
	 * @param d2
	 * @return Quantidade de anos em diferença (sempre positivo)
	 */
	public static Integer intervaloAnos(Date d1, Date d2) {
		Integer intervaloDias = intervaloDias(d1, d2);
		return intervaloDias != null ? (intervaloDias / 365) : null;
	}
	
	/**
	 * Recebe um Date de referência e retorna a quantidade de dias naquele mês: <br/>
	 * Ex: 31 <<== getQtdDiasMes(01/01/2011) 
	 * @param mesRef
	 * @return
	 */
	public static Integer getQtdDiasMes(Date mesRef){
		return intervaloDias(getPrimeiroDiaMes(mesRef), getUltimaHoraDia(getUltimoDiaMes(mesRef))) + 1;
	}

	/**
	 * Converte a data para string no formato dd/mm/yyyy hh:mm:ss
	 * 
	 * @param data
	 * @return
	 */
	public static String strDataHora(Date data) {

		if (data == null) {
			return "";
		}

		DateFormat fmtExtenso = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		return fmtExtenso.format(data);

	}
	
	public static String strHoraMinuto(Date data) {
		
		DateFormat fmtExtenso = new SimpleDateFormat("HH:mm");
		
		return fmtExtenso.format(data);
	}
	
	public static String strDia(Date data) {
		
		DateFormat fmtExtenso = new SimpleDateFormat("dd");
		
		return fmtExtenso.format(data);
	}
	
	/**
	 * Converte a data para string no formato yyyymmdd
	 * 
	 * @param data
	 * @return
	 */
	public static String strDataYyyyMmDd(Date data) {

		if (data == null) {
			return "";
		}

		DateFormat fmtExtenso = new SimpleDateFormat("yyyyMMdd");

		return fmtExtenso.format(data);

	}
	
	/**
	 * Converte a data para string no formato yyyymmdd
	 * 
	 * @param data
	 * @return
	 */
	public static String strDataYyyyMmDdTraco(Date data) {

		if (data == null) {
			return "";
		}

		DateFormat fmtExtenso = new SimpleDateFormat("yyyy-MM-dd");

		return fmtExtenso.format(data);

	}
	
	/**
	 * Converte a data para string no formato "2011-10-29"
	 * @param data
	 * @return
	 */
	public static String strDataYyyyMmDdSeparadoPorHifem(Date data){
		if (data == null) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder(strDataYyyyMmDd(data));
		
		sb.insert(4, "-");
		sb.insert(7, "-");
		
		return sb.toString();
	}
	public static String strDataYyyyMmDdHHMMSSSeparadoPorHifem(Date data){
		if (data == null) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder(strDataYyyyMmDdHh24MiSsDoisPontos(data));
		
		sb.insert(10, "T");
		
		return sb.toString();
	}
	
	public static String strHoraHhMmSsSeparadoPorHifem(Date data) {

		if (data == null) {
			return "";
		}

		DateFormat fmtExtenso = new SimpleDateFormat("HH:mm:ss");

		return fmtExtenso.format(data);

	}

	/**
	 * Converte a data para string no formato yyyymmddhh24miss
	 * 
	 * @param data
	 * @return
	 */
	public static String strDataYyyyMmDdHh24MiSs(Date data) {

		if (data == null) {
			return "";
		}

		DateFormat fmtExtenso = new SimpleDateFormat("yyyyMMddHHmmss");

		return fmtExtenso.format(data);

	}
	public static String strDataYyyyMmDdHh24MiSsDoisPontos(Date data) {
		
		if (data == null) {
			return "";
		}
		
		DateFormat fmtExtenso = new SimpleDateFormat("yyyy-MM-ddHH:mm:ssXXX");
		
		return fmtExtenso.format(data).replace("GMT", "");
		
	}

	/**
	 * Converte a data para string no formato yyyymmddhh24mm
	 * 
	 * @param data
	 * @return
	 */
	public static String strDataYyyyMmDdHh24Mi(Date data) {

		if (data == null) {
			return "";
		}

		DateFormat fmtExtenso = new SimpleDateFormat("yyyyMMddHHmm");

		return fmtExtenso.format(data);

	}

	/**
	 * Converte a data para string no formato yyyymmddhh24
	 * 
	 * @param data
	 * @return
	 */
	public static String strDataYyyyMmDdHh24(Date data) {

		if (data == null) {
			return "";
		}

		DateFormat fmtExtenso = new SimpleDateFormat("yyyyMMddHH");

		return fmtExtenso.format(data);

	}

	/**
	 * Adiciona horas a data
	 * 
	 * @param data
	 * @return
	 */
	public static Date adicionarHoras(Date data, Integer horas) {

		if (data == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		calendar.add(Calendar.HOUR, horas);

		return calendar.getTime();

	}
	
	/**
	 * Adiciona dias a data
	 * 
	 * @param data
	 * @return
	 */
	public static Date adicionarDias(Date data, Integer dias) {

		if (data == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		calendar.add(Calendar.DAY_OF_MONTH, dias);

		return calendar.getTime();

	}
	
	/**
	 * Adiciona dias a data
	 * 
	 * @param data
	 * @return
	 */
	public static Date adicionarMes(Date data, Integer dias) {

		if (data == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		calendar.add(Calendar.MONTH, dias);

		return calendar.getTime();

	}
	
	/**
	 * Adiciona data com hora inicial e final
	 * 
	 * @param data
	 * @return
	 */
	public static Date retornarDataInicialFinal(Date data, String dataString) {

		if (data == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);

		if (dataString.equals(DATA_INICIAL)) {
			calendar.add(Calendar.HOUR, 00);
			calendar.add(Calendar.MINUTE, 00);
			calendar.add(Calendar.SECOND, 00);
			calendar.add(Calendar.MILLISECOND, 000);
		} else {
			calendar.add(Calendar.HOUR, 23);
			calendar.add(Calendar.MINUTE, 59);
			calendar.add(Calendar.SECOND, 59);
			calendar.add(Calendar.MILLISECOND, 999);
		}

		return calendar.getTime();

	}

	/**
	 * Adiciona minutos a data
	 * 
	 * @param data
	 * @return
	 */
	public static Date adicionarMinutos(Date data, Integer min) {

		if (data == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		calendar.add(Calendar.MINUTE, min);

		return calendar.getTime();

	}

	/**
	 * Converte um Timestamp para string no formato dd/mm/yyyy hh:mm:ss
	 * 
	 * @param data
	 * @return
	 */
	public static String strDataHora(Timestamp data) {

		if (data == null) {
			return "";
		}

		DateFormat fmtExtenso = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		return fmtExtenso.format(data);

	}
	
	public static Timestamp parseDataRVWS(String data) throws ParseException {
		if (data == null) {
			return null;
		}
		DateFormat fmtExtenso = new SimpleDateFormat("dd-MM-yyyy");
		
		return new Timestamp(fmtExtenso.parse(data).getTime());
	}
	
	public static Timestamp parseDataHoraRVWS(String data) throws ParseException {
		if (data == null) {
			return null;
		}
		DateFormat fmtExtenso = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		
		return new Timestamp(fmtExtenso.parse(data).getTime());
	}
	
	public static Timestamp parseData(String data) throws ParseException {
		if (data == null) {
			return null;
		}
		DateFormat fmtExtenso = new SimpleDateFormat("dd/MM/yyyy");
		
		return new Timestamp(fmtExtenso.parse(data).getTime());
	}
	
	public static Timestamp parseDataHoraMin(String data) throws ParseException {
		if (data == null) {
			return null;
		}
		DateFormat fmtExtenso = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		return new Timestamp(fmtExtenso.parse(data).getTime());
	}
	public static Timestamp parseDataHora(String data) throws ParseException {
		if (data == null) {
			return null;
		}
		DateFormat fmtExtenso = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		return new Timestamp(fmtExtenso.parse(data).getTime());
	}
	
	public static String strDataRVWS(Timestamp data) {
		if (data == null) {
			return "";
		}
		
		DateFormat fmtExtenso = new SimpleDateFormat("dd-MM-yyyy");
		
		return fmtExtenso.format(data);
	}
	
	public static String strDataDDMMYYYY_hhmmss(Timestamp data) {
		
		if (data == null) {
			return "";
		}

		DateFormat fmtExtenso = new SimpleDateFormat("ddMMyyyy_HHmmss");

		return fmtExtenso.format(data);
		
	}
	public static String strData(Timestamp data) {

		if (data == null) {
			return "";
		}

		DateFormat fmtExtenso = new SimpleDateFormat("dd/MM/yyyy");

		return fmtExtenso.format(data);

	}
	public static String strDataBanco(Timestamp data) {
		
		if (data == null) {
			return "";
		}
		
		DateFormat fmtExtenso = new SimpleDateFormat("yyyy/MM/dd");
		
		return fmtExtenso.format(data);
		
	}
	
	/**
	 * Converte um Timestamp para string no formato yyyyMMdd HH:mm:ss.SSS
	 * 
	 * @param data
	 * @return
	 */
	public static String strDataYyyyMmDdHhMmSsMmm(Date data) {

		if (data == null) {
			return "";
		}

		DateFormat fmtExtenso = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");

		return fmtExtenso.format(data);

	}
	public static String strDataRVWS(Date data) {
		if (data == null) {
			return null;
		}
		
		DateFormat fmtExtenso = new SimpleDateFormat("dd-MM-yyyy");
		
		return fmtExtenso.format(data);
	}

	/**
	 * Converte a data para string no formato dd/mm/yyyy
	 * 
	 * @param data
	 * @return
	 */
	public static String strData(Date data) {
		if (data == null) {
			return null;
		}
		
		DateFormat fmtExtenso = new SimpleDateFormat("dd/MM/yyyy");
		
		return fmtExtenso.format(data);


	}
	
	/**
	 * Converte a data para string no formato dd/mm/yy
	 * 
	 * @param data
	 * @return
	 */
	public static String strDataYY(Date data) {

		if (data == null) {
			return null;
		}

		DateFormat fmtExtenso = new SimpleDateFormat("dd/MM/yy");

		return fmtExtenso.format(data);

	}
	
	/**
	 * Converte a data para string no formato mm/yyyy
	 * 
	 * @param data
	 * @return
	 */
	public static String strMesAno(Date data) {
		
		if (data == null) {
			return null;
		}
		
		DateFormat fmtExtenso = new SimpleDateFormat("MM/yyyy");
		
		return fmtExtenso.format(data);
	}


	/**
	 * Converte a data para string no formato d/m/yy
	 * 
	 * @param data
	 * @return
	 */
	
	public static String strDiaMesAnoSemZero(Date data) {
		
		if (data == null) {
			return null;
		}
		
		DateFormat fmtExtenso = new SimpleDateFormat("d/M/yyyy");
		
		return fmtExtenso.format(data);
	}

	/**
	 * Converte a data para string no formato dd/mm/yy
	 * 
	 * @param data
	 * @return
	 */
	
	public static String strDiaMesAno(Date data) {
		
		if (data == null) {
			return null;
		}
		
		DateFormat fmtExtenso = new SimpleDateFormat("dd/MM/yy");
		
		return fmtExtenso.format(data);
		
	}
	
	/**
	 * Converte a data para string no formato mm/yyyy
	 * 
	 * @param data
	 * @return
	 */
	public static String strDataCompetencia(Date data) {

		if (data == null) {
			return null;
		}

		DateFormat fmtExtenso = new SimpleDateFormat("MM/yyyy");

		return fmtExtenso.format(data);

	}
	
	/**
	 * Converte a data para string no formato yyyy-mm
	 * 
	 * @param data
	 * @return
	 */
	public static String strDataCompetenciaPadraoBanco(Date data) {

		if (data == null) {
			return null;
		}

		DateFormat fmtExtenso = new SimpleDateFormat("yyyy-MM");

		return fmtExtenso.format(data);

	}
	
	/**
	 * metodo para transfomar uma string num objeto date
	 * 
	 * @param data
	 * @return
	 */
	public static Date transformaStringParaDateYyyyMmDd(String data) {

		SimpleDateFormat sdf;

		// Caso o formato esteja como ddMMyy
		if (data.length() == 6) {
			data = data.substring(0, 4) + "20" + data.substring(4, 6);
			sdf = new SimpleDateFormat("yyyyMMdd");
		} else if (data.length() == 8){
			sdf = new SimpleDateFormat("yyyyMMdd");
		} else if (data.length() == 14){
			sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		} else {
			sdf = new SimpleDateFormat("yyyy/MM/dd");
		}

		Date objData = null;
		try {
			objData = sdf.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return objData;
	}
	
	/**
	 * metodo para transfomar uma string num objeto date
	 * 
	 * @param data
	 * @return
	 */
	public static Date transformaStringParaDate(String data) {

		SimpleDateFormat sdf;

		// Caso o formato esteja como ddMMyy
		if (data.length() == 6) {
			data = data.substring(0, 4) + "20" + data.substring(4, 6);
			sdf = new SimpleDateFormat("ddMMyyyy");
		} else if (data.length() == 8){
			sdf = new SimpleDateFormat("ddMMyyyy");
		} else if (data.length() == 14){
			sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		} else {
			sdf = new SimpleDateFormat("dd/MM/yyyy");
		}

		Date objData = null;
		try {
			objData = sdf.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return objData;
	}
	
	/**
	 * metodo para transfomar uma string num objeto date
	 * 
	 * @param data
	 * @return
	 *//*
	public static Date transformaStringParaDateCompetencia(String data) {

		SimpleDateFormat sdf;

		// Caso o formato esteja como ddMMyy
		if (data.length() == 6) {
			data = data.substring(2, 4) + "20" + data.substring(4, 6);
			sdf = new SimpleDateFormat("MMyyyy");
		} else {
			sdf = new SimpleDateFormat("MM/yyyy");
		}

		Date objData = null;
		try {
			objData = sdf.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return objData;
	}*/

	/**
	 * metodo para transfomar uma string num objeto date
	 */
	public static Date transformaStringParaDateCompleta(String data) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date objData = null;
		try {
			objData = sdf.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return objData;
	}
	
	/**
	 * metodo para transfomar uma string num objeto date até MM
	 */
	public static Date transformaStringParaDateAteMM(String data) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date objData = null;
		try {
			objData = sdf.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return objData;
	}

	/**
	 * metodo para transfomar uma string no formato do banco num objeto date
	 */
	public static Date transformaStringParaDateBanco(String data) {
//		01/05/2016 00:42
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date objData = null;
		try {
			objData = sdf.parse(data.replaceAll("'", ""));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return objData;
	}

	/**
	 * Metodo para formatar um objeto data para o formato yyyy-MM-dd
	 * HH:mm:ss.SSS
	 * 
	 * @param data
	 *            a ser formatada
	 * @return string formatada
	 */
	public static String formatarParaDataBancoMs(Date data) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return sdf.format(data);
	}

	/**
	 * Metodo para formatar um objeto data para o formato yyyy-MM-dd HH:mm:ss
	 * 
	 * @param data
	 *            a ser formatada
	 * @return string formatada
	 */
	public static String formatarParaDataBanco(Date data) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(data);
	}
	
	/**
	 * Metodo para formatar um objeto data para o formato yyyy-MM-dd
	 * 
	 * @param data
	 *            a ser formatada
	 * @return string formatada
	 */
	public static String formatarParaDataBancoSemHora(Date data) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(data);
	}

	public static String strHHMMSSAtual() {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		Date agora = new Date(GregorianCalendar.getInstance().getTimeInMillis());
		return sdf.format(agora);
	}

	/**
	 * retorna o tempo em segundos entre 2 datas
	 */
	public static String intervaloSegundosMinutos(Date d1, Date d2) {
		NumberFormat formatador = NumberFormat.getNumberInstance();
		formatador.setMaximumFractionDigits(2);
		
		long diferenca = d2.getTime() - d1.getTime();
		return (diferenca / 1000) + "s - " + formatador.format((diferenca / 1000) / 60d) + "m";
	}

	/**
	 * Retorna
	 */

	public static boolean checaDataIgualSemHora(Date data1, Date data2) {

		Calendar calendario1 = Utils.setPrimeiraHoraDia(data1);
		Calendar calendario2 = Utils.setPrimeiraHoraDia(data2);

		if (calendario1.equals(calendario2)) {
			return true;
		} else {
			return false;
		}

	}

	public static Timestamp getPrimeiroDiaAno(Date date) {
		Calendar calendar = Calendar.getInstance();
		Date dataAtual = calendar.getTime();
		
		calendar.setTime(dataAtual);
		calendar.set(Calendar.MONTH, calendar.getActualMinimum(Calendar.MONTH));
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return new Timestamp(calendar.getTimeInMillis());
	}
	
	public static Timestamp getUltimoDiaAno(Date date) {
		Calendar calendar = Calendar.getInstance();
		Date dataAtual = calendar.getTime();
		
		calendar.setTime(dataAtual);
		calendar.set(Calendar.MONTH, calendar.getActualMaximum(Calendar.MONTH));
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return new Timestamp(calendar.getTimeInMillis());
	}
	
	public static Timestamp getUltimaHoraDia(Date data) {

		Calendar cFim = Calendar.getInstance();
		cFim.setTime(data);
		
		cFim.set(Calendar.HOUR_OF_DAY,23);
		cFim.set(Calendar.MINUTE,59);
		cFim.set(Calendar.SECOND,59);
		cFim.set(Calendar.MILLISECOND,999);
		
		return new Timestamp(cFim.getTimeInMillis());
	}
	
	public static Timestamp getPrimeiraHoraDiaTretaWS(Date data) {
		Calendar cInicio = Calendar.getInstance();
		cInicio.setTime(data);
		
		cInicio.set(Calendar.HOUR_OF_DAY,10);
		cInicio.set(Calendar.MINUTE,19);
		cInicio.set(Calendar.SECOND,0);
		cInicio.set(Calendar.MILLISECOND,0);
		
		return new Timestamp(cInicio.getTimeInMillis());
	}
	public static Timestamp getPrimeiraHoraDia(Date data) {
		Calendar cInicio = Calendar.getInstance();
		cInicio.setTime(data);
		
		cInicio.set(Calendar.HOUR_OF_DAY,0);
		cInicio.set(Calendar.MINUTE,0);
		cInicio.set(Calendar.SECOND,0);
		cInicio.set(Calendar.MILLISECOND,0);
		
		return new Timestamp(cInicio.getTimeInMillis());
	}
	
	public static Timestamp getPrimeiroMinutoHora(Date data) {
		Calendar cInicio = Calendar.getInstance();
		cInicio.setTime(data);
				
		cInicio.set(Calendar.MINUTE,0);
		cInicio.set(Calendar.SECOND,0);
		cInicio.set(Calendar.MILLISECOND,0);
		
		return new Timestamp(cInicio.getTimeInMillis());
	}
	
	public static Date getData(int dia, int mes, int ano){
		
		Calendar calen = Calendar.getInstance();
		calen.set(Calendar.YEAR, ano);
		calen.set(Calendar.MONTH, mes);
		calen.set(Calendar.DAY_OF_MONTH, dia);
		
		return calen.getTime();
	}


	public static List<Date> getListDataIntervalo(Date datInicio, Date datFim) {

		Calendar calenInicio = Calendar.getInstance();
		calenInicio.setTime(datInicio);
		
		Calendar calenFim = Calendar.getInstance();
		calenFim.setTime(datFim);
		
		List<Date> listaDatasGerar = new ArrayList<Date>();
		
		while (calenInicio.before(calenFim) || calenInicio.equals(calenFim)) {
			listaDatasGerar.add(calenInicio.getTime());
			calenInicio.add(Calendar.DAY_OF_MONTH, 1);
		}
		return listaDatasGerar;
		
	}


	public static XMLGregorianCalendar parseStringToXmlGregorianCalendar(String ret) throws ParseException, DatatypeConfigurationException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date dob = sdf.parse(ret);
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(dob);
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
	}

}
