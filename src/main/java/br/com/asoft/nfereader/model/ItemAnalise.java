package br.com.asoft.nfereader.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemAnalise {
    @EqualsAndHashCode.Include
    private String name;
    private int qtd;

    public ItemAnalise(String name) {
        this.name = name;
        this.qtd = 1;
    }

    public void addCount() {
        this.qtd++;
    }
}
