package br.com.asoft.nfereader.model.nfe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProtNFe {
	private String tpAmb;
	private String chNFe;
	private String dhRecbto;
	private String cStat;
	private String cxMotivo;
}
