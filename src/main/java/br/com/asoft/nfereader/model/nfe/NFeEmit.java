package br.com.asoft.nfereader.model.nfe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NFeEmit {
	private String CNPJ;
	private String xNome;
	private String xFant;
	private String IE;
	private String CRT;
}
