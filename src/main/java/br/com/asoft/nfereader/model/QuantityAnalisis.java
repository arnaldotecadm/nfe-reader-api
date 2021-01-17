package br.com.asoft.nfereader.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class QuantityAnalisis {
    private int qtdNfe;
    private long nfeNaoProcessavel;
    private List<ItemAnalise> tpAmbiente;
    private List<ItemAnalise> tpNFe;
    private List<ItemAnalise> ufDestList;
    private List<ItemAnalise> natOperacaoList;
}
