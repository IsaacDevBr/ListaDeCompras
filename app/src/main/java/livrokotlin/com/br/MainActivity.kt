package livrokotlin.com.br

import android.content.Intent
import android.graphics.Bitmap
import android.icu.text.NumberFormat
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import livrokotlin.com.br.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var imageBitMap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Implementando o adaptador
        val produtosAdapter = ProdutoAdapter(this)

        binding.listViewProducts.adapter = produtosAdapter

        binding.btnAdicionar.setOnClickListener {
            //Criando a intent explícita
            val intent = Intent(this, CadastroActivity::class.java)

            //iniciando a atividade
            startActivity(intent)

        }

        //implementando metodo de deletar toda vez que o usuario clicar e segurar um item da lista
        binding.listViewProducts.setOnItemLongClickListener { adapterView: AdapterView<*>, view: View,
                                                              position: Int,
                                                              id: Long ->

            //implementando metodo remove conforme a posiotion passada
            val item = produtosAdapter.getItem(position)

            //remove
            produtosAdapter.remove(item)
            binding.txtTotal.clearComposingText()

            //retorno indicado que o click foi indicado com sucesso
            true
        }

    }

    //criando o metodo onResume
    override fun onResume() {
        super.onResume()

        val adapter = binding.listViewProducts.adapter as ProdutoAdapter

        adapter.clear()
        adapter.addAll(Utils.produtosGlobal)

        val soma = Utils.produtosGlobal.sumOf { it.preco * it.quantidade }

        val f = java.text.NumberFormat.getCurrencyInstance(Locale("pt", "br"))
        binding.txtTotal.text = "TOTAL: ${f.format(soma)}"
    }
}