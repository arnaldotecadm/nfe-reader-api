package br.com.asoft.nfereader.analise.quantitativa;

import br.com.asoft.nfereader.analise.AnaliseQuantitativaInterface;
import br.com.asoft.nfereader.analise.AnaliseUtil;
import br.com.asoft.nfereader.model.ItemAnalise;
import br.com.asoft.nfereader.model.nfe.NfeProc;

import java.util.ArrayList;
import java.util.List;

public class TotalNotaFiscalAnalise implements AnaliseQuantitativaInterface {

    AnaliseUtil analiseUtil = new AnaliseUtil();

    @Override
    public List<ItemAnalise> processar(List<NfeProc> lista) {
        List<ItemAnalise> tpAmbienteList = new ArrayList<>();

        for (NfeProc item : lista) {
            // neste caso não é uma nota de emissao possivelmente sera outro tipo de ocorrencia i.e inutilizacao
            if (null == item.getProtNFe().getCStat()) {
                continue;
            }

            analiseUtil.populateArrayWithString(tpAmbienteList, "Total de Notas Fiscais Processadas");

        }
        return tpAmbienteList;
    }

    @Override
    public String getNomeAnalise() {
        return "totalNotaFiscal";
    }
}
