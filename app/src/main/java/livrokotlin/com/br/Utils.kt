package livrokotlin.com.br

class Utils {

    //criando uma variavel global para armazenar itens da lista enquanto não há banco de dados

    companion object {
        val produtosGlobal = mutableListOf<Produto>()
    }
}