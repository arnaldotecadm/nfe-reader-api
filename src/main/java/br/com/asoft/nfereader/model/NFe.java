package br.com.asoft.nfereader.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NFe {
	private InfNFe infNFe = new InfNFe();
	private NfeSignature signature = new NfeSignature();
}
