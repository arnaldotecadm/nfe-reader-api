package br.com.asoft.nfereader.analise.quantitativa;

import br.com.asoft.nfereader.analise.AnaliseQuantitativaInterface;
import br.com.asoft.nfereader.analise.AnaliseUtil;
import br.com.asoft.nfereader.model.ItemAnalise;
import br.com.asoft.nfereader.model.nfe.NfeProc;

import java.util.ArrayList;
import java.util.List;

public class TipoAmbienteAnalise implements AnaliseQuantitativaInterface {

    AnaliseUtil analiseUtil = new AnaliseUtil();

    public List<ItemAnalise> processar(List<NfeProc> nfeProcList) {
        List<ItemAnalise> tpAmbienteList = new ArrayList<>();

        for (NfeProc item : nfeProcList) {
            // neste caso não é uma nota de emissao possivelmente sera outro tipo de ocorrencia i.e inutilizacao
            if (null == item.getProtNFe().getCStat()) {
                continue;
            }
            String tpAmb = item.getProtNFe().getTpAmb();

            analiseUtil.populateArrayWithString(tpAmbienteList, tpAmb);

        }
        return tpAmbienteList;
    }

    @Override
    public String getNomeAnalise() {
        return "tipoAmbiente";
    }
}
