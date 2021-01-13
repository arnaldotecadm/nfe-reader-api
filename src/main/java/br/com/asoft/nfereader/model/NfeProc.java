package br.com.asoft.nfereader.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NfeProc {
	private NFe nfe = new NFe();
	private ProtNFe protNFe = new ProtNFe();
}
