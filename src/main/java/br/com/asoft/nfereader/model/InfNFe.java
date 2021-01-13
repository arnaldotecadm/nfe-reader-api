package br.com.asoft.nfereader.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InfNFe {
	private Ide ide = new Ide();
	private NFeEmit emit = new NFeEmit();
	private Dest dest = new Dest();
	private List<Det> detList;
}
