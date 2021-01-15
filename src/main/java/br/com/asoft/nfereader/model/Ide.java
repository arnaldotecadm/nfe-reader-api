package br.com.asoft.nfereader.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({ "cUF", "cNF", "natOp", "nNF", "dhEmi", "tpNF", "idDest", "cMunFG", "indFinal", "indPres" })
public class Ide {
	private String cUF;
	private String cNF;
	private String natOp;
	private String nNF;
	private String dhEmi;
	private String tpNF;
	private String idDest;
	private String cMunFG;
	private String indFinal;
	private String indPres;

}
