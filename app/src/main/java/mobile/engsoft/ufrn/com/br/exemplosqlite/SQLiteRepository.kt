package mobile.engsoft.ufrn.com.br.exemplosqlite

import android.content.ContentValues
import android.content.Context
import android.database.AbstractCursor
import android.database.Cursor

class SQLiteRepository(ctx: Context) : ProdutoRepository {
    override fun findAll(): List<Produto> {
        var sql ="SELECT * FROM $TABLE_NAME"
        var args: Array<String>? = null

        sql += " ORDER BY $COLUMN_NAME"
        val db = helper.writableDatabase
        val cursor = db.rawQuery(sql, args)
        val produtos = ArrayList<Produto>()
        while(cursor.moveToNext()){
            val produto = produtoFromCursor(cursor)
            produtos.add(produto)
        }
        cursor.close()
        db.close()
        return produtos
    }

    private val helper: ProdutoSqlHelper = ProdutoSqlHelper(ctx)

    private fun insert(p : Produto){
        val db = helper.writableDatabase
        val cv = ContentValues().apply{
            put(COLUMN_NAME, p.name)
            put(COLUMN_VALUE, p.value)
        }

        val id = db.insert(TABLE_NAME, null, cv)
        if(id != 1L){
            p.id = id
        }
        db.close()
    }


    private fun update(p : Produto){
        val db = helper.writableDatabase
        val cv = ContentValues().apply{
            put(COLUMN_NAME, p.name)
            put(COLUMN_VALUE, p.value)
        }

        db.update(TABLE_NAME, cv, "$COLUMN_ID = ?", arrayOf(p.id.toString()))

        db.close()
    }

    override fun save(produto: Produto) {
        if(produto.id == 0L){
            insert(produto)
        } else {
            update(produto)
        }
    }

    override fun remove(vararg produtos: Produto) {
        val db = helper.writableDatabase
        for(p in produtos){
            db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(p.id.toString()))
        }
        db.close()
    }

    override fun produtoById(id: Long, callback: (Produto?) -> Unit) {
        val sql = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ? "
        val db = helper.writableDatabase
        val cursor = db.rawQuery(sql, arrayOf(id.toString()))
        val p = if(cursor.moveToNext())produtoFromCursor(cursor) else null

        callback(p)
    }

    private fun produtoFromCursor(cursor: Cursor) : Produto {
        val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
        val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
        val value = cursor.getFloat(cursor.getColumnIndex(COLUMN_VALUE))
        return Produto(id, name, value)
    }

    override fun search(term: String, callback: (List<Produto>) -> Unit) {
        var sql ="SELECT * FROM $TABLE_NAME"
        var args: Array<String>? = null

        if(term.isNotEmpty()){
            sql += " WHERE $COLUMN_NAME LIKE ?"
            args = arrayOf("%$term%")
        }

        sql += " ORDER BY $COLUMN_NAME"
        val db = helper.writableDatabase
        val cursor = db.rawQuery(sql, args)
        val produtos = ArrayList<Produto>()
        while(cursor.moveToNext()){
            val produto = produtoFromCursor(cursor)
            produtos.add(produto)
        }
        cursor.close()
        db.close()
        callback(produtos)
    }
}