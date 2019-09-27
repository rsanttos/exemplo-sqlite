package mobile.engsoft.ufrn.com.br.exemplosqlite

interface ProdutoRepository {
    fun save(produto: Produto)
    fun remove(vararg produtos: Produto)
    fun produtoById(id: Long, callback: (Produto?) -> Unit)
    fun search(term: String, callback: (List<Produto>) -> Unit)
    fun findAll() : List<Produto>
}