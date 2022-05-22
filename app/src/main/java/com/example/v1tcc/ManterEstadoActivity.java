package com.example.v1tcc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ManterEstadoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manter_estado);
    }


}
/*
Vai ser um caderninho
vai ter os defaults, o ideal seria unina alimentação, decubito ec
mas vai ter que deixar pra depois ainda sim fazer pensando nisso
ainda nos defaults vai ter os de expiração pra datas de aparelho


CATEGORIAS: defaults, expiração, toDos (protocolos de saida por ex e nao lembretes)

anotar e ver a melhor forma de compilar a principio
um caderno com os estados e filtro por categoria, como o relatorio
podendo clicar e expandir para edição

interface pra adicionar
> primeiro seleciona categoria confirma esconde tudo e abre a tela pra edição
> ao editar nao é possivel trocar a categoria/tipo de indicação de estado
> alguns estados default podem ser fixados na tela inicial, a principio o escopo
> iria cobrir os defaults mas agora pensar em que fixar...
> ajudaria abrir uma tela de registro no caso a os botoes default seria pra inclusão manual
> nos registros, adicionar trocar de fralda manualmente e registra no registro, adicionando categoria
> de higiene especifica talvez... tipo urina: e grava no relatorio a ultima fralda, mas como
> seria a sinalização e interação com a tela principal?
*/