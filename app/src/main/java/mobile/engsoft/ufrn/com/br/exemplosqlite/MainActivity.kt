package mobile.engsoft.ufrn.com.br.exemplosqlite

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import mobile.engsoft.ufrn.com.br.exemplosqlite.Produto

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val produto : Produto = Produto(0L, "teste", 123F)

        val produtoRepository = SQLiteRepository(this)
        produtoRepository.save(produto)

        Log.v("produtos - ", produtoRepository.findAll().toString())
    }
}
