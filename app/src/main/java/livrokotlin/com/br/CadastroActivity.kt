package livrokotlin.com.br


import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import livrokotlin.com.br.databinding.ActivityCadastroBinding

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        uri : Uri? -> uri.let {

        //faça algo com a uri da imagem selecionada
            binding.imgFotoProduto.setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgFotoProduto.setOnClickListener{
            abrirGaleria()
        }


        //definição do ouvinte do botão
        binding.btnInserir.setOnClickListener {

            //pegando o valor digitado pelo usuario
            val produto = binding.txtProduto.text.toString()
            val qtd = binding.txtQtd.text.toString()
            val valor = binding.txtValor.text.toString()

            if (produto.isNotEmpty() && qtd.isNotEmpty() && valor.isNotEmpty()) {
                //criando um objeto com as informações que o usuario inseriu
                val prod = Produto(produto, qtd.toInt(), valor.toDouble())
                Utils.produtosGlobal.add(prod)

                binding.txtProduto.text.clear()
                binding.txtQtd.text.clear()
                binding.txtValor.text.clear()
            } else {
                binding.txtProduto.error = if (binding.txtProduto.text.isEmpty()) "Preencha o nome do produto"
                else null

                binding.txtQtd.error = if (binding.txtQtd.text.isEmpty()) "Preencha a quantidade"
                else null

                binding.txtValor.error = if (binding.txtValor.text.isEmpty()) "Preencha o valor"
                else null
            }



        }
    }
    //função para abrir a galeria
    private fun abrirGaleria() {
        getContent.launch("image/*")
    }
}
